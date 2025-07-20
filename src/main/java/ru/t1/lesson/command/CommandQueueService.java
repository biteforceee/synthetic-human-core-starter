package ru.t1.lesson.command;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.t1.lesson.exeption.QueueIsFullException;
import ru.t1.lesson.metrics.StatisticsService;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
@Service
public class CommandQueueService {

    private final ThreadPoolExecutor criticalExecutor;
    private final ThreadPoolExecutor commonExecutor;
    private final StatisticsService statisticsService;

    public CommandQueueService(
            @Value("${command.queue.max-size:10}") int queueSize,
            StatisticsService statisticsService
    ) {
        this.criticalExecutor = new ThreadPoolExecutor(1, 1, 0L, MILLISECONDS,
                new SynchronousQueue<>());

        this.commonExecutor = new ThreadPoolExecutor(
                1,
                1,
                0L, MILLISECONDS,
                new ArrayBlockingQueue<>(queueSize),
                Executors.defaultThreadFactory(),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        throw new QueueIsFullException("Очередь команд переполнена");
                    }
                }
        );

        this.statisticsService = statisticsService;
    }

    public void submit(Command command) throws InterruptedException {
        try {
            if (command.getPriority() == CommandPriority.CRITICAL) {
                criticalExecutor.submit(() -> execute(command));
            } else {
                commonExecutor.submit(() -> execute(command));
                Thread.sleep(4000);
            }
        } catch (InterruptedException e){
            throw new InterruptedException(e.getMessage());
        }
    }

    private void execute(Command command) {
        log.info("Executing {} command: {}", command.getPriority(), command);
        statisticsService.incrementCompletedCommandCount(command.getAuthor());
    }

    public int getCurrentQueueSize() {
        return commonExecutor.getQueue().size();
    }

    @PreDestroy
    public void shutdown() {
        criticalExecutor.shutdownNow();
        commonExecutor.shutdownNow();
    }

    public int getCurrentQueueSize(CommandQueueService commandQueueService) {
        return commonExecutor.getQueue().size();
    }
}
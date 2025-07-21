package ru.t1.lesson.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final MeterRegistry meterRegistry;
    private final Map<String, Counter> authorCommandCounts = new ConcurrentHashMap<>();

    public void registerQueueSizeSupplier(Supplier<Number> queueSize) {
        Gauge.builder("synthetic.queue.size", queueSize)
                .description("Current number of commands in queue")
                .register(meterRegistry);
    }

    public void incrementCompletedCommandCount(String author) {
        Counter counter = authorCommandCounts.computeIfAbsent(author,
                a -> Counter.builder("synthetic.commands.completed")
                        .tag("author", a)
                        .description("Total completed commands by author")
                        .register(meterRegistry)
        );

        counter.increment();
    }
}

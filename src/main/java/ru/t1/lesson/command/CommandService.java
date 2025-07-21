package ru.t1.lesson.command;

import org.springframework.stereotype.Service;

@Service
public interface CommandService {
    void submit(Command cmd) throws InterruptedException;
    int getCurrentQueueSize();
}

package ru.t1.lesson.exeption;

public class QueueIsFullException extends RuntimeException {
    public QueueIsFullException(String message) {
        super(message);
    }
}

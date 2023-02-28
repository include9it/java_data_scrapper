package io.saltpay.threads;

import io.saltpay.utils.SaltLogger;

import java.util.UUID;

public class Task implements Runnable {
    private final UUID taskId;

    public Task() {
        this.taskId = UUID.randomUUID();
    }

    @Override
    public void run() {
        // Code to be executed in the thread
        SaltLogger.basic("Task " + taskId + " is running in thread " + Thread.currentThread().getName());
    }
}

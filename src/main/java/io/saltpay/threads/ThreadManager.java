package io.saltpay.threads;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    public static void start(int numberOfThreads, List<Task> listOfTask) {
        // Create a thread pool with numberOfThreads
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        // Submit tasks to the executor
        listOfTask.forEach(executor::submit);

        // Shutdown the executor when all tasks are completed
        executor.shutdown();
    }
}

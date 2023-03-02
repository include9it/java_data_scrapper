package io.saltpay.robot;

import io.saltpay.model.SsnChunk;
import io.saltpay.model.SsnData;
import io.saltpay.support.DriverManager;
import io.saltpay.tasks.CreditInfoScrapperCallable;
import io.saltpay.utils.ChunkUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CreditInfoThreadBot {
    public static List<SsnData> start(int numberOfThreads, List<String> listOfSsn, DriverManager driverManager) {
        // Create a thread pool with numberOfThreads
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        List<SsnData> listOfSsnData = new ArrayList<>();
        List<Future<List<SsnData>>> futuresOfSsnListData = new ArrayList<>();

        List<SsnChunk> listOfChunks = ChunkUtil.prepareChunks(numberOfThreads, listOfSsn);

        // Submit tasks to the executor
        listOfChunks.forEach(chunk -> {
            Callable<List<SsnData>> callable = new CreditInfoScrapperCallable(chunk.hashCode(), driverManager, chunk);

            Future<List<SsnData>> future = executor.submit(callable);

            futuresOfSsnListData.add(future);
        });

        // Get ssn data from futures
        futuresOfSsnListData.forEach(ssnDataListFuture -> {
            try {
                listOfSsnData.addAll(ssnDataListFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        // Shutdown the executor when all tasks are completed
        executor.shutdown();

        return listOfSsnData;
    }
}

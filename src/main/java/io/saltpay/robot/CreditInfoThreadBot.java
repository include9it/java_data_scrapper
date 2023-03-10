package io.saltpay.robot;

import io.saltpay.models.chunk.SsnChunk;
import io.saltpay.models.SsnData;
import io.saltpay.support.Driver;
import io.saltpay.tasks.CreditInfoCallableTask;
import io.saltpay.utils.ChunkUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CreditInfoThreadBot {
    public static List<SsnData> start(int numberOfThreads, List<String> listOfSsn, Driver driver) {
        // Create a thread pool with numberOfThreads
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        List<SsnData> listOfSsnData = new ArrayList<>();
        List<Future<List<SsnData>>> futuresOfSsnListData = new ArrayList<>();

        List<SsnChunk> listOfChunks = ChunkUtil.prepareChunks(numberOfThreads, listOfSsn);

        // Submit tasks to the executor
        listOfChunks.forEach(chunk -> {
            Callable<List<SsnData>> callable = new CreditInfoCallableTask(chunk.hashCode(), driver, chunk);

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

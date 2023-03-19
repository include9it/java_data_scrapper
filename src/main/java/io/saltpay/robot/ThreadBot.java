package io.saltpay.robot;

import io.saltpay.models.Chunk;
import io.saltpay.scripts.ScrapperScript;
import io.saltpay.scripts.StartScript;
import io.saltpay.tasks.ScrapperCallableTask;
import io.saltpay.utils.ChunkUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadBot {

    public static <T, O> List<O> start(int numberOfThreads, List<T> listOfData, StartScript startScript, ScrapperScript<O> scrapperScript) {
        // Create a thread pool with numberOfThreads
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        List<Future<List<O>>> futuresOfCollectedDataList = new ArrayList<>();
        List<Chunk<T>> listOfChunks = ChunkUtil.prepareChunks(numberOfThreads, listOfData);

        // Submit tasks to the executor
        listOfChunks.forEach(chunk -> {
            Callable<List<O>> callable = new ScrapperCallableTask<>(chunk.hashCode(), chunk, startScript, scrapperScript);

            Future<List<O>> future = executor.submit(callable);

            futuresOfCollectedDataList.add(future);
        });

        List<O> listOfCollectedData = new ArrayList<>();

        // Get ssn data from futures
        futuresOfCollectedDataList.forEach(ssnDataListFuture -> {
            try {
                listOfCollectedData.addAll(ssnDataListFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        // Shutdown the executor when all tasks are completed
        executor.shutdown();

        return listOfCollectedData;
    }
}

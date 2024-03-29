package io.saltpay.robot;

import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.models.chunk.SsnDataChunk;
import io.saltpay.support.Driver;
import io.saltpay.tasks.JaPhoneCallableTask;
import io.saltpay.utils.ChunkUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class JaPhoneThreadBot {
    public static List<ProcuratorPhones> start(int numberOfThreads, List<SsnData> listOfSsn, Driver driver) {
        // Create a thread pool with numberOfThreads
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        List<ProcuratorPhones> procuratorPhonesList = new ArrayList<>();
        List<Future<List<ProcuratorPhones>>> futuresOfProcuratorPhones = new ArrayList<>();

//        List<List<SsnData>> listOfSplitSsnData = ChunkUtil.splitToChunks(CHUNKS, listOfSsn); // this line can split chunks into smaller chunks
        List<SsnDataChunk> listOfSsnDataChunks = ChunkUtil.prepareSsnDataChunks(numberOfThreads, listOfSsn);

        // Submit tasks to the executor
        listOfSsnDataChunks.forEach(chunk -> {
            Callable<List<ProcuratorPhones>> callable = new JaPhoneCallableTask(chunk.hashCode(), driver, chunk);

            Future<List<ProcuratorPhones>> future = executor.submit(callable);

            futuresOfProcuratorPhones.add(future);
        });

        // Get phones data from futures
        futuresOfProcuratorPhones.forEach(phonesListFuture -> {
            try {
                procuratorPhonesList.addAll(phonesListFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        // Shutdown the executor when all tasks are completed
        executor.shutdown();

        return procuratorPhonesList;
    }
}

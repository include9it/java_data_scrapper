package io.saltpay.robot;

import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.models.chunk.SsnDataChunk;
import io.saltpay.support.DriverManager;
import io.saltpay.tasks.JaPhoneCallableTask;
import io.saltpay.utils.ChunkUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static io.saltpay.utils.Constants.CHUNKS;

public class JaPhoneThreadBot {
    public static List<ProcuratorPhones> start(int numberOfThreads, List<SsnData> listOfSsn, DriverManager driverManager) {
        // Create a thread pool with numberOfThreads
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        List<ProcuratorPhones> procuratorPhonesList = new ArrayList<>();
        List<Future<List<ProcuratorPhones>>> futuresOfProcuratorPhones = new ArrayList<>();

        List<List<SsnData>> listOfSplitSsnData = ChunkUtil.splitToChunks(CHUNKS, listOfSsn);
        List<SsnDataChunk> listOfSsnDataChunks = ChunkUtil.prepareSsnDataChunks(numberOfThreads, listOfSplitSsnData.get(0));

        // Submit tasks to the executor
        listOfSsnDataChunks.forEach(chunk -> {
            Callable<List<ProcuratorPhones>> callable = new JaPhoneCallableTask(chunk.hashCode(), driverManager, chunk);

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

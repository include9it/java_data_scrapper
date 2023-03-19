package io.saltpay.tasks;

import io.saltpay.models.Chunk;
import io.saltpay.scripts.ScrapperScript;
import io.saltpay.scripts.StartScript;
import io.saltpay.utils.SaltLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ScrapperCallableTask<T, O> implements Callable<List<O>> {

    private final int threadId;

    private final StartScript startScript;
    private final ScrapperScript<O> scrapperScript;

    private final Chunk<T> chunk;

    private final List<O> listOfCollectedData = new ArrayList<>();

    public ScrapperCallableTask(int threadId, Chunk<T> chunk, StartScript startScript, ScrapperScript<O> scrapperScript) {
        this.threadId = threadId;

        this.startScript = startScript;
        this.scrapperScript = scrapperScript;

        this.chunk = chunk;
    }

    @Override
    public List<O> call() throws Exception {
        SaltLogger.basic("Thread -> " + threadId);

        startScript.start();

        // Get data by value
        chunk.dataList().forEach(value -> {
            SaltLogger.basic("Thread -> " + threadId);

            O collectedRecord = scrapperScript.findAndCollectDataByValue(String.valueOf(value));

            listOfCollectedData.add(collectedRecord);
        });

//        scrapperScript.finish(); TODO

        return listOfCollectedData;
    }
}

package io.saltpay.tasks;

import io.saltpay.models.chunk.SsnChunk;
import io.saltpay.models.SsnData;
import io.saltpay.scrapper.CreditInfoScrapper;
import io.saltpay.support.Driver;
import io.saltpay.utils.SaltLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CreditInfoCallableTask implements Callable<List<SsnData>> {

    private final int threadId;
    private final CreditInfoScrapper creditInfoScrapper;

    private final SsnChunk ssnChunk;

    private final List<SsnData> listOfSsnData = new ArrayList<>();

    public CreditInfoCallableTask(int threadId, Driver driver, SsnChunk ssnChunk) {
        this.threadId = threadId;
        this.creditInfoScrapper = new CreditInfoScrapper(driver);
        this.ssnChunk = ssnChunk;
    }

    @Override
    public List<SsnData> call() throws Exception {
        SaltLogger.basic("Thread -> " + threadId);

        creditInfoScrapper.enterAndLogin();
        creditInfoScrapper.changeLocale();

        // Get Procurators data by SSN number
        ssnChunk.getListOfSsn().forEach(ssn -> {
            SaltLogger.basic("Thread -> " + threadId);

            SsnData ssnData = creditInfoScrapper.findAndCollectDataBySsn(ssn);

            listOfSsnData.add(ssnData);
        });

        creditInfoScrapper.finish();

        return listOfSsnData;
    }
}

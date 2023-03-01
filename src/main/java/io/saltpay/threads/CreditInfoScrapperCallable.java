package io.saltpay.threads;

import io.saltpay.model.SsnChunk;
import io.saltpay.model.SsnData;
import io.saltpay.scrapper.CreditInfoScrapper;
import io.saltpay.utils.SaltLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CreditInfoScrapperCallable implements Callable<List<SsnData>> {

    private final int threadId;
    private final CreditInfoScrapper creditInfoScrapper;

    private final SsnChunk ssnChunk;

    private final List<SsnData> listOfSsnData = new ArrayList<>();

    public CreditInfoScrapperCallable(int threadId, CreditInfoScrapper creditInfoScrapper, SsnChunk ssnChunk) {
        this.threadId = threadId;
        this.creditInfoScrapper = creditInfoScrapper;
        this.ssnChunk = ssnChunk;
    }

    @Override
    public List<SsnData> call() throws Exception {
        SaltLogger.basic("Thread -> " + threadId);

        creditInfoScrapper.enterAndLogin();
        creditInfoScrapper.changeLocale();

        // Get Procurators data by SSN number
        ssnChunk.getListOfSsn().forEach(ssn -> {
            SsnData ssnData = creditInfoScrapper.findAndCollectDataBySsn(ssn);

            listOfSsnData.add(ssnData);
        });

        return listOfSsnData;
    }
}

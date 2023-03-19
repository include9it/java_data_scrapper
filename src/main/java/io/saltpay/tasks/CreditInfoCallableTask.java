package io.saltpay.tasks;

import io.saltpay.models.chunk.SsnChunk;
import io.saltpay.models.SsnData;
import io.saltpay.scripts.CreditInfoScrapperScript;
import io.saltpay.scripts.CreditInfoStartScript;
import io.saltpay.support.Driver;
import io.saltpay.utils.SaltLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CreditInfoCallableTask implements Callable<List<SsnData>> {

    private final int threadId;

    private final CreditInfoStartScript creditInfoStartScript;
    private final CreditInfoScrapperScript creditInfoScrapperScript;

    private final SsnChunk ssnChunk;

    private final List<SsnData> listOfSsnData = new ArrayList<>();

    public CreditInfoCallableTask(int threadId, Driver driver, SsnChunk ssnChunk) {
        this.threadId = threadId;

        this.creditInfoStartScript = new CreditInfoStartScript(driver);
        this.creditInfoScrapperScript = new CreditInfoScrapperScript(driver);

        this.ssnChunk = ssnChunk;
    }

    @Override
    public List<SsnData> call() throws Exception {
        SaltLogger.basic("Thread -> " + threadId);

        creditInfoStartScript.enterAndLogin();
        creditInfoStartScript.changeLocale();

        // Get Procurators data by SSN number
        ssnChunk.listOfSsn().forEach(ssn -> {
            SaltLogger.basic("Thread -> " + threadId);

            SsnData ssnData = creditInfoScrapperScript.findAndCollectDataBySsn(ssn);

            listOfSsnData.add(ssnData);
        });

        creditInfoScrapperScript.finish();

        return listOfSsnData;
    }
}

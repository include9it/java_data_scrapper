package io.saltpay.threads;

import io.saltpay.model.SsnChunk;
import io.saltpay.model.SsnData;
import io.saltpay.scrapper.CreditInfoScrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CreditInfoScrapperCallable implements Callable<List<SsnData>> {

    private final CreditInfoScrapper creditInfoScrapper;

    private final SsnChunk ssnChunk;

    private final List<SsnData> listOfSsnData = new ArrayList<>();

    public CreditInfoScrapperCallable(CreditInfoScrapper creditInfoScrapper, SsnChunk ssnChunk) {
        this.creditInfoScrapper = creditInfoScrapper;
        this.ssnChunk = ssnChunk;
    }

    @Override
    public List<SsnData> call() throws Exception {
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

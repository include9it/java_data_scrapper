package io.saltpay.tasks;

import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.chunk.SsnDataChunk;
import io.saltpay.scrapper.JaPhoneNumberScrapper;
import io.saltpay.support.DriverManager;
import io.saltpay.utils.SaltLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class JaPhoneCallableTask implements Callable<List<ProcuratorPhones>> {

    private final int threadId;
    private final JaPhoneNumberScrapper jaPhoneNumberScrapper;

    private final SsnDataChunk ssnDataChunk;

    private final List<ProcuratorPhones> listOfProcuratorPhones = new ArrayList<>();

    public JaPhoneCallableTask(int threadId, DriverManager driverManager, SsnDataChunk ssnDataChunk) {
        this.threadId = threadId;
        this.jaPhoneNumberScrapper = new JaPhoneNumberScrapper(driverManager);
        this.ssnDataChunk = ssnDataChunk;
    }

    @Override
    public List<ProcuratorPhones> call() throws Exception {
        SaltLogger.basic("Thread -> " + threadId);

        jaPhoneNumberScrapper.enterWebsite();

        // Get Procurator phone numbers by Full Name
        ssnDataChunk.getListOfSsnData().forEach(ssnData ->
                ssnData.getListOfProcurator().forEach(procurator -> {
                    SaltLogger.basic("Thread -> " + threadId);

                    ProcuratorPhones procuratorPhones = jaPhoneNumberScrapper.findAndCollectDataByFullName(
                            procurator.getFullName()
                    );

                    listOfProcuratorPhones.add(procuratorPhones);
                })
        );

        return listOfProcuratorPhones;
    }
}

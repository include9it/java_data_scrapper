package io.saltpay.tasks;

import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.models.Chunk;
import io.saltpay.scripts.JaPhoneScrapperScript;
import io.saltpay.scripts.JaPhoneStartScript;
import io.saltpay.support.Driver;
import io.saltpay.utils.SaltLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class JaPhoneCallableTask implements Callable<List<ProcuratorPhones>> {

    private final int threadId;

    private final JaPhoneStartScript jaPhoneStartScript;
    private final JaPhoneScrapperScript jaPhoneScrapperScript;

    private final Chunk<SsnData> ssnDataChunk;

    private final List<ProcuratorPhones> listOfProcuratorPhones = new ArrayList<>();

    public JaPhoneCallableTask(int threadId, Driver driver, Chunk<SsnData> ssnDataChunk) {
        this.threadId = threadId;

        this.jaPhoneStartScript = new JaPhoneStartScript(driver);
        this.jaPhoneScrapperScript = new JaPhoneScrapperScript(driver);

        this.ssnDataChunk = ssnDataChunk;
    }

    @Override
    public List<ProcuratorPhones> call() throws Exception {
        SaltLogger.basic("Thread -> " + threadId);

        jaPhoneStartScript.start();

        // Get Procurator phone numbers by Full Name
        ssnDataChunk.dataList().forEach(ssnData ->
                ssnData.listOfProcurator().forEach(procurator -> {
                    SaltLogger.basic("Thread -> " + threadId);

                    ProcuratorPhones procuratorPhones = jaPhoneScrapperScript.findAndCollectDataByValue(procurator.fullName());

                    listOfProcuratorPhones.add(procuratorPhones);
                })
        );

        jaPhoneScrapperScript.finish();

        return listOfProcuratorPhones;
    }
}

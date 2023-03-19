package io.saltpay.scrapper;

import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.storage.FileStorageController;
import io.saltpay.utils.SaltLogger;

import java.util.ArrayList;
import java.util.List;

public class JaPhoneScriptWrapper {
    private static final String TAG = JaPhoneScriptWrapper.class.getName();
    private final JaPhoneScrapperScript jaPhoneScrapperScript;
    private final List<SsnData> ssnDataList;
    private final FileStorageController phoneStorage;
    private final List<ProcuratorPhones> listOfProcuratorPhones = new ArrayList<>();

    public JaPhoneScriptWrapper(
            JaPhoneScrapperScript jaPhoneScrapperScript,
            List<SsnData> ssnDataList,
            FileStorageController phoneStorage
    ) {
        this.jaPhoneScrapperScript = jaPhoneScrapperScript;
        this.ssnDataList = ssnDataList;
        this.phoneStorage = phoneStorage;
    }

    public void start() {
        SaltLogger.i(TAG, "Ja Phone Bot started!");

        jaPhoneScrapperScript.enterWebsite();

        // Get phone numbers by Procurator
        ssnDataList.forEach(ssnData ->
                ssnData.listOfProcurator().forEach(procurator -> {
                    ProcuratorPhones phoneNumbers = jaPhoneScrapperScript.findAndCollectDataByFullName(procurator.fullName());

                    phoneStorage.saveData(phoneNumbers);

                    listOfProcuratorPhones.add(phoneNumbers);
                })
        );

        jaPhoneScrapperScript.finish();
    }

    public List<ProcuratorPhones> getListOfProcuratorPhones() {
        return listOfProcuratorPhones;
    }
}

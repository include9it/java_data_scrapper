package io.saltpay.scrapper;

import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.storage.FileStorageController;
import io.saltpay.utils.SaltLogger;

import java.util.ArrayList;
import java.util.List;

public class JaPhoneNumberScriptWrapper {
    private static final String TAG = JaPhoneNumberScriptWrapper.class.getName();
    private final JaPhoneNumberScrapperScript jaPhoneNumberScrapperScript;
    private final List<SsnData> ssnDataList;
    private final FileStorageController phoneStorage;
    private final List<ProcuratorPhones> listOfProcuratorPhones = new ArrayList<>();

    public JaPhoneNumberScriptWrapper(
            JaPhoneNumberScrapperScript jaPhoneNumberScrapperScript,
            List<SsnData> ssnDataList,
            FileStorageController phoneStorage
    ) {
        this.jaPhoneNumberScrapperScript = jaPhoneNumberScrapperScript;
        this.ssnDataList = ssnDataList;
        this.phoneStorage = phoneStorage;
    }

    public void start() {
        SaltLogger.i(TAG, "Ja Phone Bot started!");

        jaPhoneNumberScrapperScript.enterWebsite();

        // Get phone numbers by Procurator
        ssnDataList.forEach(ssnData ->
                ssnData.listOfProcurator().forEach(procurator -> {
                    ProcuratorPhones phoneNumbers = jaPhoneNumberScrapperScript.findAndCollectDataByFullName(procurator.fullName());

                    phoneStorage.saveData(phoneNumbers);

                    listOfProcuratorPhones.add(phoneNumbers);
                })
        );

        jaPhoneNumberScrapperScript.finish();
    }

    public List<ProcuratorPhones> getListOfProcuratorPhones() {
        return listOfProcuratorPhones;
    }
}

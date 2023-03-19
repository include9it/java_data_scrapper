package io.saltpay.scripts;

import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.storage.FileStorageController;
import io.saltpay.support.Driver;
import io.saltpay.utils.SaltLogger;

import java.util.ArrayList;
import java.util.List;

public class JaPhoneScriptController {
    private static final String TAG = JaPhoneScriptController.class.getName();

    private final JaPhoneStartScript jaPhoneStartScript;
    private final JaPhoneScrapperScript jaPhoneScrapperScript;
    private final List<SsnData> ssnDataList;
    private final FileStorageController phoneStorage;
    private final List<ProcuratorPhones> listOfProcuratorPhones = new ArrayList<>();

    public JaPhoneScriptController(
            Driver driver,
            List<SsnData> ssnDataList,
            FileStorageController phoneStorage
    ) {
        this.jaPhoneStartScript = new JaPhoneStartScript(driver);
        this.jaPhoneScrapperScript = new JaPhoneScrapperScript(driver);

        this.ssnDataList = ssnDataList;
        this.phoneStorage = phoneStorage;
    }

    public void start() {
        SaltLogger.i(TAG, "Ja Phone Bot started!");

        jaPhoneStartScript.enterWebsite();

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

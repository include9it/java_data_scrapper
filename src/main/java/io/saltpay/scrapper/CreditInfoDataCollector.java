package io.saltpay.scrapper;

import io.saltpay.model.SsnData;
import io.saltpay.utils.CreditInfoSaveManager;

import java.util.ArrayList;
import java.util.List;

public class CreditInfoDataCollector {
    private final CreditInfo creditInfo;
    private final List<String> listOfSsn;
    private final CreditInfoSaveManager ciSaveManager;
    private final List<SsnData> listOfSsnData = new ArrayList<>();

    public CreditInfoDataCollector(CreditInfo creditInfo, List<String> listOfSsn, CreditInfoSaveManager ciSaveManager) {
        this.creditInfo = creditInfo;
        this.listOfSsn = listOfSsn;
        this.ciSaveManager = ciSaveManager;
    }

    public void start() {
        creditInfo.enterAndLogin();
        creditInfo.changeLocale();

        // Get Procurators data by SSN number
        listOfSsn.forEach(ssn -> {
            SsnData ssnData = creditInfo.findAndCollectDataBySsn(ssn);

            ciSaveManager.saveSsnData(ssnData);

            listOfSsnData.add(ssnData);
        });
    }

    public List<SsnData> getListOfSsnData() {
        return listOfSsnData;
    }
}

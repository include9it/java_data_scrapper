package io.saltpay.utils;

import io.saltpay.models.Procurator;
import io.saltpay.models.SsnData;
import io.saltpay.storage.CreditInfoStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.CREDIT_INFO_BACKUP_FILE;
import static io.saltpay.utils.Constants.CREDIT_INFO_WRITE_FILE;

public class RestoreManager {
    public static void restoreModel() throws IOException {
        ExcelManager excelManager = new ExcelManager();
        List<String> ssnList = excelManager.getColumnData(CREDIT_INFO_WRITE_FILE, 0);
        List<String> nameList = excelManager.getColumnData(CREDIT_INFO_WRITE_FILE, 1);
        List<String> codeList = excelManager.getColumnData(CREDIT_INFO_WRITE_FILE, 2);

        List<Procurator> procuratorList = new ArrayList<>();

        int index = 0;
        for (String name : nameList) {
            procuratorList.add(new Procurator(name, codeList.get(index)));
        }

        List<SsnData> ssnDataList = new ArrayList<>();

        List<Procurator> procuratorChunk = new ArrayList<>();

        int indexCurrent = 0, indexNext = 1;
        String nextSsn = ssnList.get(indexNext);
        for (String ssn : ssnList) {
            procuratorChunk.add(procuratorList.get(indexCurrent));

            if (!ssn.equals(nextSsn)) {
                ssnDataList.add(new SsnData(ssn, procuratorChunk));

                procuratorChunk = new ArrayList<>();
            }

            if (ssnList.size() == indexNext) {
                break;
            }

            indexCurrent++;
            indexNext++;
        }

        CreditInfoStorage ciSaveManager = new CreditInfoStorage();
        ciSaveManager.saveSsnData(CREDIT_INFO_BACKUP_FILE, ssnDataList);

        SaltLogger.displaySsnData(ciSaveManager.readSavedSsnData(CREDIT_INFO_BACKUP_FILE));
    }
}

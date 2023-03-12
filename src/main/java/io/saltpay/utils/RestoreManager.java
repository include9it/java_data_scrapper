package io.saltpay.utils;

import io.saltpay.models.Procurator;
import io.saltpay.models.SsnData;
import io.saltpay.storage.StorageController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.*;

public class RestoreManager {
    public static void restoreModel() throws IOException {
        ExcelManager excelManager = new ExcelManager();
        List<String> ssnList = excelManager.getColumnData(MERGED_WRITE_FILE, 0);
        List<String> nameList = excelManager.getColumnData(MERGED_WRITE_FILE, 1);
        List<String> codeList = excelManager.getColumnData(MERGED_WRITE_FILE, 2);

        List<Procurator> procuratorList = new ArrayList<>();

        int index = 0;
        for (String name : nameList) {
            procuratorList.add(new Procurator(name, codeList.get(index)));

            index++;
        }

        List<SsnData> ssnDataList = new ArrayList<>();

        List<Procurator> procuratorChunk = new ArrayList<>();

        int listSize = ssnList.size();
        int indexCurrent = 0, indexNext = 1;
        String nextSsn = ssnList.get(indexNext);
        for (String ssn : ssnList) {
            procuratorChunk.add(procuratorList.get(indexCurrent));

            if (!ssn.equals(nextSsn)) {
                ssnDataList.add(new SsnData(ssn, procuratorChunk));

                procuratorChunk = new ArrayList<>();
            }

            indexCurrent++;
            indexNext++;

            if (indexNext == listSize) {
                procuratorChunk.add(procuratorList.get(indexCurrent));

                ssnDataList.add(new SsnData(nextSsn, procuratorChunk));

                break;
            }

            nextSsn = ssnList.get(indexNext);
        }

        StorageController ssnStorage = new StorageController(CREDIT_INFO_BACKUP_FILE);
//        ssnStorage.saveSsnData(CREDIT_INFO_BACKUP_FILE, ssnDataList);

        SaltLogger.basic("CREDIT_INFO_BACKUP_FILE -> ");
//        SaltLogger.displaySsnData(Arrays.asList(ssnStorage.readData().get(ssnDataList.size() - 1)));
//        SaltLogger.displaySsnData(ssnStorage.readSavedSsnData(CREDIT_INFO_BACKUP_FILE));
    }
}

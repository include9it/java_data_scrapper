package io.saltpay;

import io.saltpay.models.Procurator;
import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.robot.CreditInfoRobot;
import io.saltpay.robot.JaPhoneRobot;
import io.saltpay.storage.CreditInfoStorage;
import io.saltpay.storage.JaPhoneStorage;
import io.saltpay.support.DriverManager;
import io.saltpay.utils.ExcelManager;
import io.saltpay.utils.ListUtil;
import io.saltpay.utils.SaltLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.*;

public class Main {

    public static void main(String[] args) throws Exception {
        DriverManager driverManager = new DriverManager();

        // CreditInfo Island company registry
        CreditInfoRobot creditInfoRobot = new CreditInfoRobot(driverManager);
//        creditInfoRobot.basicCollect();
//        creditInfoRobot.multiThreadCollect();

        // Phone number registry
        JaPhoneRobot jaPhoneRobot = new JaPhoneRobot(driverManager);
//        jaPhoneRobot.basicCollect();
        jaPhoneRobot.multiThreadCollect();

//        mergeFilterAndCollect();

//        restoreModel();
    }

    private static void mergeFilterAndCollect() {
        CreditInfoStorage ciSaveManager = new CreditInfoStorage();
        JaPhoneStorage jaPhoneStorage = new JaPhoneStorage();

        List<SsnData> savedThreadSsnData = ciSaveManager.readSavedSsnData(CREDIT_INFO_BACKUP_FILE);
        SaltLogger.basic("savedSsnThreadData size: " + savedThreadSsnData.size());

        List<ProcuratorPhones> savedThreadPhonesData = jaPhoneStorage.readSavedPhonesData(JA_PHONE_BACKUP_FILE);
        SaltLogger.basic("savedThreadPhonesData size: " + savedThreadPhonesData.size());

        boolean hasDuplicates = ListUtil.hasDuplicateIdentifiers(savedThreadPhonesData, ProcuratorPhones::getFullName);
        SaltLogger.basic("savedThreadPhonesData has duplicate names -> " + hasDuplicates);

        if (hasDuplicates) {
            savedThreadPhonesData = ListUtil.removeDuplicates(savedThreadPhonesData, ProcuratorPhones::getFullName);

            SaltLogger.basic("Duplicates - removed");
        }

        ///

    }

    private static void restoreModel() throws IOException {
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
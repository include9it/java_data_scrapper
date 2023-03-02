package io.saltpay;

import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.robot.JaPhoneRobot;
import io.saltpay.storage.CreditInfoStorage;
import io.saltpay.storage.JaPhoneStorage;
import io.saltpay.support.DriverManager;
import io.saltpay.utils.ListUtil;
import io.saltpay.utils.SaltLogger;

import java.util.List;

import static io.saltpay.utils.Constants.CREDIT_INFO_BACKUP_FILE;
import static io.saltpay.utils.Constants.JA_PHONE_BACKUP_FILE;

public class Main {

    public static void main(String[] args) throws Exception {
        DriverManager driverManager = new DriverManager();

        // CreditInfo Island company registry
//        CreditInfoRobot creditInfoRobot = new CreditInfoRobot(driverManager);
//        creditInfoRobot.basicCollect();
//        creditInfoRobot.multiThreadCollect();

        // Phone number registry
        JaPhoneRobot jaPhoneRobot = new JaPhoneRobot(driverManager);
        jaPhoneRobot.basicCollect();
//        jaPhoneRobot.multiThreadCollect();

//        mergeFilterAndCollect();
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
}
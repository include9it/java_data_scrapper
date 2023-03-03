package io.saltpay.utils;

import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.models.excel.ExcelData;
import io.saltpay.models.excel.SheetData;
import io.saltpay.storage.CreditInfoStorage;
import io.saltpay.storage.JaPhoneStorage;

import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.*;

public class DataPreparation {
    public static void mergeFilterAndCollect() {
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
        SheetData phonesSheet = DataCollectUtil.collectMergedData(savedThreadSsnData, savedThreadPhonesData);

        List<SheetData> dataSheets = new ArrayList<>();
        dataSheets.add(phonesSheet);

        // Create new doc with target info
        ExcelManager excelManager = new ExcelManager();
        ExcelData excelData = new ExcelData(RESOURCE_FILE_PATH + MERGED_WRITE_FILE, dataSheets);
        excelManager.writeExcel(excelData);
    }
}

package io.saltpay.utils;

import io.saltpay.model.Procurator;
import io.saltpay.model.ProcuratorPhones;
import io.saltpay.model.SsnData;
import io.saltpay.model.excel.ExcelData;
import io.saltpay.model.excel.SheetData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.*;

public class JaPhoneProcuratorPhoneManager {
    private static final String TAG = JaPhoneProcuratorPhoneManager.class.getName();

    private final ExcelManager excelManager = new ExcelManager();

    public List<SsnData> preparePhonesStartData(CreditInfoSaveManager ciSaveManager, JaPhoneSaveManager jaPhoneSaveManager) {
        // Get list of SSN values
        List<SsnData> listOfSsnData = ciSaveManager.readSavedSsnData(CREDIT_INFO_BACKUP_FILE);

        if (listOfSsnData == null) {
            return null;
        }

        List<ProcuratorPhones> procuratorPhonesList = jaPhoneSaveManager.readSavedPhonesData(JA_PHONE_BACKUP_FILE);

        if (procuratorPhonesList != null) {
            int lastEntryIndex = procuratorPhonesList.size() - 1;

            ProcuratorPhones lastPhonesEntry = procuratorPhonesList.get(lastEntryIndex);

            SsnData targetSsnData = findTargetSsnData(listOfSsnData, lastPhonesEntry);

            if (targetSsnData == null) {
                return listOfSsnData;
            }

            int sizeOfSsnList = listOfSsnData.size();
            int cutIndex = listOfSsnData.indexOf(targetSsnData);

            return listOfSsnData.subList(cutIndex, sizeOfSsnList);
        }

        return listOfSsnData;
    }

    public void prepareExcelWithProcuratorPhoneData(List<ProcuratorPhones> listOfProcuratorPhones) {
        SaltLogger.i(TAG, "Preparing Excel file...");

        SheetData phonesSheet = DataCollectUtil.collectPhonesSheetData(listOfProcuratorPhones);

        List<SheetData> dataSheets = new ArrayList<>();
        dataSheets.add(phonesSheet);

        // Create new doc with target info
        ExcelData excelData = new ExcelData(RESOURCE_FILE_PATH + JA_PHONE_WRITE_FILE, dataSheets);
        excelManager.writeExcel(excelData);
    }

    private SsnData findTargetSsnData(List<SsnData> listOfSsnData, ProcuratorPhones lastPhonesEntry) {
        // This search will make data duplication!
        for (SsnData ssnData : listOfSsnData) {
            for (Procurator procurator : ssnData.getListOfProcurator()) {
                if (procurator.getFullName().equals(lastPhonesEntry.getFullName())) {
                    return ssnData;
                }
            }
        }
        return null;
    }
}
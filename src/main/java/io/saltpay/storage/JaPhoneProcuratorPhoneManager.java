package io.saltpay.storage;

import io.saltpay.models.Procurator;
import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.models.excel.ExcelData;
import io.saltpay.models.excel.SheetData;
import io.saltpay.utils.DataCollectUtil;
import io.saltpay.utils.SaltLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.saltpay.utils.Constants.*;

public class JaPhoneProcuratorPhoneManager {
    private static final String TAG = JaPhoneProcuratorPhoneManager.class.getName();

    private final ExcelManager excelManager = new ExcelManager();

    public List<SsnData> preparePhonesStartData(StorageController ssnStorage, StorageController phoneStorage) {
        // Get list of SSN values
        List<SsnData> listOfSsnData = ssnStorage.readData();

        if (listOfSsnData == null) {
            return null;
        }

        List<ProcuratorPhones> procuratorPhonesList = phoneStorage.readData();

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

    public List<SsnData> preparePhonesStartDataV2() throws IOException {
        List<String> listOfNames = excelManager.getColumnData("Soletrader data - ja.is.xlsx", 0);
        List<String> listOfSurenames = excelManager.getColumnData("Soletrader data - ja.is.xlsx", 1);

        List<String> listOfFullNames = new ArrayList<>();

        int index = 0;
        for (String name : listOfNames) {
            listOfFullNames.add(name + " " + listOfSurenames.get(index));
            index++;
        }

        List<Procurator> soleTraders = new ArrayList<>();
        listOfFullNames.forEach(name -> soleTraders.add(new Procurator(name, "")));

        List<SsnData> listOfSsnData = new ArrayList<>();

        soleTraders.forEach(trader -> listOfSsnData.add(new SsnData("", Arrays.asList(trader))));

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

    public void prepareExcelWithProcuratorPhoneDataV2(List<ProcuratorPhones> listOfProcuratorPhones) {
        SaltLogger.i(TAG, "Preparing Excel file...");

        SheetData phonesSheet = DataCollectUtil.collectPhonesSheetData(listOfProcuratorPhones);

        List<SheetData> dataSheets = new ArrayList<>();
        dataSheets.add(phonesSheet);

        // Create new doc with target info
        ExcelData excelData = new ExcelData(RESOURCE_FILE_PATH + JA_PHONE_TRADER_WRITE_FILE, dataSheets);
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

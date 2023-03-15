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
import java.util.Collections;
import java.util.List;

import static io.saltpay.utils.Constants.*;

public class JaPhoneProcuratorPhoneManager {
    private static final String TAG = JaPhoneProcuratorPhoneManager.class.getName();

    private final ExcelController excelController = new ExcelController();

    public List<SsnData> preparePhonesStartDataV2(StorageController ssnStorage, StorageController phoneStorage) {
        // Get list of all SSN values
        List<SsnData> listOfSsnData = ssnStorage.readData();

        if (listOfSsnData == null) {
            return null;
        }

        return new DataPreparationManager().prepareStartData(listOfSsnData, phoneStorage);
    }

    public List<SsnData> preparePhonesStartData(StorageController ssnStorage, StorageController phoneStorage) {
        // Get list of all SSN values
        List<SsnData> listOfSsnData = ssnStorage.readData();

        if (listOfSsnData == null) {
            return null;
        }

        // Get list of saved phone numbers
        List<ProcuratorPhones> procuratorPhonesList = phoneStorage.readData();

        if (procuratorPhonesList != null) {
            SsnData targetSsnData = findTargetValue(listOfSsnData, procuratorPhonesList);

            if (targetSsnData == null) {
                return listOfSsnData;
            }

            return extractLeftData(listOfSsnData, targetSsnData);
        }

        return listOfSsnData;
    }

    public List<SsnData> preparePhonesStartDataV2() throws IOException {
        List<String> listOfNames = excelController.getColumnData("Soletrader data - ja.is.xlsx", 0);
        List<String> listOfSurenames = excelController.getColumnData("Soletrader data - ja.is.xlsx", 1);

        List<String> listOfFullNames = new ArrayList<>();

        int index = 0;
        for (String name : listOfNames) {
            listOfFullNames.add(name + " " + listOfSurenames.get(index));
            index++;
        }

        List<Procurator> soleTraders = new ArrayList<>();
        listOfFullNames.forEach(name -> soleTraders.add(new Procurator(name, "")));

        List<SsnData> listOfSsnData = new ArrayList<>();

        soleTraders.forEach(trader -> listOfSsnData.add(new SsnData("", Collections.singletonList(trader))));

        return listOfSsnData;
    }

    public void prepareExcelWithProcuratorPhoneData(List<ProcuratorPhones> listOfProcuratorPhones) {
        SaltLogger.i(TAG, "Preparing Excel file...");

        SheetData phonesSheet = DataCollectUtil.collectPhonesSheetData(listOfProcuratorPhones);

        // Create new doc with target info
        ExcelData excelData = new ExcelData(RESOURCE_FILE_PATH + JA_PHONE_WRITE_FILE, Collections.singletonList(phonesSheet));
        excelController.writeExcel(excelData);
    }

    public void prepareExcelWithProcuratorPhoneDataV2(List<ProcuratorPhones> listOfProcuratorPhones) {
        SaltLogger.i(TAG, "Preparing Excel file...");

        SheetData phonesSheet = DataCollectUtil.collectPhonesSheetData(listOfProcuratorPhones);

        // Create new doc with target info
        ExcelData excelData = new ExcelData(RESOURCE_FILE_PATH + JA_PHONE_TRADER_WRITE_FILE, Collections.singletonList(phonesSheet));
        excelController.writeExcel(excelData);
    }

    private SsnData findTargetValue(List<SsnData> listOfSsnData, List<ProcuratorPhones> procuratorPhonesList) {
        int lastEntryIndex = procuratorPhonesList.size() - 1;
        ProcuratorPhones lastPhonesEntry = procuratorPhonesList.get(lastEntryIndex);

        // This search will make data duplication!
        for (SsnData ssnData : listOfSsnData) {
            for (Procurator procurator : ssnData.listOfProcurator()) {
                if (procurator.fullName().equals(lastPhonesEntry.fullName())) {
                    return ssnData;
                }
            }
        }
        return null;
    }

    private List<SsnData> extractLeftData(List<SsnData> listOfSsnData, SsnData targetSsnData) {
        int sizeOfSsnList = listOfSsnData.size();
        int cutIndex = listOfSsnData.indexOf(targetSsnData);

        return listOfSsnData.subList(cutIndex, sizeOfSsnList);
    }
}

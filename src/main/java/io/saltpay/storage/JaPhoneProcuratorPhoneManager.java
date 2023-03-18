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

    public List<SsnData> preparePhonesStartData(StorageController ssnStorage, StorageController phoneStorage) {
        // Get list of all SSN values
        List<SsnData> listOfSsnData = ssnStorage.readData();

        if (listOfSsnData == null) {
            return null;
        }

        // Get list of saved phone numbers
        List<ProcuratorPhones> procuratorPhonesList = phoneStorage.readData();

        if (procuratorPhonesList != null) {
            ProcuratorPhones lastPhoneEntry = findLastValue(procuratorPhonesList);
            SsnData targetSsnData = findLastValue(listOfSsnData, lastPhoneEntry);

            if (targetSsnData == null) {
                return listOfSsnData;
            }

            return extractLeftData(listOfSsnData, targetSsnData);
        }

        return listOfSsnData;
    }

    public List<SsnData> preparePhonesStartDataSoleTrader() throws IOException {
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

    public void prepareExcelWithProcuratorPhoneDataSoleTrader(List<ProcuratorPhones> listOfProcuratorPhones) {
        SaltLogger.i(TAG, "Preparing Excel file...");

        SheetData phonesSheet = DataCollectUtil.collectPhonesSheetData(listOfProcuratorPhones);

        // Create new doc with target info
        ExcelData excelData = new ExcelData(RESOURCE_FILE_PATH + JA_PHONE_TRADER_WRITE_FILE, Collections.singletonList(phonesSheet));
        excelController.writeExcel(excelData);
    }

    private ProcuratorPhones findLastValue(List<ProcuratorPhones> procuratorPhonesList) {
        int lastEntryIndex = procuratorPhonesList.size() - 1;

        return procuratorPhonesList.get(lastEntryIndex);
    }

    private SsnData findLastValue(List<SsnData> listOfSsnData, ProcuratorPhones lastPhoneEntry) {
        // This search will make data duplication!
        return listOfSsnData.stream().filter(ssnData ->
                        ssnData.listOfProcurator().stream().anyMatch(procurator ->
                                procurator.fullName().equals(lastPhoneEntry.fullName())))
                .findFirst()
                .orElse(null);
    }

    private List<SsnData> extractLeftData(List<SsnData> listOfSsnData, SsnData targetSsnData) {
        int sizeOfSsnList = listOfSsnData.size();
        int cutIndex = listOfSsnData.indexOf(targetSsnData);

        return listOfSsnData.subList(cutIndex, sizeOfSsnList);
    }
}

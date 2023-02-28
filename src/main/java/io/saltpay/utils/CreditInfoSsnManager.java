package io.saltpay.utils;

import io.saltpay.model.SsnData;
import io.saltpay.model.excel.ExcelData;
import io.saltpay.model.excel.SheetData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.CREDIT_INFO_WRITE_FILE;
import static io.saltpay.utils.Constants.RESOURCE_FILE_PATH;

public class CreditInfoSsnManager {
    private final ExcelManager excelManager = new ExcelManager();

    public List<String> prepareSsnStartData(CreditInfoSaveManager ciSaveManager) throws IOException {
        // Get list of SSN values
        List<String> listOfSsn = excelManager.getFirstColumnData();

        List<SsnData> savedSsnList = ciSaveManager.readSavedSsnData();

        if (savedSsnList != null) {
            int lastEntryIndex = savedSsnList.size() - 1;

            SsnData lastSsnEntry = savedSsnList.get(lastEntryIndex);

            int sizeOfSsnList = listOfSsn.size();
            int cutIndex = listOfSsn.indexOf(lastSsnEntry.getSsnValue());

            return listOfSsn.subList(cutIndex, sizeOfSsnList);
        }

        return listOfSsn;
    }

    public void prepareExcelWithSsnData(List<SsnData> listOfSsnData) {
        SheetData ssnSheet = DataCollectUtil.collectSheetData(listOfSsnData);

        List<SheetData> dataSheets = new ArrayList<>();
        dataSheets.add(ssnSheet);

        // Create new doc with target info
        ExcelData excelData = new ExcelData(RESOURCE_FILE_PATH + CREDIT_INFO_WRITE_FILE, dataSheets);
        excelManager.writeExcel(excelData);
    }
}

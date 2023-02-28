package io.saltpay.model.excel;

import java.util.List;

public class ExcelData {
    private final String excelName;
    private final List<SheetData> sheets;

    public ExcelData(String excelName, List<SheetData> sheets) {
        this.excelName = excelName;
        this.sheets = sheets;
    }

    public String getExcelName() {
        return excelName;
    }

    public List<SheetData> getSheets() {
        return sheets;
    }
}

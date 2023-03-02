package io.saltpay.models.excel;

import java.util.List;

public class SheetData {
    private final String sheetName;
    private final List<ColumnData> listOfColumns;

    public SheetData(String sheetName, List<ColumnData> listOfColumns) {
        this.sheetName = sheetName;
        this.listOfColumns = listOfColumns;
    }

    public String getSheetName() {
        return sheetName;
    }

    public List<ColumnData> getListOfColumns() {
        return listOfColumns;
    }
}

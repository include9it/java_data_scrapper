package io.saltpay.models.excel;

import java.util.List;

public record SheetData(String sheetName, List<ColumnData> listOfColumns) {
}

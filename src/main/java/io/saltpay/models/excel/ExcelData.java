package io.saltpay.models.excel;

import java.util.List;

public record ExcelData(String excelName, List<SheetData> sheets) {
}

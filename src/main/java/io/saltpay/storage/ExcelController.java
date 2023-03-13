package io.saltpay.storage;

import io.saltpay.utils.SaltLogger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static io.saltpay.utils.Constants.RESOURCE_FILE_PATH;

public class ExcelController extends ExcelStorage {

    public List<String> getColumnData(String fileName, int column) throws IOException {
        XSSFSheet sheet = readSheet(RESOURCE_FILE_PATH + fileName, 0);

        return readColumnData(sheet, column).stream()
                .skip(1)
                .toList();
    }

    private List<String> readColumnData(XSSFSheet sheet, int column) {
        return StreamSupport.stream(sheet.spliterator(), false)
                .map(row -> row.getCell(column))
                .filter(Objects::nonNull)
                .map(Cell::getStringCellValue)
                .peek(SaltLogger::basic)
                .collect(Collectors.toList());
    }

    private XSSFSheet readSheet(String filePath, int sheetNumber) throws IOException {
        XSSFWorkbook workbook = readExcel(filePath);

        // Get the sheet by number from the workbook
        XSSFSheet sheet = workbook.getSheetAt(sheetNumber);

        // Close the workbook
        workbook.close();

        return sheet;
    }
}

package io.saltpay.storage;

import io.saltpay.models.excel.ColumnData;
import io.saltpay.models.excel.ExcelData;
import io.saltpay.utils.SaltLogger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import static io.saltpay.utils.Constants.RESOURCE_FILE_PATH;

public class ExcelManager {

    public List<String> getColumnData(String fileName, int column) throws IOException {
        XSSFSheet sheet = readSheet(RESOURCE_FILE_PATH + fileName, 0);

        return readColumnData(sheet, column).stream()
                .skip(1)
                .toList();
    }

    public void writeExcel(ExcelData excelData) {
        // Create a new workbook
        Workbook workbook = new XSSFWorkbook();

        excelData.getSheets().forEach(sheetData -> {
            Sheet sheet = workbook.createSheet(sheetData.getSheetName());

            writeRowsAndColumns(sheet, sheetData.getListOfColumns());
        });

        createExcel(excelData.getExcelName(), workbook);
    }

    private void writeRowsAndColumns(Sheet sheet, List<ColumnData> listOfColumnData) {
        // write headers
        Row headerRow = sheet.createRow(0);
        IntStream.range(0, listOfColumnData.size())
                .forEach(i -> headerRow.createCell(i).setCellValue(listOfColumnData.get(i).getHeaderName()));

        // write data
        IntStream.range(0, listOfColumnData.get(0).getValues().size())
                .forEach(rowIndex ->
                        IntStream.range(0, listOfColumnData.size())
                                .forEach(colIndex ->
                                        sheet.createRow(rowIndex + 1)
                                                .createCell(colIndex)
                                                .setCellValue(listOfColumnData.get(colIndex).getValues().get(rowIndex))
                                )
                );
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

        // Close the workbook
        workbook.close();

        // Get the sheet by number from the workbook
        return workbook.getSheetAt(sheetNumber);
    }

    private XSSFWorkbook readExcel(String filePath) throws IOException {
        // Specify the path of the .xlsx file
        File file = new File(filePath);

        // Create an input stream to read the file
        FileInputStream fis = new FileInputStream(file);

        // Create a workbook object from the input stream
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        // Close the input stream
        fis.close();

        return workbook;
    }

    private void createExcel(String fileName, Workbook workbook) {
        // Write the workbook to a file
        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package io.saltpay.utils;

import io.saltpay.models.excel.ColumnData;
import io.saltpay.models.excel.ExcelData;
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
import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.CREDIT_INFO_READ_FILE;
import static io.saltpay.utils.Constants.RESOURCE_FILE_PATH;

public class ExcelManager {

    public List<String> getFirstColumnData() throws IOException {
        XSSFSheet sheet = readExcel(RESOURCE_FILE_PATH + CREDIT_INFO_READ_FILE);

        return readColumnData(sheet, 0).stream()
                .skip(1)
                .toList();
    }

    public void writeExcel(ExcelData excelData) {
        // Create a new workbook
        Workbook workbook = new XSSFWorkbook();

        excelData.getSheets().forEach(sheetData -> {
            Sheet sheet = workbook.createSheet(sheetData.getSheetName());

            List<ColumnData> listOfColumnData = sheetData.getListOfColumns();

            writeRowsAndColumns(sheet, listOfColumnData);
        });

        createExcel(excelData.getExcelName(), workbook);
    }

    private void writeRowsAndColumns(Sheet sheet, List<ColumnData> listOfColumnData) {
        int rowNum = 0;
        int colNum = 0;
        Row row = sheet.createRow(rowNum++);

        // write headers
        for (ColumnData column : listOfColumnData) {
            Cell cell = row.createCell(colNum++);
            cell.setCellValue(column.getHeaderName());
        }

        // write data
        for (int i = 0; i < listOfColumnData.get(0).getValues().size(); i++) {
            row = sheet.createRow(rowNum++);
            colNum = 0;
            for (ColumnData column : listOfColumnData) {
                Cell cell = row.createCell(colNum++);
                cell.setCellValue(column.getValues().get(i));
            }
        }
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

    private XSSFSheet readExcel(String filePath) throws IOException {
        // Specify the path of the .xlsx file
        File file = new File(filePath);

        // Create an input stream to read the file
        FileInputStream fis = new FileInputStream(file);

        // Create a workbook object from the input stream
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        // Get the first sheet from the workbook
        XSSFSheet sheet = workbook.getSheetAt(0);

        // Close the workbook and input stream
        workbook.close();
        fis.close();

        return sheet;
    }

    private List<String> readColumnData(XSSFSheet sheet, int column) {
        // Create a list to store the data from the column
        List<String> columnData = new ArrayList<>();

        // Get the iterator to iterate over all the rows in the sheet
        sheet.forEach(row -> {
            Cell cell = row.getCell(column);
            if (cell != null) {
                columnData.add(cell.getStringCellValue());
            }
        });

        // Print the data from the column
        for (String value : columnData) {
            SaltLogger.basic(value);
        }

        return columnData;
    }
}

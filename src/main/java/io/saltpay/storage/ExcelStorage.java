package io.saltpay.storage;

import io.saltpay.models.excel.ColumnData;
import io.saltpay.models.excel.ExcelData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

public class ExcelStorage {

    public void writeExcel(ExcelData excelData) {
        // Create a new workbook
        Workbook workbook = new XSSFWorkbook();

        excelData.sheets().forEach(sheetData -> {
            Sheet sheet = workbook.createSheet(sheetData.sheetName());

            writeRowsAndColumns(sheet, sheetData.listOfColumns());
        });

        createExcel(excelData.excelName(), workbook);
    }

    XSSFWorkbook readExcel(String filePath) throws IOException {
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

    private void writeRowsAndColumns(Sheet sheet, List<ColumnData> listOfColumnData) {
        // write headers
        Row headerRow = sheet.createRow(0);
        IntStream.range(0, listOfColumnData.size())
                .forEach(i -> headerRow.createCell(i).setCellValue(listOfColumnData.get(i).headerName()));

        // write data
        IntStream.range(0, listOfColumnData.get(0).values().size())
                .forEach(rowIndex ->
                        IntStream.range(0, listOfColumnData.size())
                                .forEach(colIndex ->
                                        sheet.createRow(rowIndex + 1)
                                                .createCell(colIndex)
                                                .setCellValue(listOfColumnData.get(colIndex).values().get(rowIndex))
                                )
                );
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

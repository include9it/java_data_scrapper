package io.saltpay.utils;

import io.saltpay.model.Procurator;
import io.saltpay.model.SsnData;
import io.saltpay.model.excel.ColumnData;
import io.saltpay.model.excel.SheetData;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class DataCollectUtil {

    public static SheetData collectSheetData(List<SsnData> listOfSsnData) {
        List<String> listOfFullName = new ArrayList<>();
        List<String> listOfPersonalCodes = new ArrayList<>();

        List<ColumnData> columnData = new ArrayList<>();

        boolean hasToWriteHeaders = true;

        String headerName = "Full Name", headerCode = "Personal code";

        for (SsnData ssnData : listOfSsnData) {
            columnData.add(new ColumnData(ssnData.getSsnValue(), new ArrayList<>()));

            ssnData.getListOfProcurator().forEach(data -> {
                listOfFullName.add(data.getFullName());
                listOfPersonalCodes.add(data.getPersonalCode());
            });

            columnData.add(new ColumnData(headerName, listOfFullName));
            columnData.add(new ColumnData(headerCode, listOfPersonalCodes));

            if (hasToWriteHeaders) {
                headerName = "";
                headerCode = "";

                hasToWriteHeaders = false;
            }
        }

        return new SheetData("Procurator list", columnData);
    }

    public static SheetData collectProcuratorSheetData(List<Procurator> listOfProcurator) {
        List<String> listOfFullName = new ArrayList<>();
        List<String> listOfPersonalCodes = new ArrayList<>();

        listOfProcurator.forEach(data -> {
            listOfFullName.add(data.getFullName());
            listOfPersonalCodes.add(data.getPersonalCode());
        });

        List<ColumnData> columnData = new ArrayList<>();

        columnData.add(new ColumnData("Full Name", listOfFullName));
        columnData.add(new ColumnData("Personal code", listOfPersonalCodes));

        return new SheetData("Procurator list", columnData);
    }

    public static List<Procurator> collectProcuratorsData(List<List<WebElement>> procurators) {
        List<Procurator> listOfProcurator = new ArrayList<>();

        procurators.forEach(proc -> listOfProcurator.add(collectProcuratorData(proc)));

        return listOfProcurator;
    }

    private static Procurator collectProcuratorData(List<WebElement> cellData) {
        String fullName = cellData.get(0).getText();
        String personalCode = cellData.get(1).getText();

        return new Procurator(fullName, StringUtil.removeParentheses(personalCode));
    }
}

package io.saltpay.utils;

import io.saltpay.model.PhoneNumbers;
import io.saltpay.model.Procurator;
import io.saltpay.model.ProcuratorPhones;
import io.saltpay.model.SsnData;
import io.saltpay.model.excel.ColumnData;
import io.saltpay.model.excel.SheetData;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class DataCollectUtil {

    public static SheetData collectSsnSheetData(List<SsnData> listOfSsnData) {
        List<String> listOfFullName = new ArrayList<>();
        List<String> listOfPersonalCodes = new ArrayList<>();
        List<String> ssnList = new ArrayList<>();

        List<ColumnData> columnData = new ArrayList<>();

        String headerName = "Full Name", headerCode = "Personal code", headerSsn = "Company SSN";

        for (SsnData ssnData : listOfSsnData) {
            ssnData.getListOfProcurator().forEach(data -> {
                listOfFullName.add(data.getFullName());
                listOfPersonalCodes.add(StringUtil.removeDash(data.getPersonalCode()));
                ssnList.add(ssnData.getSsnValue());
            });
        }

        columnData.add(new ColumnData(headerSsn, ssnList));
        columnData.add(new ColumnData(headerName, listOfFullName));
        columnData.add(new ColumnData(headerCode, listOfPersonalCodes));

        return new SheetData("Procurator list", columnData);
    }

    public static SheetData collectPhonesSheetData(List<ProcuratorPhones> listOfProcuratorPhones) {
        List<String> listOfFullName = new ArrayList<>();
        List<String> listOfPhone1 = new ArrayList<>();
        List<String> listOfPhone2 = new ArrayList<>();

        List<ColumnData> columnData = new ArrayList<>();

        String headerName = "Full Name", headerPhone1 = "Phone 1", headerPhone2 = "Phone 2";

        for (ProcuratorPhones procuratorPhones : listOfProcuratorPhones) {
            listOfFullName.add(procuratorPhones.getFullName());
            listOfPhone1.add(procuratorPhones.getPhoneNumbers().getPhone1());
            listOfPhone2.add(procuratorPhones.getPhoneNumbers().getPhone2());
        }

        columnData.add(new ColumnData(headerName, listOfFullName));
        columnData.add(new ColumnData(headerPhone1, listOfPhone1));
        columnData.add(new ColumnData(headerPhone2, listOfPhone2));

        return new SheetData("Procurator list", columnData);
    }

    public static List<Procurator> collectProcuratorsData(List<List<WebElement>> procurators) {
        List<Procurator> listOfProcurator = new ArrayList<>();

        procurators.forEach(proc -> listOfProcurator.add(collectProcuratorData(proc)));

        return listOfProcurator;
    }

    public static PhoneNumbers collectProcuratorPhones(List<WebElement> phoneNumbers) {
        List<String> phones = new ArrayList<>();

        phoneNumbers.forEach(phone -> phones.add(collectPhoneNumbersData(phone)));

        return new PhoneNumbers(phones.get(0), phones.get(1));
    }

    private static Procurator collectProcuratorData(List<WebElement> cellData) {
        String fullName = cellData.get(0).getText();
        String personalCode = cellData.get(1).getText();

        return new Procurator(fullName, StringUtil.removeParentheses(personalCode));
    }

    private static String collectPhoneNumbersData(WebElement elementData) {
        return elementData.getText();
    }
}

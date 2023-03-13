package io.saltpay.utils;

import io.saltpay.models.PhoneNumbers;
import io.saltpay.models.Procurator;
import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.models.excel.ColumnData;
import io.saltpay.models.excel.SheetData;
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
            ssnData.listOfProcurator().forEach(data -> {
                listOfFullName.add(data.fullName());
                listOfPersonalCodes.add(StringUtil.removeDash(data.personalCode()));
                ssnList.add(ssnData.ssnValue());
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
            listOfFullName.add(procuratorPhones.fullName());
            listOfPhone1.add(procuratorPhones.phoneNumbers().phone1());
            listOfPhone2.add(procuratorPhones.phoneNumbers().phone2());
        }

        columnData.add(new ColumnData(headerName, listOfFullName));
        columnData.add(new ColumnData(headerPhone1, listOfPhone1));
        columnData.add(new ColumnData(headerPhone2, listOfPhone2));

        return new SheetData("Phone list", columnData);
    }

    public static SheetData collectMergedData(List<SsnData> listOfSsnData, List<ProcuratorPhones> listOfProcuratorPhones) {
        List<String> listOfFullName = new ArrayList<>();
        List<String> listOfPersonalCodes = new ArrayList<>();
        List<String> ssnList = new ArrayList<>();
        List<String> phone1List = new ArrayList<>();
        List<String> phone2List = new ArrayList<>();

        List<ColumnData> columnData = new ArrayList<>();

        String headerName = "Full Name", headerCode = "Personal code", headerSsn = "Company SSN", headerPhone1 = "Phone1", headerPhone2 = "Phone2";

        for (SsnData ssnData : listOfSsnData) {
            for (Procurator currentProcurator : ssnData.listOfProcurator()) {
                listOfFullName.add(currentProcurator.fullName());
                listOfPersonalCodes.add(StringUtil.removeDash(currentProcurator.personalCode()));
                ssnList.add(ssnData.ssnValue());

                listOfProcuratorPhones.forEach(procuratorPhones -> {
                    if (currentProcurator.fullName().equals(procuratorPhones.fullName())) {
                        phone1List.add(procuratorPhones.phoneNumbers().phone1());
                        phone2List.add(procuratorPhones.phoneNumbers().phone2());
                    }
                });
            }
        }

        columnData.add(new ColumnData(headerSsn, ssnList));
        columnData.add(new ColumnData(headerName, listOfFullName));
        columnData.add(new ColumnData(headerCode, listOfPersonalCodes));
        columnData.add(new ColumnData(headerPhone1, phone1List));
        columnData.add(new ColumnData(headerPhone2, phone2List));

        return new SheetData("Procurator list", columnData);
    }

    public static List<Procurator> collectProcuratorsData(List<List<WebElement>> procurators) {
        List<Procurator> listOfProcurator = new ArrayList<>();

        procurators.forEach(proc -> listOfProcurator.add(collectProcuratorData(proc)));

        return listOfProcurator;
    }

    public static PhoneNumbers collectProcuratorPhones(List<WebElement> phoneNumbers) {
        List<String> phones = new ArrayList<>();

        // TODO temp
        if (phoneNumbers.size() == 0) {
            return new PhoneNumbers("", "");
        }

        phoneNumbers.forEach(phone -> phones.add(collectPhoneNumbersData(phone)));

        if (phones.size() == 1) {
            return new PhoneNumbers(phones.get(0), "");
        }

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

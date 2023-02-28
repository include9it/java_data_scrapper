package io.saltpay.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class DataRowUtil {

    public static List<WebElement> extractFirstCell(List<WebElement> rows) {
        List<WebElement> listOfFirstCell = new ArrayList<>();

        rows.forEach(row -> listOfFirstCell.add(getFirstCell(row)));

        return listOfFirstCell;
    }

    private static WebElement getFirstCell(WebElement row) {
        return row.findElement(By.tagName("td"));
    }

    public static WebElement getChildDivBlock(WebElement webElement) {
        return webElement.findElement(By.xpath(".//div"));
    }

    public static List<WebElement> getChildDivBlocks(WebElement webElement) {
        return webElement.findElements(By.xpath(".//div"));
    }
}

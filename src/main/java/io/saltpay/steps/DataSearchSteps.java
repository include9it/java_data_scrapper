package io.saltpay.steps;

import io.saltpay.utils.DataRowUtil;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

public class DataSearchSteps {
    private static final String TAG = DataSearchSteps.class.getName();

    private final ChromeDriver chromeDriver;

    public DataSearchSteps(ChromeDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
    }

    public List<List<WebElement>> findProcuratorFirstCellData(List<WebElement> procuratorRows) {
        List<WebElement> procuratorCells = DataRowUtil.extractFirstCell(procuratorRows);

        return extractProcuratorCellData(procuratorCells);
    }

    private List<List<WebElement>> extractProcuratorCellData(List<WebElement> procuratorCells) {
        List<List<WebElement>> listOfProcuratorCellData = new ArrayList<>();

        procuratorCells.forEach(cell -> listOfProcuratorCellData.add(
                DataRowUtil.getChildDivBlocks(DataRowUtil.getChildDivBlock(cell))
        ));

        return listOfProcuratorCellData;
    }
}

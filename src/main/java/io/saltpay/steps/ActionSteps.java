package io.saltpay.steps;

import io.saltpay.utils.SaltLogger;
import io.saltpay.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class ActionSteps {

    private static final String TAG = ActionSteps.class.getName();

    private final ChromeDriver chromeDriver;

    public ActionSteps(ChromeDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
    }

    public void changeLanguage() {
        WaitUtil.wait(ExpectedConditions.visibilityOfElementLocated(By.className("icon-language")));

        List<WebElement> menuButtons = chromeDriver.findElements(By.className("menu-button"));

        WebElement languageButton = menuButtons.get(0);

        WaitUtil.wait(ExpectedConditions.elementToBeClickable(languageButton));
        languageButton.click();

        SaltLogger.i(TAG, "Language changed.");
    }
}

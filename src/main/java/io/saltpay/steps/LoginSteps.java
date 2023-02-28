package io.saltpay.steps;

import io.saltpay.utils.SaltLogger;
import io.saltpay.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginSteps {
    private static final String TAG = LoginSteps.class.getName();

    private final ChromeDriver chromeDriver;

    public LoginSteps(ChromeDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
    }

    public void startPage(String webUrl) {
        chromeDriver.get(webUrl);

        WaitUtil.wait(ExpectedConditions.urlContains(webUrl));

        SaltLogger.i(TAG, "Redirected to the correct page.");
    }

    public void enterCredentials(String loginId, String passwordId, String login, String password) {
        WaitUtil.wait(ExpectedConditions.visibilityOfElementLocated(By.id(loginId)));

        WebElement usernameField = chromeDriver.findElement(By.id(loginId));
        WebElement passwordField = chromeDriver.findElement(By.id(passwordId));

        usernameField.sendKeys(login);
        passwordField.sendKeys(password);

        SaltLogger.i(TAG, "Credentials entered.");
    }

    public void login(String loginButtonId) {
        WaitUtil.wait(ExpectedConditions.visibilityOfElementLocated(By.id(loginButtonId)));

        WebElement loginButton = chromeDriver.findElement(By.id(loginButtonId));
        loginButton.click();

        SaltLogger.i(TAG, "Signing-in...");
    }
}

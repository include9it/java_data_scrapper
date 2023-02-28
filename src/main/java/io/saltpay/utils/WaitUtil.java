package io.saltpay.utils;

import io.saltpay.support.Driver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static io.saltpay.utils.Constants.*;

public class WaitUtil {

    public static <T> void wait(ExpectedCondition<T> expectedCondition) {
        wait(Duration.ofSeconds(BASE_WAIT_TIME), expectedCondition);
    }

    public static <T> void waitLong(ExpectedCondition<T> expectedCondition) {
        wait(Duration.ofSeconds(BASE_WAIT_TIME_LONG), expectedCondition);
    }

    private static <T> void wait(Duration duration, ExpectedCondition<T> expectedCondition) {
        ChromeDriver chromeDriver = Driver.getInstance().getDriver();

        new WebDriverWait(chromeDriver, duration).until(expectedCondition);
    }
}
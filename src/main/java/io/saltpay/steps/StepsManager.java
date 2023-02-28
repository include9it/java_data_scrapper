package io.saltpay.steps;

import org.openqa.selenium.chrome.ChromeDriver;

public class StepsManager {
    private final LoginSteps loginSteps;
    private final ActionSteps actionSteps;
    private final NavigationSteps navigationSteps;
    private final PageSearchSteps pageSearchSteps;
    private final DataSearchSteps dataSearchSteps;

    public StepsManager(ChromeDriver chromeDriver) {
        loginSteps = new LoginSteps(chromeDriver);
        actionSteps = new ActionSteps(chromeDriver);
        navigationSteps = new NavigationSteps(chromeDriver);
        pageSearchSteps = new PageSearchSteps(chromeDriver);
        dataSearchSteps = new DataSearchSteps(chromeDriver);
    }

    public LoginSteps getLoginSteps() {
        return loginSteps;
    }

    public ActionSteps getActionSteps() {
        return actionSteps;
    }

    public NavigationSteps getNavigationSteps() {
        return navigationSteps;
    }

    public PageSearchSteps getPageSearchSteps() {
        return pageSearchSteps;
    }

    public DataSearchSteps getDataSearchSteps() {
        return dataSearchSteps;
    }
}

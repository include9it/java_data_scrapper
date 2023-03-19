package io.saltpay.scripts;

import io.saltpay.support.Driver;

import static io.saltpay.utils.Constants.CREDIT_INFO_LINK;

public class CreditInfoStartScript extends Script {
    private static final String TAG = CreditInfoStartScript.class.getName();

    public CreditInfoStartScript(Driver driver) {
        super(driver);
    }

    public void enterAndLogin() {
        stepController.getLoginSteps().startPage(CREDIT_INFO_LINK);
        stepController.getLoginSteps().enterCredentials(
                "usernameInput",
                "Password",
                "Salt.Elisabet",
                "Elisabet_69"
        );
        stepController.getLoginSteps().login("audkenni-button");
    }

    public void changeLocale() {
        stepController.getActionSteps().changeLanguage();
    }
}
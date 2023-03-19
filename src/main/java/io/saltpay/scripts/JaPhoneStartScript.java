package io.saltpay.scripts;

import io.saltpay.support.Driver;

import static io.saltpay.utils.Constants.JA_PHONE_REGISTRY_LINK;

public class JaPhoneStartScript extends Script implements StartScript {
    private static final String TAG = JaPhoneStartScript.class.getName();

    public JaPhoneStartScript(Driver driver) {
        super(driver);
    }

    @Override
    public void start() {
        enterWebsite();
    }

    private void enterWebsite() {
        stepController.getLoginSteps().startPage(JA_PHONE_REGISTRY_LINK);
    }
}

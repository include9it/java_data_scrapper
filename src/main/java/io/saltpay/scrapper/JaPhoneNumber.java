package io.saltpay.scrapper;

import io.saltpay.steps.StepsManager;

import static io.saltpay.utils.Constants.JA_PHONE_REGISTRY_LINK;

public class JaPhoneNumber {

    private final StepsManager stepsManager;

    public JaPhoneNumber(StepsManager stepsManager) {
        this.stepsManager = stepsManager;
    }

    public void start() {
        // Read generated .xlsx file to get list of Full Name`s

        enterWebsite();

        // Make search by Full Name
        // Find and collect all related phone numbers

        // Mix and add to previously generated .xlsx file, found phone numbers
    }

    private void enterWebsite() {
        stepsManager.getLoginSteps().startPage(JA_PHONE_REGISTRY_LINK);
    }
}

package io.saltpay.scrapper;

import io.saltpay.steps.StepController;

import static io.saltpay.utils.Constants.JA_PHONE_REGISTRY_LINK;

public class JaPhoneNumber extends Scrapper {

    public JaPhoneNumber(StepController stepController) {
        super(stepController);
    }

    @Override
    public void start() {
        // Read generated .xlsx file to get list of Full Name`s

        enterWebsite();

        // Make search by Full Name
        // Find and collect all related phone numbers

        // Mix and add to previously generated .xlsx file, found phone numbers
    }

    private void enterWebsite() {
        getStepsManager().getLoginSteps().startPage(JA_PHONE_REGISTRY_LINK);
    }
}

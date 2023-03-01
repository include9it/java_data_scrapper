package io.saltpay.scrapper;

import io.saltpay.steps.StepController;

public abstract class Scrapper {
    private final StepController stepController;

    Scrapper(StepController stepController) {
        this.stepController = stepController;
    }

    public abstract void start();

    StepController getStepsManager() {
        return stepController;
    }
}

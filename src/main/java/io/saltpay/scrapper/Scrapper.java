package io.saltpay.scrapper;

import io.saltpay.steps.StepsManager;

import java.io.IOException;

public abstract class Scrapper {
    private final StepsManager stepsManager;

    Scrapper(StepsManager stepsManager) {
        this.stepsManager = stepsManager;
    }

    public abstract void start() throws IOException;

    StepsManager getStepsManager() {
        return stepsManager;
    }
}

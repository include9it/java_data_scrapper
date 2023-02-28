package io.saltpay.threads;

import io.saltpay.scrapper.Scrapper;

import java.io.IOException;

public class ScrapperTask extends Task {

    private final Scrapper scrapper;

    public ScrapperTask(Scrapper scrapper) {
        this.scrapper = scrapper;
    }

    @Override
    public void run() {
        super.run();

        try {
            scrapper.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

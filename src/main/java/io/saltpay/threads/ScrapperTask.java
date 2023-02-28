package io.saltpay.threads;

import io.saltpay.scrapper.Scrapper;

public class ScrapperTask extends Task {

    private final Scrapper scrapper;

    public ScrapperTask(Scrapper scrapper) {
        this.scrapper = scrapper;
    }

    @Override
    public void run() {
        super.run();

        scrapper.start();
    }
}

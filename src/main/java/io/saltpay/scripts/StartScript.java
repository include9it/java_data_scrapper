package io.saltpay.scripts;

import io.saltpay.support.Driver;

abstract class StartScript extends Script {
    public StartScript(Driver driver) {
        super(driver);
    }

    abstract void start();
}

package io.saltpay.robot;

import io.saltpay.storage.FileStorage;
import io.saltpay.support.Driver;

import java.io.IOException;

abstract class ScrapperRobot {

    protected final Driver driver;

    protected final FileStorage fileStorage;

    public ScrapperRobot(Driver driver, FileStorage fileStorage) {
        this.driver = driver;
        this.fileStorage = fileStorage;
    }

    abstract void basicCollect() throws IOException;

    abstract void turboCollect() throws IOException;
}

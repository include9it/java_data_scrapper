package io.saltpay.robot;

import io.saltpay.storage.FileStorageController;
import io.saltpay.support.Driver;

import java.io.IOException;

abstract class ScrapperRobot {

    protected final Driver driver;

    protected final FileStorageController fileStorageController;

    public ScrapperRobot(Driver driver, FileStorageController fileStorageController) {
        this.driver = driver;
        this.fileStorageController = fileStorageController;
    }

    abstract void basicCollect() throws IOException;

    abstract void turboCollect() throws IOException;
}

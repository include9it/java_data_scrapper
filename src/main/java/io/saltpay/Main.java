package io.saltpay;

import io.saltpay.models.Procurator;
import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.robot.CreditInfoRobot;
import io.saltpay.robot.JaPhoneRobot;
import io.saltpay.storage.CreditInfoStorage;
import io.saltpay.storage.JaPhoneStorage;
import io.saltpay.support.DriverManager;
import io.saltpay.utils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.*;

public class Main {

    public static void main(String[] args) throws Exception {
        DriverManager driverManager = new DriverManager();

        // CreditInfo Island company registry
        CreditInfoRobot creditInfoRobot = new CreditInfoRobot(driverManager);
//        creditInfoRobot.basicCollect();
//        creditInfoRobot.multiThreadCollect();

        // Phone number registry
        JaPhoneRobot jaPhoneRobot = new JaPhoneRobot(driverManager);
//        jaPhoneRobot.basicCollect();
//        jaPhoneRobot.multiThreadCollect();


        // Prepare Data
//        RestoreManager.restoreModel();
        DataPreparation.mergeFilterAndCollect();
    }
}
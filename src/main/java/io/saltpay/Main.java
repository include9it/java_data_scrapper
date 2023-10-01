package io.saltpay;

import io.saltpay.robot.CreditInfoRobot;
import io.saltpay.robot.JaPhoneRobot;
import io.saltpay.support.Driver;

public class Main {

    public static void main(String[] args) throws Exception {
        Driver driver = new Driver();

        // CreditInfo Island company registry
        CreditInfoRobot creditInfoRobot = new CreditInfoRobot(driver);
//        creditInfoRobot.basicCollect();
//        creditInfoRobot.multiThreadCollect();

        // Phone number registry
        JaPhoneRobot jaPhoneRobot = new JaPhoneRobot(driver);
        jaPhoneRobot.basicCollect();
//        jaPhoneRobot.multiThreadCollect();
//        jaPhoneRobot.multiThreadCollectV2();


        // Prepare Data
//        RestoreManager.restoreModel();
//        DataPreparation.mergeFilterAndCollect();
    }
}
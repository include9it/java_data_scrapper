package io.saltpay;

import io.saltpay.robot.CreditInfoRobot;
import io.saltpay.robot.JaPhoneRobot;
import io.saltpay.support.DriverManager;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        DriverManager driverManager = new DriverManager();

        // CreditInfo Island company registry
        CreditInfoRobot creditInfoRobot = new CreditInfoRobot(driverManager);
        creditInfoRobot.basicCollectCreditInfo();
        creditInfoRobot.multiThreadCollectCreditInfo();

        // Phone number registry
        JaPhoneRobot jaPhoneRobot = new JaPhoneRobot(driverManager);
        jaPhoneRobot.basicCollectPhoneNumbers();
    }
}
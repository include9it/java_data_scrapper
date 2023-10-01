package io.saltpay.utils;


import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SaltLogger {

    public static void i(@NonNull String tag, @Nullable String message) {
        Logger.getLogger(tag).log(Level.INFO, message);
    }

    public static void w(@NonNull String tag, @Nullable String message) {
        Logger.getLogger(tag).log(Level.WARNING, message);
    }

    public static void e(@NonNull String tag, @Nullable String message) {
        Logger.getLogger(tag).log(Level.SEVERE, message);
    }

    public static void d(@NonNull String tag, @Nullable String message) {
        Logger.getLogger(tag).log(Level.FINE, message);
    }

    public static void basic(@Nullable String message) {
        System.out.println(message);
    }

    public static void displaySsnData(List<SsnData> ssnDataList) {
        ssnDataList.forEach(ssnData -> {
            SaltLogger.basic("SSN data: " + ssnData.ssnValue());
            ssnData.listOfProcurator().forEach(procurator ->
                    SaltLogger.basic("Procurator Name: " + procurator.fullName() + "|" + procurator.personalCode())
            );
        });
    }

    public static void displayPhonesData(List<ProcuratorPhones> procuratorPhonesList) {
        procuratorPhonesList.forEach(procuratorPhones -> {
            SaltLogger.basic("Procurator name: " + procuratorPhones.fullName());
            SaltLogger.basic("Phones: " + procuratorPhones.phoneNumbers().phone1()
                    + "|" + procuratorPhones.phoneNumbers().phone2());
        });
    }
}
package io.saltpay.utils;


import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nullable;
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
}
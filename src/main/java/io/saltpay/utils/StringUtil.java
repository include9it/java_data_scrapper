package io.saltpay.utils;

public class StringUtil {
    public static String removeParentheses(String value) {
        return value.replaceAll("[()]", "");
    }
}

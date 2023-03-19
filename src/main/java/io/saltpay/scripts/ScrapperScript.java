package io.saltpay.scripts;

public interface ScrapperScript<T> {
    T findAndCollectDataByValue(String value);
}

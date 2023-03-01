package io.saltpay.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

    public static <T> List<List<T>> splitList(List<T> list, int parts) {
        if (parts <= 0) {
            throw new IllegalArgumentException("Number of parts must be positive");
        }

        int size = list.size();
        int batchSize = size / parts;
        int remainder = size % parts;
        int start = 0;

        List<List<T>> result = new ArrayList<>(parts);

        for (int i = 0; i < parts; i++) {
            int end = start + batchSize + (remainder-- > 0 ? 1 : 0);
            result.add(list.subList(start, end));
            start = end;
        }

        return result;
    }
}

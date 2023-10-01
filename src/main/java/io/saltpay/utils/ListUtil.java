package io.saltpay.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

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

    public static <T> boolean hasDuplicateIdentifiers(List<T> list, Function<T, String> identifierGetter) {
        Set<String> seenIdentifiers = new HashSet<>();

        for (T item : list) {
            String identifier = identifierGetter.apply(item);
            if (seenIdentifiers.contains(identifier)) {
                return true;
            } else {
                seenIdentifiers.add(identifier);
            }
        }

        return false;
    }

    public static <T> List<T> removeDuplicates(List<T> list, Function<T, String> identifierGetter) {
        Set<String> seenIdentifiers = new HashSet<>();

        List<T> result = new ArrayList<>();

        for (T item : list) {
            String identifier = identifierGetter.apply(item);
            if (!seenIdentifiers.contains(identifier)) {
                seenIdentifiers.add(identifier);
                result.add(item);
            }
        }

        return result;
    }

    public static <T> List<String> findMissingIdentifiers(List<T> list, Function<T, String> identifierGetter, List<String> targetIdentifiers) {
        Set<String> existingIdentifiers = new HashSet<>();

        for (T item : list) {
            existingIdentifiers.add(identifierGetter.apply(item));
        }

        List<String> missingIdentifiers = new ArrayList<>();

        for (String targetIdentifier : targetIdentifiers) {
            if (!existingIdentifiers.contains(targetIdentifier)) {
                missingIdentifiers.add(targetIdentifier);
            }
        }

        return missingIdentifiers;
    }

}

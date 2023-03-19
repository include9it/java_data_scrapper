package io.saltpay.data;

import io.saltpay.storage.FileStorageController;

import java.util.List;
import java.util.function.Function;

public class DataPreparationManager {

    public <T, O> List<T> prepareStartData(List<T> initialList, FileStorageController storage, Function<O, T> identifierGetter) {
        // Get list of saved data
        List<O> savedList = storage.readData();

        if (savedList != null) {
            O lastSavedEntry = getLastValue(savedList);

            return extractLeftData(initialList, lastSavedEntry, identifierGetter);
        }

        return initialList;
    }

    private <O> O getLastValue(List<O> savedList) {
        int lastEntryIndex = savedList.size() - 1;

        return savedList.get(lastEntryIndex);
    }

    private <T, O> List<T> extractLeftData(List<T> initialList, O lastSavedEntry, Function<O, T> identifierGetter) {
        int sizeOfSsnList = initialList.size();

        int cutIndex = initialList.indexOf(identifierGetter.apply(lastSavedEntry));

        return initialList.subList(cutIndex, sizeOfSsnList);
    }
}

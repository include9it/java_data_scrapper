package io.saltpay.storage;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class DataPreparationManager {

    public <T> List<T> prepareStartData(List<T> initialList, StorageController storage) {
        return prepareStartData(initialList, storage, null);
    }

    public <T, O> List<T> prepareStartData(List<T> initialList, StorageController storage, @Nullable Function<O, T> identifierGetter) {
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
        T lastSavedEntryValue = identifierGetter != null ? identifierGetter.apply(lastSavedEntry) : (T) lastSavedEntry;
        int cutIndex = initialList.indexOf(lastSavedEntryValue);

        return initialList.subList(cutIndex, sizeOfSsnList);
    }
}

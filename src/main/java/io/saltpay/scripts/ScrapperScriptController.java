package io.saltpay.scripts;

import io.saltpay.storage.FileStorageController;
import io.saltpay.utils.SaltLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ScrapperScriptController<T, O> {
    private static final String TAG = ScrapperScriptController.class.getName();

    private final StartScript startScript;
    private final ScrapperScript<O> scrapperScript;
    private final FileStorageController storage;
    private final List<T> listOfData;
    private final List<O> listOfScrappedData = new ArrayList<>();

    public ScrapperScriptController(StartScript startScript, ScrapperScript<O> scrapperScript, FileStorageController storage, List<T> listOfData) {
        this.startScript = startScript;
        this.scrapperScript = scrapperScript;

        this.storage = storage;
        this.listOfData = listOfData;
    }

    public void start(Function<T, String> valueGetter) {
        SaltLogger.i(TAG, "Bot started!");

        startScript.start();

        listOfData.forEach(data -> {
            O scrapedData = scrapperScript.findAndCollectDataByValue(valueGetter.apply(data));

            storage.saveData(scrapedData);

            listOfScrappedData.add(scrapedData);
        });

//        startScript.finish(); TODO
    }

    public List<O> getListOfScrappedData() {
        return listOfScrappedData;
    }
}

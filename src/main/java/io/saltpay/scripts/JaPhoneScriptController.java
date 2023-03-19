package io.saltpay.scripts;

import io.saltpay.models.Procurator;
import io.saltpay.models.ProcuratorPhones;
import io.saltpay.storage.FileStorageController;
import io.saltpay.support.Driver;
import io.saltpay.utils.SaltLogger;

import java.util.ArrayList;
import java.util.List;

public class JaPhoneScriptController {
    private static final String TAG = JaPhoneScriptController.class.getName();

    private final ScrapperScriptController<Procurator, ProcuratorPhones> scrapperScriptController;

    public JaPhoneScriptController(
            Driver driver,
            List<Procurator> procuratorList,
            FileStorageController phoneStorage
    ) {
        this.scrapperScriptController = new ScrapperScriptController<>(
                new JaPhoneStartScript(driver),
                new JaPhoneScrapperScript(driver),
                phoneStorage,
                procuratorList
        );
    }

    public void start() {
        SaltLogger.i(TAG, "Ja Phone Bot started!");

        scrapperScriptController.start(Procurator::fullName);
    }

    public List<ProcuratorPhones> getListOfProcuratorPhones() {
        return scrapperScriptController.getListOfScrappedData();
    }
}

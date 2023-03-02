package io.saltpay.robot;

import io.saltpay.model.Procurator;
import io.saltpay.model.ProcuratorPhones;
import io.saltpay.model.SsnData;
import io.saltpay.scrapper.JaPhoneNumberScrapper;
import io.saltpay.utils.JaPhoneSaveManager;

import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.JA_PHONE_BACKUP_FILE;

public class JaPhoneNumberDataCollector {
    private final JaPhoneNumberScrapper jaPhoneNumberScrapper;
    private final List<SsnData> ssnDataList;
    private final JaPhoneSaveManager jaPhoneSaveManager;
    private final List<ProcuratorPhones> listOfProcuratorPhones = new ArrayList<>();

    public JaPhoneNumberDataCollector(JaPhoneNumberScrapper jaPhoneNumberScrapper, List<SsnData> ssnDataList, JaPhoneSaveManager jaPhoneSaveManager) {
        this.jaPhoneNumberScrapper = jaPhoneNumberScrapper;
        this.ssnDataList = ssnDataList;
        this.jaPhoneSaveManager = jaPhoneSaveManager;
    }

    public void start() {
        jaPhoneNumberScrapper.enterWebsite();

        // Get phone numbers by Procurator
//        ssnDataList.forEach(ssnData ->
//                ssnData.getListOfProcurator().forEach(procurator -> {
//                    ProcuratorPhones phoneNumbers = jaPhoneNumberScrapper.findAndCollectDataByFullName(procurator.getFullName());
//
//                    jaPhoneSaveManager.saveProcuratorPhoneData(JA_PHONE_BACKUP_FILE, phoneNumbers);
//
//                    listOfProcuratorPhones.add(phoneNumbers);
//                })
//        );

        Procurator procurator = ssnDataList.get(0).getListOfProcurator().get(0);

        ProcuratorPhones phoneNumbers = jaPhoneNumberScrapper.findAndCollectDataByFullName(procurator.getFullName());

        jaPhoneSaveManager.saveProcuratorPhoneData(JA_PHONE_BACKUP_FILE, phoneNumbers);

        listOfProcuratorPhones.add(phoneNumbers);
    }

    public List<ProcuratorPhones> getListOfProcuratorPhones() {
        return listOfProcuratorPhones;
    }
}

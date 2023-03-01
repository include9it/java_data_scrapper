package io.saltpay.utils;

public class Constants {
    public static int BASE_WAIT_TIME = 5;
    public static int BASE_WAIT_TIME_LONG = 60;

    public static String RESOURCE_FILE_PATH = "src/main/resources/";

    // CreditInfo Island company registry
    public static String CREDIT_INFO_LINK = "https://fyrirtaeki.creditinfo.is";
    public static String CREDIT_INFO_READ_FILE = "ISL Missing Authorizers.xlsx";
    public static String CREDIT_INFO_WRITE_FILE = "ProcuratorList.xlsx";
    public static String CREDIT_INFO_COMPANY_REGISTRY = "/financeInfo/subjectOverview/%s/companyRegistration";
    public static String CREDIT_INFO_BACKUP_FILE = "ssn_list_backup";
    public static String DATA_MODEL_FILE_EXTENSION = ".dat";

    // Ja Phone Island phone registry
    public static String JA_PHONE_REGISTRY_LINK = "https://ja.is";
    public static String JA_PHONE_BACKUP_FILE = "phone_list_by_ssn_backup";
}
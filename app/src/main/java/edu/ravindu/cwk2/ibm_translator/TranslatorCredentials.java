package edu.ravindu.cwk2.ibm_translator;

/**
 * Created by Ravindu Fernando on 2020-03-20 at 07:22 PM
 */
public class TranslatorCredentials {

    private final String API_KEY = "HmLCLimeEArAMAgMh8_nQo0AmaGXbbUE59DSH8IHW9Zc";
    private final String URL = "https://api.us-east.language-translator.watson.cloud.ibm.com/instances/0fa78682-37a2-41b1-aa5e-23a22c16e93b";
    private final String VERSION = "2018-05-01";

    public String getApiKey() {
        return API_KEY;
    }

    public String getUrl() {
        return URL;
    }

    public String getVersion() {
        return VERSION;
    }
}
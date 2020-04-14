package edu.ravindu.cwk2.model;

/**
 * Created by Ravindu Fernando on 2020-04-13 at 07:49 PM
 */
public class Language {
    private int id;
    private String languageCode;
    private String languageName;
    private String subscribeStatus;

    public Language() {
        subscribeStatus = "N";// default status
    }

    public int getLanguageId() {
        return id;
    }

    public void setLanguageId(int id) {
        this.id = id;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getSubscribeStatus() {
        return subscribeStatus;
    }

    public void setSubscribeStatus(String subscribeStatus) {
        this.subscribeStatus = subscribeStatus;
    }
}


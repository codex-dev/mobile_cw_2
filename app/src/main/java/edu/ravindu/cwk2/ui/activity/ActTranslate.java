package edu.ravindu.cwk2.ui.activity;

import android.os.Bundle;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.discovery.v2.Discovery;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;

import edu.ravindu.cwk2.R;
import edu.ravindu.cwk2.ibm_translator.TranslatorCredentials;

public class ActTranslate extends ActCommon {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_translate);
        setupActionbar(getString(R.string.btn_translate), true);

        getAvailableLanguages();
    }

    private void getAvailableLanguages() {
        IamAuthenticator authenticator = new IamAuthenticator(TranslatorCredentials.getApiKey());
        Discovery service = new Discovery("2019-04-30", authenticator);
        service.setServiceUrl(TranslatorCredentials.getUrl());






        LanguageTranslator translator = new LanguageTranslator(TranslatorCredentials.getVersion(), authenticator);
        translator.setServiceUrl(TranslatorCredentials.getUrl());

//        IdentifiableLanguages languages = translator.listIdentifiableLanguages().execute().getResult();
//        System.out.println("/// languages : "+languages);

        TranslateOptions translateOptions = new TranslateOptions.Builder().addText("Hello").modelId("en-es").build();
        TranslationResult result = translator.translate(translateOptions).execute().getResult();

        System.out.println(result);
    }
}

package com.nikolaev.horeca.services;

import com.nikolaev.horeca.configs.LocaleConfig;
import com.nikolaev.horeca.misc.Component;
import com.nikolaev.horeca.misc.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocaleService {
    @Autowired
    private LocaleConfig localeConfig;

    public String localeData = "";
    public Locale currentLang = localeConfig.getLocale();

    public static String getString(String query) {
        return query;
    }

    public LocaleService(Component component){
        //// TODO: read compoent lang
    }
}

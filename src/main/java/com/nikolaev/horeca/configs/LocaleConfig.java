package com.nikolaev.horeca.configs;

import com.nikolaev.horeca.misc.Locale;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocaleConfig {
    private Locale locale = Locale.RU;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}

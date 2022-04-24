package com.nikolaev.horeca.misc;

public enum ErrorType {
    AUTH("invalid_authentication"),
    INVALID_EMAIL("invalid_email");

    private final String templateValue;
    ErrorType(String templateValue)
    {
        this.templateValue = templateValue;
    }

    public String getTemplateValue(){
        return this.templateValue;
    }
}

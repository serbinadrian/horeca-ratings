package com.nikolaev.horeca.misc;

public enum ErrorType {
    INVALID_AUTH("invalid_authentication"),
    INVALID_EMAIL("invalid_email"),
    INVALID_NAME("invalid_name"),
    INVALID_USER("invalid_username"),
    INVALID_PASSWORD("invalid_password");

    private final String templateValue;
    ErrorType(String templateValue)
    {
        this.templateValue = templateValue;
    }

    public String getTemplateValue(){
        return this.templateValue;
    }
}

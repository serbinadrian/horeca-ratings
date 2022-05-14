package com.nikolaev.horeca.misc;

public enum ErrorType {
    INVALID_AUTH("invalid_authentication"),
    INVALID_EMAIL("invalid_email"),
    INVALID_NAME("invalid_name"),
    INVALID_USER("invalid_username"),
    INVALID_PASSWORD("invalid_password"),
    ALREADY_EXISTS("already_exists"),
    INCOMPATIBLE_DATASETS_SIZES("incompatible_datasets_sizes"),
    INCOMPATIBLE_CAFES_SIZES("incompatible_cafes_sizes"),
    INCOMPATIBLE_RATING_SIZES("incompatible_rating_sizes");

    private final String templateValue;
    ErrorType(String templateValue)
    {
        this.templateValue = templateValue;
    }

    public String getTemplateValue(){
        return this.templateValue;
    }
}

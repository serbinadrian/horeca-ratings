package com.nikolaev.horeca.domains;

import com.nikolaev.horeca.misc.ErrorType;

public class ErrorMessage {
    private String message;
    private ErrorType type;

    public ErrorMessage(String message, ErrorType type){
        this.message = message;
        this.type = type;
    }

    public void print(){
        System.out.println("Error occurred: " + this.message);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorType getType() {
        return this.type;
    }

    public void setType(ErrorType type) {
        this.type = type;
    }
}

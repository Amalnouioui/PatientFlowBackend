package com.core.Parameterization.Entities;

public class Message {
    String message ;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return " " +
              message
                ;
    }
}


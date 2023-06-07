package com.tradingengine.authorizationservice.exception;

public class UserNotFoundException extends Exception    {
    public UserNotFoundException() {
        super("Invalid Credentials");
    }
}

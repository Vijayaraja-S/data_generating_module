package com.p3.poc.exception;

public class InvalidInputException extends Exception{
    public InvalidInputException(final String message) {
        super(message);
    }

    public InvalidInputException(final String message, final Throwable cause) {
        super(message + ": " + cause.getMessage(), cause);
    }
}

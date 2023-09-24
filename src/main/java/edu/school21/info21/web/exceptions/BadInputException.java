package edu.school21.info21.web.exceptions;

public class BadInputException extends RuntimeException {

    public BadInputException() {
        super();
    }

    public BadInputException(String message) {
        super(message);
    }
}

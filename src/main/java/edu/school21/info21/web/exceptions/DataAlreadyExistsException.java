package edu.school21.info21.web.exceptions;

public class DataAlreadyExistsException extends RuntimeException {

    public DataAlreadyExistsException() {
    }

    public DataAlreadyExistsException(String message) {
        super(message);
    }
}

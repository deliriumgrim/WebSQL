package edu.school21.info21.web.exceptions;

public class DataNotExistsException extends RuntimeException {

    public DataNotExistsException() {
    }

    public DataNotExistsException(String message) {
        super(message);
    }
}

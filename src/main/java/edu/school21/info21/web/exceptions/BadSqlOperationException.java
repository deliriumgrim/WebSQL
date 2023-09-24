package edu.school21.info21.web.exceptions;

public class BadSqlOperationException extends RuntimeException {

    public BadSqlOperationException() {
        super();
    }

    public BadSqlOperationException(String message) {
        super(message);
    }
}

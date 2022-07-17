package com.kata.banking.exception;

import org.springframework.http.HttpStatus;

/**
 *
 * Generic exception
 *
 * @author Ons Ben Lakhal
 */
public class KataBankingException extends RuntimeException {

    /**
     * Generated serial ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Exception status
     */
    protected HttpStatus status;

    /**
     * Exception code
     */
    protected int code;

    /**
     * Customised constructor
     *
     * @param status  : Exception status
     * @param code    : Exception code
     * @param message : Exception message
     */
    public KataBankingException(HttpStatus status, int code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }
}

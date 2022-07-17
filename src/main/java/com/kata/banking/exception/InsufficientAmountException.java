package com.kata.banking.exception;

import org.springframework.http.HttpStatus;

/**
 *
 * Insufficient amount exception
 *
 * @author Ons Ben Lakhal
 */
public class InsufficientAmountException extends KataBankingException {

    public static final String INSUFFICIENT_AMOUNT = "You don't have enough balance to withdraw";

    /**
     * Generated serial ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Exception status
     */
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    /**
     * Exception code
     */
    private static final int CODE = 4001;

    /**
     * Exception message
     */
    private static final String MESSAGE = "Insufficient amount";

    /**
     * Customised constructor
     *
     * @param message : Exception message
     */
    public InsufficientAmountException(String message) {
        super(STATUS, CODE, message);
    }
}

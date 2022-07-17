package com.kata.banking.exception;

import org.springframework.http.HttpStatus;

/**
 *
 * Insufficient data exception
 *
 * @author Ons Ben Lakhal
 */
public class InvalidDataException extends KataBankingException {

    public static final String INVALID_ACCOUNT_ID = "Account cannot be null nor empty";
    public static final String EXISTING_ACCOUNT = "Account with client id %s is already existing";
    public static final String INVALID_AMOUNT = "The amount to %s cannot be negative";
    public static final String MISSING_ACCOUNT = "Account with id %s does not exist";
    public static final String INVALID_DATES = "Start date must be before end date";
    public static final String INVALID_PAGE = "Invalid page number";
    public static final String INVALID_SIZE = "Invalid page size";

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
    private static final int CODE = 4002;

    /**
     * Exception message
     */
    private static final String MESSAGE = "Invalid data";

    /**
     * Customised constructor
     *
     * @param message : Exception message
     */
    public InvalidDataException(String message) {
        super(STATUS, CODE, message);
    }
}

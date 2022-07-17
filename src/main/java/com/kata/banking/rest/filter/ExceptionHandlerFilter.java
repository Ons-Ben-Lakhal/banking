package com.kata.banking.rest.filter;

import com.google.common.collect.Lists;
import com.kata.banking.exception.InsufficientAmountException;
import com.kata.banking.exception.InvalidDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * Exception handler for client response
 *
 * @author Ons Ben Lakhal
 */
@ControllerAdvice
public class ExceptionHandlerFilter {

    /**
     * Handle invalid data exception
     *
     * @param exception : InvalidDataException
     * @return {@link ResponseEntity} of {@link DataExceptionDto}
     */
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<DataExceptionDto> handleInvalidDataException(InvalidDataException exception) {
        final DataExceptionDto error = new DataExceptionDto(HttpStatus.BAD_REQUEST, exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle insufficient amount exception
     *
     * @param exception : InsufficientAmountException
     * @return {@link ResponseEntity} of {@link DataExceptionDto}
     */
    @ExceptionHandler(InsufficientAmountException.class)
    public ResponseEntity<DataExceptionDto> handleInsufficientAmountException(InsufficientAmountException exception) {
        final DataExceptionDto error = new DataExceptionDto(HttpStatus.BAD_REQUEST, exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle invalid arguments exception
     *
     * @param ex : Exception
     * @return {@link ResponseEntity} of {@link DataExceptionDto}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DataExceptionDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        final List<String> errors = Lists.newArrayList();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(String.join("", Lists.newArrayList(error.getField(), ": ", error.getDefaultMessage())));
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(String.join("", Lists.newArrayList(error.getObjectName(), ": ", error.getDefaultMessage())));
        }
        DataExceptionDto error = new DataExceptionDto(HttpStatus.BAD_REQUEST, String.join("", errors));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle exception
     *
     * @param exception : InvalidDataException
     * @return {@link ResponseEntity} of {@link DataExceptionDto}
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<DataExceptionDto> handleException(Exception exception) {
        final DataExceptionDto error = new DataExceptionDto(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

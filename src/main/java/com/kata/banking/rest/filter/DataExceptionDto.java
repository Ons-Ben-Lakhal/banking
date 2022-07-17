package com.kata.banking.rest.filter;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.LocalDateTime;

/**
 *
 * Exception Dto
 *
 * @author Ons Ben Lakhal
 */
@Getter
@Setter
public class DataExceptionDto {

    /**
     * Exception code
     */
    private int code;

    /**
     * Exception status
     */
    private String status;

    /**
     * Exception message
     */
    private String message;

    /**
     * Exception date
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime time;

    public DataExceptionDto(HttpStatus httpStatus, String message) {
        this.time = LocalDateTime.now(ZoneId.of("UTC"));
        this.code = httpStatus.value();
        this.status = httpStatus.name();
        this.message = message;
    }
}

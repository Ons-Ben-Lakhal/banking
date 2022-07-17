package com.kata.banking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kata.banking.enums.Operation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 *
 * Keep account statement details
 *
 * @author Ons Ben Lakhal
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountStatement {

    /**
     * Amount of operation Deposit / withdraw
     */
    private int amount;

    /**
     * Account balance after operation Deposit / Withdraw
     */
    private int accountBalance;

    /**
     * Operation type
     */
    private Operation operation;

    /**
     * Operation date
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime operationDate;
}

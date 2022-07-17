package com.kata.banking.rest.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * Withdraw request details
 *
 * @author Ons Ben Lakhal
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithdrawRequest {

    /**
     * Account id
     */
    private String accountId;

    /**
     * Amount
     */
    private int amount;
}

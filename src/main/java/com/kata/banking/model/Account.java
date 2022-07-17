package com.kata.banking.model;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 *
 * Keep account details
 *
 * @author Ons Ben Lakhal
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    /**
     * Account id
     */
    private String id;

    /**
     * Current balance
     */
    private int currentBalance;

    /**
     * User id
     */
    private String clientId;

    /**
     * {@link List} of {@link AccountStatement}
     */
    private List<AccountStatement> accountStatements = Lists.newArrayList();
}

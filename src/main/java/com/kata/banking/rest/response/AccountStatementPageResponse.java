package com.kata.banking.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 *
 * Account statement page response details
 *
 * @author Ons Ben Lakhal
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountStatementPageResponse {

    /**
     * Account statements
     */
    private List<AccountStatementResponse> accountStatement;

    /**
     * Total pages
     */
    private int totalPages;

    /**
     * Total elements
     */
    private long totalElements;
}

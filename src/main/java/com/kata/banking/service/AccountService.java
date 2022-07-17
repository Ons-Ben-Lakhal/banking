package com.kata.banking.service;

import com.kata.banking.model.Account;
import com.kata.banking.model.AccountStatement;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Account service for managing banking operations
 *
 * @author Ons Ben Lakhal
 */
public interface AccountService {

    /**
     * Find account details with id
     *
     * @param accountId : Account id
     * @return {@link Optional} of {@link Account}
     */
    Optional<Account> findAccountById(String accountId);

    /**
     * Find account statements by account id
     *
     * @param accountId : Account Id
     * @return {@link List} of {@link AccountStatement}
     */
    List<AccountStatement> findAccountStatementsByAccountId(String accountId);

    /**
     * Create Account for User
     *
     * @param clientId : User id
     * @param amount   : Amount to deposit
     * @return {@link Account}
     */
    Account createAccount(String clientId, int amount);

    /**
     * Deposit money in account
     *
     * @param amount    : Amount to deposit
     * @param accountId : Account Identifier
     */
    void deposit(int amount, String accountId);

    /***
     * Withdrawal money in account
     *
     * @param amount    : Amount to withdrawal
     * @param accountId : Account Identifier
     */
    void withdraw(int amount, String accountId);

    /**
     * Get page of account statements by account id within a period
     *
     * @param accountId : Account id
     * @param startDate : Start date of statement operation date
     * @param endDate   : End date of statement operation date
     * @param page      : Page number
     * @param size      : Page size
     * @return {@link Page} of {@link AccountStatement}
     */
    Page<AccountStatement> getAccountStatement(String accountId, LocalDateTime startDate, LocalDateTime endDate, int page,
                                                    int size);

    /**
     * Get list of account statements by account id within a period
     *
     * @param accountId : Account id
     * @param startDate : Start date of statement operation date
     * @param endDate   : End date of statement operation date
     * @return {@link List} of {@link AccountStatement}
     */
    List<AccountStatement> getAccountStatement(String accountId, LocalDateTime startDate, LocalDateTime endDate);
}

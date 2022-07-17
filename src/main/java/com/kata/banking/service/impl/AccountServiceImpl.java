package com.kata.banking.service.impl;

import com.google.common.collect.Lists;
import com.kata.banking.exception.InsufficientAmountException;
import com.kata.banking.exception.InvalidDataException;
import com.kata.banking.model.Account;
import com.kata.banking.model.AccountStatement;
import com.kata.banking.enums.Operation;
import com.kata.banking.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of account service
 * Here persistence layer is hidden and replaces with fake data
 *
 * @author Ons Ben Lakhal
 */
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final List<Account> accounts = Lists.newArrayList();

    /**
     * This method is used to init fake data to replace the database
     */
    @PostConstruct
    public void init() {
        Account account = createAccount("1", 0);
        account.setId("1");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Account> findAccountById(String accountId) {

        if (StringUtils.isEmpty(accountId)) {
            throw new InvalidDataException(InvalidDataException.INVALID_ACCOUNT_ID);
        }
        return accounts.stream()
                .filter(account -> accountId.equals(account.getId())).findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AccountStatement> findAccountStatementsByAccountId(String accountId) {

        if (StringUtils.isEmpty(accountId)) {
            throw new InvalidDataException(InvalidDataException.INVALID_ACCOUNT_ID);
        }
        return accounts.stream()
                .filter(account -> accountId.equals(account.getId()))
                .map(Account::getAccountStatements)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Account createAccount(String clientId, int amount) {

        // check if account already exists
        final Optional<Account> existingAccount = findAccountByClientId(clientId);
        if (existingAccount.isPresent()) {
            final String errorMessage = String.format(InvalidDataException.EXISTING_ACCOUNT, clientId);
            log.error(errorMessage);
            throw new InvalidDataException(errorMessage);
        }
        // create account
        final Account account = Account.builder()
                .clientId(clientId)
                .id(UUID.randomUUID().toString())
                .currentBalance(amount)
                .build();
        // create statement
        final AccountStatement accountStatement = AccountStatement.builder()
                .amount(amount)
                .accountBalance(amount)
                .operation(Operation.DEPOSIT)
                .operationDate(LocalDateTime.now(ZoneId.of("UTC")))
                .build();
        account.setAccountStatements(Lists.newArrayList(accountStatement));
        // add to list
        accounts.add(account);
        return account;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deposit(int amount, String accountId) {

        // check for account
        final Optional<Account> optionalAccount = findAccountById(accountId);
        if (optionalAccount.isEmpty()) {
            final String errorMessage = String.format(InvalidDataException.MISSING_ACCOUNT, accountId);
            log.error(errorMessage);
            throw new InvalidDataException(errorMessage);
        }
        // check amount
        verifyAmount(amount, Operation.DEPOSIT);
        // calculate new balance
        final Account account = optionalAccount.get();
        final int newBalance = account.getCurrentBalance() + amount;
        account.setCurrentBalance(newBalance);
        // create statement
        final AccountStatement accountStatement = AccountStatement.builder()
                .amount(amount)
                .accountBalance(newBalance)
                .operation(Operation.DEPOSIT)
                .operationDate(LocalDateTime.now(ZoneId.of("UTC")))
                .build();
        account.getAccountStatements().add(accountStatement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void withdraw(int amount, String accountId) {

        // check for account
        final Optional<Account> optionalAccount = findAccountById(accountId);
        if (optionalAccount.isEmpty()) {
            final String errorMessage = String.format(InvalidDataException.MISSING_ACCOUNT, accountId);
            log.error(errorMessage);
            throw new InvalidDataException(errorMessage);
        }
        // check amount
        verifyAmount(amount, Operation.WITHDRAW);
        final Account account = optionalAccount.get();
        if (account.getCurrentBalance() < amount) {
            log.error(InsufficientAmountException.INSUFFICIENT_AMOUNT);
            throw new InsufficientAmountException(InsufficientAmountException.INSUFFICIENT_AMOUNT);
        }
        // calculate new balance
        final int newBalance = account.getCurrentBalance() - amount;
        account.setCurrentBalance(newBalance);
        // create statement
        final AccountStatement accountStatement = AccountStatement.builder()
                .amount(amount)
                .accountBalance(newBalance)
                .operation(Operation.WITHDRAW)
                .operationDate(LocalDateTime.now(ZoneId.of("UTC")))
                .build();
        account.getAccountStatements().add(accountStatement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<AccountStatement> getAccountStatement(String accountId, LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        // check for account
        final Optional<Account> optionalAccount = findAccountById(accountId);
        if (optionalAccount.isEmpty()) {
            final String errorMessage = String.format(InvalidDataException.MISSING_ACCOUNT, accountId);
            log.error(errorMessage);
            throw new InvalidDataException(errorMessage);
        }
        // check dates
        if (Objects.nonNull(startDate) && Objects.nonNull(endDate) && endDate.isBefore(startDate)) {
            log.error(InvalidDataException.INVALID_DATES);
            throw new InvalidDataException(InvalidDataException.INVALID_DATES);
        }
        // check page number
        if (page < 0) {
            log.error(InvalidDataException.INVALID_PAGE);
            throw new InvalidDataException(InvalidDataException.INVALID_PAGE);
        }
        // check page size
        if (size < 1) {
            log.error(InvalidDataException.INVALID_SIZE);
            throw new InvalidDataException(InvalidDataException.INVALID_SIZE);
        }
        final Pageable pageable = PageRequest.of(page, size);
        final List<AccountStatement> resultStatements = getListWithinDates(accountId, startDate, endDate);
        return mapListToPage(resultStatements, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AccountStatement> getAccountStatement(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        // check for account
        final Optional<Account> optionalAccount = findAccountById(accountId);
        if (optionalAccount.isEmpty()) {
            final String errorMessage = String.format(InvalidDataException.MISSING_ACCOUNT, accountId);
            log.error(errorMessage);
            throw new InvalidDataException(errorMessage);
        }
        // check dates
        if (Objects.nonNull(startDate) && Objects.nonNull(endDate) && endDate.isBefore(startDate)) {
            log.error(InvalidDataException.INVALID_DATES);
            throw new InvalidDataException(InvalidDataException.INVALID_DATES);
        }
        return getListWithinDates(accountId, startDate, endDate);
    }

    /**
     * Find account by client id
     * @param clientId : Client id
     * @return {@link Optional} of {@link Account}
     */
    private Optional<Account> findAccountByClientId(String clientId) {

        if (StringUtils.isEmpty(clientId)) {
            return Optional.empty();
        }
        return accounts.stream()
                .filter(account -> clientId.equals(account.getClientId())).findFirst();
    }

    /**
     * Verify amount
     * @param amount : Amount
     */
    private void verifyAmount(int amount, Operation operation) {
        if (amount <= 0) {
            final String errorMessage = String.format(InvalidDataException.INVALID_AMOUNT, operation);
            log.error(errorMessage);
            throw new InvalidDataException(errorMessage);
        }
    }

    /**
     * Convert List to Page of account statements
     *
     * @param accountStatements : Account statements
     * @param pageable          : Page
     * @return {@link Page} of {@link AccountStatement}
     */
    private Page<AccountStatement> mapListToPage(List<AccountStatement> accountStatements, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), accountStatements.size());
        try {
            return new PageImpl<>(accountStatements.subList(start, end), pageable, accountStatements.size());
        } catch (Exception e) {
            return new PageImpl<>(new ArrayList<>(), pageable, accountStatements.size());
        }

    }

    /**
     * Get account statements within given start and end dates
     * @param accountId : Account id
     * @param startDate : Start date
     * @param endDate   : End date
     * @return {@link List} of {@link AccountStatement}
     */
    private List<AccountStatement> getListWithinDates(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        final List<AccountStatement> accountStatements = findAccountStatementsByAccountId(accountId);
        final List<AccountStatement> resultStatements = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(accountStatements)) {
            if (Objects.nonNull(startDate) && Objects.nonNull(endDate)) {
                resultStatements.addAll(accountStatements.stream()
                        .filter(accountStatement -> accountStatement.getOperationDate()
                                .compareTo(startDate) >= 0 &&
                                accountStatement.getOperationDate()
                                        .compareTo(endDate) <= 0)
                        .collect(Collectors.toList()));
            } else if (Objects.nonNull(startDate)) {
                resultStatements.addAll(accountStatements.stream()
                        .filter(accountStatement -> accountStatement.getOperationDate()
                                .compareTo(startDate) >= 0)
                        .collect(Collectors.toList()));
            } else if (Objects.nonNull(endDate)) {
                resultStatements.addAll(accountStatements.stream()
                        .filter(accountStatement -> accountStatement.getOperationDate()
                                .compareTo(endDate) <= 0)
                        .collect(Collectors.toList()));
            } else {
                resultStatements.addAll(accountStatements);
            }
        }
        return resultStatements;
    }
}

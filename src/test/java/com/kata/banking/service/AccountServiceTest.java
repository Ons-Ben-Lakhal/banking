package com.kata.banking.service;

import com.kata.banking.exception.InsufficientAmountException;
import com.kata.banking.exception.InvalidDataException;
import com.kata.banking.model.AccountStatement;
import com.kata.banking.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Ons Ben Lakhal
 */
@ExtendWith(SpringExtension.class)
public class AccountServiceTest {

    private static final String ACCOUNT_ID = "1";

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    public void deposit_whenInvalidAccountId_thenThrowException() {
        Exception exception = Assertions.assertThrows(InvalidDataException.class, () -> accountService.deposit(1, ""));
        String expectedMessage = "Account cannot be null nor empty";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void deposit_whenMissingAccount_thenThrowException() {
        Exception exception = Assertions.assertThrows(InvalidDataException.class, () -> accountService.deposit(1, "2"));
        String expectedMessage = "Account with id 2 does not exist";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void deposit_whenInvalidAmount_thenThrowException() {
        accountService.init();
        Exception exception = Assertions.assertThrows(InvalidDataException.class, () -> accountService.deposit(0, ACCOUNT_ID));
        String expectedMessage = "The amount to DEPOSIT cannot be negative";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void deposit() {
        accountService.init();
        accountService.deposit(100, ACCOUNT_ID);
        Assertions.assertEquals(2, accountService.findAccountStatementsByAccountId(ACCOUNT_ID).size());
    }

    @Test
    public void withdraw_whenInvalidAccountId_thenThrowException() {
        Exception exception = Assertions.assertThrows(InvalidDataException.class, () -> accountService.withdraw(1, ""));
        String expectedMessage = "Account cannot be null nor empty";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void withdraw_whenMissingAccount_thenThrowException() {
        Exception exception = Assertions.assertThrows(InvalidDataException.class, () -> accountService.withdraw(1, "2"));
        String expectedMessage = "Account with id 2 does not exist";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void withdraw_whenInvalidAmount_thenThrowException() {
        accountService.init();
        Exception exception = Assertions.assertThrows(InvalidDataException.class, () -> accountService.withdraw(0, ACCOUNT_ID));
        String expectedMessage = "The amount to WITHDRAW cannot be negative";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void withdraw_whenInsufficientAmount_thenThrowException() {
        accountService.init();
        Exception exception = Assertions.assertThrows(InsufficientAmountException.class, () -> accountService.withdraw(50, ACCOUNT_ID));
        String expectedMessage = "You don't have enough balance to withdraw";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void withdraw() {
        accountService.init();
        accountService.deposit(100, ACCOUNT_ID);
        accountService.withdraw(50, ACCOUNT_ID);
        Assertions.assertEquals(3, accountService.findAccountStatementsByAccountId(ACCOUNT_ID).size());
    }

    @Test
    public void getAccountStatement_whenMissingAccount_thenThrowException() {
        Exception exception = Assertions.assertThrows(InvalidDataException.class, () -> accountService.getAccountStatement("2", null, null, 0, 5));
        String expectedMessage = "Account with id 2 does not exist";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void getAccountStatement_whenInvalidDates_thenThrowException() {
        accountService.init();
        Exception exception = Assertions.assertThrows(InvalidDataException.class, () -> accountService
                .getAccountStatement(ACCOUNT_ID, LocalDateTime.of(2022, 1, 2, 0, 0), LocalDateTime.of(2022, 1, 1, 0, 0), 0, 5));
        String expectedMessage = "Start date must be before end date";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void getAccountStatement_whenInvalidPageNumber_thenThrowException() {
        accountService.init();
        Exception exception = Assertions.assertThrows(InvalidDataException.class, () -> accountService
                .getAccountStatement(ACCOUNT_ID, null, LocalDateTime.of(2022, 1, 1, 0, 0), -1, 5));
        String expectedMessage = "Invalid page number";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void getAccountStatement_whenInvalidPageSize_thenThrowException() {
        accountService.init();
        Exception exception = Assertions.assertThrows(InvalidDataException.class, () -> accountService
                .getAccountStatement(ACCOUNT_ID, null, LocalDateTime.of(2022, 1, 1, 0, 0), 0, 0));
        String expectedMessage = "Invalid page size";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void getAccountStatement() {
        accountService.init();
        accountService.deposit(100, ACCOUNT_ID);
        accountService.withdraw(50, ACCOUNT_ID);
        Page<AccountStatement> accountStatementPage = accountService
                .getAccountStatement(ACCOUNT_ID, LocalDateTime.of(2022, 1, 1, 0, 0), null, 0, 2);

        Assertions.assertEquals(2, accountStatementPage.getTotalPages());
        Assertions.assertEquals(3, accountStatementPage.getTotalElements());
    }

    @Test
    public void getAccountStatementList_whenMissingAccount_thenThrowException() {
        Exception exception = Assertions.assertThrows(InvalidDataException.class, () -> accountService.getAccountStatement("2", null, null));
        String expectedMessage = "Account with id 2 does not exist";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void getAccountStatementList_whenInvalidDates_thenThrowException() {
        accountService.init();
        Exception exception = Assertions.assertThrows(InvalidDataException.class, () -> accountService
                .getAccountStatement(ACCOUNT_ID, LocalDateTime.of(2022, 1, 2, 0, 0), LocalDateTime.of(2022, 1, 1, 0, 0)));
        String expectedMessage = "Start date must be before end date";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void getAccountStatementList() {
        accountService.init();
        accountService.deposit(100, ACCOUNT_ID);
        accountService.withdraw(50, ACCOUNT_ID);
        List<AccountStatement> accountStatements = accountService
                .getAccountStatement(ACCOUNT_ID, null, LocalDateTime.of(2050, 1, 1, 0, 0));

        Assertions.assertEquals(3, accountStatements.size());
    }
}

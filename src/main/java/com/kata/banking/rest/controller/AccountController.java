package com.kata.banking.rest.controller;

import com.kata.banking.model.AccountStatement;
import com.kata.banking.rest.mapper.AccountStatementMapper;
import com.kata.banking.rest.request.AccountStatementRequest;
import com.kata.banking.rest.request.DepositRequest;
import com.kata.banking.rest.request.WithdrawRequest;
import com.kata.banking.rest.response.AccountStatementPageResponse;
import com.kata.banking.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Account controller for banking operations
 *
 * @author Ons Ben Lakhal
 */
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/account")
@AllArgsConstructor
public class AccountController {

    private static final String STATEMENT_URL = "/statements";
    private static final String DEPOSIT_URL = "/deposit";
    private static final String WITHDRAW_URL = "/withdraw";

    /**
     * Account Service
     */
    private final AccountService accountService;

    /**
     * Account statement mapper
     */
    private final AccountStatementMapper accountStatementMapper;

    @GetMapping(STATEMENT_URL)
    public ResponseEntity<AccountStatementPageResponse> getAccountStatements(
            @RequestBody AccountStatementRequest accountStatementRequest) {

        Page<AccountStatement> accountStatementsResponse = accountService.getAccountStatement(
                accountStatementRequest.getAccountId(), accountStatementRequest.getStartDate(),
                accountStatementRequest.getEndDate(), accountStatementRequest.getPage(), accountStatementRequest.getSize());
        return ResponseEntity.status(HttpStatus.OK)
                .body(AccountStatementPageResponse.builder()
                        .accountStatement(accountStatementMapper.accountStatementEntityToDto(
                                accountStatementsResponse.getContent()))
                        .totalPages(accountStatementsResponse.getTotalPages())
                        .totalElements(accountStatementsResponse.getTotalElements())
                        .build());
    }

    @PostMapping(DEPOSIT_URL)
    public ResponseEntity deposit(@Valid @RequestBody DepositRequest depositRequest) {
        accountService.deposit(depositRequest.getAmount(), depositRequest.getAccountId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(WITHDRAW_URL)
    public ResponseEntity withdraw(@Valid @RequestBody WithdrawRequest withdrawRequest) {
        accountService.withdraw(withdrawRequest.getAmount(), withdrawRequest.getAccountId());
        return new ResponseEntity(HttpStatus.OK);
    }
}

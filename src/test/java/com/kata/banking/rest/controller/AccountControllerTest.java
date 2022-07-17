package com.kata.banking.rest.controller;

import com.kata.banking.enums.Operation;
import com.kata.banking.exception.InvalidDataException;
import com.kata.banking.rest.mapper.AccountStatementMapper;
import com.kata.banking.service.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

/**
 * Account controller unit tests
 *
 * @author Ons Ben Lakhal
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountStatementMapper accountStatementMapper;

    private static final String ACCOUNT_URL = "/account";
    private static final String DEPOSIT_URL = "/deposit";
    private static final String WITHDRAW_URL = "/withdraw";
    private static final String STATEMENT_URL = "/statements";

    @Test
    public void deposit_whenServiceException_thenReturnBadRequest() throws Exception {
        // given
        Mockito.doThrow(new InvalidDataException(String.format(InvalidDataException.INVALID_AMOUNT, Operation.DEPOSIT))).when(accountService)
                .deposit(Mockito.anyInt(), Mockito.anyString());
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(String.join("", ACCOUNT_URL, DEPOSIT_URL))
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n \"accountId\": \"1\",\n \"amount\": \"-100\"\n}")
                .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(MockMvcResultMatchers.status()
                .is(400)).andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The amount to DEPOSIT cannot be negative"));
    }

    @Test
    public void deposit_whenValidRequest_thenReturnOk() throws Exception {
        // given
        Mockito.doNothing().when(accountService)
                .deposit(Mockito.anyInt(), Mockito.anyString());
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(String.join("", ACCOUNT_URL, DEPOSIT_URL))
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n \"accountId\": \"1\",\n \"amount\": \"100\"\n}")
                .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(MockMvcResultMatchers.status()
                .is(200));

    }

    @Test
    public void withdraw_whenServiceException_thenReturnBadRequest() throws Exception {
        // given
        Mockito.doThrow(new InvalidDataException(String.format(InvalidDataException.INVALID_AMOUNT, Operation.WITHDRAW))).when(accountService)
                .withdraw(Mockito.anyInt(), Mockito.anyString());
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(String.join("", ACCOUNT_URL, WITHDRAW_URL))
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n \"accountId\": \"1\",\n \"amount\": \"-100\"\n}")
                .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(MockMvcResultMatchers.status()
                .is(400)).andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The amount to WITHDRAW cannot be negative"));
    }

    @Test
    public void withdraw_whenValidRequest_thenReturnOk() throws Exception {
        // given
        Mockito.doNothing().when(accountService)
                .withdraw(Mockito.anyInt(), Mockito.anyString());
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(String.join("", ACCOUNT_URL, WITHDRAW_URL))
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n \"accountId\": \"1\",\n \"amount\": \"100\"\n}")
                .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(MockMvcResultMatchers.status()
                .is(200));
    }

    @Test
    public void retrieveAccountStatement() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 5);
        Mockito.when(accountService.getAccountStatement(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(new PageImpl<>(new ArrayList<>(), pageable, 0));
        // when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get(String.join("", ACCOUNT_URL, STATEMENT_URL))
                        .content("{\n \"accountId\": \"1\",\n \"size\": 5,\n \"startDate\": \"2022-01-01T00:00:00\"\n}")
                        .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(MockMvcResultMatchers.status()
                .is(200)).andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(0));
    }
}

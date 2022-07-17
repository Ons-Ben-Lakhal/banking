package com.kata.banking.rest.mapper;

import com.kata.banking.model.AccountStatement;
import com.kata.banking.rest.response.AccountStatementResponse;
import org.mapstruct.Mapper;

import java.util.List;

/**
 *
 * Account statement mapper
 *
 * @author Ons Ben Lakhal
 */
@Mapper(componentModel = "spring")
public interface AccountStatementMapper {

    List<AccountStatementResponse> accountStatementEntityToDto(List<AccountStatement> accountStatements);
}

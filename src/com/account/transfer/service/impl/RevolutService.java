package com.account.transfer.service.impl;

import com.account.transfer.datamodel.Account;
import com.account.transfer.datamodel.AccountResponse;
import com.account.transfer.datamodel.TransferRequest;
import com.account.transfer.datamodel.TransferResponse;
import com.account.transfer.processor.AccountProcessor;
import com.account.transfer.service.AccountService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

@Path("/accounts")
public class RevolutService implements AccountService {

    private AccountProcessor accountProcessor;

    public RevolutService() {
        super();
        accountProcessor = AccountProcessor.getInstance();
    }

    @Override
    @POST
    @Path("/account/register")
    @Produces(MediaType.APPLICATION_XML)
    public AccountResponse registerAccount(Account account) {
        return accountProcessor.addAccount(account);
    }

    @Override
    @POST
    @Path("/account/transfer")
    @Produces(MediaType.APPLICATION_XML)
    public TransferResponse transfer(final TransferRequest transferRequest) {
        return accountProcessor.transferMoney(transferRequest);
    }

    @Override
    @GET
    @Path("/account/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Account checkBalance(@PathParam("id") String accountNumber) {
        return accountProcessor.checkBalance(accountNumber);
    }

    @Override
    @Path("/test")
    @GET
    public String testMethod() {
        return "Hello Welcome!!!";
    }
}

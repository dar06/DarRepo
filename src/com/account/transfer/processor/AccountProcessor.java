package com.account.transfer.processor;

import com.account.transfer.datamodel.Account;
import com.account.transfer.datamodel.AccountResponse;
import com.account.transfer.datamodel.TransferRequest;
import com.account.transfer.datamodel.TransferResponse;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AccountProcessor {

    private final Map<String, Account> accounts;

    private AccountProcessor() {
        this.accounts = new ConcurrentHashMap<>();
    }

    public AccountResponse addAccount(final Account account) {
        final AccountResponse response = new AccountResponse();
        accounts.put(account.getAccountNumber(), account);
        response.setSuccess(true);
        return response;
    }

    public TransferResponse transferMoney(final TransferRequest transferRequest) {

        final TransferResponse response = new TransferResponse();
        final String fromAccountNumber = transferRequest.getFromAccountNumber();
        final String toAccountNumber = transferRequest.getToAccountNumber();
        final BigDecimal amount = transferRequest.getAmount();
        if (fromAccountNumber.equals(toAccountNumber)) {
            return setErrorResponse(response, "FromAccountNumber and ToAccountNumber are same, cannot transfer");
        }

        final Account fromAccount = createOrFetchAccount(fromAccountNumber);
        final Account toAccount = createOrFetchAccount(toAccountNumber);

        synchronized (this) {
            if (fromAccount.getAmount().compareTo(amount) < 0) {
                return setErrorResponse(response, "InSufficentBalance, cannot transfer");
            }
            accounts.compute(fromAccount.getAccountNumber(), (K, V) -> new Account(fromAccount.getAccountNumber(), fromAccount.getAmount().subtract(amount)));
            accounts.compute(toAccount.getAccountNumber(), (K, V) -> new Account(toAccount.getAccountNumber(), toAccount.getAmount().add(amount)));
        }

        response.setFromAccount(accounts.get(fromAccountNumber));
        response.setToAccount(accounts.get(toAccountNumber));
        response.setSuccess(true);
        return response;

    }


    private TransferResponse setErrorResponse(TransferResponse response, String errorMsg) {
        response.setSuccess(false);
        response.setErrorMsg(errorMsg);
        return response;
    }


    private Account createOrFetchAccount(String accountNumber) {
        return accounts.computeIfAbsent(accountNumber, k -> new Account(accountNumber));
    }


    public Account checkBalance(final String accountNumber) {
        return accounts.get(accountNumber);
    }

    private static class AccountProcessorLoader {
        private static final AccountProcessor INSTANCE = new AccountProcessor();
    }

    public static AccountProcessor getInstance() {
        return AccountProcessorLoader.INSTANCE;
    }
}

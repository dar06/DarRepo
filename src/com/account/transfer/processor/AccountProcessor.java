package com.account.transfer.processor;

import com.account.transfer.datamodel.Account;
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

    public TransferResponse transferMoney(final TransferRequest transferRequest) {

        final TransferResponse response = new TransferResponse();
        response.setSuccess(true);
        final Account fromAccountInRequest = transferRequest.getFromAccount();
        final Account toAccountInRequest = transferRequest.getToAccount();
        final BigDecimal amount = transferRequest.getAmount();

        if (fromAccountInRequest == null || fromAccountInRequest.getAccountNumber() == null) {
            return errorResponse("FromAccount cannot be null, cannot transfer", fromAccountInRequest, toAccountInRequest, response);
        }

        if (toAccountInRequest == null || toAccountInRequest.getAccountNumber() == null) {
            return errorResponse("ToAccount cannot be null, cannot transfer", fromAccountInRequest, toAccountInRequest, response);
        }

        if (fromAccountInRequest.getAccountNumber().equals(toAccountInRequest.getAccountNumber())) {
            return errorResponse("FromAccountNumber and ToAccountNumber are same, cannot transfer",
                    fromAccountInRequest, toAccountInRequest, response);
        }

        synchronized (this) {
            final Account fromAccount = createOrFetchAccount(fromAccountInRequest);
            final Account toAccount = createOrFetchAccount(toAccountInRequest);

            if (fromAccount.getAmount().compareTo(amount) < 0) {
                return errorResponse("InSufficentBalance, cannot transfer", fromAccount, toAccount, response);
            }
            accounts.compute(fromAccount.getAccountNumber(), (K, V) -> new Account(fromAccount.getAccountNumber(), fromAccount.getAmount().subtract(amount)));
            accounts.compute(toAccount.getAccountNumber(), (K, V) -> new Account(toAccount.getAccountNumber(), toAccount.getAmount().add(amount)));
        }
        response.setFromAccount(accounts.get(fromAccountInRequest.getAccountNumber()));
        response.setToAccount(accounts.get(toAccountInRequest.getAccountNumber()));
        return response;
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }

    private TransferResponse errorResponse(final String errorMsg, final Account fromAccount, final Account toAccount, final TransferResponse response) {
        response.setSuccess(false);
        response.setErrorMsg(errorMsg);
        response.setFromAccount(fromAccount);
        response.setToAccount(toAccount);
        return response;
    }

    private Account createOrFetchAccount(Account account) {
        return accounts.computeIfAbsent(account.getAccountNumber(), k -> new Account(account.getAccountNumber(), account.getAmount()));
    }

    private static class AccountProcessorLoader {
        private static final AccountProcessor INSTANCE = new AccountProcessor();
    }

    public static AccountProcessor getInstance() {
        return AccountProcessorLoader.INSTANCE;
    }
}

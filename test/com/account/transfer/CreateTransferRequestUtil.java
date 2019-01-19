package com.account.transfer;

import com.account.transfer.datamodel.Account;
import com.account.transfer.datamodel.TransferRequest;

import java.math.BigDecimal;

public class CreateTransferRequestUtil {

    public static TransferRequest createTransferRequest(final Account fromAccount, final Account toAccount, final BigDecimal amount) {
        final TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFromAccount(fromAccount);
        transferRequest.setToAccount(toAccount);
        transferRequest.setAmount(amount);
        return transferRequest;
    }
}

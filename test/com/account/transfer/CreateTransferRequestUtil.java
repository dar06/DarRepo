package com.account.transfer;

import com.account.transfer.datamodel.TransferRequest;

import java.math.BigDecimal;

public class CreateTransferRequestUtil {

    public static TransferRequest createTransferRequest(final String fromAccount, final String toAccount, final BigDecimal amount) {
        final TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFromAccountNumber(fromAccount);
        transferRequest.setToAccountNumber(toAccount);
        transferRequest.setAmount(amount);
        return transferRequest;
    }
}

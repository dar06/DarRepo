package com.account.transfer.service;

import com.account.transfer.datamodel.Account;
import com.account.transfer.datamodel.AccountResponse;
import com.account.transfer.datamodel.TransferRequest;
import com.account.transfer.datamodel.TransferResponse;

import java.math.BigDecimal;

public interface AccountService {

    TransferResponse transfer(TransferRequest transferRequest);
}

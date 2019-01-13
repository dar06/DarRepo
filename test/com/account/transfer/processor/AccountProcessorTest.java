package com.account.transfer.processor;

import com.account.transfer.datamodel.Account;
import com.account.transfer.datamodel.TransferRequest;
import com.account.transfer.datamodel.TransferResponse;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.account.transfer.CreateTransferRequestUtil.createTransferRequest;
import static org.junit.Assert.*;

public class AccountProcessorTest {

    private AccountProcessor accountProcessor;

    @Before
    public void setUp() {
        accountProcessor = AccountProcessor.getInstance();
    }

    @Test
    public void shouldTransferMoneyFromOneAccountToOther() {
        //given
        accountProcessor.addAccount(new Account("111", new BigDecimal("200")));
        final TransferRequest transferRequest = createTransferRequest("111", "222", new BigDecimal("50"));
        //when
        final TransferResponse response = accountProcessor.transferMoney(transferRequest);

        //then
        assertTrue(response.isSuccess());
        assertEquals(new BigDecimal("150"), accountProcessor.checkBalance("111").getAmount());
        assertEquals(new BigDecimal("50"), accountProcessor.checkBalance("222").getAmount());

    }


    @Test
    public void shouldSetErrorMsgWhenFromAndToAccountAreSame() {
        //when
        final TransferResponse response = accountProcessor.transferMoney(createTransferRequest("111", "111", BigDecimal.TEN));

        //then
        assertFalse(response.isSuccess());
        assertThat(response.getErrorMsg(), Matchers.containsString("FromAccountNumber and ToAccountNumber are same"));
    }


    @Test
    public void shouldSetErrorMsgWhenInSufficientBalance() {
        //when
        final TransferResponse response = accountProcessor.transferMoney(createTransferRequest("111", "222", new BigDecimal("500")));

        //then
        assertFalse(response.isSuccess());
        assertThat(response.getErrorMsg(), Matchers.containsString("InSufficentBalance"));

    }





}
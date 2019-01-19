package unit.com.account.transfer.service.impl;

import com.account.transfer.datamodel.Account;
import com.account.transfer.datamodel.TransferResponse;
import com.account.transfer.service.AccountService;
import com.account.transfer.service.impl.RevolutService;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.math.BigDecimal;

import static com.account.transfer.CreateTransferRequestUtil.createTransferRequest;
import static org.junit.Assert.*;

public class RevolutServiceTest {

    @Test
    public void shouldTransferMoney() {

        //given
        final AccountService accountService = new RevolutService();
        final Account fromAccount = new Account("111", new BigDecimal("200"));
        final Account toAccount = new Account("222", new BigDecimal("50"));

        //when
        final TransferResponse transferResponse = accountService.transfer(createTransferRequest(fromAccount, toAccount, new BigDecimal("50")));

        //then
        assertTrue(transferResponse.isSuccess());
        assertEquals(new BigDecimal("150"), transferResponse.getFromAccount().getAmount());
        assertEquals(new BigDecimal("100"), transferResponse.getToAccount().getAmount());
    }

    @Test
    public void shouldNotTransferWhenInsufficientBalance() {

        //given
        final AccountService accountService = new RevolutService();
        final Account fromAccount = new Account("111", new BigDecimal("200"));
        final Account toAccount = new Account("222", new BigDecimal("50"));

        //when
        final TransferResponse transferResponse = accountService.transfer(createTransferRequest(fromAccount, toAccount, new BigDecimal("250")));

        //then
        assertFalse(transferResponse.isSuccess());
        assertThat(transferResponse.getErrorMsg(), Matchers.containsString("InSufficentBalance"));
    }

    @Test
    public void shouldNotTransferWhenFromAndToAccountSame() {

        //given
        final AccountService accountService = new RevolutService();
        final Account fromAccount = new Account("111", new BigDecimal("200"));

        //when
        final TransferResponse transferResponse = accountService.transfer(createTransferRequest(fromAccount, fromAccount, new BigDecimal("250")));

        //then
        assertFalse(transferResponse.isSuccess());
        assertThat(transferResponse.getErrorMsg(), Matchers.containsString("FromAccountNumber and ToAccountNumber are same, cannot transfer"));
    }
}






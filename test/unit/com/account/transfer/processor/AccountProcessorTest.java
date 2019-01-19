package unit.com.account.transfer.processor;

import com.account.transfer.datamodel.Account;
import com.account.transfer.datamodel.TransferRequest;
import com.account.transfer.datamodel.TransferResponse;
import com.account.transfer.processor.AccountProcessor;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.*;

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
        final Account fromAccount = new Account("111", new BigDecimal("200"));
        final Account toAccount = new Account("222", null);
        final TransferRequest transferRequest = createTransferRequest(fromAccount, toAccount, new BigDecimal("50"));
        //when
        final TransferResponse response = accountProcessor.transferMoney(transferRequest);

        //then
        assertTrue(response.isSuccess());
        assertEquals(new BigDecimal("150"), response.getFromAccount().getAmount());
        assertEquals(new BigDecimal("50"), response.getToAccount().getAmount());

    }


    @Test
    public void shouldSetErrorMsgWhenFromAndToAccountAreSame() {
        //when
        final Account account = new Account("111", new BigDecimal("200"));
        final TransferResponse response = accountProcessor.transferMoney(createTransferRequest(account, account, BigDecimal.TEN));

        //then
        assertFalse(response.isSuccess());
        assertThat(response.getErrorMsg(), Matchers.containsString("FromAccountNumber and ToAccountNumber are same"));
    }

    @Test
    public void shouldSetErrorMsgWhenInSufficientBalance() {
        //when
        final Account fromAccount = new Account("333", new BigDecimal("200"));
        final Account toAccount = new Account("444", new BigDecimal("200"));

        final TransferResponse response = accountProcessor.transferMoney(createTransferRequest(fromAccount, toAccount, new BigDecimal("500")));

        //then
        assertFalse(response.isSuccess());
        assertThat(response.getErrorMsg(), Matchers.containsString("InSufficentBalance"));
    }

    @Test
    public void shouldTransferMoneyAsyncTest() throws ExecutionException, InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final Account fromAccount = new Account("555", new BigDecimal("6000"));
        final Account toAccount = new Account("666", null);
        final TransferRequest request = new TransferRequest();
        request.setFromAccount(fromAccount);
        request.setToAccount(toAccount);
        request.setAmount(new BigDecimal("2000"));
        final int threads = 2;
        final ExecutorService service = Executors.newFixedThreadPool(threads);
        final Collection<Future<TransferResponse>> futures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            futures.add(
                    service.submit(
                            () -> {
                                latch.await();
                                TransferResponse response = accountProcessor.transferMoney(request);
                                return response;
                            }
                    ));
        }
        latch.countDown();
        for (int j = 0; j < futures.size(); j++) {
            ((ArrayList<Future<TransferResponse>>) futures).get(j).get();
        }
        service.shutdown();
        assertEquals(BigDecimal.ZERO, accountProcessor.getAccounts().get("555").getAmount());
        assertEquals(new BigDecimal("6000"), accountProcessor.getAccounts().get("666").getAmount());
    }
}
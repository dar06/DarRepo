package integration.com.account.transfer.service;


import com.account.transfer.datamodel.Account;
import com.account.transfer.datamodel.TransferRequest;
import com.account.transfer.datamodel.TransferResponse;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class RevolutServiceRestTest {

    @Test
    public void shouldTransferMoney() {
        final Client client = ClientBuilder.newClient();
        final WebTarget webTarget = client.target("http://localhost:8080").path("/accounts/account/transfer");

        final TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFromAccount(new Account("111", new BigDecimal("100")));
        transferRequest.setToAccount(new Account("222", BigDecimal.ZERO));
        transferRequest.setAmount(new BigDecimal("100"));

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
        Response response = invocationBuilder.post(Entity.entity(transferRequest, MediaType.APPLICATION_XML));
        final TransferResponse transferResponse = response.readEntity(TransferResponse.class);
        assertEquals(200, response.getStatus());
        final Account fromAccount = transferResponse.getFromAccount();
        assertEquals("111", fromAccount.getAccountNumber());
        assertEquals(BigDecimal.ZERO, fromAccount.getAmount());
        final Account toAccount = transferResponse.getToAccount();
        assertEquals("222", toAccount.getAccountNumber());
        assertEquals(new BigDecimal("100"), toAccount.getAmount());
    }

    @Test
    public void shouldNotTransferMoneyWhenAccountNull() {
        final Client client = ClientBuilder.newClient();
        final WebTarget webTarget = client.target("http://localhost:8080").path("/accounts/account/transfer");

        final TransferRequest transferRequest = new TransferRequest();

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
        Response response = invocationBuilder.post(Entity.entity(transferRequest, MediaType.APPLICATION_XML));
        final TransferResponse transferResponse = response.readEntity(TransferResponse.class);

        assertEquals(200, response.getStatus());
        assertThat(transferResponse.getErrorMsg(), Matchers.containsString("FromAccount cannot be null, cannot transfer")) ;
    }

    @Test
    public void shouldNotTransferMoneyWhenFromAndToAccountAreSame() {
        final Client client = ClientBuilder.newClient();
        final WebTarget webTarget = client.target("http://localhost:8080").path("/accounts/account/transfer");

        final TransferRequest transferRequest = new TransferRequest();
        final Account account = new Account("111", new BigDecimal("500"));
        transferRequest.setFromAccount(account);
        transferRequest.setToAccount(account);

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
        Response response = invocationBuilder.post(Entity.entity(transferRequest, MediaType.APPLICATION_XML));
        final TransferResponse transferResponse = response.readEntity(TransferResponse.class);

        assertEquals(200, response.getStatus());
        assertThat(transferResponse.getErrorMsg(), Matchers.containsString("FromAccountNumber and ToAccountNumber are same")) ;
    }
}

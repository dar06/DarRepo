package com.account.transfer.datamodel;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement
public class Account {

    private String accountNumber;
    private BigDecimal amount;

    public Account() {
    }

    public Account(final String accountNumber, final BigDecimal amount) {
        this.accountNumber = accountNumber;
        this.amount = amount == null ? BigDecimal.ZERO : amount;
    }

    public Account(final String accountNumber) {
        this.accountNumber = accountNumber;
        this.amount = BigDecimal.ZERO;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Account other = (Account) obj;
        return new EqualsBuilder()
                .append(accountNumber, other.accountNumber)
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(accountNumber)
                .append(amount)
                .toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append(accountNumber)
                .append(amount).toString();
    }

}




package com.company.misc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Locale;

/**
 * Original code - Martin Fowler from the book Enterprise Architecture Patterns
 * I've added some additional functions, such as Add(), Multiply, Subtract, etc. cleanup
 */

public class Money {

    private static final Currency USD = Currency.getInstance("USD");
    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;

    private BigDecimal amount;
    private Currency currency;


    public static Money dollars(String amount) {
        return dollars(new BigDecimal(amount));
    }

    public static Money dollars(BigDecimal amount) {
        return new Money(amount, USD);
    }

    public Money add(Money amount){
        return new Money(this.amount.add(amount.amount), currency);
    }
    public Money subtract (Money amount){
        return new Money(this.amount.subtract(amount.amount), currency);
    }
    public Money multiply(Money amount){
        return new Money(this.amount.multiply(amount.amount), currency);
    }
    public Money divide(Money amount){
        return new Money(this.amount.divide(amount.amount), currency);
    }


    /**
     * Compare to another object
     * @param other
     * @return
     *  -1 = less than other
     *  0  = equal than other
     *  1  = greater than other
     *
     */
    public int compareTo(Money other){
        return this.amount.compareTo(other.amount);
    }

    Money(BigDecimal amount, Currency currency) {
        this(amount, currency, DEFAULT_ROUNDING);
    }

    Money(BigDecimal amount, Currency currency, RoundingMode rounding) {
        this.amount = amount;
        this.currency = currency;

        this.amount = amount.setScale(currency.getDefaultFractionDigits(), rounding);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return getCurrency().getSymbol() + " " + getAmount();
    }

    public String toString(Locale locale) {
        return getCurrency().getSymbol(locale) + " " + getAmount();
    }
}

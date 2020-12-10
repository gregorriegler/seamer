package com.gregorriegler.seamer.demos.springcdi;

import java.math.BigDecimal;

public class Money {

    private final Currency currency = new Currency("EUR");
    private final BigDecimal amount;

    private Money() {
        amount = null;
    }

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return currency.toString() + " " + amount.toString();
    }
}

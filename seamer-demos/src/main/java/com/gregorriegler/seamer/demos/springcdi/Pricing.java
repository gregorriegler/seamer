package com.gregorriegler.seamer.demos.springcdi;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Pricing {
    Money calculatePrice(String product) {
        return new Money(BigDecimal.ONE);
    }
}

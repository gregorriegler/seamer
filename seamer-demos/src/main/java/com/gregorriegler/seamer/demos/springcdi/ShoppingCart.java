package com.gregorriegler.seamer.demos.springcdi;

import com.gregorriegler.seamer.Seamer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCart {

    private Pricing pricing;

    private ShoppingCart() {
        pricing = null;
    }

    @Autowired
    public ShoppingCart(Pricing pricing) {
        this.pricing = pricing;
    }

    public Money price() {
        String product = "product";
        Money money = Seamer.create()
            .define("price", a -> pricing.calculatePrice((String) a[0]))
            .invokeAndRecord(product);
        return money;
    }
}

package com.gregorriegler.seamer.demos.springcdi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class ShoppingCartApp {

    private static final Logger LOG = LoggerFactory.getLogger(ShoppingCartApp.class);

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ShoppingCartApp.class);
        ShoppingCart cart = context.getBean(ShoppingCart.class);
        Money money = cart.price();
        LOG.info("" + money);
    }
}

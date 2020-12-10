package com.gregorriegler.seamer.demos.springcdi;

import com.gregorriegler.seamer.Seamer;
import org.junit.jupiter.api.Test;

class ShoppingCartTest {

    @Test
    void seamer() {
        Seamer.create()
            .get("price")
            .verify((a, i) -> a.asString().isEqualTo(i.toString()));
    }
}
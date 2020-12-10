package com.gregorriegler.seamer.demos.springcdi;

public class Currency {
    private String value;

    public Currency(String value) {
        this.value = value;
    }

    private Currency() {
        value = null;
    }

    @Override
    public String toString() {
        return value;
    }
}

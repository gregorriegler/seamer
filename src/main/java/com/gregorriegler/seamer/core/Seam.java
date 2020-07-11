package com.gregorriegler.seamer.core;

public class Seam<T> {

    private final String id;
    private final Invokable<T> invokable;

    public Seam(String id, Invokable<T> invokable) {
        if(id == null) throw new IllegalArgumentException("id is mandatory");
        if(invokable == null) throw new IllegalArgumentException("seam is mandatory");
        this.id = id;
        this.invokable = invokable;
    }

    public String id() {
        return id;
    }

    public Invokable<T> seam() {
        return invokable;
    }
}

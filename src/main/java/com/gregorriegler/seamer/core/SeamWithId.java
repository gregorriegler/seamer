package com.gregorriegler.seamer.core;

public class SeamWithId<T> {

    private final String id;
    private final Suture<T> suture;

    public SeamWithId(String id, Suture<T> suture) {
        if(id == null) throw new IllegalArgumentException("id is mandatory");
        if(suture == null) throw new IllegalArgumentException("seam is mandatory");
        this.id = id;
        this.suture = suture;
    }

    public String id() {
        return id;
    }

    public Suture<T> seam() {
        return suture;
    }
}

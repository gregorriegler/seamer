package com.gregorriegler.seamer.core;

public class SeamWithId<T> {

    private final String id;
    private final Seam<T> seam;

    public SeamWithId(String id, Seam<T> seam) {
        if(id == null) throw new IllegalArgumentException("id is mandatory");
        if(seam == null) throw new IllegalArgumentException("seam is mandatory");
        this.id = id;
        this.seam = seam;
    }

    public String id() {
        return id;
    }

    public Seam<T> seam() {
        return seam;
    }
}

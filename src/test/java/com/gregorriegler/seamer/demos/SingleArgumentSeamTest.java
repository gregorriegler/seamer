package com.gregorriegler.seamer.demos;

import com.gregorriegler.seamer.Seamer;
import com.gregorriegler.seamer.core.InvokableWith1Argument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SingleArgumentSeamTest {

    private static final String SEAM_ID = "SingleArgumentSeam";
    private final Seamer seamer = Seamer.create();

    @BeforeEach
    public void recordInvocations() {
        seamer.reset(SEAM_ID);

        SomeClass someClass = new SomeClass();
        for (int i = 0; i < 5; i++) {
            someClass.entryPoint(i);
        }
    }

    @Test
    void verify() {
        Seamer.verify(SEAM_ID);
    }

    public static class SomeClass {

        public void entryPoint(Integer arg1) {
            Seamer.createSeam(
                SEAM_ID,
                (InvokableWith1Argument<Integer, Integer>) this::blackbox
            ).invokeAndRecord(arg1);
        }

        public Integer blackbox(Integer i) {
            return i * 2;
        }

    }
}

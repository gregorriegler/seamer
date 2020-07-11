package com.gregorriegler.seamer.demos;

import com.gregorriegler.seamer.Seamer;
import com.gregorriegler.seamer.core.SutureWith1Argument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SingleArgumentSeamTest {

    private static final String SEAM_ID = "SingleArgumentSeam";

    @BeforeEach
    public void recordInvocations() {
        Seamer.reset(SEAM_ID);

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
            Seamer.create(
                SEAM_ID,
                (SutureWith1Argument<Integer, Integer>) this::blackbox
            ).invokeAndRecord(arg1);
        }

        public Integer blackbox(Integer i) {
            return i * 2;
        }

    }
}

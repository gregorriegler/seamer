package com.gregorriegler.seamer.demos;

import com.gregorriegler.seamer.Seamer;
import com.gregorriegler.seamer.core.MethodWith1Argument;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SingleArgumentSeamTest {

    private static final String SEAM_ID = "SingleArgumentSeam";

    @BeforeAll
    public void recordInvocations() {
        Seamer.reset(SEAM_ID);

        SomeClass someClass = new SomeClass();
        for (int i = 0; i < 5; i++) {
            someClass.entryPoint(i);
        }
    }

    public static class SomeClass {

        public void entryPoint(Integer arg1) {
            Seamer.intercept(
                SEAM_ID,
                (MethodWith1Argument<Integer, Integer>) this::blackbox
            ).invokeAndRecord(arg1);
        }

        public Integer blackbox(Integer i) {
            return i * 2;
        }
    }

    @Test
    void verify() {
        Seamer.verify(SEAM_ID);
    }
}

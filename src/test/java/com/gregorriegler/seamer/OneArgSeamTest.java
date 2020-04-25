package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Signature1;
import com.gregorriegler.seamer.test.SeamerTest;
import org.junit.jupiter.api.BeforeAll;

public class OneArgSeamTest extends SeamerTest {

    private static final String SEAM_ID = "OneArgSeamTest";

    @BeforeAll
    @Override
    public void setup() {
        Seamer.reset(SEAM_ID);

        SomeClass someClass = new SomeClass();

        for (int i = 0; i < 5; i++) {
            someClass.entryPoint(i);
        }

        seam = Seamer.load(SEAM_ID, SomeClass.class);
    }

    @Override
    public Class<?> capturingClass() {
        return SomeClass.class;
    }

    @Override
    public String seamId() {
        return SEAM_ID;
    }

    public static class SomeClass {

        public void entryPoint(Integer arg1) {
            Seamer.intercept(
                "OneArgSeamTest",
                this.getClass(),
                (Signature1<Integer, Integer>) this::blackbox
            ).invoke(arg1);
        }

        public Integer blackbox(Integer i) {
            return i * 2;
        }

    }
}

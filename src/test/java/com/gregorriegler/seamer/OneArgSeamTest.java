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

        SomeClass oneArgSeam = new SomeClass();

        for (int i = 0; i < 5; i++) {
            oneArgSeam.entryPoint(i);
        }

        seam = Seamer.load(SomeClass.class, SEAM_ID);
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
                (Signature1<Integer, Integer>) this::blackbox,
                this.getClass(),
                "OneArgSeamTest"
            ).invoke(arg1);
        }

        public Integer blackbox(Integer i) {
            return i * 2;
        }

    }
}

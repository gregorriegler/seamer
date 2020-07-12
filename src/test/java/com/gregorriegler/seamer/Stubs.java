package com.gregorriegler.seamer;

import com.gregorriegler.seamer.core.Invocation;
import com.gregorriegler.seamer.core.Invocations;
import com.gregorriegler.seamer.core.ProxyInvokable;

import java.util.List;

public class Stubs {
    public static Invocations invocations() {
        return new Invocations() {
            @Override
            public void record(String seamId, Invocation invocation) {

            }

            @Override
            public List<Invocation> getAll(String seamId) {
                return null;
            }
        };
    }

    public static ProxyInvokable<Object> invokable() {
        return ProxyInvokable.of(new Object(), "test");
    }
}

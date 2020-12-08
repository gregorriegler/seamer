package com.gregorriegler.seamer.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ArgCandidates implements Serializable {
    private final Map<Integer, List<Object>> argCandidates = new HashMap<>();

    public ArgCandidates() {
    }

    public void addCandidates(int i, List<Object> candidates) {
        if (!argCandidates.containsKey(i)) argCandidates.put(i, new ArrayList<>());
        argCandidates.get(i).addAll(candidates);
    }

    public void addCandidates(int i, Supplier<List<Object>> supplier) {
        argCandidates.put(i, supplier.get());
    }

    public List<Object[]> shuffle() {
        List<Object[]> argCombinations = new ArrayList<>();
        loopOverPosition(0, new ArrayList<>(), argCombinations);
        return argCombinations;
    }

    private void loopOverPosition(int pos, List<Object> leftArgs, List<Object[]> argCombinations) {
        for (int i = 0; i < argCandidates.get(pos).size(); i++) {
            ArrayList<Object> objects = new ArrayList<>(leftArgs);
            objects.add(argCandidates.get(pos).get(i));

            if (onLastPosition(pos)) {
                argCombinations.add(objects.toArray(new Object[0]));
            } else {
                loopOverPosition(pos + 1, objects, argCombinations);
            }
        }
    }

    private boolean onLastPosition(int pos) {
        return pos == argCandidates.size() - 1;
    }

}

package seamer.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.ArrayUtils.addAll;

public class ArgCandidates implements Serializable {
    private Map<Integer, List<Object>> argCandidates = new HashMap<>();

    public ArgCandidates() {
    }

    public void addCandidates(int i, List candidates) {
        if (!argCandidates.containsKey(i)) argCandidates.put(i, new ArrayList<>());
        argCandidates.get(i).addAll(candidates);
    }

    public List<Object[]> shuffle() {
        List<Object[]> argCombinations = new ArrayList<>();
        loopOverPosition(0, new Object[]{}, argCombinations);
        return argCombinations;
    }

    private void loopOverPosition(int pos, Object[] leftArgs, List<Object[]> argCombinations) {
        for (int i = 0; i < argCandidates.get(pos).size(); i++) {
            Object arg = argCandidates.get(pos).get(i);

            if (onLastPosition(pos)) {
                argCombinations.add(addAll(leftArgs, arg));
            } else {
                loopOverPosition(pos + 1, addAll(leftArgs, arg), argCombinations);
            }
        }
    }

    private boolean onLastPosition(int pos) {
        return pos == argCandidates.size() - 1;
    }


}

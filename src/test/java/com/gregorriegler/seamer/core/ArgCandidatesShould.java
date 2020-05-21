package com.gregorriegler.seamer.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class ArgCandidatesShould {

    private ArgCandidates candidates;

    @BeforeEach
    void setUp() {
        candidates = new ArgCandidates();
    }

    @Test
    void add_candidates_and_shuffle() {
        candidates.addCandidates(0, asList("hello", "world"));

        List<Object[]> result = candidates.shuffle();

        assertThat(result).containsExactlyInAnyOrder(
            new Object[]{"hello"},
            new Object[]{"world"}
        );
    }

    @Test
    void add_candidates_through_supplier() {
        candidates.addCandidates(0, () -> asList("hello", "world"));

        List<Object[]> result = candidates.shuffle();

        assertThat(result).containsExactlyInAnyOrder(
            new Object[]{"hello"},
            new Object[]{"world"}
        );
    }

    @Test
    void shuffle_two_args() {
        candidates.addCandidates(0, asList("hello", "world"));
        candidates.addCandidates(1, asList(1, 2, 3));

        List<Object[]> result = candidates.shuffle();

        assertThat(result).containsExactlyInAnyOrder(
            new Object[]{"hello", 1},
            new Object[]{"hello", 2},
            new Object[]{"hello", 3},
            new Object[]{"world", 1},
            new Object[]{"world", 2},
            new Object[]{"world", 3}
        );
    }
}

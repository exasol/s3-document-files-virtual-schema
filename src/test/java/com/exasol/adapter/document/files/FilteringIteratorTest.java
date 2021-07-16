package com.exasol.adapter.document.files;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class FilteringIteratorTest {

    @Test
    void test() {
        final List<Integer> result = new ArrayList<>();
        new FilteringIterator<>(List.of(1, 2, 3, 4).iterator(), x -> x % 2 == 0).forEachRemaining(result::add);
        assertThat(result, Matchers.contains(2, 4));
    }
}
package com.selfxdsd.todos;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Iterator;

/**
 * Unit tests for {@link PuzzlesWithFallback}.
 * @author criske
 * @version $Id$
 * @since 0.0.2
 */
public final class PuzzlesWithFallbackTestCase {

    /**
     * Can process the original puzzles.
     * @throws PuzzlesProcessingException Something went wrong.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void canProcessOriginalPuzzles() throws PuzzlesProcessingException {
        final Puzzles<String> original = Mockito.mock(Puzzles.class);
        final Iterator<Puzzle> originalIterator = Mockito.mock(Iterator.class);
        Mockito.when(original.iterator()).thenReturn(originalIterator);
        final Puzzles<String> fallback = Mockito.mock(Puzzles.class);
        final Iterator<Puzzle> fallbackIterator = Mockito.mock(Iterator.class);
        Mockito.when(fallback.iterator()).thenReturn(fallbackIterator);

        final Puzzles<String> puzzles = new PuzzlesWithFallback<>(original,
            fallback);

        puzzles.process("");

        MatcherAssert.assertThat(puzzles.iterator(),
            Matchers.equalTo(originalIterator));
    }

    /**
     * Falls back when original puzzles failed to be processed.
     * @throws PuzzlesProcessingException Something went wrong.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void fallsBackWhenOriginalFails() throws PuzzlesProcessingException {
        final Puzzles<String> original = Mockito.mock(Puzzles.class);
        final Iterator<Puzzle> originalIterator = Mockito.mock(Iterator.class);
        Mockito.when(original.iterator()).thenReturn(originalIterator);
        Mockito.doThrow(PuzzlesProcessingException.class)
            .when(original).process(Mockito.anyString());
        final Puzzles<String> fallback = Mockito.mock(Puzzles.class);
        final Iterator<Puzzle> fallbackIterator = Mockito.mock(Iterator.class);
        Mockito.when(fallback.iterator()).thenReturn(fallbackIterator);

        final Puzzles<String> puzzles = new PuzzlesWithFallback<>(original,
            fallback);

        puzzles.process("");

        MatcherAssert.assertThat(puzzles.iterator(),
            Matchers.equalTo(fallbackIterator));
    }

    /**
     * Throws when original and fallback fail the processed.
     * @throws PuzzlesProcessingException Something went wrong.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void throwsWhenAllFail() throws PuzzlesProcessingException {
        final Puzzles<String> original = Mockito.mock(Puzzles.class);
        Mockito.doThrow(PuzzlesProcessingException.class)
            .when(original).process(Mockito.anyString());
        final Puzzles<String> fallback = Mockito.mock(Puzzles.class);
        Mockito.doThrow(PuzzlesProcessingException.class)
            .when(fallback).process(Mockito.anyString());

        final Puzzles<String> puzzles = new PuzzlesWithFallback<>(original,
            fallback);

        Assertions.assertThrows(PuzzlesProcessingException.class,
            () -> puzzles.process(""));
    }
}

/**
 * Copyright (c) 2020, Self XDSD Contributors
 * All rights reserved.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"),
 * to read the Software only. Permission is hereby NOT GRANTED to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.selfxdsd.todos;

import java.util.Collections;
import java.util.Iterator;

/**
 * Processes the "fallback" puzzle strategy when the "original" fails.
 * @param <I> The type of input used for processing.
 * @author criske
 * @version $Id$
 * @since 0.0.2
 */
public final class PuzzlesWithFallback<I> implements Puzzles<I> {

    /**
     * Original puzzles.
     */
    private final Puzzles<I> original;

    /**
     * Fallback puzzles.
     */
    private final Puzzles<I> fallback;

    /**
     * Processed puzzles.
     */
    private Iterator<Puzzle> puzzles;

    /**
     * Ctor.
     * @param original Original puzzles.
     * @param fallback Fallback puzzles in case original fails.
     */
    public PuzzlesWithFallback(final Puzzles<I> original,
                               final Puzzles<I> fallback) {
        this.original = original;
        this.fallback = fallback;
        this.puzzles = Collections.emptyIterator();
    }

    @Override
    public void process(final I input) throws PuzzlesProcessingException {
        try {
            original.process(input);
            puzzles = original.iterator();
        } catch (final PuzzlesProcessingException originalException) {
            try {
                fallback.process(input);
                puzzles = fallback.iterator();
            } catch (final PuzzlesProcessingException fallbackException) {
                //merging exceptions: original ex will be shown first
                //and then fallback ex in the suppressed section of stacktrace.
                originalException.addSuppressed(fallbackException);
                throw originalException;
            }
        }
    }

    @Override
    public Iterator<Puzzle> iterator() {
        return puzzles;
    }

}

package com.selfxdsd.todos;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Iterator;

/**
 * Puzzles obtained from "resources".
 * @author criske
 * @version $Id$
 * @since 0.0.1
 */
final class ResourcesPuzzles implements Puzzles<String> {

    /**
     * Next puzzles processor.
     */
    private final Puzzles<String> next;

    /**
     * Ctor.
     * @param next Next puzzles processor.
     */
    ResourcesPuzzles(final Puzzles<String> next) {
        this.next = next;
    }

    @Override
    public void process(final String input) throws PuzzlesProcessingException {
        try {
            final URL resource = getClass()
                .getClassLoader()
                .getResource(input);
            if (resource != null) {
                final File file = new File(resource.getFile());
                final String result = Files.readString(file.toPath());
                this.next.process(result);
            } else {
                throw new PuzzlesProcessingException("Puzzles resource "
                    + input + " not found.");
            }
        } catch (final IOException exception) {
            throw new PuzzlesProcessingException(exception);
        }
    }

    @Override
    public Iterator<Puzzle> iterator() {
        return next.iterator();
    }
}

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

import com.jcabi.ssh.Shell;
import com.jcabi.ssh.Ssh;
import com.selfxdsd.api.Project;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Component which connects to the SSH server via SSH and reads
 * the puzzles.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
@Component
@RequestScope
public class PuzzlesComponent {

    /**
     * SSH Connection.
     */
    private final Shell ssh = new Ssh(
        System.getenv("host"),
        Integer.valueOf(System.getenv("port")),
        System.getenv("username"),
        Files.readString(
            Path.of(System.getenv("privatekey"))
        ));

    /**
     * Ctor.
     * @throws IOException If any IO problems occur while connecting
     *  to the SSH server.
     */
    public PuzzlesComponent() throws IOException { }

    /**
     * Handle the puzzles for the given Project.
     * @param project Project.
     * @return Iterable of Puzzle.
     * @throws PuzzlesProcessingException If something went wrong during
     * processing the puzzles.
     */
    public Puzzles<Project> read(final Project project)
        throws PuzzlesProcessingException {
        final Puzzles<Project> puzzles = new SshPuzzles(
            this.ssh,
            new DocumentPuzzles(project)
        );
        puzzles.process(project);
        return puzzles;
    }
}

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
import com.selfxdsd.api.Issue;
import com.selfxdsd.api.Issues;
import com.selfxdsd.api.Project;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Component which connects to the SSH server via SSH, reads
 * the puzzles and opens/closes issues based on them.
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
     * Review the puzzles of the given Project. If it's a new puzzle,
     * open an Issue for it. If the Project contains open Issues which
     * don't have a corresponding puzzle, close them.
     * @param project Project.
     * @throws PuzzlesProcessingException If something went wrong during
     * processing the puzzles.
     */
    @Async
    public void review(final Project project)
        throws PuzzlesProcessingException {
        final Puzzles<Project> puzzles = new SshPuzzles(
            this.ssh,
            new DocumentPuzzles(project)
        );
        puzzles.process(project);

        final String owner = project.repoFullName().split("/")[0];
        final String name = project.repoFullName().split("/")[1];

        final Issues issues = project
            .projectManager()
            .provider()
            .repo(owner, name)
            .issues();
        this.openNewTickets(puzzles, issues);
        this.closeRemovedPuzzles(puzzles, issues);
    }

    /**
     * Open new issues for puzzles which don't already have a correspondent.
     * @param puzzles Puzzles found in the repo.
     * @param issues Issues API.
     */
    private void openNewTickets(
        final Puzzles<Project> puzzles,
        final Issues issues
    ) {
        for(final Puzzle puzzle : puzzles) {
            boolean foundIssue = false;
            for(final Issue issue : issues) {
                if(issue.json().getString("body").contains(puzzle.getId())) {
                    foundIssue = true;
                    break;
                }
            }
            if(!foundIssue) {
                issues.open(
                    puzzle.issueTitle(),
                    puzzle.issueBody(),
                    "puzzle"
                );
            }
        }
    }

    /**
     * Close issues which don't have a corresponding puzzle
     * (puzzle has been removed from code).
     * @param puzzles Puzzles found in the repo.
     * @param issues Issues API.
     */
    private void closeRemovedPuzzles(
        final Puzzles<Project> puzzles,
        final Issues issues
    ) {
        for(final Issue issue : issues) {
            boolean foundPuzzle = false;
            for(final Puzzle puzzle : puzzles) {
                if(issue.json().getString("body").contains(puzzle.getId())) {
                    foundPuzzle = true;
                    break;
                }
            }
            if(!foundPuzzle && !issue.isClosed()) {
                issue.close();
                issue.comments().post(
                    "Puzzle disappeared from the code, "
                    + "that's why I closed this ticket."
                );
            }
        }
    }
}

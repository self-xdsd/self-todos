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
import com.selfxdsd.api.*;
import com.selfxdsd.core.Env;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Component which connects to a server via SSH, reads
 * the puzzles and opens/closes issues based on them.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
@Component
@RequestScope
public class PuzzlesComponent {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(
        PuzzlesComponent.class
    );

    /**
     * SSH Connection.
     */
    private final Shell ssh = new Ssh(
        System.getenv(Env.PDD_HOST),
        Integer.valueOf(System.getenv(Env.PDD_PORT)),
        System.getenv(Env.PDD_USERNAME),
        Files.readString(
            Path.of(System.getenv(Env.PDD_PRIVATE_KEY))
        )
    );

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
     * @param event Event that triggered it.
     * processing the puzzles.
     */
    @Async
    public void review(final Event event) {
        final Project project = event.project();
        final Commit commit = event.commit();
        final Puzzles<Project> puzzles = new SshPuzzles(
            this.ssh,
            new DocumentPuzzles(project, commit)
        );
        try {
            puzzles.process(project);
            final String owner = project.repoFullName().split("/")[0];
            final String name = project.repoFullName().split("/")[1];

            final Issues issues = project
                .projectManager()
                .provider()
                .repo(owner, name)
                .issues()
                .search("", Puzzle.LABEL);
            this.openNewTickets(puzzles, issues, commit);
            this.closeRemovedPuzzles(puzzles, issues, commit);
        } catch (final PuzzlesProcessingException ex) {
            LOG.error(
                "Exception while reviewing puzzles for Project "
                + project.repoFullName() + " at " + project.provider() + ": ",
                ex
            );
        }
    }

    /**
     * Open new issues for puzzles which don't already have a correspondent.
     * @param puzzles Puzzles found in the repo.
     * @param issues Issues API.
     * @param commit Commit which triggered everything.
     */
    private void openNewTickets(
        final Puzzles<Project> puzzles,
        final Issues issues,
        final Commit commit
    ) {
        final List<String> opened = new ArrayList<>();
        for(final Puzzle puzzle : puzzles) {
            boolean foundIssue = false;
            for(final Issue issue : issues) {
                if(issue.body().contains(puzzle.getId())) {
                    foundIssue = true;
                    break;
                }
            }
            if(!foundIssue) {
                final Issue newIssue = issues.open(
                    puzzle.issueTitle(),
                    puzzle.issueBody(),
                    Puzzle.LABEL
                );
                opened.add("#" + newIssue.issueId());
            }
        }
        if(opened.size() > 0) {
            commit.comments().post(
                "@" + commit.author() + " I've opened the Issues "
                + opened + " for the newly added to-dos."
            );
        }
    }

    /**
     * Close issues which don't have a corresponding puzzle
     * (puzzle has been removed from code).
     * @param puzzles Puzzles found in the repo.
     * @param issues Issues API.
     * @param commit Commit which triggered everything.
     */
    private void closeRemovedPuzzles(
        final Puzzles<Project> puzzles,
        final Issues issues,
        final Commit commit
    ) {
        final List<String> closed = new ArrayList<>();
        for(final Issue issue : issues) {
            boolean foundPuzzle = false;
            for(final Puzzle puzzle : puzzles) {
                if(issue.body().contains(puzzle.getId())) {
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
                closed.add("#" + issue.issueId());
            }
        }
        if(closed.size() > 0) {
            commit.comments().post(
                "@" + commit.author() + " I've closed the Issues "
                + closed + " because their to-dos disappeared from the code."
            );
        }
    }
}

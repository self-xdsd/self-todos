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

import com.selfxdsd.api.Issue;
import com.selfxdsd.api.Issues;
import com.selfxdsd.api.Project;
import com.selfxdsd.api.Self;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Puzzles REST Controller.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
@RestController
public class PuzzlesApi {

    /**
     * Self's core.
     */
    private final Self selfCore;

    /**
     * Puzzles Component.
     */
    private final PuzzlesComponent puzzlesComponent;

    /**
     * Ctor.
     * @param selfCode Self Core, injected by Spring automatically.
     * @param puzzlesComponent Puzzles Component.
     */
    @Autowired
    public PuzzlesApi(
        final Self selfCode,
        final PuzzlesComponent puzzlesComponent
    ) {
        this.selfCore = selfCode;
        this.puzzlesComponent = puzzlesComponent;
    }

    /**
     * Trigger the reading/processing of the puzzles for the given Project.
     * @param provider Provider name (github, gitlab etc).
     * @param owner Owner login (user or organization name).
     * @param name Simple name of the repository.
     * @return Response OK.
     * @throws PuzzlesProcessingException Something went wrong during reading
     *  puzzles.
     */
    @GetMapping(value = "/pdd/{provider}/{owner}/{name}")
    public ResponseEntity<String> readPuzzles(
        @PathVariable final String provider,
        @PathVariable final String owner,
        @PathVariable final String name
    ) throws PuzzlesProcessingException {
        final ResponseEntity<String> resp;
        final Project project = this.selfCore.projects().getProjectById(
            owner + "/" + name, provider
        );
        if(project == null) {
            resp = ResponseEntity.badRequest().build();
        } else {
            resp = ResponseEntity.ok().build();
            final Issues issues = project
                .projectManager()
                .provider()
                .repo(owner, name)
                .issues();
            final Puzzles<Project> puzzles = this.puzzlesComponent
                .read(project);
            this.openNewTickets(puzzles, issues);
            this.closeRemovedPuzzles(puzzles, issues);
        }
        return resp;
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
                    "",
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

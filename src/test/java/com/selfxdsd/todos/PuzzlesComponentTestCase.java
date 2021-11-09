/**
 * Copyright (c) 2020-2021, Self XDSD Contributors
 * All rights reserved.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permission is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.selfxdsd.todos;

import com.jcabi.ssh.Shell;
import com.selfxdsd.api.Comments;
import com.selfxdsd.api.Commit;
import com.selfxdsd.api.Event;
import com.selfxdsd.api.Issue;
import com.selfxdsd.api.Issues;
import com.selfxdsd.api.Labels;
import com.selfxdsd.api.Project;
import com.selfxdsd.api.ProjectManager;
import com.selfxdsd.api.Provider;
import com.selfxdsd.api.Repo;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

/**
 * Unit tests for {@link PuzzlesComponent}.
 *
 * @author criske
 * @version $Id$
 * @since 0.0.9
 * @todo #75:60min Write remaining tests for PuzzleComponent#review(...).
 */
public final class PuzzlesComponentTestCase {


    /**
     * PuzzleComponent can close the issue if puzzle associated with
     * it is removed. It also posts comments about it and removes the "puzzle"
     * label.
     */
    @Test
    public void shouldCloseIssueWhenCorrespondingPuzzleIsRemoved(){
        final Event event = Mockito.mock(Event.class);
        final Project project = Mockito.mock(Project.class);
        final Comments commitComments = Mockito.mock(Comments.class);
        final Comments issueComments = Mockito.mock(Comments.class);
        final Labels labels = Mockito.mock(Labels.class);
        final Issue issue = this.mockPuzzleLabeledIssue(
            "",
            labels,
            issueComments
        );
        final Commit commit = this.mockCommit(
            "john",
            commitComments
        );
        this.mockPuzzleLabeledIssues(project, issue);

        final PuzzlesComponent.ShellProjectPuzzlesProvider puzzlesProvider =
            shell -> (proj, comm) -> {
                Puzzles<Project> puzzles = Mockito.mock(Puzzles.class);
                Mockito.when(puzzles.iterator())
                    .thenReturn(Collections.emptyIterator());
                return puzzles;
            };


        final PuzzlesComponent component = new PuzzlesComponent(
            Mockito.mock(Shell.class),
            puzzlesProvider
        );

        Mockito.when(event.project()).thenReturn(project);
        Mockito.when(event.commit()).thenReturn(commit);

        component.review(event);

        Mockito.verify(issue).close();
        Mockito.verify(issueComments).post("Puzzle disappeared from the code, "
            + "that's why I closed this ticket.");
        Mockito.verify(labels).remove("puzzle");
        Mockito.verify(commitComments).post("@john I've closed the Issues "
            + "[#1] since their to-dos disappeared from the code.\n\n"
            + "The to-dos may have been removed in an earlier commit, but "
            + "I've found it just now.");
    }


    /**
     * Mocks a puzzle labeled issue.
     * @param body Body.
     * @param labels Labels.
     * @param comments Comments.
     * @return Issue.
     */
    private Issue mockPuzzleLabeledIssue(
        final String body,
        final Labels labels,
        final Comments comments){
        final Issue issue = Mockito.mock(Issue.class);
        Mockito.when(issue.issueId()).thenReturn("1");
        Mockito.when(issue.body()).thenReturn(body);
        Mockito.when(issue.comments()).thenReturn(comments);
        Mockito.when(issue.labels()).thenReturn(labels);
        return issue;
    }

    /**
     * Mocks puzzle label issues associated with the Project.
     * @param project Project.
     * @param issue Array of issues.
     */
    private void mockPuzzleLabeledIssues(
        final Project project,
        final Issue... issue
    ){
        final ProjectManager manager = Mockito.mock(ProjectManager.class);
        final Provider provider = Mockito.mock(Provider.class);
        final Repo repo = Mockito.mock(Repo.class);
        final Issues issues = Mockito.mock(Issues.class);
        final Issues foundIssues = Mockito.mock(Issues.class);

        Mockito.when(project.repoFullName()).thenReturn("john/test");
        Mockito.when(project.projectManager()).thenReturn(manager);
        Mockito.when(manager.provider()).thenReturn(provider);
        Mockito.when(provider.repo(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(repo);
        Mockito.when(repo.issues()).thenReturn(issues);
        Mockito.when(issues.search("", Puzzle.PUZZLE_LABEL))
            .thenReturn(foundIssues);
        Mockito.when(foundIssues.iterator())
            .thenReturn(List.of(issue).iterator());
    }

    /**
     * Mocks a commit.
     * @param author Author.
     * @param comments Comments.
     * @return Commit.
     */
    private Commit mockCommit(final String author, final Comments comments) {
        final Commit commit = Mockito.mock(Commit.class);
        Mockito.when(commit.author()).thenReturn(author);
        Mockito.when(commit.comments()).thenReturn(comments);
        return commit;
    }
}
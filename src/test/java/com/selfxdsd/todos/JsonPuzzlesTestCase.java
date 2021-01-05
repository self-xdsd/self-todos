package com.selfxdsd.todos;

import com.selfxdsd.api.*;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link JsonPuzzles}.
 * @author criske
 * @version $Id$
 * @since 0.0.2
 */
public final class JsonPuzzlesTestCase {

    /**
     * JsonPuzzles can process Puzzles from xml string.
     * @throws PuzzlesProcessingException Something went wrong.
     * @checkstyle ExecutableStatementCount (100 lines).
     */
    @Test
    public void canParsePuzzlesFromJson() throws PuzzlesProcessingException {
        final Commits commits = Mockito.mock(Commits.class);
        Mockito.when(commits.latest()).thenReturn(Mockito.mock(Commit.class));
        final Repo repo = Mockito.mock(Repo.class);
        Mockito.when(repo.commits()).thenReturn(commits);
        final Project project = Mockito.mock(Project.class);
        Mockito.when(project.repo()).thenReturn(repo);

        final Puzzles<String> puzzles = new ResourcesPuzzles(
            new JsonPuzzles(
                project,
                Mockito.mock(Commit.class)
            )
        );
        puzzles.process("puzzles.json");

        MatcherAssert.assertThat(puzzles, Matchers.iterableWithSize(2));
        final Puzzle puzzle = puzzles.iterator().next();
        MatcherAssert.assertThat(puzzle.getId(),
            Matchers.equalTo("1194770182"));
        MatcherAssert.assertThat(puzzle.getTicket(),
            Matchers.equalTo(153));
        MatcherAssert.assertThat(puzzle.getEstimate(),
            Matchers.equalTo(30));
        MatcherAssert.assertThat(puzzle.getRole(),
            Matchers.equalTo("DEV"));
        MatcherAssert.assertThat(puzzle.getStart(),
            Matchers.equalTo(42));
        MatcherAssert.assertThat(puzzle.getEnd(),
            Matchers.equalTo(42));
        MatcherAssert.assertThat(puzzle.getBody(),
            Matchers.equalTo("Add integration tests for filters."));
        MatcherAssert.assertThat(puzzle.getFile(),
            Matchers.equalTo(".\\RtImagesITCase.java"));
        MatcherAssert.assertThat(puzzle.getAuthor(),
            Matchers.equalTo(""));
        MatcherAssert.assertThat(puzzle.getEmail(),
            Matchers.equalTo(""));
        MatcherAssert.assertThat(puzzle.getTime(),
            Matchers.equalTo(""));
        MatcherAssert.assertThat(puzzle.issueTitle(),
            Matchers.equalTo(
                "RtImagesITCase.java: Add integration tests for fil... "
            )
        );
        System.out.println(puzzle.issueBody());
    }

    /**
     * JsonPuzzles can fail when processing invalid json.
     *
     * @throws PuzzlesProcessingException if something went wrong.
     */
    @Test(expected = PuzzlesProcessingException.class)
    public void failToParseInvalidJson() throws PuzzlesProcessingException {
        Commit commit = Mockito.mock(Commit.class);
        Comments comments = Mockito.mock(Comments.class);
        Mockito.when(commit.comments()).thenReturn(comments);
        Comment comment = Mockito.mock(Comment.class);
        Mockito.when(comments.post(Mockito.anyString())).thenReturn(comment);
        new JsonPuzzles(
            Mockito.mock(Project.class),
            commit
        ).process("invalid json");
        Mockito.verify(comments, Mockito.times(1)).post(Mockito.anyString());
    }
}

package com.selfxdsd.todos;

import com.selfxdsd.api.*;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Iterator;

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
            Matchers.equalTo("amihaiemil"));
        MatcherAssert.assertThat(puzzle.getEmail(),
            Matchers.equalTo(""));
        MatcherAssert.assertThat(puzzle.getTime(),
            Matchers.equalTo("2020-11-08 20:19:56 +0100"));
        MatcherAssert.assertThat(puzzle.issueTitle(),
            Matchers.equalTo(
                "RtImagesITCase.java: Add integration tests for fil... "
            )
        );
    }

    /**
     * It can print the Issue's body if this is a Github puzzle.
     * @throws PuzzlesProcessingException Something went wrong.
     * @checkstyle ExecutableStatementCount (100 lines).
     * @checkstyle LineLength (100 lines).
     */
    @Test
    public void printsGithubIssueBody() throws PuzzlesProcessingException {
        final Commits commits = Mockito.mock(Commits.class);
        Mockito.when(commits.latest()).thenReturn(Mockito.mock(Commit.class));
        final Repo repo = Mockito.mock(Repo.class);
        Mockito.when(repo.commits()).thenReturn(commits);
        final Project project = Mockito.mock(Project.class);
        Mockito.when(project.provider()).thenReturn(Provider.Names.GITHUB);
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

        MatcherAssert.assertThat(
            puzzle.issueBody(),
            Matchers.equalTo(
                "The puzzle ``1194770182`` originating from #153 has to be resolved:\n\n"
                + "https://github.com/null/blob/null/.\\RtImagesITCase.java#L42-L42\n\n"
                + "\"Add integration tests for filters.\".\n\n\n"
                + "The puzzle was created by @amihaiemil at ``2020-11-08 20:19:56 +0100``. \n"
                + "Estimation is ``30 minutes``.\n"
                + "If you have any technical questions, don't ask me, I won't be able to help. Open new issues instead."
            )
        );
    }

    /**
     * It can print the Issue's body if this is a Gitlab puzzle.
     * @throws PuzzlesProcessingException Something went wrong.
     * @checkstyle ExecutableStatementCount (100 lines).
     * @checkstyle LineLength (100 lines).
     */
    @Test
    public void printsGitLabIssueBody() throws PuzzlesProcessingException {
        final Commits commits = Mockito.mock(Commits.class);
        Mockito.when(commits.latest()).thenReturn(Mockito.mock(Commit.class));
        final Repo repo = Mockito.mock(Repo.class);
        Mockito.when(repo.commits()).thenReturn(commits);
        final Project project = Mockito.mock(Project.class);
        Mockito.when(project.provider()).thenReturn(Provider.Names.GITLAB);
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

        MatcherAssert.assertThat(
            puzzle.issueBody(),
            Matchers.equalTo(
                "The puzzle ``1194770182`` originating from #153 has to be resolved:\n\n"
                    + "\"Add integration tests for filters.\"\n\n"
                    + "It is located at .\\RtImagesITCase.java#42-42. \n\n\n"
                    + "The puzzle was created by @amihaiemil at ``2020-11-08 20:19:56 +0100``. \n"
                    + "Estimation is ``30 minutes``.\n"
                    + "If you have any technical questions, don't ask me, I won't be able to help. Open new issues instead."
            )
        );
    }

    /**
     * It can print the Issue's body if this is a Github puzzle
     * without an author and timestamp..
     * @throws PuzzlesProcessingException Something went wrong.
     * @checkstyle ExecutableStatementCount (100 lines).
     * @checkstyle LineLength (100 lines).
     */
    @Test
    public void printsGithubIssueBodyWithoutAuthor() throws PuzzlesProcessingException {
        final Commits commits = Mockito.mock(Commits.class);
        Mockito.when(commits.latest()).thenReturn(Mockito.mock(Commit.class));
        final Repo repo = Mockito.mock(Repo.class);
        Mockito.when(repo.commits()).thenReturn(commits);
        final Project project = Mockito.mock(Project.class);
        Mockito.when(project.provider()).thenReturn(Provider.Names.GITHUB);
        Mockito.when(project.repo()).thenReturn(repo);

        final Puzzles<String> puzzles = new ResourcesPuzzles(
            new JsonPuzzles(
                project,
                Mockito.mock(Commit.class)
            )
        );
        puzzles.process("puzzles.json");

        MatcherAssert.assertThat(puzzles, Matchers.iterableWithSize(2));
        final Iterator<Puzzle> iterator = puzzles.iterator();
        iterator.next();
        final Puzzle puzzle = iterator.next();

        MatcherAssert.assertThat(
            puzzle.issueBody(),
            Matchers.equalTo(
                "The puzzle ``-1377131499`` originating from #187 has to be resolved:\n\n"
                    + "https://github.com/null/blob/null/.\\RtImagesITCase.java#L69-L72\n\n"
                    + "\"Add integration tests for filters.\".\n\n\n"
                    + "Estimation is ``30 minutes``.\n"
                    + "If you have any technical questions, don't ask me, I won't be able to help. Open new issues instead."
            )
        );
    }

    /**
     * It can print the Issue's body if this is a Gitlab puzzle without author/timestamp.
     * @throws PuzzlesProcessingException Something went wrong.
     * @checkstyle ExecutableStatementCount (100 lines).
     * @checkstyle LineLength (100 lines).
     */
    @Test
    public void printsGitLabIssueBodyWithoutAuthor() throws PuzzlesProcessingException {
        final Commits commits = Mockito.mock(Commits.class);
        Mockito.when(commits.latest()).thenReturn(Mockito.mock(Commit.class));
        final Repo repo = Mockito.mock(Repo.class);
        Mockito.when(repo.commits()).thenReturn(commits);
        final Project project = Mockito.mock(Project.class);
        Mockito.when(project.provider()).thenReturn(Provider.Names.GITLAB);
        Mockito.when(project.repo()).thenReturn(repo);

        final Puzzles<String> puzzles = new ResourcesPuzzles(
            new JsonPuzzles(
                project,
                Mockito.mock(Commit.class)
            )
        );
        puzzles.process("puzzles.json");

        MatcherAssert.assertThat(puzzles, Matchers.iterableWithSize(2));
        final Iterator<Puzzle> iterator = puzzles.iterator();
        iterator.next();
        final Puzzle puzzle = iterator.next();

        MatcherAssert.assertThat(
            puzzle.issueBody(),
            Matchers.equalTo(
            "The puzzle ``-1377131499`` originating from #187 has to be resolved:\n\n"
                + "\"Add integration tests for filters.\"\n\n"
                + "It is located at .\\RtImagesITCase.java#69-72. \n\n\n"
                + "Estimation is ``30 minutes``.\n"
                + "If you have any technical questions, don't ask me, I won't be able to help. Open new issues instead."
            )
        );
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

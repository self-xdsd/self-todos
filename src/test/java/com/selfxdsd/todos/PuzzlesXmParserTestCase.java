package com.selfxdsd.todos;

import com.selfxdsd.api.*;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import javax.json.Json;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * Unit tests for {@link PuzzlesXmlParser}.
 * @author criske
 * @version $Id$
 * @since 0.0.1
 */
public final class PuzzlesXmParserTestCase {

    /**
     * PuzzlesXmParser can parse Puzzles from xml string.
     * @throws IOException If something went wrong.
     * @throws ParserConfigurationException If something went wrong.
     * @throws SAXException  If something went wrong.
     * @checkstyle ExecutableStatementCount (100 lines).
     */
    @Test
    public void canParsePuzzlesFromXml() throws IOException,
        SAXException, ParserConfigurationException {
        final String xml = " <puzzles>\n  <puzzle>\n    "
            + "<ticket>516</ticket>\n  "
            + " <estimate>15</estimate>\n    <role>DEV</role>\n   "
            + " <id>3-fec5f6c3</id>\n    <lines>61-63</lines>\n   "
            + " <body>This has to be fixed later...</body>\n  "
            + " <file>src/test/java/org/takes/SomeTest.java</file>\n   "
            + " <author>Yegor Bugayenko</author>\n   "
            + " <email>yegor256@gmail.com</email>\n    "
            + "<time>2018-01-01T21:09:03Z</time>\n  </puzzle>\n</puzzles>";

        final Project project = Mockito.mock(Project.class);
        final Repo repo = Mockito.mock(Repo.class);
        final Issues issues = Mockito.mock(Issues.class);
        final Issue issue = Mockito.mock(Issue.class);

        Mockito.when(issue.json()).thenReturn(Json.createObjectBuilder()
            .add("body", "The puzzle 3-fec5f6c3 from #3 has to be resolved:")
            .build());
        Mockito.when(issues.spliterator())
            .thenReturn(List.of(issue).spliterator());
        Mockito.when(repo.issues()).thenReturn(issues);
        Mockito.when(project.repo()).thenReturn(repo);

        final PuzzlesXmlParser parser = new PuzzlesXmlParser();
        final Iterable<Puzzle> puzzles = parser.parseForProject(project, xml);

        MatcherAssert.assertThat(puzzles, Matchers.iterableWithSize(1));
        final Puzzle puzzle = puzzles.iterator().next();

        MatcherAssert.assertThat(puzzle.getId(),
            Matchers.equalTo("3-fec5f6c3"));
        MatcherAssert.assertThat(puzzle.getTicket(),
            Matchers.equalTo(516));
        MatcherAssert.assertThat(puzzle.getEstimate(),
            Matchers.equalTo(15));
        MatcherAssert.assertThat(puzzle.getRole(),
            Matchers.equalTo("DEV"));
        MatcherAssert.assertThat(puzzle.getLines(),
            Matchers.equalTo("61-63"));
        MatcherAssert.assertThat(puzzle.getBody(),
            Matchers.equalTo("This has to be fixed later..."));
        MatcherAssert.assertThat(puzzle.getFile(),
            Matchers.equalTo("src/test/java/org/takes/SomeTest.java"));
        MatcherAssert.assertThat(puzzle.getAuthor(),
            Matchers.equalTo("Yegor Bugayenko"));
        MatcherAssert.assertThat(puzzle.getEmail(),
            Matchers.equalTo("yegor256@gmail.com"));
        MatcherAssert.assertThat(puzzle.getTime(),
            Matchers.equalTo("2018-01-01T21:09:03Z"));
    }
}

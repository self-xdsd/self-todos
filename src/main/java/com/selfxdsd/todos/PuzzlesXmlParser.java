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

import com.selfxdsd.api.Project;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Project puzzles XML parser.
 * @author criske
 * @version $Id$
 * @since 0.0.1
 */
@Component
@RequestScope
public final class PuzzlesXmlParser {

    /**
     * Parses puzzles related to given Project in the XML String data.
     * @param project Project.
     * @param xml XML String.
     * @return Iterable of puzzles.
     * @throws ParserConfigurationException If something went wrong.
     * @throws IOException If something went wrong.
     * @throws SAXException  If something went wrong.
     */
    public Iterable<Puzzle> parseForProject(final Project project,
                                            final String xml) throws
        ParserConfigurationException, IOException, SAXException {

        //first comment of each Issue.
        final List<String> bodyIssues = StreamSupport
            .stream(project.repo().issues().spliterator(), false)
            .map(issue -> issue.json().getString("body"))
            .collect(Collectors.toList());

        final NodeList puzzleTags = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .parse(new InputSource(new StringReader(xml)))
            .getElementsByTagName("puzzle");

        final List<Puzzle> puzzles = new ArrayList<>();
        for (int i = 0; i < puzzleTags.getLength(); i++) {
            final Element puzzleTag = (Element) puzzleTags.item(i);
            final String idText = this.textContext(puzzleTag, "id");
            for (final String body : bodyIssues) {
                if (body.contains(idText)) {
                    puzzles.add(new Puzzle.Builder()
                        .setId(idText)
                        .setTicket(Integer
                            .parseInt(this.textContext(puzzleTag, "ticket")))
                        .setBody(this.textContext(puzzleTag, "body"))
                        .setEstimate(Integer
                            .parseInt(this.textContext(puzzleTag, "estimate")))
                        .setFile(this.textContext(puzzleTag, "file"))
                        .setLines(this.textContext(puzzleTag, "lines"))
                        .setRole(this.textContext(puzzleTag, "role"))
                        .setAuthor(this.textContext(puzzleTag, "author"))
                        .setEmail(this.textContext(puzzleTag, "email"))
                        .setTime(this.textContext(puzzleTag, "time"))
                        .build());
                    break;
                }
            }
        }
        return  puzzles;
    }

    /**
     * Text content from an xml tag.
     * @param parent Parent Node.
     * @param tagName Tag name.
     * @return Text content.
     */
    private String textContext(final Element parent, final String tagName){
        return parent
            .getElementsByTagName(tagName).item(0)
            .getTextContent();
    }


}

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

import com.selfxdsd.api.Commit;
import com.selfxdsd.api.Project;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Representation of pdd puzzles from processing a XML Document
 * of a String input.
 * @author criske
 * @version $Id$
 * @since 0.0.1
 */
public final class DocumentPuzzles implements Puzzles<String> {

    /**
     * Project where these puzzles are coming from.
     */
    private final Project project;

    /**
     * Commit which triggered everything.
     */
    private final Commit commit;

    /**
     * Processed puzzles.
     */
    private final List<Puzzle> puzzles;

    /**
     * Ctor.
     * @param project Project where these puzzles are coming from.
     * @param commit Commit which triggered everything.
     */
    public DocumentPuzzles(final Project project, final Commit commit) {
        this.project = project;
        this.commit = commit;
        this.puzzles = new ArrayList<>();
    }

    @Override
    public void process(final String input) throws PuzzlesProcessingException {
        try {
            final String schemaLanguage =
                "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
            final String schema = "http://www.w3.org/2001/XMLSchema";
            DocumentBuilderFactory factory = DocumentBuilderFactory
                .newInstance();
            factory.setNamespaceAware(true);
            factory.setAttribute(schemaLanguage, schema);
            final Document document = factory
                .newDocumentBuilder()
                .parse(new InputSource(new StringReader(input)));
            SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                .newSchema(new ClassPathResource("0.20.5.xsd").getFile())
                .newValidator()
                .validate(new DOMSource(document));
            final Element root = document
                .getDocumentElement();
            final NodeList puzzleTags = root.getElementsByTagName("puzzle");
            for (int i = 0; i < puzzleTags.getLength(); i++) {
                final Node node = puzzleTags.item(i);
                puzzles.add(new PuzzleElement((Element) node));
            }
        } catch (final SAXException
            | IOException
            | ParserConfigurationException exception) {
            this.commit.comments().post(
                "@" + this.commit.author() + " There's been a problem while "
                + "parsing the to-dos in the code. Most likely, the format is "
                + "not correct. Read more about the to-do format [here]"
                + "(https://docs.self-xdsd.com/todos.html#grammar). "
                + "If you can't understand the error, just open an Issue "
                + "[here](https://github.com/self-xdsd/self-todos).\n\n"
                + "Parse result:\n\n"
                + "```\n"
                + input
                + "```\n\n"
                + "Error:\n\n"
                + "```java\n"
                + exception.getMessage() + "\n\n"
                + exception.getStackTrace() + "\n"
                + "```"
            );
            throw new PuzzlesProcessingException(exception);
        }
    }

    @Override
    public Iterator<Puzzle> iterator() {
        return puzzles.iterator();
    }

    /**
     * Puzzle representation from a DOM Element.
     */
    private final class PuzzleElement implements Puzzle{

        /**
         * Delegate.
         */
        private final Puzzle delegate;

        /**
         * Ctor.
         * @param element DOM Element.
         */
        private PuzzleElement(final Element element){
            this.delegate = new Puzzle.Builder()
                .setProject(DocumentPuzzles.this.project)
                .setId(this.textContext(element, "id"))
                .setTicket(Integer
                    .parseInt(this.textContext(element, "ticket")))
                .setBody(this.textContext(element, "body"))
                .setEstimate(Integer
                    .parseInt(this.textContext(element, "estimate")))
                .setFile(this.textContext(element, "file"))
                .setLines(this.textContext(element, "lines"))
                .setRole(this.textContext(element, "role"))
                .setAuthor(this.textContext(element, "author"))
                .setEmail(this.textContext(element, "email"))
                .setTime(this.textContext(element, "time"))
                .build();
        }

        @Override
        public String getId() {
            return delegate.getId();
        }

        @Override
        public int getTicket() {
            return delegate.getTicket();
        }

        @Override
        public String getBody() {
            return delegate.getBody();
        }

        @Override
        public int getEstimate() {
            return delegate.getEstimate();
        }

        @Override
        public String getFile() {
            return delegate.getFile();
        }

        @Override
        public String getLines() {
            return delegate.getLines();
        }

        @Override
        public String getRole() {
            return delegate.getRole();
        }

        @Override
        public String getAuthor() {
            return delegate.getAuthor();
        }

        @Override
        public String getEmail() {
            return delegate.getEmail();
        }

        @Override
        public String getTime() {
            return delegate.getTime();
        }

        @Override
        public String issueTitle() {
            return delegate.issueTitle();
        }

        @Override
        public String issueBody() {
            return delegate.issueBody();
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


}

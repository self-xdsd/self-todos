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
     * Processed puzzles.
     */
    private final List<Puzzle> puzzles = new ArrayList<>();

    @Override
    public void process(final String input) throws PuzzlesProcessingException {
        try {
            final Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(new StringReader(input)));
            SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                .newSchema(new ClassPathResource("0.19.4.xsd").getFile())
                .newValidator()
                .validate(new DOMSource(document));
            final Element root = document
                .getDocumentElement();
            if(!root.getNodeName().equals("puzzles")){
                throw new PuzzlesProcessingException("Invalid document. Root "
                    + "element must be <puzzles>");
            }
            final NodeList puzzleTags = root.getElementsByTagName("puzzle");
            for (int i = 0; i < puzzleTags.getLength(); i++) {
                final Node node = puzzleTags.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    puzzles.add(new PuzzleElement((Element) node));
                } else {
                    throw new PuzzlesProcessingException("Invalid document. "
                        + "Current node must be an element");
                }
            }
        } catch (final SAXException
            | IOException
            | ParserConfigurationException exception) {
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
    private static final class PuzzleElement implements Puzzle{

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

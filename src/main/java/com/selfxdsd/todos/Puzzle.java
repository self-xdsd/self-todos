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
import com.selfxdsd.api.Provider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Representation of a pdd puzzle.
 * @author criske
 * @version $Id$
 * @since 0.0.1
 * @checkstyle HiddenField (500 lines).
 * @checkstyle ParameterNumber (500 lines).
 */
public interface Puzzle {

    /**
     * Unique ID of the puzzle.
     * @return String.
     */
    String getId();

    /**
     * Is a ticket name puzzle marker starts from, in most cases it will be
     * the number of Provider issue.
     * @return Integer.
     */
    int getTicket();

    /**
     * Body of the puzzle.
     * @return String.
     */
    String getBody();

    /**
     * The amount of minutes the puzzle is supposed to take.
     * @return Integer.
     */
    int getEstimate();

    /**
     * The file path where the puzzle is added.
     * @return String.
     */
    String getFile();

    /**
     * Lines is where the puzzle is found, inside the file.
     * @return String.
     */
    String getLines();

    /**
     * The role that is allowed to solve the puzzle.
     * @return String.
     */
    String getRole();

    /**
     * Author of the puzzle.
     * @return String.
     */
    String getAuthor();

    /**
     * Author's email.
     * @return String.
     */
    String getEmail();

    /**
     * Timestamp creation of the puzzle.
     * @return String.
     */
    String getTime();

    /**
     * Get the Issue title.
     * @return String.
     */
    String issueTitle();

    /**
     * Get the Issue body.
     * @return String.
     */
    String issueBody();

    /**
     * Puzzle builder.
     */
    class Builder {

        /**
         * Project where the puzzle comes from.
         */
        private Project project;

        /**
         * Unique ID of the puzzle.
         */
        private String id;

        /**
         * The ticket where this puzzle originated from.
         */
        private int ticket;

        /**
         * Body of the puzzle.
         */
        private String body;


        /**
         * The amount of minutes the puzzle is supposed to take.
         */
        private int estimate;


        /**
         * The file path where the puzzle is added.
         */
        private String file;


        /**
         * Lines is where the puzzle is found, inside the file.
         */
        private String lines;


        /**
         * The role that is allowed to solve the puzzle.
         */
        private String role;

        /**
         * Author of the puzzle.
         */
        private String author;

        /**
         * Author's email.
         */
        private String email;

        /**
         * Timestamp creation of the puzzle.
         */
        private String time;

        /**
         * Sets the Project.
         * @param project Project.
         * @return Builder.
         */
        public Builder setProject(final Project project) {
            this.project = project;
            return this;
        }

        /**
         * Sets the id.
         * @param id Id.
         * @return Builder.
         */
        public Builder setId(final String id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the ticket.
         * @param ticket Ticket.
         * @return Builder.
         */
        public Builder setTicket(final int ticket) {
            this.ticket = ticket;
            return this;
        }

        /**
         * Sets the body.
         * @param body Body.
         * @return Builder.
         */
        public Builder setBody(final String body) {
            this.body = body;
            return this;
        }

        /**
         * Sets the estimation.
         * @param estimate Estimation.
         * @return Builder.
         */
        public Builder setEstimate(final int estimate) {
            this.estimate = estimate;
            return this;
        }

        /**
         * Sets the file.
         * @param file File.
         * @return Builder.
         */
        public Builder setFile(final String file) {
            this.file = file;
            return this;
        }

        /**
         * Sets the lines.
         * @param lines Lines.
         * @return Builder.
         */
        public Builder setLines(final String lines) {
            this.lines = lines;
            return this;
        }

        /**
         * Sets the role.
         * @param role Role.
         * @return Builder.
         */
        public Builder setRole(final String role) {
            this.role = role;
            return this;
        }

        /**
         * Sets the author.
         * @param author Author.
         * @return Builder.
         */
        public Builder setAuthor(final String author) {
            this.author = author;
            return this;
        }

        /**
         * Sets the email.
         * @param email Email.
         * @return Builder.
         */
        public Builder setEmail(final String email) {
            this.email = email;
            return this;
        }

        /**
         * Sets the time.
         * @param time Time.
         * @return Builder.
         */
        public Builder setTime(final String time) {
            this.time = time;
            return this;
        }

        /**
         * Builds the {@link Puzzle}.
         * @return Puzzle.
         * @throws IllegalStateException if one of the fields are missing.
         * @checkstyle CyclomaticComplexity (100 lines).
         * @checkstyle NPathComplexity (100 lines).
         * @checkstyle JavaNCSS (100 lines).
         */
        public Puzzle build() {
            if (this.project == null) {
                throw new IllegalStateException("Project is missing");
            }
            if (this.id == null) {
                throw new IllegalStateException("Id is missing");
            }
            if (this.ticket == 0) {
                throw new IllegalStateException("Ticket is missing");
            }
            if (this.body == null) {
                throw new IllegalStateException("Body is missing");
            }
            if (this.estimate == 0) {
                throw new IllegalStateException("Estimate is missing");
            }
            if (this.file == null) {
                throw new IllegalStateException("File is missing");
            }
            if (this.lines == null) {
                throw new IllegalStateException("Lines is missing");
            }
            if (this.role == null) {
                throw new IllegalStateException("Role is missing");
            }
            if (this.author == null) {
                throw new IllegalStateException("Author is missing");
            }
            if (this.email == null) {
                throw new IllegalStateException("Email is missing");
            }
            if (this.time == null) {
                throw new IllegalStateException("Time is missing");
            }

            return new Puzzle() {
                @Override
                public String getId() {
                    return id;
                }

                @Override
                public int getTicket() {
                    return ticket;
                }

                @Override
                public String getBody() {
                    return body;
                }

                @Override
                public int getEstimate() {
                    return estimate;
                }

                @Override
                public String getFile() {
                    return file;
                }

                @Override
                public String getLines() {
                    final String[] numbers = lines.split("-");
                    return "L" + numbers[0] + "-L" + numbers[1];
                }

                @Override
                public String getRole() {
                    return role;
                }

                @Override
                public String getAuthor() {
                    return author;
                }

                @Override
                public String getEmail() {
                    return email;
                }

                @Override
                public String getTime() {
                    return time;
                }

                @Override
                public String issueTitle() {
                    final String[] path = this.getFile().split("/");
                    final String fileName = path[path.length - 1];
                    final String body = this.getBody();
                    final String bodySnippet;
                    if(body.length() < 30) {
                        bodySnippet = body;
                    } else {
                        bodySnippet = body.substring(0, 29) + "... ";
                    }

                    return fileName + ": " + bodySnippet;
                }

                @Override
                public String issueBody() {
                    String issueBody;
                    final String provider = project.provider();
                    final String body;
                    if(Provider.Names.GITHUB.equalsIgnoreCase(provider)) {
                        body = "\"\n" + this.getBody() + "\n\"\n\n"
                            + "It is is located at "
                            + "[" + this.getFile() + "#" + this.getLines()
                            + "](" + "https://github.com/"
                            + project.repoFullName()
                            + "/blob/master/" + this.getFile()
                            + "#" + this.getLines() + ").";
                    } else {
                        body = "\"" + this.getBody() + "\"\n\n"
                            + "It is located at " + this.getFile()
                            + "#" + this.getLines() + ". ";
                    }
                    try {
                        issueBody = String.format(
                            Files.readString(
                                Path.of("src/main/resources/issueBody.txt")
                            ),
                            this.getId(),
                            "#" + this.getTicket(),
                            body,
                            this.getAuthor(),
                            this.getTime(),
                            this.getEstimate()
                        );
                    } catch (final IOException ex) {
                        issueBody = this.getBody();
                    }
                    return issueBody;
                }

                @Override
                public String toString() {
                    return "Puzzle{"
                        + "id='" + id + '\''
                        + ", ticket=" + ticket
                        + ", body='" + body + '\''
                        + ", estimate=" + estimate
                        + ", file='" + file + '\''
                        + ", lines='" + lines + '\''
                        + ", role='" + role + '\''
                        + ", author='" + author + '\''
                        + ", email='" + email + '\''
                        + ", time='" + time + '\''
                        + '}';
                }

            };
        }
    }

}

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

/**
 * Representation of a pdd puzzle.
 * @author criske
 * @version $Id$
 * @since 0.0.1
 * @checkstyle HiddenField (500 lines).
 * @checkstyle ParameterNumber (500 lines).
 */
public final class Puzzle {

    /**
     * Unique ID of the puzzle.
     */
    private final String id;

    /**
     * Is a ticket name puzzle marker starts from, in most cases it will be
     * the number of Provider issue.
     */
    private final int ticket;

    /**
     * Body of the puzzle.
     */
    private final String body;


    /**
     * The amount of minutes the puzzle is supposed to take.
     */
    private final int estimate;


    /**
     * The file path where the puzzle is added.
     */
    private final String file;


    /**
     * Lines is where the puzzle is found, inside the file.
     */
    private final String lines;

    /**
     * The role that is allowed to solve the puzzle.
     */
    private final String role;

    /**
     * Author of the puzzle.
     */
    private final String author;

    /**
     * Author's email.
     */
    private final String email;

    /**
     * Timestamp creation of the puzzle.
     */
    private final String time;

    /**
     * Private constructor. Use the builder instead.
     * @param id Unique ID of the puzzle.
     * @param ticket Is a ticket name puzzle marker starts from,
     *               in most cases it will be the number of Provider issue.
     * @param body Body of the puzzle.
     * @param estimate The amount of minutes the puzzle is supposed to take.
     * @param file The file path where the puzzle is added.
     * @param lines Lines is where the puzzle is found, inside the file.
     * @param role The role that is allowed to solve the puzzle.
     * @param author Author of the puzzle.
     * @param email Author's email.
     * @param time Timestamp creation of the puzzle.
     */
    private Puzzle(final String id,
                   final int ticket,
                   final String body,
                   final int estimate,
                   final String file,
                   final String lines,
                   final String role,
                   final String author,
                   final String email,
                   final String time) {
        this.id = id;
        this.ticket = ticket;
        this.body = body;
        this.estimate = estimate;
        this.file = file;
        this.lines = lines;
        this.role = role;
        this.author = author;
        this.email = email;
        this.time = time;
    }


    /**
     * Unique ID of the puzzle.
     * @return String.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Is a ticket name puzzle marker starts from, in most cases it will be
     * the number of Provider issue.
     * @return Integer.
     */
    public int getTicket() {
        return this.ticket;
    }

    /**
     * Body of the puzzle.
     * @return String.
     */
    public String getBody() {
        return this.body;
    }

    /**
     * The amount of minutes the puzzle is supposed to take.
     * @return Integer.
     */
    public int getEstimate() {
        return this.estimate;
    }

    /**
     * The file path where the puzzle is added.
     * @return String.
     */
    public String getFile() {
        return this.file;
    }

    /**
     * Lines is where the puzzle is found, inside the file.
     * @return String.
     */
    public String getLines() {
        return this.lines;
    }

    /**
     * The role that is allowed to solve the puzzle.
     * @return String.
     */
    public String getRole() {
        return this.role;
    }

    /**
     * Author of the puzzle.
     * @return String.
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * Author's email.
     * @return String.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Timestamp creation of the puzzle.
     * @return String.
     */
    public String getTime() {
        return this.time;
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

    /**
     * Puzzle builder.
     */
    public static class Builder {
        
        /**
         * Unique ID of the puzzle.
         */
        private String id;

        /**
         * Is a ticket name puzzle marker starts from, in most cases it will be
         * the number of Provider issue.
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
         */
        public Puzzle build() {
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
            return new Puzzle(this.id, this.ticket, this.body,
                this.estimate, this.file, this.lines, this.role,
                this.author, this.email, this.time);
        }
    }

}

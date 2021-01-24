/**
 * Copyright (c) 2020-2021, Self XDSD Contributors
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

import com.selfxdsd.api.*;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;

/**
 * GitLab Push Hook Event.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class GitlabWebhookEvent implements Event {

    /**
     * Event in JSON.
     */
    private final JsonObject event;

    /**
     * Project where this event happened.
     */
    private final Project project;

    /**
     * Ctor.
     * @param project Project where the event happened.
     * @param payload Payload in JSON String.
     */
    public GitlabWebhookEvent(
        final Project project,
        final String payload
    ) {
        this.project = project;
        this.event = Json.createReader(
            new StringReader(payload)
        ).readObject();
    }


    @Override
    public String type() {
        return "Push Hook";
    }

    @Override
    public Issue issue() {
        throw new UnsupportedOperationException(
            "No Issue in the Push Hook event."
        );
    }

    @Override
    public Comment comment() {
        throw new UnsupportedOperationException(
            "No Comment in the Push Hook event."
        );
    }

    @Override
    public Commit commit() {
        final JsonObject latest = this.event.getJsonArray(
            "commits"
        ).getJsonObject(0);
        final String repoFullName = this.project.repoFullName();
        return this.project.projectManager().provider().repo(
            repoFullName.split("/")[0],
            repoFullName.split("/")[1]
        ).commits().getCommit(latest.getString("id"));
    }

    @Override
    public Project project() {
        return this.project;
    }
}

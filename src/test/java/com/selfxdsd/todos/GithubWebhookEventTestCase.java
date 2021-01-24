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
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import javax.json.Json;

/**
 * Unit tests for {@link GithubWebhookEvent}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class GithubWebhookEventTestCase {

    /**
     * Type of the event is push.
     */
    @Test
    public void typeIsPush() {
        MatcherAssert.assertThat(
            new GithubWebhookEvent(
                Mockito.mock(Project.class),
                "{}"
            ).type(),
            Matchers.equalTo("push")
        );
    }

    /**
     * It doesn't have an Issue.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void issueIsNotSupported() {
        new GithubWebhookEvent(
            Mockito.mock(Project.class),
            "{}"
        ).issue();
    }

    /**
     * It doesn't have a Comment.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void commentIsNotSupported() {
        new GithubWebhookEvent(
            Mockito.mock(Project.class),
            "{}"
        ).comment();
    }

    /**
     * It can return the Project.
     */
    @Test
    public void returnsProject() {
        final Project project = Mockito.mock(Project.class);
        MatcherAssert.assertThat(
            new GithubWebhookEvent(project, "{}").project(),
            Matchers.is(project)
        );
    }

    /**
     * It can return the Commit.
     */
    @Test
    public void returnsCommit() {
        final Commit commit = Mockito.mock(Commit.class);
        final Commits commits = Mockito.mock(Commits.class);
        Mockito.when(commits.getCommit("sha123")).thenReturn(commit);

        final Repo repo = Mockito.mock(Repo.class);
        Mockito.when(repo.commits()).thenReturn(commits);

        final Project project = Mockito.mock(Project.class);
        Mockito.when(project.repoFullName()).thenReturn("mihai/repo");
        final Provider github = Mockito.mock(Provider.class);
        Mockito.when(github.repo("mihai", "repo")).thenReturn(repo);

        final ProjectManager manager = Mockito.mock(ProjectManager.class);
        Mockito.when(manager.provider()).thenReturn(github);
        Mockito.when(project.projectManager()).thenReturn(manager);

        MatcherAssert.assertThat(
            new GithubWebhookEvent(
                project,
                Json.createObjectBuilder()
                    .add(
                        "commits",
                        Json.createArrayBuilder()
                            .add(
                                Json.createObjectBuilder()
                                    .add("id", "sha123")
                                    .build()
                            )
                    ).build().toString()
            ).commit(),
            Matchers.is(commit)
        );

    }

}

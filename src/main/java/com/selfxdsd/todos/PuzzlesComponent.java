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
import com.selfxdsd.core.projects.WebhookEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Component which connects to a server via SSH, reads
 * the puzzles and opens/closes issues based on them.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
@Component
public class PuzzlesComponent {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(
        PuzzlesComponent.class
    );

    /**
     * Spring container to manually obtain Self.
     */
    private final BeanFactory beanFactory;

    /**
     * Puzzles reviewer.
     */
    private final PuzzlesReviewer reviewer;

    /**
     * Ctor.
     * @param beanFactory Spring container.
     * @param reviewer Puzzles reviewer.
     */
    @Autowired
    public PuzzlesComponent(
        final BeanFactory beanFactory,
        final PuzzlesReviewer reviewer
    ) {
        this.beanFactory = beanFactory;
        this.reviewer = reviewer;
    }

    /**
     * Listener for {@link PuzzlesWebhookEvent} Spring event triggered
     * on webhook request.
     * @param event Puzzles Webhook Event.
     **@throws Exception If something goes wrong on closing database connection.
     */
    @Async
    @EventListener
    public void onPuzzleWebhookEvent(final PuzzlesWebhookEvent event)
        throws Exception {
        final Self selfCore = this.beanFactory.getBean(Self.class);
        final String provider = event.getProvider();
        final String repoFullName = event.getProjectFullName();
        final Project project = selfCore.projects().getProjectById(
            repoFullName, provider
        );
        if (project != null) {
            if (provider.equalsIgnoreCase(Provider.Names.GITHUB)) {
                this.reviewer.review(WebhookEvents.create(project,
                    "push",
                    event.getPayload()
                ));
            } else if (provider.equalsIgnoreCase(Provider.Names.GITLAB)) {
                this.reviewer.review(WebhookEvents.create(
                    project,
                    "Push Hook",
                    event.getPayload()
                ));
            } else {
                LOG.error("Unsupported provider {}. "
                        + "Reviewing puzzles is canceled.",
                    provider
                );
            }
        } else {
            LOG.error("Project {} for provider {} not found."
                + " Reviewing puzzles is canceled.", repoFullName, provider);
        }
        selfCore.close();
    }

}

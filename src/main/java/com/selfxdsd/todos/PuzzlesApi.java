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

import com.selfxdsd.api.Project;
import com.selfxdsd.api.Self;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Puzzles REST Controller.
 *
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
@RestController
public class PuzzlesApi {

    /**
     * Spring container to manually obtain Self.
     */
    private final BeanFactory beanFactory;


    /**
     * Application event publisher.
     */
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Ctor.
     * @param beanFactory Spring container to obtain Self.
     * @param eventPublisher Event publisher.
     */
    @Autowired
    public PuzzlesApi(
        final BeanFactory beanFactory,
        final ApplicationEventPublisher eventPublisher
    ) {
        this.beanFactory = beanFactory;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Trigger the reading/processing of the puzzles for the given Project.
     * This endpoint should be called internally by self-pm, which should
     * forward the "push" event to it.<br><br>
     *
     * @param provider Provider name (github, gitlab etc).
     * @param owner Owner login (user or organization name).
     * @param name Simple name of the repository.
     * @param payload Payload of the PUSH event that triggered everything.
     * @return Response OK.
     * @throws Exception If something goes wrong on closing database connection.
     * @checkstyle ParameterNumber (40 lines)
     */
    @PostMapping(
        value = "/pdd/{provider}/{owner}/{name}",
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> reviewPuzzles(
        @PathVariable final String provider,
        @PathVariable final String owner,
        @PathVariable final String name,
        @RequestBody final String payload
    ) throws Exception {
        final Self selfCore = this.beanFactory.getBean(Self.class);
        final ResponseEntity<String> resp;
        final String fullName = owner + "/" + name;
        final Project project = selfCore.projects().getProjectById(
            fullName, provider
        );
        if (project == null) {
            resp = ResponseEntity.badRequest().build();
        } else {
            this.eventPublisher.publishEvent(
                new PuzzlesWebhookEvent(provider, fullName, payload)
            );
            resp = ResponseEntity.ok().build();
        }
        selfCore.close();
        return resp;
    }
}

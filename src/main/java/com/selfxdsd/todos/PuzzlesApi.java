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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Puzzles REST Controller.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
@RestController
public class PuzzlesApi {

    /**
     * Self's core.
     */
    private final Self selfCore;

    /**
     * Puzzles Component.
     */
    private final PuzzlesComponent puzzlesComponent;

    /**
     * Ctor.
     * @param selfCode Self Core, injected by Spring automatically.
     * @param puzzlesComponent Puzzles Component.
     */
    @Autowired
    public PuzzlesApi(
        final Self selfCode,
        final PuzzlesComponent puzzlesComponent
    ) {
        this.selfCore = selfCode;
        this.puzzlesComponent = puzzlesComponent;
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
     * @checkstyle ParameterNumber (40 lines)
     * @throws PuzzlesProcessingException Something went wrong during reading
     *  puzzles.
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
    ) {
        final ResponseEntity<String> resp;
        final Project project = this.selfCore.projects().getProjectById(
            owner + "/" + name, provider
        );
        if (project == null) {
            resp = ResponseEntity.badRequest().build();
        } else {
            if(provider.equalsIgnoreCase(Provider.Names.GITHUB)) {
                this.puzzlesComponent.review(
                    WebhookEvents.create(project, "push", payload)
                );
            } else if(provider.equalsIgnoreCase(Provider.Names.GITLAB)) {
                this.puzzlesComponent.review(
                    WebhookEvents.create(project, "Push Hook", payload)
                );
            }
            resp = ResponseEntity.ok().build();
        }
        return resp;
    }
}

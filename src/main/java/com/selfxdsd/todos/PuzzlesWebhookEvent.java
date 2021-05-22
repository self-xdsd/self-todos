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

import org.springframework.context.ApplicationEvent;

/**
 * Representation of a puzzle webhook Spring event.
 * @author criske
 * @version $Id$
 * @since 0.0.3
 */
public class PuzzlesWebhookEvent extends ApplicationEvent {

    /**
     * Provider.
     */
    private final String provider;

    /**
     * Project's full name.
     */
    private final String projectFullName;

    /**
     * Create a new {@code ApplicationEvent}.
     * @param provider One of {@link com.selfxdsd.api.Provider.Names}.
     * @param projectFullName Project's full name.
     * @param payload Webhook raw payload.
     */
    public PuzzlesWebhookEvent(
        final String provider,
        final String projectFullName,
        final String payload
    ) {
        super(payload);
        this.provider = provider;
        this.projectFullName = projectFullName;
    }

    /**
     * Payload.
     * @return String.
     */
    public String getPayload() {
        return (String) this.getSource();
    }

    /**
     * One of {@link com.selfxdsd.api.Provider.Names}.
     * @return Provider name.
     */
    public String getProvider(){
        return this.provider;
    }

    /**
     * Project's full name.
     * It assumes project exists.
     * @return String.
     */
    public String getProjectFullName(){
        return this.projectFullName;
    }
}
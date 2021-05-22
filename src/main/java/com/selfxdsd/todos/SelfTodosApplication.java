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

import com.selfxdsd.api.storage.Storage;
import com.selfxdsd.core.Env;
import com.selfxdsd.storage.MySql;
import com.selfxdsd.storage.SelfJooq;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

/**
 * Entry point for SpringBoot.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @checkstyle HideUtilityClassConstructor (100 lines)
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class SelfTodosApplication {

    /**
     * Main method entry point.
     * @param args Command-line arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(SelfTodosApplication.class, args);
    }

    /**
     * Storage bean.
     * @return Storage.
     */
    @Bean
    @Scope(value = "prototype")
    public Storage storage() {
        return new SelfJooq(new MySql(
            System.getenv(Env.DB_URL),
            System.getenv(Env.DB_USER),
            System.getenv(Env.DB_PASSWORD)
        ));
    }

    /**
     * PuzzlesReviewer bean.
     * @return PuzzleReviewer.
     * @throws IOException If something goes wrong.
     */
    @Bean
    public PuzzlesReviewer puzzlesReviewer() throws IOException {
        return new PuzzlesReviewer.DefaultPuzzleReviewer();
    }
}

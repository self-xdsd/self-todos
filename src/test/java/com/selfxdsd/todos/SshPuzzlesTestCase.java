package com.selfxdsd.todos;

import com.jcabi.ssh.Shell;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

/**
 * Unit tests for {@link SshPuzzles}.
 * @author Ali Fellai (fellahi.ali@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
public final class SshPuzzlesTestCase {

    /**
     * SshPuzzles.exec(cmd) fail if the ssh command exits with non-zero code.
     * @throws IOException if something went wrong.
     */
    @Test(expected = IllegalStateException.class)
    public void execCanFail() throws IOException {
        new SshPuzzles(
            new Shell.Fake(-1, "output", "error"),
            Mockito.mock(Puzzles.class)
        ).exec("bad cmd");
    }

    /**
     * SshPuzzles.exec(cmd) can return the output of executed command.
     * @throws IOException if something went wrong.
     */
    @Test
    public void execReturnsCmdOutput() throws IOException {
        MatcherAssert.assertThat(
            new SshPuzzles(
                new Shell.Fake(0, "output", "error"),
                Mockito.mock(Puzzles.class)
            ).exec("cmd"),
            Matchers.equalTo("output")
        );

    }
}
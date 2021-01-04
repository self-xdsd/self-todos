package com.selfxdsd.todos;

import com.jcabi.ssh.Shell;
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
}
package com.selfxdsd.todos;

import com.selfxdsd.api.Event;
import com.selfxdsd.api.Project;
import com.selfxdsd.api.Projects;
import com.selfxdsd.api.storage.Storage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Integration tests for {@link PuzzlesApi}.
 *
 * @author criske
 * @version $Id$
 * @since 0.0.3
 */
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application-test.properties")
@Import({SelfCoreComponent.class, PuzzlesComponent.class})
@WebMvcTest({PuzzlesApi.class})
public final class PuzzlesApiTestCase {

    /**
     * Mock mvc.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mock storage.
     */
    @MockBean
    private Storage storage;

    /**
     * Mock puzzle reviewer.
     */
    @MockBean
    private PuzzlesReviewer puzzlesReviewer;

    /**
     * PuzzlesApi should dispatch an application event containing
     * the webhook payload to be processed by PuzzleReviewer.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void shouldSendEventOnWebHookAndReviewPuzzles() throws Exception {

        final Projects projects = Mockito.mock(Projects.class);
        final Project project = Mockito.mock(Project.class);

        Mockito.when(this.storage.projects()).thenReturn(projects);
        Mockito.when(projects.getProjectById("john/test", "gitlab"))
            .thenReturn(project);
        Mockito.when(project.repoFullName()).thenReturn("john/test");
        Mockito.when(project.provider()).thenReturn("gitlab");

        this.mockMvc
            .perform(MockMvcRequestBuilders
                .post("/pdd/gitlab/john/test")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk());

        this.mockMvc
            .perform(MockMvcRequestBuilders
                .post("/pdd/gitlab/john/test")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(this.puzzlesReviewer, Mockito.times(2))
            .review(Mockito.any(Event.class));
    }

}


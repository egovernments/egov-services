package org.egov.web.controller;

import org.egov.domain.service.CrnGeneratorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CrnController.class)
public class CrnControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrnGeneratorService crnGeneratorService;

    @Test
    public void testGetCrn() throws Exception {
        when(crnGeneratorService.generate("default")).thenReturn("crn_value");

        mockMvc.perform(
                get("/crn").param("tenantId","default")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"value\": \"crn_value\"}"));
    }

    @Test
    public void testCreateCrn() throws Exception {
        when(crnGeneratorService.generate("default")).thenReturn("crn_value");

        mockMvc.perform(
            post("/crn/v1/_create")
            .param("tenantId","default")
        )
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json("{\"value\": \"crn_value\"}"));
    }
}
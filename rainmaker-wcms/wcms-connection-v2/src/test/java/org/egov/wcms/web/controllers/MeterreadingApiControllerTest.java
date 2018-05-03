package org.egov.wcms.web.controllers;

import org.egov.wcms.web.models.ErrorRes;
import org.egov.wcms.web.models.MeterReadingReq;
import org.egov.wcms.web.models.MeterReadingRes;
import org.egov.wcms.web.models.RequestInfo;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.egov.wcms.TestConfiguration;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
* API tests for MeterreadingApiController
*/
@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(MeterreadingApiController.class)
@Import(TestConfiguration.class)
public class MeterreadingApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void meterreadingCreatePostSuccess() throws Exception {
        mockMvc.perform(post("/connection/v2/meterreading/_create").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void meterreadingCreatePostFailure() throws Exception {
        mockMvc.perform(post("/connection/v2/meterreading/_create").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void meterreadingSearchPostSuccess() throws Exception {
        mockMvc.perform(post("/connection/v2/meterreading/_search").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void meterreadingSearchPostFailure() throws Exception {
        mockMvc.perform(post("/connection/v2/meterreading/_search").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void meterreadingUpdatePostSuccess() throws Exception {
        mockMvc.perform(post("/connection/v2/meterreading/_update").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void meterreadingUpdatePostFailure() throws Exception {
        mockMvc.perform(post("/connection/v2/meterreading/_update").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

}

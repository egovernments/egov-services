package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.Resources;
import org.egov.pgrrest.TestConfiguration;
import org.egov.pgrrest.read.domain.model.*;
import org.egov.pgrrest.read.domain.service.DraftService;
import org.egov.pgrrest.read.web.contract.DraftDeleteRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DraftController.class)
@Import(TestConfiguration.class)
public class DraftControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Resources resources = new Resources();

    @MockBean
    private DraftService draftService;

    @Test
    public void test_to_create_draft() throws Exception {

        when(draftService.save(getCreateDraftRequest())).thenReturn(1L);

        mockMvc.perform(post("/draft/v1/_create")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("createDraftRequest.json")))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("createDraftResponse.json")));
    }

    @Test
    public void test_to_update_draft() throws Exception {

        mockMvc.perform(post("/draft/v1/_update")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("updateDraft.json")))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("draftResponse.json")));

        verify(draftService).update(getUpdateDraftRequest());
    }

    @Test
    public void test_to_delete_draft() throws Exception {
        List<Long> ids = Collections.singletonList(1L);
        mockMvc.perform(post("/draft/v1/_delete")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("draftDeleteRequest.json")))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("draftResponse.json")));

       verify(draftService).delete(ids);
    }

    @Test
    public void test_to_search_draft() throws Exception {

        final DraftSearchCriteria searchCriteria = new DraftSearchCriteria(73L, "NOC", "default");
        when(draftService.getDrafts(searchCriteria)).thenReturn(getDraftSearchResponse());

        mockMvc.perform(post("/draft/v1/_search?tenantId=default&serviceCode=NOC")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(resources.getFileContents("draftSearchRequest.json")))
            .andExpect(status().isOk())
            .andExpect(content().json(resources.getFileContents("draftSearchResponse.json")));
    }

    private NewDraft getCreateDraftRequest() {
        return NewDraft.builder().userId(73L).tenantId("tenantId").draft(getDraft()).serviceCode("NOC").build();
    }

    private UpdateDraft getUpdateDraftRequest() {
        return UpdateDraft.builder().id(1L).draft(getDraft()).build();
    }

    private org.egov.pgrrest.read.domain.model.DraftCreateResponse getCreateDraftResponse() {
        return org.egov.pgrrest.read.domain.model.DraftCreateResponse.builder().id(1L).build();
    }

    private DraftResult getDraftSearchResponse() {
        return DraftResult.builder().drafts(getDraftSearch()).build();
    }

    private List<Draft> getDraftSearch() {
        Draft build = Draft.builder().id(9L).draft(getDraft()).build();
        List<Draft> draftList = new ArrayList<Draft>();
        draftList.add(build);
        return draftList;
    }

    private HashMap<String, Object> getDraft() {
        HashMap<String, Object> draft = new HashMap<String, Object>();
        draft.put("serviceCode", "BRKNB");
        return draft;
    }

}
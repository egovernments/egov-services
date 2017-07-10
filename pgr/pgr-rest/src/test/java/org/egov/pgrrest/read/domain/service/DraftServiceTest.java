package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.read.domain.model.DraftCreateRequest;
import org.egov.pgrrest.read.domain.model.DraftCreateResponse;
import org.egov.pgrrest.read.domain.model.DraftSearchResponse;
import org.egov.pgrrest.read.domain.model.DraftUpdateRequest;
import org.egov.pgrrest.read.persistence.repository.DraftRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DraftServiceTest {

    @Mock
    private DraftRepository draftRepository;

    @InjectMocks
    private DraftService draftService;

    @Test
    public void testShouldReturnDraft() {
        final DraftSearchResponse expectedDraft = getDraftSearchResponse();

        when(draftRepository.getDrafts(1L, "NOC", "default")).thenReturn(getDraftSearchResponse());

        final org.egov.pgrrest.read.domain.model.DraftSearchResponse actualDraft = draftService.getDrafts(1L, "NOC", "default");
        assertEquals(actualDraft, expectedDraft);
        assertEquals(1L, actualDraft.getDraftResponses().get(0).getId().longValue());
    }

    @Test
    public void testShouldSaveDraft() {
        final DraftCreateResponse expectedResponse = getSaveDraftResponse();

        when(draftRepository.saveDraft(saveDraftRequest())).thenReturn(getSaveDraftResponse());

        final DraftCreateResponse actualDraft = draftService.save(saveDraftRequest());
        assertEquals(expectedResponse, actualDraft);
    }

    @Test
    public void testShouldUpdateDraft() {
        draftService.update(updateDraftRequest());
        verify(draftRepository).updateDraft(updateDraftRequest());
    }

    @Test
    public void testShouldDeleteDraft() {
        List<Long> draftIdList = Arrays.asList(1L);
        draftService.delete(draftIdList);
        verify(draftRepository).deleteDraft(draftIdList);
    }

    private DraftCreateRequest saveDraftRequest() {
        return DraftCreateRequest.builder().serviceCode("NOC").tenantId("default").draft(getDraftData()).build();
    }

    private DraftUpdateRequest updateDraftRequest() {
        return DraftUpdateRequest.builder().id(1L).draft(getDraftData()).build();
    }

    private DraftCreateResponse getSaveDraftResponse() {
        return DraftCreateResponse.builder().id(1L).build();
    }

    private org.egov.pgrrest.read.domain.model.DraftSearchResponse getDraftSearchResponse() {
        List<org.egov.pgrrest.read.domain.model.Draft> drafts = new ArrayList<org.egov.pgrrest.read.domain.model.Draft>();

        org.egov.pgrrest.read.domain.model.Draft draftModel = org.egov.pgrrest.read.domain.model.Draft.builder().id(1L).draft(getDraftData()).build();
        drafts.add(draftModel);

        return org.egov.pgrrest.read.domain.model.DraftSearchResponse.builder().draftResponses(drafts).build();
    }

    private HashMap<String, Object> getDraftData() {
        HashMap<String, Object> draftData = new HashMap<String, Object>();
        draftData.put("serviceCode", "NOC");
        return draftData;
    }
}
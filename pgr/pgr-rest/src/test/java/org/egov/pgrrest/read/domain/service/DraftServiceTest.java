package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.read.domain.model.*;
import org.egov.pgrrest.read.persistence.repository.DraftRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

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
        final DraftResult expectedDraft = getDraftSearchResponse();

        final DraftSearchCriteria searchCriteria = new DraftSearchCriteria(1L, "NOC", "default");
        when(draftRepository.getDrafts(searchCriteria))
            .thenReturn(getDraftSearchResponse());

        final DraftResult actualDraft = draftService.getDrafts(searchCriteria);
        assertEquals(actualDraft, expectedDraft);
        assertEquals(1L, actualDraft.getDrafts().get(0).getId().longValue());
    }

    @Test
    public void testShouldSaveDraft() {
        when(draftRepository.saveDraft(saveDraftRequest())).thenReturn(1L);

        final long id = draftService.save(saveDraftRequest());

        assertEquals(1L, id);
    }

    @Test
    public void testShouldUpdateDraft() {
        draftService.update(updateDraftRequest());
        verify(draftRepository).updateDraft(updateDraftRequest());
    }

    @Test
    public void testShouldDeleteDraft() {
        List<Long> draftIdList = Collections.singletonList(1L);
        draftService.delete(draftIdList);
        verify(draftRepository).delete(draftIdList);
    }

    private NewDraft saveDraftRequest() {
        return NewDraft.builder().serviceCode("NOC").tenantId("default").draft(getDraftData()).build();
    }

    private UpdateDraft updateDraftRequest() {
        return UpdateDraft.builder().id(1L).draft(getDraftData()).build();
    }

    private DraftResult getDraftSearchResponse() {
        List<org.egov.pgrrest.read.domain.model.Draft> drafts = new ArrayList<org.egov.pgrrest.read.domain.model.Draft>();

        org.egov.pgrrest.read.domain.model.Draft draftModel = org.egov.pgrrest.read.domain.model.Draft.builder()
            .id(1L)
            .draft(getDraftData())
            .build();
        drafts.add(draftModel);

        return DraftResult.builder().drafts(drafts).build();
    }

    private HashMap<String, Object> getDraftData() {
        HashMap<String, Object> draftData = new HashMap<String, Object>();
        draftData.put("serviceCode", "NOC");
        return draftData;
    }
}
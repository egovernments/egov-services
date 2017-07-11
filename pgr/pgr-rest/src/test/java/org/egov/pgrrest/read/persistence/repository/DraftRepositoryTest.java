package org.egov.pgrrest.read.persistence.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pgrrest.read.domain.exception.MalformedDraftException;
import org.egov.pgrrest.read.domain.model.DraftSearchCriteria;
import org.egov.pgrrest.read.domain.model.NewDraft;
import org.egov.pgrrest.read.domain.model.UpdateDraft;
import org.egov.pgrrest.read.persistence.entity.Draft;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DraftRepositoryTest {

    @Mock
    private DraftRepository draftRepository;

    @Mock
    private DraftJpaRepository draftJpaRepository;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        draftRepository = new DraftRepository(draftJpaRepository, objectMapper);
    }

    @Test
    public void test_draft_repository_should_save_draft() {
        final NewDraft newDraft = getDraftCreateRequest();
        doAnswer(invocationOnMock -> {
			final Draft entityDraft = (Draft) invocationOnMock.getArguments()[0];
			entityDraft.setId(4L);
			return entityDraft;
		}).when(draftJpaRepository).save(any(Draft.class));

        final long id = draftRepository.saveDraft(newDraft);

        verify(draftJpaRepository).save(getSaveDraftEntity());
        assertEquals(4L, id);
    }

    @Test
    public void test_draft_repository_should_update_draft() {
        draftJpaRepository.save(getUpdateOrDeleteDraftEntity());
        draftJpaRepository.findOne(1L);
        draftRepository.updateDraft(getDraftUpdateRequest());

        verify(draftJpaRepository).save(getUpdateOrDeleteDraftEntity());
    }

    @Test
    public void test_draft_repository_should_delete_draft() {
        List<Long> draftIdList = Collections.singletonList(1L);

        draftRepository.delete(draftIdList);

        verify(draftJpaRepository).deleteByIdList(draftIdList);
    }

    @Test
    public void test_draft_repository_should_search_draft_with_service_code() {
        draftRepository.getDrafts(new DraftSearchCriteria(73L, "NOC", "default"));
        verify(draftJpaRepository).findByUserIdAndServiceCodeAndTenantId(73L, "NOC", "default");
    }

    @Test
    public void test_draft_repository_should_search_draft_without_service_code() {
        draftRepository.getDrafts(new DraftSearchCriteria(73L, null, "default"));
        verify(draftJpaRepository).findByUserIdAndTenantId(73L, "default");
    }

    public NewDraft getDraftCreateRequest() {
        return NewDraft.builder()
            .tenantId("default")
            .userId(73L)
            .serviceCode("NOC")
            .draft(getDraft())
            .build();
    }

    public UpdateDraft getDraftUpdateRequest() {
        return UpdateDraft.builder().id(1L).draft(getDraft()).build();
    }

    private HashMap<String, Object> getDraft() {
        HashMap<String, Object> draft = new HashMap<String, Object>();
        draft.put("serviceCode", "BRKNB");
        return draft;
    }

    private Draft getSaveDraftEntity() {
        String draftMapper;
        try {
            draftMapper = objectMapper.writeValueAsString(getDraft());
        } catch (JsonProcessingException e) {
            throw new MalformedDraftException(e);
        }
        return Draft.builder()
            .userId(73L)
            .serviceCode("NOC")
            .tenantId("default")
            .draft(draftMapper)
            .id(4L)
            .build();
    }

    private Draft getUpdateOrDeleteDraftEntity() {
        String draftMapper;
        try {
            draftMapper = objectMapper.writeValueAsString(getDraft());
        } catch (JsonProcessingException e) {
            throw new MalformedDraftException(e);
        }
        return Draft.builder().id(1L).userId(73L).serviceCode("NOC").tenantId("default").draft(draftMapper).build();
    }

}
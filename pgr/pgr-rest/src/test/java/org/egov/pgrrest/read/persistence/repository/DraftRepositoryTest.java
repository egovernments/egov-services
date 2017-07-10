package org.egov.pgrrest.read.persistence.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pgrrest.read.domain.exception.MalformedDraftException;
import org.egov.pgrrest.read.domain.model.DraftCreateRequest;
import org.egov.pgrrest.read.domain.model.DraftUpdateRequest;
import org.egov.pgrrest.read.persistence.entity.Draft;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DraftRepositoryTest {

    @Mock
    private DraftRepository draftRepository;

    @Mock
    private DraftJpaRepository draftJpaRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        draftRepository =
            new DraftRepository(draftJpaRepository, objectMapper);
    }

    @Test
    public void test_draft_repository_should_save_draft() {

        draftRepository.saveDraft(getDraftCreateRequest());

        verify(draftJpaRepository).save(getSaveDraftEntity());
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
        List<Long> draftIdList = Arrays.asList(1L);

        draftRepository.deleteDraft(draftIdList);

        verify(draftJpaRepository).deleteByIdList(draftIdList);
    }

    @Test
    public void test_draft_repository_should_search_draft_with_service_code() {
        draftRepository.getDrafts(73L, "NOC", "default");
        verify(draftJpaRepository).findByUserIdAndServiceCodeAndTenantId(73L, "NOC", "default");
    }

    @Test
    public void test_draft_repository_should_search_draft_without_service_code() {
        draftRepository.getDrafts(73L, null, "default");
        verify(draftJpaRepository).findByUserIdAndTenantId(73L, "default");
    }

    public DraftCreateRequest getDraftCreateRequest() {
        return DraftCreateRequest.builder().tenantId("default").userId(73L).serviceCode("NOC").draft(getDraft()).build();
    }

    public DraftUpdateRequest getDraftUpdateRequest() {
        return DraftUpdateRequest.builder().id(1L).draft(getDraft()).build();
    }

    private HashMap<String, Object> getDraft() {
        HashMap<String, Object> draft = new HashMap<String, Object>();
        draft.put("serviceCode", "BRKNB");
        return draft;
    }

    private Draft getSaveDraftEntity() {
        String draftMapper = null;
        try {
            draftMapper = objectMapper.writeValueAsString(getDraft());
        } catch (JsonProcessingException e) {
            throw new MalformedDraftException(e);
        }
        return Draft.builder().userId(73L).serviceCode("NOC").tenantId("default").draft(draftMapper).build();
    }

    private Draft getUpdateOrDeleteDraftEntity() {
        String draftMapper = null;
        try {
            draftMapper = objectMapper.writeValueAsString(getDraft());
        } catch (JsonProcessingException e) {
            throw new MalformedDraftException(e);
        }
        return Draft.builder().id(1L).userId(73L).serviceCode("NOC").tenantId("default").draft(draftMapper).build();
    }

}
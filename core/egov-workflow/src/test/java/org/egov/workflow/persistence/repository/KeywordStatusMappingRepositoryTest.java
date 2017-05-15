package org.egov.workflow.persistence.repository;

import org.egov.workflow.persistence.entity.ComplaintStatus;
import org.egov.workflow.persistence.entity.KeywordStatusMapping;
import org.egov.workflow.persistence.entity.KeywordStatusMappingKey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KeywordStatusMappingRepositoryTest {

    @Mock
    private KeywordStatusMappingJpaRepository keywordStatusMappingJpaRepository;

    @Mock
    private ComplaintStatusJpaRepository complaintStatusJpaRepository;

    private KeywordStatusMappingRepository keywordStatusMappingRepository;

    @Before
    public void setUp(){
        keywordStatusMappingRepository = new KeywordStatusMappingRepository(keywordStatusMappingJpaRepository,
            complaintStatusJpaRepository);
    }

    @Test
    public void test_for_get_status_for_keyword(){
        when(keywordStatusMappingJpaRepository
            .findByKeywordAndTenantId("Complaint","default"))
            .thenReturn(getKeyWordStatusMappingEntities());

        List<KeywordStatusMapping> statusList = keywordStatusMappingJpaRepository.findByKeywordAndTenantId(
            "Complaint","default");

        when(complaintStatusJpaRepository.findByCodeInAndTenantId(asList("REGISTERED","CLOSED"),"default"))
            .thenReturn(getServiceStatusList());

        List<org.egov.workflow.domain.model.ComplaintStatus> domainStatuses = keywordStatusMappingRepository
            .getAllStatusForKeyword("Complaint","default");

        assertEquals(domainStatuses.size(),2);
        assertEquals(domainStatuses.get(0).getCode(), "0001");
    }

    private List<KeywordStatusMapping> getKeyWordStatusMappingEntities(){
        return asList(
            new KeywordStatusMapping(new KeywordStatusMappingKey("Complaint","default","REGISTERED")),
            new KeywordStatusMapping(new KeywordStatusMappingKey("Complaint","default","CLOSED"))
        );
    }

    private List<ComplaintStatus> getServiceStatusList(){
        return asList(
            new ComplaintStatus(1l,"REGISTERED","default","0001"),
            new ComplaintStatus(2l,"CLOSED","default","0002")
        );
    }

}
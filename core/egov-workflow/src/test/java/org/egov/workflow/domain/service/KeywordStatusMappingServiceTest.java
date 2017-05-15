package org.egov.workflow.domain.service;

import org.egov.workflow.domain.model.ComplaintStatus;
import org.egov.workflow.domain.model.KeywordStatusMapping;
import org.egov.workflow.domain.model.KeywordStatusMappingSearchCriteria;
import org.egov.workflow.persistence.repository.KeywordStatusMappingRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KeywordStatusMappingServiceTest {

    @Mock
    private KeywordStatusMappingRepository keywordStatusMappingRepository;

    private KeywordStatusMappingService keywordStatusMappingService;

    @Before
    public void setUp(){
        keywordStatusMappingService = new KeywordStatusMappingService(keywordStatusMappingRepository);
    }

    @Test
    public void test_should_return_status_given_keyword() throws Exception{
        List<ComplaintStatus> statusMappings = getKeywordStatusList();

        when(keywordStatusMappingService.getStatusForKeyword(new KeywordStatusMappingSearchCriteria("Complaint","default")))
            .thenReturn(statusMappings);

        List<ComplaintStatus> expectedKeywordMappings = keywordStatusMappingService
            .getStatusForKeyword(new KeywordStatusMappingSearchCriteria("Complaint","default"));

        assertThat(expectedKeywordMappings).isEqualTo(statusMappings);
    }

    private List<ComplaintStatus> getKeywordStatusList(){
        return asList(
            new ComplaintStatus(1l,"Complaint","default","REGISTERED"),
            new ComplaintStatus(2l,"Complaint","default","WITHDRAWN")
        );
    }
}
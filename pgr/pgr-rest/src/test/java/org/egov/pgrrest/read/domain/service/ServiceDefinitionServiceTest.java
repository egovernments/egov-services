package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.common.domain.model.ServiceDefinition;
import org.egov.pgrrest.read.domain.model.ServiceDefinitionSearchCriteria;
import org.egov.pgrrest.read.persistence.repository.ServiceDefinitionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceDefinitionServiceTest {

    @Mock
    private ServiceDefinitionRepository serviceDefinitionRepository;

    @InjectMocks
    private ServiceDefinitionService serviceDefinitionService;

    @Test
    public void test_should_return_service_definition_for_given_search_criteria() {
        final ServiceDefinitionSearchCriteria searchCriteria =
            new ServiceDefinitionSearchCriteria("serviceCode", "tenantId");
        final ServiceDefinition expectedServiceDefinition = ServiceDefinition.builder().build();
        when(serviceDefinitionRepository.find(searchCriteria)).thenReturn(expectedServiceDefinition);

        final ServiceDefinition actualServiceDefinition = serviceDefinitionService.find(searchCriteria);

        assertEquals(expectedServiceDefinition, actualServiceDefinition);
    }

}
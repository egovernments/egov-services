package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.common.contract.web.SevaRequest;
import org.egov.pgrrest.common.domain.model.*;
import org.egov.pgrrest.read.domain.exception.InvalidServiceTypeCodeException;
import org.egov.pgrrest.read.domain.model.ServiceDefinitionSearchCriteria;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.model.ServiceRequestLocation;
import org.egov.pgrrest.read.domain.model.ServiceRequestType;
import org.egov.pgrrest.read.domain.service.validator.AttributeValueValidator;
import org.egov.pgrrest.read.domain.factory.JSScriptEngineFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceRequestCustomFieldServiceTest {

    @Mock
    private ServiceDefinitionService serviceDefinitionService;
    
    @Mock
    private AttributeValueValidator attributeValueValidator;

    private ServiceRequestCustomFieldService customFieldService;
    
    @Before
    public void before() {
        final List<AttributeValueValidator> attributeValueValidators =
            Collections.singletonList(attributeValueValidator);
        final JSScriptEngineFactory scriptEngineFactory =
            new JSScriptEngineFactory("2000", "1");
        customFieldService = new ServiceRequestCustomFieldService(serviceDefinitionService,
            attributeValueValidators, scriptEngineFactory);
    }
    
    @Test
    public void test_should_update_seva_request_with_computed_field() {
        final List<AttributeEntry> attributeEntries = Arrays.asList(
            new AttributeEntry("gender", "male"),
            new AttributeEntry("age", "30")
        );
        final ServiceRequest serviceRequest = createServiceRequest(attributeEntries);
        final SevaRequest contractSevaRequest = mock(SevaRequest.class);
        final AttributeDefinition genderAttributeDefinition = AttributeDefinition.builder()
            .code("gender")
            .actions(Collections.emptyList())
            .dataType(AttributeDataType.SINGLE_VALUE_LIST)
            .build();
        final AttributeDefinition ageAttributeDefinition = AttributeDefinition.builder()
            .code("age")
            .dataType(AttributeDataType.INTEGER)
            .actions(Collections.emptyList())
            .build();
        final ComputeRuleDefinition ruleDefinition1 = ComputeRuleDefinition.builder()
            .rule("age > 30 && gender == 'male'")
            .value("50")
            .build();
        final ComputeRuleDefinition ruleDefinition2 = ComputeRuleDefinition.builder()
            .rule("age == 30 && gender == 'male'")
            .value("60")
            .build();
        final List<ComputeRuleDefinition> computeRules = Arrays.asList(
            ruleDefinition1,
            ruleDefinition2
        );
        final AttributeActionsDefinition attributeActionsDefinition =
            new AttributeActionsDefinition(ServiceStatus.COMPLAINT_REGISTERED);
        final AttributeDefinition applicationFeeAttributeDefinition = AttributeDefinition.builder()
            .code("applicationFee")
            .dataType(AttributeDataType.DOUBLE)
            .readOnly(true)
            .computeRules(computeRules)
            .actions(Collections.singletonList(attributeActionsDefinition))
            .build();
        final List<AttributeDefinition> attributeDefinitions = Arrays.asList(
            genderAttributeDefinition,
            ageAttributeDefinition,
            applicationFeeAttributeDefinition
        );
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(attributeDefinitions)
            .build();
        final org.egov.pgrrest.common.contract.web.ServiceRequest mockServiceRequest =
            mock(org.egov.pgrrest.common.contract.web.ServiceRequest.class);
        final List<org.egov.pgr.common.contract.AttributeEntry> contractAttributeEntries = new ArrayList<>();
        contractAttributeEntries.add(new org.egov.pgr.common.contract.AttributeEntry("gender", "male"));
        contractAttributeEntries.add(new org.egov.pgr.common.contract.AttributeEntry("age", "30"));
        when(contractSevaRequest.getAttributeValues()).thenReturn(contractAttributeEntries);
        when(contractSevaRequest.getServiceRequest()).thenReturn(mockServiceRequest);
        final ServiceDefinitionSearchCriteria searchCriteria =
            new ServiceDefinitionSearchCriteria("serviceCode", "tenantId");
        when(serviceDefinitionService.find(searchCriteria))
            .thenReturn(serviceDefinition);

        customFieldService.enrich(serviceRequest, contractSevaRequest, ServiceStatus.COMPLAINT_REGISTERED);

        assertEquals(3, contractAttributeEntries.size());
        assertEquals("60", contractAttributeEntries.get(2).getName());
        assertEquals("applicationFee", contractAttributeEntries.get(2).getKey());
    }

    @Test
    public void test_should_use_value_from_first_matching_rule_to_set_computed_field() {
        final List<AttributeEntry> attributeEntries = Arrays.asList(
            new AttributeEntry("gender", "male"),
            new AttributeEntry("age", "30")
        );
        final ServiceRequest serviceRequest = createServiceRequest(attributeEntries);
        final SevaRequest contractSevaRequest = mock(SevaRequest.class);
        final AttributeDefinition genderAttributeDefinition = AttributeDefinition.builder()
            .code("gender")
            .dataType(AttributeDataType.SINGLE_VALUE_LIST)
            .actions(Collections.emptyList())
            .build();
        final AttributeDefinition ageAttributeDefinition = AttributeDefinition.builder()
            .code("age")
            .dataType(AttributeDataType.INTEGER)
            .actions(Collections.emptyList())
            .build();
        final ComputeRuleDefinition ruleDefinition1 = ComputeRuleDefinition.builder()
            .rule("true")
            .value("50")
            .build();
        final ComputeRuleDefinition ruleDefinition2 = ComputeRuleDefinition.builder()
            .rule("true")
            .value("60")
            .build();
        final List<ComputeRuleDefinition> computeRules = Arrays.asList(
            ruleDefinition1,
            ruleDefinition2
        );
        final AttributeActionsDefinition attributeActionsDefinition =
            new AttributeActionsDefinition(ServiceStatus.COMPLAINT_REGISTERED);
        final AttributeDefinition applicationFeeAttributeDefinition = AttributeDefinition.builder()
            .code("applicationFee")
            .dataType(AttributeDataType.DOUBLE)
            .readOnly(true)
            .computeRules(computeRules)
            .actions(Collections.singletonList(attributeActionsDefinition))
            .build();
        final List<AttributeDefinition> attributeDefinitions = Arrays.asList(
            genderAttributeDefinition,
            ageAttributeDefinition,
            applicationFeeAttributeDefinition
        );
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(attributeDefinitions)
            .build();
        final org.egov.pgrrest.common.contract.web.ServiceRequest mockServiceRequest =
            mock(org.egov.pgrrest.common.contract.web.ServiceRequest.class);
        final List<org.egov.pgr.common.contract.AttributeEntry> contractAttributeEntries = new ArrayList<>();
        contractAttributeEntries.add(new org.egov.pgr.common.contract.AttributeEntry("gender", "male"));
        contractAttributeEntries.add(new org.egov.pgr.common.contract.AttributeEntry("age", "30"));
        when(contractSevaRequest.getAttributeValues()).thenReturn(contractAttributeEntries);
        final ServiceDefinitionSearchCriteria searchCriteria =
            new ServiceDefinitionSearchCriteria("serviceCode", "tenantId");
        when(serviceDefinitionService.find(searchCriteria))
            .thenReturn(serviceDefinition);

        customFieldService.enrich(serviceRequest, contractSevaRequest, ServiceStatus.COMPLAINT_REGISTERED);

        assertEquals(3, contractAttributeEntries.size());
        assertEquals("50", contractAttributeEntries.get(2).getName());
        assertEquals("applicationFee", contractAttributeEntries.get(2).getKey());
    }

    @Test
    public void test_should_not_set_computed_field_when_action_does_not_match_current_action() {
        final List<AttributeEntry> attributeEntries = Arrays.asList(
            new AttributeEntry("gender", "male"),
            new AttributeEntry("age", "30")
        );
        final ServiceRequest serviceRequest = createServiceRequest(attributeEntries);
        final SevaRequest contractSevaRequest = mock(SevaRequest.class);
        final AttributeDefinition genderAttributeDefinition = AttributeDefinition.builder()
            .code("gender")
            .dataType(AttributeDataType.SINGLE_VALUE_LIST)
            .actions(Collections.emptyList())
            .build();
        final AttributeDefinition ageAttributeDefinition = AttributeDefinition.builder()
            .code("age")
            .dataType(AttributeDataType.INTEGER)
            .actions(Collections.emptyList())
            .build();
        final ComputeRuleDefinition ruleDefinition1 = ComputeRuleDefinition.builder()
            .rule("true")
            .value("50")
            .build();
        final ComputeRuleDefinition ruleDefinition2 = ComputeRuleDefinition.builder()
            .rule("true")
            .value("60")
            .build();
        final List<ComputeRuleDefinition> computeRules = Arrays.asList(
            ruleDefinition1,
            ruleDefinition2
        );
        final AttributeActionsDefinition attributeActionsDefinition =
            new AttributeActionsDefinition(ServiceStatus.COMPLAINT_REGISTERED);
        final AttributeDefinition applicationFeeAttributeDefinition = AttributeDefinition.builder()
            .code("applicationFee")
            .dataType(AttributeDataType.DOUBLE)
            .readOnly(true)
            .computeRules(computeRules)
            .actions(Collections.singletonList(attributeActionsDefinition))
            .build();
        final List<AttributeDefinition> attributeDefinitions = Arrays.asList(
            genderAttributeDefinition,
            ageAttributeDefinition,
            applicationFeeAttributeDefinition
        );
        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
            .attributes(attributeDefinitions)
            .build();
        final org.egov.pgrrest.common.contract.web.ServiceRequest mockServiceRequest =
            mock(org.egov.pgrrest.common.contract.web.ServiceRequest.class);
        final List<org.egov.pgr.common.contract.AttributeEntry> contractAttributeEntries = new ArrayList<>();
        contractAttributeEntries.add(new org.egov.pgr.common.contract.AttributeEntry("gender", "male"));
        contractAttributeEntries.add(new org.egov.pgr.common.contract.AttributeEntry("age", "30"));
        when(contractSevaRequest.getAttributeValues()).thenReturn(contractAttributeEntries);
        final ServiceDefinitionSearchCriteria searchCriteria =
            new ServiceDefinitionSearchCriteria("serviceCode", "tenantId");
        when(serviceDefinitionService.find(searchCriteria))
            .thenReturn(serviceDefinition);

        customFieldService.enrich(serviceRequest, contractSevaRequest, ServiceStatus.COMPLAINT_PROCESSING);

        assertEquals(2, contractAttributeEntries.size());
    }

    @Test(expected = InvalidServiceTypeCodeException.class)
    public void test_should_throw_exception_when_service_definition_is_not_defined() {
        final List<AttributeEntry> attributeEntries = Arrays.asList(
            new AttributeEntry("gender", "male"),
            new AttributeEntry("age", "30")
        );
        final ServiceRequest serviceRequest = createServiceRequest(attributeEntries);
        final SevaRequest contractSevaRequest = mock(SevaRequest.class);
        final org.egov.pgrrest.common.contract.web.ServiceRequest mockServiceRequest =
            mock(org.egov.pgrrest.common.contract.web.ServiceRequest.class);
        final List<org.egov.pgr.common.contract.AttributeEntry> contractAttributeEntries = new ArrayList<>();
        contractAttributeEntries.add(new org.egov.pgr.common.contract.AttributeEntry("gender", "male"));
        contractAttributeEntries.add(new org.egov.pgr.common.contract.AttributeEntry("age", "30"));
        when(contractSevaRequest.getAttributeValues()).thenReturn(contractAttributeEntries);
        final ServiceDefinitionSearchCriteria searchCriteria =
            new ServiceDefinitionSearchCriteria("serviceCode", "tenantId");
        when(serviceDefinitionService.find(searchCriteria)).thenReturn(null);

        customFieldService.enrich(serviceRequest, contractSevaRequest, ServiceStatus.COMPLAINT_REGISTERED);
    }

    private ServiceRequest createServiceRequest(List<AttributeEntry> attributeEntries) {
        return ServiceRequest.builder()
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .serviceRequestLocation(ServiceRequestLocation.builder().build())
            .serviceRequestType(ServiceRequestType.builder()
                .code("serviceCode")
                .tenantId("tenantId")
                .build())
            .attributeEntries(attributeEntries)
            .requester(Requester.builder().build())
            .tenantId("tenantId")
            .build();
    }

}
package org.egov.pgrrest.read.domain.service;

import delight.nashornsandbox.NashornSandbox;
import lombok.extern.slf4j.Slf4j;
import org.egov.pgrrest.common.contract.web.SevaRequest;
import org.egov.pgrrest.common.domain.model.*;
import org.egov.pgrrest.read.domain.exception.InvalidServiceTypeCodeException;
import org.egov.pgrrest.read.domain.exception.RuleEvaluationException;
import org.egov.pgrrest.read.domain.model.ServiceDefinitionSearchCriteria;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.service.validator.AttributeValueValidator;
import org.egov.pgrrest.read.domain.factory.JSScriptEngineFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
    Responsible for validating and computing custom fields.
 */
@Service
@Slf4j
public class ServiceRequestCustomFieldService {

    private static final String RULE_FAILED_MESSAGE = "Execution of rule name: %s with definition: %s failed";
    private ServiceDefinitionService serviceDefinitionService;
    private List<AttributeValueValidator> attributeValueValidators;
    private JSScriptEngineFactory scriptEngineFactory;

    public ServiceRequestCustomFieldService(ServiceDefinitionService serviceDefinitionService,
                                            List<AttributeValueValidator> attributeValueValidators,
                                            JSScriptEngineFactory scriptEngineFactory) {
        this.serviceDefinitionService = serviceDefinitionService;
        this.attributeValueValidators = attributeValueValidators;
        this.scriptEngineFactory = scriptEngineFactory;
    }

    public void enrich(ServiceRequest serviceRequest, SevaRequest contractSevaRequest, ServiceStatus action) {
        final List<org.egov.pgr.common.contract.AttributeEntry> attribValues =
            contractSevaRequest.getAttributeValues();
        final ServiceDefinition serviceDefinition = getServiceDefinition(serviceRequest);
        validateKnownServiceDefinition(serviceRequest, serviceDefinition);
        if (serviceDefinition.isAttributesAbsent()) {
            return;
        }
        validateAttributeValues(serviceRequest, serviceDefinition, action);
        if (serviceDefinition.isComputedFieldsAbsent(action)) {
            return;
        }
        computeFields(serviceRequest, attribValues, serviceDefinition, action);
    }

    private void computeFields(ServiceRequest serviceRequest,
                               List<org.egov.pgr.common.contract.AttributeEntry> attribValues,
                               ServiceDefinition serviceDefinition, ServiceStatus action) {
        final NashornSandbox scriptEngine = scriptEngineFactory.create();
        final Map<String, List<AttributeEntry>> codeToAttributeEntriesMap =
            getCodeToAttributeEntriesMap(serviceRequest);
        loadEngineWithDefinedVariables(serviceDefinition, scriptEngine, codeToAttributeEntriesMap);
        serviceDefinition.getComputedFields(action)
            .forEach(computedAttributeDefinition ->
                computeAttribute(scriptEngine, computedAttributeDefinition, attribValues));
    }

    private void computeAttribute(NashornSandbox scriptEngine,
                                  AttributeDefinition computedAttributeDefinition,
                                  List<org.egov.pgr.common.contract.AttributeEntry> attribValues) {
        for (ComputeRuleDefinition rule : computedAttributeDefinition.getComputeRules()) {
            final boolean isRuleApplicable = isRuleApplicable(scriptEngine, rule);
            if (isRuleApplicable) {
                final String code = computedAttributeDefinition.getCode();
                final org.egov.pgr.common.contract.AttributeEntry entry =
                    new org.egov.pgr.common.contract.AttributeEntry(code, rule.getValue());
                attribValues.add(entry);
                return;
            }
        }
    }

    private boolean isRuleApplicable(NashornSandbox scriptEngine, ComputeRuleDefinition rule) {
        try {
            return (boolean) scriptEngine.eval(rule.getRule());
        } catch (Exception e) {
            log.error(String.format(RULE_FAILED_MESSAGE, rule.getName(), rule.getRule()), e);
            throw new RuleEvaluationException(rule);
        }
    }

    private Map<String, List<AttributeEntry>> getCodeToAttributeEntriesMap(ServiceRequest serviceRequest) {
        return serviceRequest.getAttributeEntries()
            .stream().collect(Collectors.groupingBy(AttributeEntry::getKey));
    }

    private void loadEngineWithDefinedVariables(ServiceDefinition serviceDefinition, NashornSandbox scriptEngine,
                                                Map<String, List<AttributeEntry>> codeToAttributeEntriesMap) {
        serviceDefinition.getNonComputedAttributes().forEach(attributeDefinition -> {
            final List<AttributeEntry> matchingAttributeEntries =
                codeToAttributeEntriesMap.get(attributeDefinition.getCode());
            if (CollectionUtils.isEmpty(matchingAttributeEntries)) {
                return;
            }
            Object parsedObject = getParsedObject(attributeDefinition, matchingAttributeEntries);
            scriptEngine.inject(attributeDefinition.getCode(), parsedObject);
        });
    }

    private Object getParsedObject(AttributeDefinition attributeDefinition,
                                   List<AttributeEntry> matchingAttributeEntries) {
        Object parsedObject;
        if (attributeDefinition.isMultiValueType()) {
            parsedObject = getParsedObject(matchingAttributeEntries, attributeDefinition);
        } else {
            parsedObject = getParsedObject(matchingAttributeEntries.get(0), attributeDefinition);
        }
        return parsedObject;
    }

    private Object getParsedObject(AttributeEntry attributeEntry, AttributeDefinition attributeDefinition) {
        return attributeDefinition.parse(attributeEntry);
    }

    private Object getParsedObject(List<AttributeEntry> attributeEntries, AttributeDefinition attributeDefinition) {
        return attributeDefinition.parse(attributeEntries);
    }

    private void validateAttributeValues(ServiceRequest serviceRequest,
                                         ServiceDefinition serviceDefinition,
                                         ServiceStatus action) {
        attributeValueValidators.forEach(validator -> validator.validate(serviceRequest, serviceDefinition, action));
    }

    private void validateKnownServiceDefinition(ServiceRequest serviceRequest, ServiceDefinition serviceDefinition) {
        if (serviceDefinition == null) {
            throw new InvalidServiceTypeCodeException(serviceRequest.getServiceTypeCode());
        }
    }

    private ServiceDefinition getServiceDefinition(ServiceRequest serviceRequest) {
        final ServiceDefinitionSearchCriteria serviceDefinitionSearchCriteria = serviceRequest
            .getServiceDefinitionSearchCriteria();
        return serviceDefinitionService.find(serviceDefinitionSearchCriteria);
    }
}

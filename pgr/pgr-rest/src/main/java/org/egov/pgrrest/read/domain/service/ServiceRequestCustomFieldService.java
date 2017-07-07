package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.common.contract.web.SevaRequest;
import org.egov.pgrrest.common.domain.model.AttributeDefinition;
import org.egov.pgrrest.common.domain.model.AttributeEntry;
import org.egov.pgrrest.common.domain.model.ComputeRuleDefinition;
import org.egov.pgrrest.common.domain.model.ServiceDefinition;
import org.egov.pgrrest.read.domain.exception.InvalidServiceTypeCodeException;
import org.egov.pgrrest.read.domain.model.ServiceDefinitionSearchCriteria;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.service.validator.AttributeValueValidator;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
    Responsible for validating and computing custom fields.
 */
@Service
public class ServiceRequestCustomFieldService {

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

    public void enrich(ServiceRequest serviceRequest, SevaRequest contractSevaRequest) {
        final List<org.egov.pgr.common.contract.AttributeEntry> attribValues = contractSevaRequest
            .getServiceRequest().getAttribValues();
        final ServiceDefinition serviceDefinition = getServiceDefinition(serviceRequest);
        validateKnownServiceDefinition(serviceRequest, serviceDefinition);
        if (serviceDefinition.isAttributesAbsent()) {
            return;
        }
        validateAttributeValues(serviceRequest, serviceDefinition);
        if (serviceDefinition.isComputedFieldsAbsent()) {
            return;
        }
        final ScriptEngine scriptEngine = scriptEngineFactory.create();
        final Map<String, List<AttributeEntry>> codeToAttributeEntriesMap =
            getCodeToAttributeEntriesMap(serviceRequest);
        loadEngineWithDefinedVariables(serviceDefinition, scriptEngine, codeToAttributeEntriesMap);
        serviceDefinition.getComputedFields().forEach(computedAttributeDefinition -> {
            for (ComputeRuleDefinition rule: computedAttributeDefinition.getComputeRules()) {
                final boolean isRuleApplicable = isRuleApplicable(scriptEngine, rule);
                if(!isRuleApplicable) {
                    continue;
                }
                final String code = computedAttributeDefinition.getCode();
                final org.egov.pgr.common.contract.AttributeEntry entry =
                    new org.egov.pgr.common.contract.AttributeEntry(code, rule.getValue());
                attribValues.add(entry);
                break;
            }
        });

    }

    private boolean isRuleApplicable(ScriptEngine scriptEngine, ComputeRuleDefinition rule) {
        try {
			return (boolean) scriptEngine.eval(rule.getRule());
		} catch (ScriptException e) {
			throw new RuntimeException(e);
		}
    }

    private Map<String, List<AttributeEntry>> getCodeToAttributeEntriesMap(ServiceRequest serviceRequest) {
        return serviceRequest.getAttributeEntries()
            .stream().collect(Collectors.groupingBy(AttributeEntry::getKey));
    }

    private void loadEngineWithDefinedVariables(ServiceDefinition serviceDefinition, ScriptEngine scriptEngine,
                                                Map<String, List<AttributeEntry>> codeToAttributeEntriesMap) {
        serviceDefinition.getNonComputedAttributes().forEach(attributeDefinition -> {
            final List<AttributeEntry> matchingAttributeEntries =
                codeToAttributeEntriesMap.get(attributeDefinition.getCode());
            if (CollectionUtils.isEmpty(matchingAttributeEntries)) {
                return;
            }
            Object parsedObject = getParsedObject(attributeDefinition, matchingAttributeEntries);
            scriptEngine.put(attributeDefinition.getCode(), parsedObject);
        });
    }

    private Object getParsedObject(AttributeDefinition attributeDefinition, List<AttributeEntry> matchingAttributeEntries) {
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

    private void validateAttributeValues(ServiceRequest serviceRequest, ServiceDefinition serviceDefinition) {
        attributeValueValidators.forEach(validator -> validator.validate(serviceRequest, serviceDefinition));
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

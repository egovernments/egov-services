package org.egov.pt.service;

import java.util.*;

import org.egov.pt.config.PropertyConfiguration;
import org.egov.pt.producer.Producer;
import org.egov.pt.repository.PropertyRepository;
import org.egov.pt.util.ResponseInfoFactory;
import org.egov.pt.validator.PropertyValidator;
import org.egov.pt.web.models.*;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

	@Autowired
	private Producer producer;

	@Autowired
	private PropertyConfiguration config;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private PropertyRepository repository;

	@Autowired
	private EnrichmentService enrichmentService;

	@Autowired
	private PropertyValidator propertyValidator;

	/**
	 * Assign ids through enrichment and pushes to kafka
	 * @param request
	 * @return
	 */
	public List<Property> createProperty(PropertyRequest request) {
		enrichmentService.enrichCreateRequest(request);
		propertyValidator.validateMasterData(request);
		producer.push(config.getSavePropertyTopic(), request);
		return request.getProperties();
	}


	/**
	 * Search property with given PropertyCriteria
	 * @param criteria
	 * @return
	 */
	public List<Property> searchProperty(PropertyCriteria criteria) {
		List<Property> properties = repository.getProperties(criteria);
		return properties;
	}

	/**
	 * Enriches and updates the property
	 * @param request
	 * @return
	 */
	public List<Property> updateProperty(PropertyRequest request) {
		PropertyCriteria propertyCriteria = propertyValidator.getPropertirsFromSearch(request);
		List<Property> responseProperties = searchProperty(propertyCriteria);
		boolean ifPropertyExists=propertyValidator.PropertyExists(request,responseProperties);
        if(ifPropertyExists) {
			enrichmentService.enrichUpdateRequest(request,responseProperties);
			producer.push(config.getUpdatePropertyTopic(), request);
			return request.getProperties();
		}
		else
		{    throw new CustomException("usr_002","invalid id");  // Change the error code

		}
	}





}
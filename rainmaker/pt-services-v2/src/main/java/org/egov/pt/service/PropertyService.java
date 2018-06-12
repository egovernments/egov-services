package org.egov.pt.service;

import java.util.*;

import org.egov.common.contract.request.RequestInfo;
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

	@Autowired
	private UserService userService;



	/**
	 * Assign ids through enrichment and pushes to kafka
	 * @param request
	 * @return
	 */
	public List<Property> createProperty(PropertyRequest request) {
		userService.createUser(request);
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
	public List<Property> searchProperty(PropertyCriteria criteria,RequestInfo requestInfo) {
		UserDetailResponse userDetailResponse = userService.getUser(criteria,requestInfo);
		enrichmentService.enrichPropertyCriteria(criteria,userDetailResponse);
		List<Property> properties = repository.getProperties(criteria);
		enrichmentService.enrichOwner(userDetailResponse,properties);
		return properties;
	}

	/**
	 * Enriches and updates the property
	 * @param request
	 * @return
	 */
	public List<Property> updateProperty(PropertyRequest request) {
		PropertyCriteria propertyCriteria = propertyValidator.getPropertyCriteriaForSearch(request);
		List<Property> responseProperties = searchProperty(propertyCriteria,request.getRequestInfo());
		boolean ifPropertyExists=propertyValidator.PropertyExists(request,responseProperties);
		boolean paid = false;
		/**
		 * Call demand api to check if payment is done
		 */
		if(ifPropertyExists && !paid) {
			enrichmentService.enrichUpdateRequest(request,responseProperties);
			userService.updateUser(request);
			producer.push(config.getUpdatePropertyTopic(), request);
			return request.getProperties();
		}
		else if(ifPropertyExists && paid){
			return createProperty(request);
		}
		else
		{    throw new CustomException("usr_002","invalid id");  // Change the error code

		}
	}





}
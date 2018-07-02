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
	 * @param request PropertyRequest containing list of properties to be created
	 * @return List of properties successfully created
	 */
	public List<Property> createProperty(PropertyRequest request) {
		userService.createUser(request);
		enrichmentService.enrichCreateRequest(request,false);
		propertyValidator.validateMasterData(request);
		producer.push(config.getSavePropertyTopic(), request);
		return request.getProperties();
	}


	/**
	 * Search property with given PropertyCriteria
	 * @param criteria PropertyCriteria containing fields on which search is based
	 * @return list of properties satisfying the containing fields in criteria
	 */
	public List<Property> searchProperty(PropertyCriteria criteria,RequestInfo requestInfo) {
		List<Property> properties;
		if(criteria.getMobileNumber()!=null || criteria.getName()!=null)
		{   UserDetailResponse userDetailResponse = userService.getUser(criteria,requestInfo);
		    // If user not found with given user fields return empty list
		    if(userDetailResponse.getUser().size()==0){
		    	return new ArrayList<>();
			}
			enrichmentService.enrichPropertyCriteriaWithOwnerids(criteria,userDetailResponse);
			properties = repository.getProperties(criteria);
			// If property not found with given propertyId or oldPropertyId or address fields return empty list
			if(properties.size()==0){
				return new ArrayList<>();
			}
			criteria=enrichmentService.getPropertyCriteriaFromPropertyIds(properties);
			properties = getPropertiesWithOwnerInfo(criteria,requestInfo);
		}
	    else{
			properties = getPropertiesWithOwnerInfo(criteria,requestInfo);
	   }
		  return properties;
	}

	/**
	 * Returns list of properties based on the given propertyCriteria with owner fields populated from user service
	 * @param criteria PropertyCriteria on which to search properties
	 * @param requestInfo RequestInfo object of the request
	 * @return properties with owner information added from user service
	 */
	private List<Property> getPropertiesWithOwnerInfo(PropertyCriteria criteria,RequestInfo requestInfo){
		List<Property> properties = repository.getProperties(criteria);
		enrichmentService.enrichPropertyCriteriaWithOwnerids(criteria,properties);
		UserDetailResponse userDetailResponse = userService.getUser(criteria,requestInfo);
		enrichmentService.enrichOwner(userDetailResponse,properties);
		return properties;
	}

	/**
	 * Updates the property
	 * @param request PropertyRequest containing list of properties to be update
	 * @return List of updated properties
	 */
	public List<Property> updateProperty(PropertyRequest request) {
		PropertyCriteria propertyCriteria = propertyValidator.getPropertyCriteriaForSearch(request);
		List<Property> propertiesFromSearchResponse = searchProperty(propertyCriteria,request.getRequestInfo());
		boolean ifPropertyExists=propertyValidator.PropertyExists(request,propertiesFromSearchResponse);
		boolean paid = false;
		/**
		 * Call demand api to check if payment is done
		 */
		if(ifPropertyExists && !paid) {
			enrichmentService.enrichUpdateRequest(request,propertiesFromSearchResponse);
			userService.updateUser(request);
			producer.push(config.getUpdatePropertyTopic(), request);
			return request.getProperties();
		}
		else if(ifPropertyExists && paid){
			enrichmentService.enrichCreateRequest(request,true);
			userService.createUser(request);
			producer.push(config.getUpdatePropertyTopic(), request);
			return request.getProperties();
		}
		else
		{    throw new CustomException("usr_002","invalid id");  // Change the error code
		}
	}

	/*private List<Property> createPropertyDetail(PropertyRequest request){
		userService.createUser(request);
		enrichmentService.enrichCreateRequest(request,true);This is in sharp contrast to the way most VCS tools branch, which involves copying all of the project’s files into a second directory. This can take several seconds or even minutes, depending on the size of the project, whereas in Git the process is always instantaneous. Also, because we’re recording the parents when we commit, finding a proper merge base for merging is automatically done for us and is generally very easy to do. These features help encourage developers to create and use branches often.


		propertyValidator.validateMasterData(request);
		producer.push(config.getSavePropertyTopic(), request);
		return request.getProperties();
	}*/





}
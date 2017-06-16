package org.egov.property.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.egov.models.AttributeNotFoundException;
import org.egov.models.IdGenerationRequest;
import org.egov.models.IdGenerationResponse;
import org.egov.models.IdRequest;
import org.egov.models.Property;
import org.egov.models.PropertyLocation;
import org.egov.models.PropertyRequest;
import org.egov.models.PropertyResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.WorkFlowDetails;
import org.egov.property.exception.InvalidPropertyBoundaryException;
import org.egov.property.exception.InvalidUpdatePropertyException;
import org.egov.property.model.BoundaryResponseInfo;
import org.egov.property.propertyConsumer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * This Service to validate the property attributes
 * @author Pavan Kumar Kamma
 *
 */

@Service
public class PropertyValidator {

	@Autowired
	private Environment env;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@Autowired
	Producer producer;
	
	@Autowired
	Environment environment;

	/**
	 * Description : This validates the property boundary
	 * @param property
	 * @throws InvalidPropertyBoundaryException
	 */
	public void validatePropertyBoundary(Property property,RequestInfo requestInfo) throws InvalidPropertyBoundaryException{

		PropertyLocation propertyLocation = property.getBoundary();
		List<String> fields = propertyLocation.getAllBoundaries();
		for(String field:fields){
			validateBoundaryFields(property, field,requestInfo);
		}
	}

	/**
	 * Description : This validates the each boundary field of the property
	 * @param property
	 * @param field
	 * @return true
	 * @throws InvalidPropertyBoundaryException
	 */
	public Boolean validateBoundaryFields(Property property, String field,RequestInfo requestInfo) throws InvalidPropertyBoundaryException{

		PropertyLocation propertyLocation = property.getBoundary();
		Long id;

		if(field.equalsIgnoreCase("revenueBoundary")){
			id = propertyLocation.getRevenueBoundary().getId();
		} else if(field.equalsIgnoreCase("locationBoundary")){
			id = propertyLocation.getLocationBoundary().getId();
		} else {
			id = propertyLocation.getAdminBoundary().getId();
		}
		StringBuffer BoundaryURI = new StringBuffer();
		BoundaryURI.append(env.getProperty("egov.services.boundary_service.hostname")).append(env.getProperty("egov.services.boundary_service.searchpath"));
		URI uri = UriComponentsBuilder.fromUriString(BoundaryURI.toString())
				.queryParam("Boundary.tenantId", property.getTenantId())
				.queryParam("Boundary.id", id).build(true).encode().toUri();

		try {
			BoundaryResponseInfo boundaryResponseInfo = restTemplate.getForObject(uri, BoundaryResponseInfo.class);
			if(boundaryResponseInfo.getResponseInfo()!=null && boundaryResponseInfo.getBoundary().size()!=0){
				return true;
			} else {
				throw new InvalidPropertyBoundaryException(requestInfo);
			}
		} catch (HttpClientErrorException ex){
			throw new InvalidPropertyBoundaryException(requestInfo);
		}
	}

	private PropertyResponse validateUpdateProperty(PropertyRequest propertyRequest) {
		//Boundary validations for all properties
		for(Property property :propertyRequest.getProperties()){

			validatePropertyBoundary(property,propertyRequest.getRequestInfo());

			String acknowledgementNo = property.getPropertyDetail().getApplicationNo();

			//Check Acknowledgement no. is null or not
			if (acknowledgementNo == null) {
				throw new AttributeNotFoundException(env.getProperty("acknowledgement.message"), propertyRequest.getRequestInfo());

			} else {
				WorkFlowDetails workflowDetails = property.getPropertyDetail().getWorkFlowDetails();

				if (workflowDetails.getAction() == null) {
					throw new InvalidUpdatePropertyException(env.getProperty("workflow.action.message"), propertyRequest.getRequestInfo());

				} else if (workflowDetails.getAssignee() == null) {
					throw new InvalidUpdatePropertyException(env.getProperty("workflow.assignee.message"), propertyRequest.getRequestInfo());

				} else if (workflowDetails.getDepartment() == null) {
					throw new InvalidUpdatePropertyException(env.getProperty("workflow.department.message"), propertyRequest.getRequestInfo());

				} else if (workflowDetails.getDesignation() == null) {
					throw new InvalidUpdatePropertyException(env.getProperty("workflow.designation.message"), propertyRequest.getRequestInfo());

				} else if (workflowDetails.getStatus() == null) {
					throw new InvalidUpdatePropertyException(env.getProperty("workflow.status.message"), propertyRequest.getRequestInfo());

				} 
			}
			producer.send(env.getProperty("user.update"), propertyRequest);
		}
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(propertyRequest.getRequestInfo(),true);
		PropertyResponse propertyResponse=new PropertyResponse();
		propertyResponse.setResponseInfo(responseInfo);
		propertyResponse.setProperties(propertyRequest.getProperties());
		return propertyResponse;
	}

	/**
	 * Description : 
	 * For each property, it checks and validate
	 * 	1.PropertyBoundary
	 * 	2.acknowledgementNo
	 *  3.workflowDetails Action
	 *  4.workflowDetails Assignee
	 *  5.workflowDetails Department
	 *  6.workflowDetails Designation
	 *  7.workflowDetails Status
	 */
	public PropertyResponse updateProperty(PropertyRequest propertyRequest) {
		return validateUpdateProperty(propertyRequest);
	}
	
	/**
	 * Description: this method for creating property
	 * @param propertyRequest
	 * @return propertyResponse
	 */
	
	public PropertyResponse createProperty(PropertyRequest propertyRequest){
		return validateCreateProperty(propertyRequest);
	}
	
	/***
	 * Description : This method will validate property request and send to kafka server
	 * @param propertyRequest
	 * @return propertyResponse
	 */
	private PropertyResponse validateCreateProperty(PropertyRequest propertyRequest){
		  List<String> applicationList=new ArrayList<String>();
	        //Boundary validations for all properties
	        for(Property property :propertyRequest.getProperties()){
	            validatePropertyBoundary(property,propertyRequest.getRequestInfo());      
	        }

	        StringBuffer idGenerationUrl=new StringBuffer();
	        idGenerationUrl.append(environment.getProperty("egov.services.id_service.hostname"));
	        idGenerationUrl.append(environment.getProperty("egov.services.id_service.basepath"));
	        idGenerationUrl.append(environment.getProperty("egov.services.id_service.createpath"));

	        //generating acknowledgement number for all properties
	        for(Property property:propertyRequest.getProperties()){
	            List<IdRequest> idRequests = new ArrayList<>();
	            IdRequest idrequest=new IdRequest();
	            idrequest.setIdName(environment.getProperty("id.idName"));
	            idrequest.setTenantId(propertyRequest.getProperties().get(0).getTenantId());
	            IdGenerationRequest idGeneration=new IdGenerationRequest();
	            idRequests.add(idrequest);
	            idGeneration.setIdRequests(idRequests); 
	            idGeneration.setRequestInfo(propertyRequest.getRequestInfo());
	            IdGenerationResponse idResponse=restTemplate.patchForObject(idGenerationUrl.toString(), idGeneration, IdGenerationResponse.class);
	            if(idResponse.getResponseInfo().getStatus().toString().equalsIgnoreCase(environment.getProperty("success"))){
	                if(idResponse.getResponseInfo().getStatus().toString().equalsIgnoreCase(environment.getProperty("failed"))){
	                    throw new AttributeNotFoundException(environment.getProperty("attribute.notfound"),propertyRequest.getRequestInfo());
	                }
	            }
	            property.getPropertyDetail().setApplicationNo(idResponse.getIdResponses().get(0).getId());      
	            applicationList.add(idResponse.getIdResponses().get(0).getId());
	            PropertyRequest propertyRequestInfo=new PropertyRequest();
	            propertyRequestInfo.setRequestInfo(propertyRequest.getRequestInfo());
	            List<Property> propertyList=new ArrayList<Property>();
	            propertyList.add(property);
	            propertyRequestInfo.setProperties(propertyList);
	            producer.send(environment.getProperty("user.create"), propertyRequestInfo);
	        }

	        ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(propertyRequest.getRequestInfo(),true);
	        PropertyResponse propertyResponse=new PropertyResponse();
	        propertyResponse.setResponseInfo(responseInfo);
	        propertyResponse.setProperties(propertyRequest.getProperties());    
	        return propertyResponse;
	}
}

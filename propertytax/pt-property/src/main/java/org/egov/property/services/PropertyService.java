package org.egov.property.services;

import org.egov.models.PropertyRequest;
import org.egov.models.PropertyResponse;
import org.egov.models.RequestInfo;
import org.egov.models.TitleTransferRequest;
import org.egov.models.TitleTransferResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface PropertyService {

	/**
	 * This method for validating request and generating id,send property object
	 * to kafka
	 * 
	 * @param propertyRequest
	 * @return propertyResponse
	 */
	public PropertyResponse createProperty(PropertyRequest propertyRequest);

	/**
	 * This method for updating property
	 * 
	 * @param propertyRequest
	 * @return propertyResponse
	 */

	public PropertyResponse updateProperty(PropertyRequest propertyRequest);

	/**
	 * This method for search properties based on input parameters
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param active
	 * @param upicNo
	 * @param pageSize
	 * @param pageNumber
	 * @param sort
	 * @param oldUpicNo
	 * @param mobileNumber
	 * @param aadhaarNumber
	 * @param houseNoBldgApt
	 * @param revenueZone
	 * @param revenueWard
	 * @param locality
	 * @param ownerName
	 * @param demandFrom
	 * @param demandTo
	 * @return
	 */

	public PropertyResponse searchProperty(RequestInfo requestInfo, String tenantId, Boolean active, String upicNo,
			Integer pageSize, Integer pageNumber, String[] sort, String oldUpicNo, String mobileNumber,
			String aadhaarNumber, String houseNoBldgApt, Integer revenueZone, Integer revenueWard, Integer locality,
			String ownerName, Integer demandFrom, Integer demandTo,String propertyId,String applicationNo) throws Exception;
	
	
	/**
	 * This api for creating title transfer request for property
	 * @param titleTransferRequest
	 * @return titleTransferResponse
	 * @throws Exception
	 */
	
	public TitleTransferResponse createTitleTransfer(@RequestBody TitleTransferRequest titleTransferRequest) throws Exception;
	
	/**
	 * This api for updating title transfer request for property
	 * @param titleTransferRequest
	 * @return titleTransferResponse
	 * @throws Exception
	 */
	public TitleTransferResponse updateTitleTransfer(@RequestBody TitleTransferRequest titleTransferRequest) throws Exception;
}

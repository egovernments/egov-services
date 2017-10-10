package org.egov.property.services;

import org.egov.models.DemandResponse;
import org.egov.models.PropertyDCBRequest;
import org.egov.models.PropertyDCBResponse;
import org.egov.models.PropertyRequest;
import org.egov.models.PropertyResponse;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.SpecialNoticeRequest;
import org.egov.models.SpecialNoticeResponse;
import org.egov.models.TitleTransferRequest;
import org.egov.models.TitleTransferResponse;
import org.egov.property.model.TitleTransferSearchResponse;

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
	 * @param usage
	 * @param adminBoundary
	 * @param oldestUpicNo
	 * @return {@link PropertyResponse}
	 */

	public PropertyResponse searchProperty(RequestInfo requestInfo, String tenantId, Boolean active, String upicNo,
			Integer pageSize, Integer pageNumber, String[] sort, String oldUpicNo, String mobileNumber,
			String aadhaarNumber, String houseNoBldgApt, String revenueZone, String revenueWard, String locality,
			String ownerName, Double demandFrom, Double demandTo, String propertyId, String applicationNo, String usage,
			String adminBoundary, String oldestUpicNo) throws Exception;

	/**
	 * This api for creating title transfer request for property
	 * 
	 * @param titleTransferRequest
	 * @return titleTransferResponse
	 * @throws Exception
	 */

	public TitleTransferResponse createTitleTransfer(TitleTransferRequest titleTransferRequest) throws Exception;

	/**
	 * This api for updating title transfer request for property
	 * 
	 * @param titleTransferRequest
	 * @return titleTransferResponse
	 * @throws Exception
	 */
	public TitleTransferResponse updateTitleTransfer(TitleTransferRequest titleTransferRequest) throws Exception;

	/**
	 * Save property history and update property
	 * 
	 * @param titleTransferRequest
	 * @throws Exception
	 */
	public PropertyRequest savePropertyHistoryandUpdateProperty(TitleTransferRequest titleTransferRequest)
			throws Exception;

	/**
	 * This Api will generate the special based on the given special notice
	 * request
	 * 
	 * @param specialNoticeRequest
	 * @return {@link SpecialNoticeResponse}
	 * @throws Exception
	 */
	public SpecialNoticeResponse generateSpecialNotice(SpecialNoticeRequest specialNoticeRequest) throws Exception;

	/**
	 * API prepares DCB data for Add/Edit DCB feature
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param upicNumber
	 * @throws Exception
	 */
	public DemandResponse getDemandsForProperty(RequestInfoWrapper requestInfo, String tenantId, String upicNumber)
			throws Exception;

	/**
	 * This API will search the title transfer based on the given parameters
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param pageSize
	 * @param pageNumber
	 * @param sort
	 * @param upicNo
	 * @param oldUpicNo
	 * @param applicationNo
	 * @return {@link TitleTransferResponse}
	 * @throws Exception
	 */
	public TitleTransferSearchResponse searchTitleTransfer(RequestInfoWrapper requestInfo, String tenantId,
			Integer pageSize, Integer pageNumber, String[] sort, String upicNo, String oldUpicNo, String applicationNo)
			throws Exception;

	/**
	 * This is api to update dcb demands
	 * 
	 * @param propertyDCBRequest
	 * @param tenantId
	 * @return
	 */
	public PropertyDCBResponse updateDcbDemand(PropertyDCBRequest propertyDCBRequest, String tenantId) throws Exception;

	/**
	 * 
	 * This will create a new property with the status as workflow
	 * 
	 * @param propertyRequest
	 */
	public PropertyResponse modifyProperty(PropertyRequest propertyRequest) throws Exception;

}

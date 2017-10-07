package org.egov.property.api;

import javax.validation.Valid;

import org.egov.models.*;
import org.egov.property.model.TitleTransferSearchResponse;
import org.egov.property.services.NoticeService;
import org.egov.property.services.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Property Controller have the api's related to property
 * 
 * @author Narendra
 */
@RestController
@RequestMapping(path = "/properties/")
public class PropertyController {

	private static final Logger logger = LoggerFactory.getLogger(PropertyController.class);

	@Autowired
	PropertyService propertyService;

	@Autowired
	NoticeService noticeService;

	/**
	 * Description: this api will use for creating property
	 * 
	 * @param propertyRequest
	 * @return PropertyResponse
	 */

	@RequestMapping(method = RequestMethod.POST, path = "_create")
	public PropertyResponse createProperty(@Valid @RequestBody PropertyRequest propertyRequest) {
		logger.info("PropertyController    PropertyRequest ---->> " + propertyRequest);
		return propertyService.createProperty(propertyRequest);

	}

	/**
	 * updateProperty method validate each property before update
	 * 
	 * @param PropertyRequest
	 */
	@RequestMapping(method = RequestMethod.POST, path = "_update")
	public PropertyResponse updateProperty(@Valid @RequestBody PropertyRequest propertyRequest) {
		return propertyService.updateProperty(propertyRequest);

	}

	/**
	 * This api for searching property based on input
	 * parameters.demandTo,demandFrom,houseNoBldgApt,revenueZone,revenueWard
	 * paramter's search not present
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
	 * @param usageType
	 * @param adminBoundary
	 * @param oldestUpicNo
	 * @return
	 */

	@RequestMapping(value = "_search", method = RequestMethod.POST)
	public PropertyResponse propertySearch(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "active", required = false) Boolean active,
			@RequestParam(value = "upicNumber", required = false) String upicNumber,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) String[] sort,
			@RequestParam(value = "oldUpicNo", required = false) String oldUpicNo,
			@RequestParam(value = "mobileNumber", required = false) String mobileNumber,
			@RequestParam(value = "aadhaarNumber", required = false) String aadhaarNumber,
			@RequestParam(value = "houseNoBldgApt", required = false) String houseNoBldgApt,
			@RequestParam(value = "revenueZone", required = false) Integer revenueZone,
			@RequestParam(value = "revenueWard", required = false) Integer revenueWard,
			@RequestParam(value = "locality", required = false) Integer locality,
			@RequestParam(value = "ownerName", required = false) String ownerName,
			@RequestParam(value = "demandFrom", required = false) Double demandFrom,
			@RequestParam(value = "demandTo", required = false) Double demandTo,
			@RequestParam(value = "propertyId", required = false) String propertyId,
			@RequestParam(value = "applicationNo", required = false) String applicationNo,
			@RequestParam(value = "usageType",required=false) String usage,
			@RequestParam(value = "adminBoundary",required=false) Integer adminBoundary,
			@RequestParam(value = "oldestUpicNo",required=false) String oldestUpicNo) throws Exception {

		return propertyService.searchProperty(requestInfo.getRequestInfo(), tenantId, active, upicNumber, pageSize,
				pageNumber, sort, oldUpicNo, mobileNumber, aadhaarNumber, houseNoBldgApt, revenueZone, revenueWard,
				locality, ownerName, demandFrom, demandTo, propertyId, applicationNo,usage,adminBoundary,oldestUpicNo);

	}

	/**
	 * Description: This api for creating title transfer request for property
	 * 
	 * @param titleTransferRequest
	 * @return titleTransferResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "transfer/_create", method = RequestMethod.POST)
	public TitleTransferResponse createTitleTransfer(@RequestBody TitleTransferRequest titleTransferRequest)
			throws Exception {

		return propertyService.createTitleTransfer(titleTransferRequest);
	}

	/**
	 * 
	 * @param titleTransferRequest
	 * @return titleTransferResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "transfer/_update", method = RequestMethod.POST)
	public TitleTransferResponse updateTitleTransfer(@RequestBody TitleTransferRequest titleTransferRequest)
			throws Exception {

		return propertyService.updateTitleTransfer(titleTransferRequest);
	}

	/**
	 * 
	 * @param specialNoticeRequest
	 * @return {@link SpecialNoticeResponse}
	 */
	@RequestMapping(path = "/specialnotice/_generate", method = RequestMethod.POST)
	public SpecialNoticeResponse generateSpecialNotice(@RequestBody SpecialNoticeRequest specialNoticeRequest)
			throws Exception {

		return propertyService.generateSpecialNotice(specialNoticeRequest);
	}

	/**
	 * API is for Add/Edit DCB feature
	 * @param requestInfo
	 * @param tenantId
	 * @param upicNumber
	 * @return demandResponse
	 * @throws Exception
	 */
	@RequestMapping(value = "_preparedcb", method = RequestMethod.POST)
	public DemandResponse prepareDCB(@RequestBody RequestInfoWrapper requestInfo,
									 @RequestParam(value = "tenantId", required = true) String tenantId,
									 @RequestParam(value = "upicNumber", required = true) String upicNumber)
			throws Exception {

		return propertyService.getDemandsForProperty(requestInfo, tenantId, upicNumber);
	}
	
	/**
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
	 */
	@RequestMapping(path="transfer/_search",method=RequestMethod.POST)
	public TitleTransferSearchResponse searchTitleTransfer(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(value="tenantId",required=true) String tenantId,
			@RequestParam(value="pageSize",required=false) Integer pageSize,
			@RequestParam(value="pageNumber",required=false) Integer pageNumber,
			@RequestParam(value="sort",required=false)  String[] sort,
			@RequestParam(value="upicNo",required=false) String upicNo,
			@RequestParam(value="oldUpicNo",required=false) String oldUpicNo,
			@RequestParam(value="applicationNo",required=false) String applicationNo) throws Exception{
		return propertyService.searchTitleTransfer(requestInfo, tenantId, pageSize, pageNumber, sort, upicNo, oldUpicNo, applicationNo);
	}

	@RequestMapping(path = "_updatedcb", method = RequestMethod.POST)
	public PropertyDCBResponse updateDemand(@RequestParam String tenantId,
										  @RequestBody PropertyDCBRequest propertyDCBRequest) throws  Exception{

	 return propertyService.updateDcbDemand(propertyDCBRequest, tenantId);
	}

	/**
	 * This API will modify the existing property & will update the demand
	 * details
	 * 
	 * @param propertyRequest
	 * @return {@link PropertyResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "/_modify", method = RequestMethod.POST)
	public PropertyResponse modifyProperty(@RequestBody PropertyRequest propertyRequest) throws Exception {

		return propertyService.modifyProperty(propertyRequest);

	}

	@RequestMapping(path = "/notice/_create", method = RequestMethod.POST)
	public NoticeResponse createNotice(@Valid @RequestBody NoticeRequest noticeRequest) throws Exception{
		noticeService.pushToQueue(noticeRequest);

		return new NoticeResponse(new ResponseInfo(),noticeRequest.getNotice());
	}
}

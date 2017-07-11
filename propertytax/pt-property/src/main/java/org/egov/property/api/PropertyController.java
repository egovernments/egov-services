package org.egov.property.api;

import javax.validation.Valid;

import org.egov.models.PropertyRequest;
import org.egov.models.PropertyResponse;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.TitleTransfer;
import org.egov.models.TitleTransferRequest;
import org.egov.models.TitleTransferResponse;
import org.egov.property.services.PropertyService;
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

	@Autowired
	PropertyService propertyService;


	/**
	 * Description: this api will use for creating property
	 * 
	 * @param propertyRequest
	 * @return PropertyResponse
	 */

	@RequestMapping(method = RequestMethod.POST, path = "_create")
	public PropertyResponse createProperty(@Valid @RequestBody PropertyRequest propertyRequest) {

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
	 * @return
	 */

	@RequestMapping(value = "_search", method = RequestMethod.POST)
	public PropertyResponse propertySearch(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "active", required = false) Boolean active,
			@RequestParam(value = "upicNo", required = false) String upicNo,
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
			@RequestParam(value = "demandFrom", required = false) Integer demandFrom,
			@RequestParam(value = "demandTo", required = false) Integer demandTo) {
		if (pageSize == null)
			pageSize = -1;

		if (pageNumber == null)
			pageNumber = -1;

		if (revenueZone == null)
			revenueZone = -1;

		if (revenueWard == null)
			revenueWard = -1;

		if (locality == null)
			locality = -1;

		if (demandFrom == null)
			demandFrom = -1;

		if (demandTo == null)
			demandTo = -1;

		return propertyService.searchProperty(requestInfo.getRequestInfo(), tenantId, active, upicNo, pageSize,
				pageNumber, sort, oldUpicNo, mobileNumber, aadhaarNumber, houseNoBldgApt, revenueZone, revenueWard,
				locality, ownerName, demandFrom, demandTo);

	}
	
	
	/**
	 * Description: This api for creating title transfer request for property
	 * @param titleTransferRequest
	 * @return titleTransferResponse
	 * @throws Exception
	 */
	@RequestMapping(path="transfer/_create",method=RequestMethod.POST)
	public TitleTransferResponse createTitleTransfer(@RequestBody TitleTransferRequest titleTransferRequest) throws Exception{
	    
	    return propertyService.createTitleTransfer(titleTransferRequest);
	}

}

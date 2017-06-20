package org.egov.property.api;

import javax.validation.Valid;

import org.egov.models.PropertyRequest;
import org.egov.models.PropertyResponse;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.ResponseInfoFactory;
import org.egov.property.propertyConsumer.Producer;
import org.egov.property.services.PersisterService;
import org.egov.property.services.PropertySearchService;
import org.egov.property.util.PropertyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
/**
 * Property Controller have the api's related to property
 * @author Narendra
 */
@RestController
@RequestMapping(path="/properties/")
public class PropertyController {

	@Autowired
	PropertyValidator propertyValidator;
	//TODO this is unsed variable, please do not declare/create unused variable
	@Autowired
	Producer producer;

	@Autowired
	Environment environment;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@Autowired
	PropertySearchService propertySearchService;

	@Autowired
	PersisterService persisterService;




	@RequestMapping(method=RequestMethod.POST,path="_create")
	public PropertyResponse createProperty(@Valid @RequestBody PropertyRequest propertyRequest){
		//TODO how we are communicating back with errors in case of validation failure? as per yml we need to send back the error codes
		return  propertyValidator.createProperty(propertyRequest);
	}


	/**
	 * updateProperty method validate each property before update
	 * @param PropertyRequest
	 * */
	@RequestMapping(method=RequestMethod.POST,path="_update")
	public PropertyResponse updateProperty(@Valid @RequestBody PropertyRequest propertyRequest) {
		//TODO how we are communicating back with errors in case of validation failure? as per yml we need to send back the error codes
		return propertyValidator.updateProperty(propertyRequest);
	}

	@RequestMapping(value="_search",method=RequestMethod.POST)
	public PropertyResponse propertySearch(
			@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(value="tenantId",required=true) String tenantId,
			@RequestParam(value="active",required=false) Boolean active ,
			@RequestParam(value ="upicNo",required=false) String upicNo,
			@RequestParam(value ="pageSize",required=false)Integer pageSize,
			@RequestParam(value="pageNumber",required=false)Integer pageNumber,
			@RequestParam (value="sort",required=false) String[] sort,
			@RequestParam(value="oldUpicNo",required=false) String oldUpicNo,
			@RequestParam(value="mobileNumber",required=false) String mobileNumber,
			@RequestParam(value="aadhaarNumber",required=false)String aadhaarNumber,
			@RequestParam(value="houseNoBldgApt",required=false)String houseNoBldgApt,
			@RequestParam(value="revenueZone",required=false) Integer revenueZone,
			@RequestParam(value="revenueWard",required=false) Integer revenueWard,
			@RequestParam(value="locality",required=false)Integer locality,
			@RequestParam(value="ownerName",required=false)String ownerName,
			@RequestParam(value="demandFrom",required=false)Integer demandFrom,
			@RequestParam(value="demandTo",required=false)Integer demandTo){
		if (pageSize ==null)
			pageSize=-1;

		if (pageNumber == null)
			pageNumber =-1;

		if ( revenueZone == null)
			revenueZone=-1;

		if ( revenueWard == null)
			revenueWard = -1;

		if ( locality == null)
			locality =-1;

		if ( demandFrom == null)
			demandFrom = -1;

		if ( demandTo == null)
			demandTo = -1;

		return propertySearchService.searchProperty(requestInfo.getRequestInfo(),
				tenantId, 
				active, 
				upicNo, 
				pageSize, 
				pageNumber,
				sort, 
				oldUpicNo, 
				mobileNumber,
				aadhaarNumber,
				houseNoBldgApt, 
				revenueZone, 
				revenueWard, 
				locality,
				ownerName, 
				demandFrom,
				demandTo);

	}

}

package org.egov.property.api;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.egov.enums.NoticeType;
import org.egov.models.DemandResponse;
import org.egov.models.DemolitionRequest;
import org.egov.models.DemolitionResponse;
import org.egov.models.DemolitionSearchCriteria;
import org.egov.models.DemolitionSearchResponse;
import org.egov.models.Error;
import org.egov.models.ErrorRes;
import org.egov.models.NoticeRequest;
import org.egov.models.NoticeResponse;
import org.egov.models.NoticeSearchCriteria;
import org.egov.models.NoticeSearchResponse;
import org.egov.models.PropertyDCBRequest;
import org.egov.models.PropertyDCBResponse;
import org.egov.models.PropertyRequest;
import org.egov.models.PropertyResponse;
import org.egov.models.PropertySearchCriteria;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.ResponseInfo;
import org.egov.models.SpecialNoticeRequest;
import org.egov.models.SpecialNoticeResponse;
import org.egov.models.TaxExemptionRequest;
import org.egov.models.TaxExemptionResponse;
import org.egov.models.TaxExemptionSearchCriteria;
import org.egov.models.TaxExemptionSearchResponse;
import org.egov.models.TitleTransferRequest;
import org.egov.models.TitleTransferResponse;
import org.egov.models.TitleTransferSearchCriteria;
import org.egov.models.VacancyRemissionRequest;
import org.egov.models.VacancyRemissionResponse;
import org.egov.models.VacancyRemissionSearchCriteria;
import org.egov.models.VacancyRemissionSearchResponse;
import org.egov.property.exception.InvalidSearchParameterException;
import org.egov.property.model.TitleTransferSearchResponse;
import org.egov.property.services.NoticeService;
import org.egov.property.services.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	 * @throws Exception
	 */

	@RequestMapping(method = RequestMethod.POST, path = "_create")
	public PropertyResponse createProperty(@Valid @RequestBody PropertyRequest propertyRequest) throws Exception {
		logger.info("PropertyController    PropertyRequest ---->> " + propertyRequest);
		return propertyService.createProperty(propertyRequest);

	}

	/**
	 * updateProperty method validate each property before update
	 * 
	 * @param PropertyRequest
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, path = "_update")
	public PropertyResponse updateProperty(@Valid @RequestBody PropertyRequest propertyRequest) throws Exception {
		return propertyService.updateProperty(propertyRequest);

	}

	/**
	 * This api for searching property based on input
	 * parameters.demandTo,demandFrom,houseNoBldgApt,revenueZone,revenueWard
	 * paramter's search not present
	 * 
	 * @param requestInfo
	 * @param PropertySearchCriteria
	 * @return PropertyResponse
	 */
	@RequestMapping(value = "_search", method = RequestMethod.POST)
	public PropertyResponse propertySearch(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid PropertySearchCriteria propertySearchCriteria, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}
		return propertyService.searchProperty(requestInfo.getRequestInfo(), propertySearchCriteria);
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
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param upicNumber
	 * @return demandResponse
	 * @throws Exception
	 */
	@RequestMapping(value = "_preparedcb", method = RequestMethod.POST)
	public DemandResponse prepareDCB(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "upicNumber", required = true) String upicNumber) throws Exception {

		return propertyService.getDemandsForProperty(requestInfo, tenantId, upicNumber);
	}

	/**
	 * 
	 * @param requestInfo
	 * @param TitleTransferSearchCriteria
	 * @return {@link TitleTransferResponse}
	 */
	@RequestMapping(path = "transfer/_search", method = RequestMethod.POST)
	public TitleTransferSearchResponse searchTitleTransfer(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid TitleTransferSearchCriteria titleTransferSearchCriteria, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}
		return propertyService.searchTitleTransfer(requestInfo, titleTransferSearchCriteria);
	}

	@RequestMapping(path = "_updatedcb", method = RequestMethod.POST)
	public PropertyDCBResponse updateDemand(@RequestParam String tenantId,
			@RequestBody PropertyDCBRequest propertyDCBRequest) throws Exception {

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

	@RequestMapping(path = "notice/_create", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> createNotice(@RequestBody @Valid NoticeRequest noticeRequest, final BindingResult errors)
			throws Exception {

		if (errors.hasErrors()) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), errors.getFieldError().toString(), null,
					new HashMap<String, String>());
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setStatus("FAILED");
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			ErrorRes errorRes = new ErrorRes(responseInfo, errorList);
			return new ResponseEntity<>(errorRes, HttpStatus.BAD_REQUEST);
		}
		noticeService.pushToQueue(noticeRequest);

		NoticeResponse noticeResponse = new NoticeResponse(new ResponseInfo(), noticeRequest.getNotice());
		return new ResponseEntity<>(noticeResponse, HttpStatus.OK);
	}

	@RequestMapping(path = "notice/_update", method = RequestMethod.POST)
	public ResponseEntity<?> updateNotice(@RequestBody @Valid NoticeRequest noticeRequest, final BindingResult errors)
			throws Exception {
		if (errors.hasErrors()) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), errors.getFieldError().toString(), null,
					new HashMap<String, String>());
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setStatus("FAILED");
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			ErrorRes errorRes = new ErrorRes(responseInfo, errorList);
			return new ResponseEntity<>(errorRes, HttpStatus.BAD_REQUEST);
		}

		NoticeResponse noticeResponse = new NoticeResponse(new ResponseInfo(), noticeRequest.getNotice());
		return new ResponseEntity<>(noticeResponse, HttpStatus.OK);
	}

	@RequestMapping(path = "notice/_search", method = RequestMethod.POST)
	public NoticeSearchResponse searchNotice(@RequestParam(value = "tenantId") String tenantId,
			@RequestParam(value = "upicNumber", required = false) String upicNumber,
			@RequestParam(value = "applicationNo", required = false) String applicationNo,
			@RequestParam(value = "noticeType") NoticeType noticeType,
			@RequestParam(value = "noticeDate", required = false) String noticeDate,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber) throws Exception {

		NoticeSearchCriteria searchCriteria = NoticeSearchCriteria.builder().tenantId(tenantId).upicNumber(upicNumber)
				.applicationNo(applicationNo).noticeType(noticeType).noticeDate(noticeDate)
				.fromDate(isEmpty(fromDate) ? null : Long.valueOf(fromDate))
				.toDate(isEmpty(toDate) ? null : Long.valueOf(toDate)).pageSize(pageSize).pageNumber(pageNumber)
				.build();

		List notices = noticeService.search(searchCriteria);

		return NoticeSearchResponse.builder().responseInfo(new ResponseInfo()).notices(notices).build();
	}

	/**
	 * This API will create a new demolition
	 * 
	 * @param demolitionRequest
	 * @return {@link DemolitionResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "demolition/_create", method = RequestMethod.POST)
	public DemolitionResponse createDemolition(@RequestBody DemolitionRequest demolitionRequest) throws Exception {
		return propertyService.createDemolition(demolitionRequest);

	}

	/**
	 * This API will update the Existisng demolition
	 * 
	 * @param demolitionRequest
	 * @return {@link DemolitionResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "demolition/_update", method = RequestMethod.POST)
	public DemolitionResponse updateDemolition(@RequestBody DemolitionRequest demolitionRequest) throws Exception {
		return propertyService.updateDemolition(demolitionRequest);

	}

	/**
	 * This API will search the demolitions based on the given parameters
	 * 
	 * @param requestInfoWrapper
	 * @param demolitionSearchCriteria
	 * @param bindingResult
	 * @return {@link DemolitionResponse}
	 * @throws Exception
	 */
	@RequestMapping(path = "demolition/_search", method = RequestMethod.POST)
	public DemolitionSearchResponse searchDemolitions(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid DemolitionSearchCriteria demolitionSearchCriteria, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfoWrapper.getRequestInfo());
		}
		return propertyService.searchDemolition(requestInfoWrapper.getRequestInfo(), demolitionSearchCriteria);

	}

	/**
	 * Description: This api for creating tax exemption request for property
	 * 
	 * @param taxExemptionRequest
	 * @return taxExemptionResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "taxexemption/_create", method = RequestMethod.POST)
	public TaxExemptionResponse createTitleTransfer(@RequestBody TaxExemptionRequest taxExemptionRequest)
			throws Exception {

		return propertyService.createTaxExemption(taxExemptionRequest);
	}

	/**
	 *
	 * @param taxExemptionRequest
	 * @return taxExemptionResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "taxexemption/_update", method = RequestMethod.POST)
	public TaxExemptionResponse updateTitleTransfer(@RequestBody TaxExemptionRequest taxExemptionRequest)
			throws Exception {

		return propertyService.updateTaxExemption(taxExemptionRequest);
	}

	/**
	 * 
	 * @param requestInfo
	 * @param TaxExemptionSearchCriteria
	 * @return {@link TaxExemptionSearchResponse}
	 */
	@RequestMapping(path = "taxexemption/_search", method = RequestMethod.POST)
	public TaxExemptionSearchResponse searchTaxExemption(@RequestBody RequestInfoWrapper requestInfo,
			@ModelAttribute @Valid TaxExemptionSearchCriteria taxExemptionSearchCriteria, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfo.getRequestInfo());
		}
		return propertyService.searchTaxExemption(requestInfo, taxExemptionSearchCriteria);
	}
	
	/**
	 * Description : This will create Vacancy Remission
	 * 
	 * @param vacancyRemissionRequest
	 * @return VacancyRemissionResponse
	 * @throws Exception
	 */
	@RequestMapping(value = "/vacancyremission/_create", method = RequestMethod.POST)
	public VacancyRemissionResponse createVacancyRemission(
			@Valid @RequestBody VacancyRemissionRequest vacancyRemissionRequest) throws Exception {
		return propertyService.createVacancyRemission(vacancyRemissionRequest);
	}
	
	/**
	 * Description : This will update Vacancy Remission
	 * 
	 * @param vacancyRemissionRequest
	 * @return VacancyRemissionResponse
	 * @throws Exception
	 */
	@RequestMapping(value = "/vacancyremission/_update", method = RequestMethod.POST)
	public VacancyRemissionResponse updateVacancyRemission(
			@Valid @RequestBody VacancyRemissionRequest vacancyRemissionRequest) throws Exception {
		return propertyService.updateVacancyRemission(vacancyRemissionRequest);
	}
	
	/**
	 * @param upicNumber
	 * @param VacancyRemissionSearchCriteria
	 * @return VacancyRemissionSearchResponse
	 * @throws Exception
	 */
	@RequestMapping(value = "/vacancyremission/_search", method = RequestMethod.POST)
	public VacancyRemissionSearchResponse searchVacancyRemission(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid VacancyRemissionSearchCriteria vacancyRemissionSearchCriteria,
			BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			throw new InvalidSearchParameterException(bindingResult, requestInfoWrapper.getRequestInfo());
		}
		return propertyService.searchVacancyRemission(requestInfoWrapper, vacancyRemissionSearchCriteria);
	}

}

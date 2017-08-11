package org.egov.tradelicense.domain.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.tl.commons.web.requests.CategoryResponse;
import org.egov.tl.commons.web.requests.DocumentTypeResponse;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.domain.exception.CustomBindException;
import org.egov.tradelicense.common.domain.exception.InvalidInputException;
import org.egov.tradelicense.common.domain.model.RequestInfoWrapper;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.repository.TradeLicenseRepository;
import org.egov.tradelicense.web.contract.TradeLicenseContract;
import org.egov.tradelicense.web.requests.TlMasterRequestInfo;
import org.egov.tradelicense.web.requests.TlMasterRequestInfoWrapper;
import org.egov.tradelicense.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.web.requests.TradeLicenseResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * CategoryService implementation class
 * 
 * @author Shubham Pratap Singh
 *
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class TradeLicenseService {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	TradeLicenseRepository tradeLicenseRepository;

	@Autowired
	private SmartValidator validator;

	private BindingResult validate(List<TradeLicense> tradeLicenses, BindingResult errors) {

		try {
			Assert.notNull(tradeLicenses, "tradeLicenses to create must not be null");
			for (TradeLicense tradeLicense : tradeLicenses) {
				validator.validate(tradeLicense, errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}

		return errors;

	}

	@Transactional
	public List<TradeLicense> add(List<TradeLicense> tradeLicenses, RequestInfo requestInfo, BindingResult errors) {

		validate(tradeLicenses, errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		// external end point validations
		validateTradeLicense(tradeLicenses, requestInfo);
		// setting the id for the license and support document and fee details
		for (TradeLicense license : tradeLicenses) {

			license.setId(tradeLicenseRepository.getNextSequence());
			if (license.getSupportDocuments() != null && license.getSupportDocuments().size() > 0) {
				for (SupportDocument supportDocument : license.getSupportDocuments()) {
					supportDocument.setLicenseId(license.getId());
					supportDocument.setId(tradeLicenseRepository.getSupportDocumentNextSequence());
				}
			}
			if (license.getFeeDetails() != null && license.getFeeDetails().size() > 0) {
				for (LicenseFeeDetail feeDetail : license.getFeeDetails()) {
					feeDetail.setLicenseId(license.getId());
					feeDetail.setId(tradeLicenseRepository.getFeeDetailNextSequence());
				}
			}
		}

		return tradeLicenses;
	}

	public void addToQue(TradeLicenseRequest request) {
		tradeLicenseRepository.add(request);
	}

	@Transactional
	public TradeLicense save(TradeLicense tradeLicense) {
		return tradeLicenseRepository.save(tradeLicense);
	}

	private void validateTradeLicense(List<TradeLicense> tradeLicenses, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		for (TradeLicense tradeLicense : tradeLicenses) {
			validateUniqueLicenseNumber(tradeLicense);
			validateTradeLocality(tradeLicense, requestInfoWrapper);
			validateTradeLocationWard(tradeLicense, requestInfoWrapper);
			validateTradeCategory(tradeLicense, requestInfoWrapper);
			validateTradeSubCategory(tradeLicense, requestInfoWrapper);
			validateTradeSupportingDocument(tradeLicense, requestInfoWrapper);
		}
	}

	private void validateUniqueLicenseNumber(TradeLicense tradeLicense) {
		
		String sql = getUniqueTenantLicenseQuery(tradeLicense);
		Integer count = null;
		try {
			count = (Integer) jdbcTemplate.queryForObject(sql, Integer.class);
		} catch (Exception e) {
			log.error("error while executing the query :"+ sql + " , error message : " +  e.getMessage());
		}

		if (count != 0) {
			throw new InvalidInputException("tradeLicense number already exists");
		}
	}

	private String getUniqueTenantLicenseQuery(TradeLicense tradeLicense) {
		
		String tenantId = tradeLicense.getTenantId();
				String licNumber = tradeLicense.getLicenseNumber();
		
		StringBuffer uniqueQuery = new StringBuffer("select count(*) from egtl_license");
		uniqueQuery.append(" where licenseNumber = '" + licNumber + "'");
		uniqueQuery.append(" AND tenantId = '" + tenantId + "'");
		
		return uniqueQuery.toString();
	}
	
	private void validateTradeSupportingDocument(TradeLicense tradeLicense, RequestInfoWrapper requestInfoWrapper) {

		for (SupportDocument supportDocument : tradeLicense.getSupportDocuments()) {

			StringBuffer supportingDocumentURI = new StringBuffer();
			TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getTlMasterRequestInfoWrapper(requestInfoWrapper);
			supportingDocumentURI.append(propertiesManager.getTradeLicenseMasterServiceHostName())
					.append(propertiesManager.getTradeLicenseMasterServiceBasePath())
					.append(propertiesManager.getDocumentServiceSearchPath());

			URI uri = UriComponentsBuilder.fromUriString(supportingDocumentURI.toString())
					.queryParam("tenantId", tradeLicense.getTenantId())
					.queryParam("ids", Long.valueOf(supportDocument.getDocumentTypeId())).build(true).encode().toUri();

			DocumentTypeResponse documentTypeResponse = null;
			try {

				documentTypeResponse = restTemplate.postForObject(uri, tlMasterRequestInfoWrapper,
						DocumentTypeResponse.class);

			} catch (Exception e) {
				log.error("Error while connecting to the document type end point");
			}
			if (documentTypeResponse == null || documentTypeResponse.getDocumentTypes().size() == 0) {
				throw new InvalidInputException("Invalid document type ");
			}
		}
	}

	private void validateTradeSubCategory(TradeLicense tradeLicense, RequestInfoWrapper requestInfoWrapper) {

		Long subCategoryId = tradeLicense.getSubCategoryId();
		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getTlMasterRequestInfoWrapper(requestInfoWrapper);
		StringBuffer supportingDocumentURI = new StringBuffer();
		supportingDocumentURI.append(propertiesManager.getTradeLicenseMasterServiceHostName())
				.append(propertiesManager.getTradeLicenseMasterServiceBasePath())
				.append(propertiesManager.getCategoryServiceSearchPath());

		URI uri = UriComponentsBuilder.fromUriString(supportingDocumentURI.toString())
				.queryParam("tenantId", tradeLicense.getTenantId()).queryParam("ids", Long.valueOf(subCategoryId))
				.queryParam("type", "subCategory").build(true).encode().toUri();

		CategoryResponse categoryResponse = null;
		try {

			categoryResponse = restTemplate.postForObject(uri, tlMasterRequestInfoWrapper, CategoryResponse.class);

		} catch (Exception e) {
			log.error("Error while connecting to the sub category end point");
		}
		if (categoryResponse == null || categoryResponse.getCategories().size() == 0) {
			throw new InvalidInputException("Invalid sub category type ");
		}
	}

	private void validateTradeCategory(TradeLicense tradeLicense, RequestInfoWrapper requestInfoWrapper) {

		Long categoryId = tradeLicense.getCategoryId();
		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getTlMasterRequestInfoWrapper(requestInfoWrapper);
		StringBuffer supportingDocumentURI = new StringBuffer();
		supportingDocumentURI.append(propertiesManager.getTradeLicenseMasterServiceHostName())
				.append(propertiesManager.getTradeLicenseMasterServiceBasePath())
				.append(propertiesManager.getCategoryServiceSearchPath());

		URI uri = UriComponentsBuilder.fromUriString(supportingDocumentURI.toString())
				.queryParam("tenantId", tradeLicense.getTenantId()).queryParam("ids", Long.valueOf(categoryId))
				.build(true).encode().toUri();

		CategoryResponse categoryResponse = null;
		try {

			categoryResponse = restTemplate.postForObject(uri, tlMasterRequestInfoWrapper, CategoryResponse.class);

		} catch (Exception e) {
			log.error("Error while connecting to the category end point");
		}
		if (categoryResponse == null || categoryResponse.getCategories().size() == 0) {
			throw new InvalidInputException("Invalid category type ");
		}
	}

	private void validateTradeLocationWard(TradeLicense tradeLicense, RequestInfoWrapper requestInfoWrapper) {

		Integer revenueWardId = tradeLicense.getRevenueWardId();

		StringBuffer supportingDocumentURI = new StringBuffer();
		supportingDocumentURI.append(propertiesManager.getLocationServiceHostName())
				.append(propertiesManager.getLocationServiceBasePath())
				.append(propertiesManager.getLocationServiceSearchPath());

		URI uri = UriComponentsBuilder.fromUriString(supportingDocumentURI.toString())
				.queryParam("boundaryIds", revenueWardId).queryParam("tenantId", tradeLicense.getTenantId()).build(true)
				.encode().toUri();

		Object locationObj = null;
		try {

			locationObj = restTemplate.postForObject(uri, requestInfoWrapper, Object.class);

		} catch (Exception e) {
			log.error("Error while connecting to the location end point");
		}
		if (locationObj == null) {
			throw new InvalidInputException("Invalid location ward ");
		}
	}

	private void validateTradeLocality(TradeLicense tradeLicense, RequestInfoWrapper requestInfoWrapper) {

		Integer localityId = tradeLicense.getLocalityId();

		StringBuffer supportingDocumentURI = new StringBuffer();
		supportingDocumentURI.append(propertiesManager.getLocationServiceHostName())
				.append(propertiesManager.getLocationServiceBasePath())
				.append(propertiesManager.getLocationServiceSearchPath());

		URI uri = UriComponentsBuilder.fromUriString(supportingDocumentURI.toString())
				.queryParam("boundaryIds", localityId).queryParam("tenantId", tradeLicense.getTenantId()).build(true)
				.encode().toUri();

		Object locationObj = null;

		try {

			locationObj = restTemplate.postForObject(uri, requestInfoWrapper, Object.class);

		} catch (Exception e) {
			log.error("Error while connecting to the location ward end point");
		}
		if (locationObj == null) {
			throw new InvalidInputException("Invalid location ");
		}
	}

	public TradeLicenseResponse getTradeLicense(RequestInfo requestInfo, String tenantId, Integer pageSize,
			Integer pageNumber, String sort, String active, String tradeLicenseId, String applicationNumber,
			String licenseNumber, String oldLicenseNumber, String mobileNumber, String aadhaarNumber, String eamilId,
			String propertyAssesmentNo, Integer revenueWard, Integer locality, String ownerName, String tradeTitle,
			String tradeType, Integer tradeCategory, Integer tradeSubCategory) {

		ResponseInfo responseInfo = new ResponseInfo();
		List<TradeLicense> tradeLicenses = new ArrayList<TradeLicense>();
		TradeLicense tradeLicense = new TradeLicense();
		tradeLicense.setActive(true);
		tradeLicense.setId(1l);
		tradeLicenses.add(tradeLicense);
		TradeLicenseResponse TradeLicenseResponse = new TradeLicenseResponse();
		// TradeLicenseResponse.setLicenses(tradeLicenses);
		TradeLicenseResponse.setResponseInfo(responseInfo);

		return TradeLicenseResponse;
	}

	public TradeLicenseResponse getTradeLicense(RequestInfo requestInfo, String tenantId, Integer pageSize,
			Integer pageNumber, String sort, String active, String tradeLicenseId, String applicationNumber,
			String licenseNumber, String mobileNumber, String aadhaarNumber, String emailId, String propertyAssesmentNo,
			Integer revenueWard, Integer locality, String ownerName, String tradeTitle, String tradeType,
			Integer tradeCategory, Integer tradeSubCategory, String legacy, Integer status) {

		List<TradeLicense> licenses = tradeLicenseRepository.search(tenantId, pageSize, pageNumber, sort, active,
				tradeLicenseId, applicationNumber, licenseNumber, mobileNumber, aadhaarNumber, emailId,
				propertyAssesmentNo, revenueWard, locality, ownerName, tradeTitle, tradeType, tradeCategory,
				tradeSubCategory, legacy, status);

		List<TradeLicenseContract> tradeLicenseContracts = new ArrayList<TradeLicenseContract>();

		ModelMapper model = new ModelMapper();

		for (TradeLicense license : licenses) {
			TradeLicenseContract tradeContract = new TradeLicenseContract();
			model.map(license, tradeContract);
			tradeLicenseContracts.add(tradeContract);
		}
		TradeLicenseResponse TradeLicenseResponse = new TradeLicenseResponse();
		TradeLicenseResponse.setResponseInfo(createResponseInfoFromRequestInfo(requestInfo, true));
		TradeLicenseResponse.setLicenses(tradeLicenseContracts);

		return TradeLicenseResponse;
	}

	public TlMasterRequestInfoWrapper getTlMasterRequestInfoWrapper(RequestInfoWrapper requestInfoWrapper) {

		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = new TlMasterRequestInfoWrapper();
		TlMasterRequestInfo tlMasterRequestInfo = new TlMasterRequestInfo();
		ModelMapper mapper = new ModelMapper();
		mapper.map(requestInfoWrapper.getRequestInfo(), tlMasterRequestInfo);
		tlMasterRequestInfo.setTs(12345l);
		tlMasterRequestInfoWrapper.setRequestInfo(tlMasterRequestInfo);

		return tlMasterRequestInfoWrapper;
	}

	public ResponseInfo createResponseInfoFromRequestInfo(RequestInfo requestInfo, Boolean success) {
		ResponseInfo responseInfo = new ResponseInfo();
		String apiId = requestInfo.getApiId();
		responseInfo.setApiId(apiId);
		String ver = requestInfo.getVer();
		responseInfo.setVer(ver);
		String ts = null;
		if (requestInfo.getTs() != null)
			ts = requestInfo.getTs().toString();
		responseInfo.setTs(ts);
		String resMsgId = "uief87324";
		responseInfo.setResMsgId(resMsgId);
		String msgId = requestInfo.getMsgId();
		responseInfo.setMsgId(msgId);
		String responseStatus = (success.booleanValue()) ? "SUCCESSFUL" : "FAILED";
		responseInfo.setStatus(responseStatus);
		return responseInfo;
	}

}
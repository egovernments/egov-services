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
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.repository.TradeLicenseRepository;
import org.egov.tradelicense.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.web.requests.TradeLicenseResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
		//validateTradeLicense(tradeLicenses, requestInfo);
		for (TradeLicense license : tradeLicenses) {
			license.setId(tradeLicenseRepository.getNextSequence());
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

		for (TradeLicense tradeLicense : tradeLicenses) {
			validateTradeLocality(tradeLicense, requestInfo);
			validateTradeLocationWard(tradeLicense, requestInfo);
			validateTradeCategory(tradeLicense, requestInfo);
			validateTradeSubCategory(tradeLicense, requestInfo);
			validateTradeSupportingDocument(tradeLicense, requestInfo);
		}
	}

	private void validateTradeSupportingDocument(TradeLicense tradeLicense, RequestInfo requestInfo) {

		for (SupportDocument supportDocument : tradeLicense.getSupportDocuments()) {

			StringBuffer supportingDocumentURI = new StringBuffer();
			supportingDocumentURI.append(propertiesManager.getTradeLicenseMasterServiceHostName())
					.append(propertiesManager.getTradeLicenseMasterServiceBasePath())
					.append(propertiesManager.getDocumentServiceSearchPath());

			URI uri = UriComponentsBuilder.fromUriString(supportingDocumentURI.toString())
					.queryParam("tenantId", tradeLicense.getTenantId())
					.queryParam("ids", Long.valueOf(supportDocument.getDocumentTypeId())).build(true).encode().toUri();

			DocumentTypeResponse documentTypeResponse = null;
			try {

				documentTypeResponse = restTemplate.postForObject(uri, requestInfo, DocumentTypeResponse.class);

			} catch (Exception e) {
				log.error("Error while connecting to the document type end point");
			}
			if (documentTypeResponse == null || documentTypeResponse.getDocumentTypes().size() == 0) {
				throw new InvalidInputException("Invalid document type ");
			}
		}
	}

	private void validateTradeSubCategory(TradeLicense tradeLicense, RequestInfo requestInfo) {

		Long subCategoryId = tradeLicense.getSubCategoryId();

		StringBuffer supportingDocumentURI = new StringBuffer();
		supportingDocumentURI.append(propertiesManager.getTradeLicenseMasterServiceHostName())
				.append(propertiesManager.getTradeLicenseMasterServiceBasePath())
				.append(propertiesManager.getCategoryServiceSearchPath());

		URI uri = UriComponentsBuilder.fromUriString(supportingDocumentURI.toString())
				.queryParam("tenantId", tradeLicense.getTenantId()).queryParam("ids", Long.valueOf(subCategoryId))
				.build(true).encode().toUri();

		CategoryResponse categoryResponse = null;
		try {

			categoryResponse = restTemplate.postForObject(uri, requestInfo, CategoryResponse.class);

		} catch (Exception e) {
			log.error("Error while connecting to the category end point");
		}
		if (categoryResponse == null || categoryResponse.getCategories().size() == 0) {
			throw new InvalidInputException("Invalid sub category type ");
		}
	}

	private void validateTradeCategory(TradeLicense tradeLicense, RequestInfo requestInfo) {

		Long categoryId = tradeLicense.getCategoryId();

		StringBuffer supportingDocumentURI = new StringBuffer();
		supportingDocumentURI.append(propertiesManager.getTradeLicenseMasterServiceHostName())
				.append(propertiesManager.getTradeLicenseMasterServiceBasePath())
				.append(propertiesManager.getCategoryServiceSearchPath());

		URI uri = UriComponentsBuilder.fromUriString(supportingDocumentURI.toString())
				.queryParam("tenantId", tradeLicense.getTenantId()).queryParam("id", Long.valueOf(categoryId))
				.build(true).encode().toUri();

		CategoryResponse categoryResponse = null;
		try {

			categoryResponse = restTemplate.postForObject(uri, requestInfo, CategoryResponse.class);

		} catch (Exception e) {
			log.error("Error while connecting to the sub category end point");
		}
		if (categoryResponse == null || categoryResponse.getCategories().size() == 0) {
			throw new InvalidInputException("Invalid category type ");
		}
	}

	private void validateTradeLocationWard(TradeLicense tradeLicense, RequestInfo requestInfo) {

		Integer revenueWardId = tradeLicense.getRevenueWardId();

		StringBuffer supportingDocumentURI = new StringBuffer();
		supportingDocumentURI.append(propertiesManager.getLocationServiceHostName())
				.append(propertiesManager.getLocationServiceBasePath())
				.append(propertiesManager.getLocationServiceSearchPath());

		URI uri = UriComponentsBuilder.fromUriString(supportingDocumentURI.toString())
				.queryParam("boundaryTypeName", "WARD").queryParam("hierarchyTypeName", "REVENUE")
				.queryParam("tenantId", tradeLicense.getTenantId()).build(true).encode().toUri();

		Object locationObj = null;
		try {

			locationObj = restTemplate.postForObject(uri, requestInfo, Object.class);

		} catch (Exception e) {
			log.error("Error while connecting to the location end point");
		}
		if(locationObj == null){
			throw new InvalidInputException("Invalid location ward ");
		}
	}

	private void validateTradeLocality(TradeLicense tradeLicense, RequestInfo requestInfo) {

		Integer wardId = tradeLicense.getLocalityId();

		StringBuffer supportingDocumentURI = new StringBuffer();
		supportingDocumentURI.append(propertiesManager.getLocationServiceHostName())
				.append(propertiesManager.getLocationServiceBasePath())
				.append(propertiesManager.getLocationServiceSearchPath());

		URI uri = UriComponentsBuilder.fromUriString(supportingDocumentURI.toString())
				.queryParam("boundaryTypeName", "LOCALITY").queryParam("hierarchyTypeName", "REVENUE")
				.queryParam("tenantId", tradeLicense.getTenantId()).build(true).encode().toUri();

		Object locationObj = null;
		try {

			locationObj = restTemplate.postForObject(uri, requestInfo, Object.class);

		} catch (Exception e) {
			log.error("Error while connecting to the location ward end point");
		}
		if(locationObj == null){
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

}
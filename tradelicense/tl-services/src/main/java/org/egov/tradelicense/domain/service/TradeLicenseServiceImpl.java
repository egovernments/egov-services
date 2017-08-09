package org.egov.tradelicense.domain.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.web.requests.TradeLicenseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * CategoryService implementation class
 * 
 * @author Shubham Pratap Singh
 *
 */
@Service
public class TradeLicenseServiceImpl implements TradeLicenseService {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	RestTemplate restTemplate;

	public TradeLicenseResponse createTradeLicense(TradeLicenseRequest tradeLicenseRequest) {

		ResponseInfo responseInfo = new ResponseInfo();
		validateTradeLicenseRequest(tradeLicenseRequest);
		TradeLicenseResponse TradeLicenseResponse = new TradeLicenseResponse();
		TradeLicenseResponse.setLicenses(tradeLicenseRequest.getLicesnses());
		TradeLicenseResponse.setResponseInfo(responseInfo);
		return TradeLicenseResponse;
	}

	private void validateTradeLicenseRequest(TradeLicenseRequest tradeLicenseRequest) {

		RequestInfo requestInfo = tradeLicenseRequest.getRequestInfo();

//		for (TradeLicense tradeLicense : tradeLicenseRequest.getLicesnses()) {
//			validateTradeLocality(tradeLicense, requestInfo);
//			validateTradeLocationWard(tradeLicense, requestInfo);
//			validateTradeCategory(tradeLicense, requestInfo);
//			validateTradeSubCategory(tradeLicense, requestInfo);
//			validateTradeSupportingDocument(tradeLicense, requestInfo);
//		}
	}

	private void validateTradeSupportingDocument(TradeLicense tradeLicense, RequestInfo requestInfo) {

		for (SupportDocument supportDocument : tradeLicense.getSupportDocuments()) {

			StringBuffer supportingDocumentURI = new StringBuffer();
			supportingDocumentURI.append(propertiesManager.getTradeLicenseMasterServiceHostName())
					.append(propertiesManager.getTradeLicenseMasterServiceBasePath())
					.append(propertiesManager.getDocumentServiceSearchPath());

			URI uri = UriComponentsBuilder.fromUriString(supportingDocumentURI.toString())
					.queryParam("id", Long.valueOf(supportDocument.getDocumentTypeId())).build(true).encode().toUri();

			try {

				Object obj = restTemplate.postForObject(uri, requestInfo, Object.class);

			} catch (Exception e) {
				e.printStackTrace();
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
				.queryParam("id", Long.valueOf(subCategoryId)).build(true).encode().toUri();

		try {

			Object obj = restTemplate.postForObject(uri, requestInfo, Object.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void validateTradeCategory(TradeLicense tradeLicense, RequestInfo requestInfo) {

		Long categoryId = tradeLicense.getCategoryId();

		StringBuffer supportingDocumentURI = new StringBuffer();
		supportingDocumentURI.append(propertiesManager.getTradeLicenseMasterServiceHostName())
				.append(propertiesManager.getTradeLicenseMasterServiceBasePath())
				.append(propertiesManager.getCategoryServiceSearchPath());

		URI uri = UriComponentsBuilder.fromUriString(supportingDocumentURI.toString())
				.queryParam("id", Long.valueOf(categoryId)).build(true).encode().toUri();

		try {

			Object obj = restTemplate.postForObject(uri, requestInfo, Object.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void validateTradeLocationWard(TradeLicense tradeLicense, RequestInfo requestInfo) {

		Integer wardId = tradeLicense.getWardId();

		StringBuffer supportingDocumentURI = new StringBuffer();
		supportingDocumentURI.append(propertiesManager.getLocationServiceHostName())
				.append(propertiesManager.getLocationServiceBasePath())
				.append(propertiesManager.getLocationServiceSearchPath());

		URI uri = UriComponentsBuilder.fromUriString(supportingDocumentURI.toString())
				.queryParam("id", Integer.valueOf(wardId)).build(true).encode().toUri();

		try {

			Object obj = restTemplate.postForObject(uri, requestInfo, Object.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void validateTradeLocality(TradeLicense tradeLicense, RequestInfo requestInfo) {

		Integer wardId = tradeLicense.getWardId();

		StringBuffer supportingDocumentURI = new StringBuffer();
		supportingDocumentURI.append(propertiesManager.getLocationServiceHostName())
				.append(propertiesManager.getLocationServiceBasePath())
				.append(propertiesManager.getLocationServiceSearchPath());

		URI uri = UriComponentsBuilder.fromUriString(supportingDocumentURI.toString())
				.queryParam("id", Integer.valueOf(wardId)).build(true).encode().toUri();

		try {

			Object obj = restTemplate.postForObject(uri, requestInfo, Object.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TradeLicenseResponse getTradeLicense(RequestInfo requestInfo,
			String tenantId, Integer pageSize, Integer pageNumber, String sort, String active, String tradeLicenseId,
			String applicationNumber, String licenseNumber, String oldLicenseNumber, String mobileNumber,
			String aadhaarNumber, String eamilId, String propertyAssesmentNo, Integer revenueWard, Integer locality,
			String ownerName, String tradeTitle, String tradeType, Integer tradeCategory, Integer tradeSubCategory) {

		ResponseInfo responseInfo = new ResponseInfo();
		List<TradeLicense> tradeLicenses = new ArrayList<TradeLicense>();
		TradeLicense tradeLicense = new TradeLicense();
		tradeLicense.setActive(true);
		tradeLicense.setId(1l);
		tradeLicenses.add(tradeLicense);
		TradeLicenseResponse TradeLicenseResponse = new TradeLicenseResponse();
		//TradeLicenseResponse.setLicenses(tradeLicenses);
		TradeLicenseResponse.setResponseInfo(responseInfo);

		return TradeLicenseResponse;
	}

}
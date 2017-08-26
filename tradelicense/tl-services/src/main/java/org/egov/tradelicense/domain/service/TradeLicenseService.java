package org.egov.tradelicense.domain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.contract.TradeLicenseSearchContract;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tl.commons.web.response.TradeLicenseSearchResponse;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.domain.exception.CustomBindException;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.model.TradeLicenseSearch;
import org.egov.tradelicense.domain.repository.TradeLicenseRepository;
import org.egov.tradelicense.domain.service.validator.TradeLicenseServiceValidator;
import org.egov.tradelicense.web.repository.BoundaryContractRepository;
import org.egov.tradelicense.web.repository.CategoryContractRepository;
import org.egov.tradelicense.web.repository.DocumentTypeContractRepository;
import org.egov.tradelicense.web.repository.FinancialYearContractRepository;
import org.egov.tradelicense.web.repository.PropertyContractRespository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * CategoryService implementation class
 * 
 * @author Shubham Pratap Singh
 *
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class TradeLicenseService {

	@Autowired
	TradeLicenseServiceValidator tradeLicenseServiceValidator;

	@Autowired
	TradeLicenseRepository tradeLicenseRepository;

	@Autowired
	BoundaryContractRepository boundaryContractRepository;

	@Autowired
	CategoryContractRepository categoryContractRepository;

	@Autowired
	DocumentTypeContractRepository documentTypeContractRepository;

	@Autowired
	PropertyContractRespository propertyContractRepository;

	@Autowired
	TradeLicenseNumberGeneratorService licenseNumberGenerationService;

	@Autowired
	ApplicationNumberGeneratorService applNumberGenrationService;

	@Autowired
	FinancialYearContractRepository financialYearContractRepository;

	@Autowired
	private SmartValidator validator;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

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
			throw new CustomBindException(errors, requestInfo);
		}
		// external end point validations
		tradeLicenseServiceValidator.validateCreateTradeLicenseRelated(tradeLicenses, requestInfo);

		// setting the id for the license and support document and fee details
		for (TradeLicense license : tradeLicenses) {

			license.setId(tradeLicenseRepository.getNextSequence());

			if (!license.getIsLegacy()) {
				// TODO: identify the proper ids for the two status and add it
				// here, for now used 2 and 3
				if (propertiesManager.getApplicatonFeeApplicable().equalsIgnoreCase("Y")) {
					license.setStatus(new Long(2)); // "Pending for Application
													// Fee payment" status id 2
				} else if (propertiesManager.getApplicatonFeeApplicable().equalsIgnoreCase("N")) {
					license.setStatus(new Long(3)); // "Pending for scrutiny"
													// status id 3
				}

			} else {
				license.setStatus(new Long(1)); // Approved status id 1
			}

			if (license.getSupportDocuments() != null && license.getSupportDocuments().size() > 0) {
				
				for (SupportDocument supportDocument : license.getSupportDocuments()) {
					
					supportDocument.setLicenseId(license.getId());
					supportDocument.setId(tradeLicenseRepository.getSupportDocumentNextSequence());
				}
			}
			if (license.getIsLegacy()){
				
				if (license.getFeeDetails() != null && license.getFeeDetails().size() > 0) {
					
					for (LicenseFeeDetail feeDetail : license.getFeeDetails()) {
						
						feeDetail.setLicenseId(license.getId());
						feeDetail.setId(tradeLicenseRepository.getFeeDetailNextSequence());
					}
				}
			} else {
				//clearing the unnecessary details for new trade license
				license.setFeeDetails(new ArrayList<>());
				license.setOldLicenseNumber(null);
				license.setLicenseNumber(null);
			}
			

			if (license.getIsLegacy()) {

				license.setLicenseNumber(licenseNumberGenerationService.generate(license.getTenantId(), requestInfo));
			} else {

				license.setApplicationNumber(applNumberGenrationService.generate(license.getTenantId(), requestInfo));
			}

		}

		return tradeLicenses;
	}

	public void addToQue(TradeLicenseRequest request, Boolean isNewRecord) {
		tradeLicenseRepository.add(request, isNewRecord);
	}

	@Transactional
	public TradeLicense save(TradeLicense tradeLicense) {
		return tradeLicenseRepository.save(tradeLicense);
	}

	@Transactional
	public List<TradeLicense> update(List<TradeLicense> tradeLicenses, RequestInfo requestInfo, BindingResult errors) {

		validate(tradeLicenses, errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors, requestInfo);
		}
		// external end point validations
		tradeLicenseServiceValidator.validateUpdateTradeLicenseRelated(tradeLicenses, requestInfo);

		return tradeLicenses;
	}

	@Transactional
	public TradeLicense update(TradeLicense tradeLicense) {
		return tradeLicenseRepository.update(tradeLicense);
	}

	public TradeLicenseSearchResponse getTradeLicense(RequestInfo requestInfo, String tenantId, Integer pageSize,
			Integer pageNumber, String sort, String active, Integer[] ids, String applicationNumber,
			String licenseNumber, String oldLicenseNumber, String mobileNumber, String aadhaarNumber, String emailId,
			String propertyAssesmentNo, Integer adminWard, Integer locality, String ownerName, String tradeTitle,
			String tradeType, Integer tradeCategory, Integer tradeSubCategory, String legacy, Integer status) {

		TradeLicenseSearchResponse tradeLicenseSearchResponse = new TradeLicenseSearchResponse();
		//
		// tradeLicenseSearchResponse = getLicensesFromEs(tenantId, pageSize,
		// pageNumber, sort, active, tradeLicenseId,
		// applicationNumber, licenseNumber, oldLicenseNumber, mobileNumber,
		// aadhaarNumber, emailId,
		// propertyAssesmentNo, adminWard, locality, ownerName, tradeTitle,
		// tradeType, tradeCategory,
		// tradeSubCategory, legacy, status);
		//
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		//
		// if (tradeLicenseSearchResponse != null &&
		// tradeLicenseSearchResponse.getLicenses() != null
		// && !(tradeLicenseSearchResponse.getLicenses().size() == 0)) {
		//
		// tradeLicenseSearchResponse.setResponseInfo(responseInfo);
		// return tradeLicenseSearchResponse;
		// }

		List<TradeLicenseSearch> licenses = tradeLicenseRepository.search(requestInfo, tenantId, pageSize, pageNumber,
				sort, active, ids, applicationNumber, licenseNumber, oldLicenseNumber, mobileNumber, aadhaarNumber,
				emailId, propertyAssesmentNo, adminWard, locality, ownerName, tradeTitle, tradeType, tradeCategory,
				tradeSubCategory, legacy, status);

		List<TradeLicenseSearchContract> tradeLicenseSearchContracts = new ArrayList<TradeLicenseSearchContract>();

		ModelMapper model = new ModelMapper();

		for (TradeLicenseSearch licenseSearch : licenses) {
			TradeLicenseSearchContract tradeSearchContract = new TradeLicenseSearchContract();
			model.map(licenseSearch, tradeSearchContract);
			tradeLicenseSearchContracts.add(tradeSearchContract);
		}

		if (tradeLicenseSearchResponse == null) {
			// instantiate for setting the values of db search
			tradeLicenseSearchResponse = new TradeLicenseSearchResponse();
		}
		tradeLicenseSearchResponse.setLicenses(tradeLicenseSearchContracts);
		tradeLicenseSearchResponse.setResponseInfo(responseInfo);

		return tradeLicenseSearchResponse;
	}

	private TradeLicenseSearchResponse getLicensesFromEs(String tenantId, Integer pageSize, Integer pageNumber,
			String sort, String active, String tradeLicenseId, String applicationNumber, String licenseNumber,
			String oldLicenseNumber, String mobileNumber, String aadhaarNumber, String emailId,
			String propertyAssesmentNo, Integer adminWard, Integer locality, String ownerName, String tradeTitle,
			String tradeType, Integer tradeCategory, Integer tradeSubCategory, String legacy, Integer status) {

		Map<String, Object> requestMap = new HashMap();
		StringBuilder url = new StringBuilder(propertiesManager.getTradeLicenseIndexerServiceHostName());
		url.append(propertiesManager.getTradeLicenseIndexerServiceBasePath());
		url.append(propertiesManager.getTradeLicenseIndexerLicenseSearchPath());

		url.append("?tenantId=" + tenantId + "&");

		if (pageSize != null) {
			url.append("pageSize=" + pageSize + "&");
		}

		if (pageNumber != null) {
			url.append("pageNumber=" + pageNumber + "&");
		}

		if (sort != null && !sort.isEmpty()) {
			url.append("sort=" + sort + "&");
		}

		if (active != null && !active.isEmpty()) {
			url.append("active=" + active + "&");
		}

		if (tradeLicenseId != null && !tradeLicenseId.isEmpty()) {
			url.append("tradeLicenseId=" + tradeLicenseId + "&");
		}

		if (applicationNumber != null && !applicationNumber.isEmpty()) {
			url.append("applicationNumber=" + applicationNumber + "&");
		}

		if (licenseNumber != null && !licenseNumber.isEmpty()) {
			url.append("licenseNumber=" + licenseNumber + "&");
		}

		if (oldLicenseNumber != null && !oldLicenseNumber.isEmpty()) {
			url.append("oldLicenseNumber=" + oldLicenseNumber + "&");
		}

		if (mobileNumber != null && !mobileNumber.isEmpty()) {
			url.append("mobileNumber=" + mobileNumber + "&");
		}
		if (aadhaarNumber != null && !aadhaarNumber.isEmpty()) {
			url.append("aadhaarNumber=" + aadhaarNumber + "&");
		}

		if (emailId != null && !emailId.isEmpty()) {
			url.append("emailId=" + emailId + "&");
		}

		if (propertyAssesmentNo != null && !propertyAssesmentNo.isEmpty()) {
			url.append("propertyAssesmentNo=" + propertyAssesmentNo + "&");
		}

		if (adminWard != null) {
			url.append("adminWard=" + adminWard + "&");
		}

		if (locality != null) {
			url.append("locality=" + locality + "&");
		}

		if (ownerName != null && !ownerName.isEmpty()) {
			url.append("ownerName=" + ownerName + "&");
		}

		if (tradeTitle != null && !tradeTitle.isEmpty()) {
			url.append("tradeTitle=" + tradeTitle + "&");
		}

		if (tradeType != null && !tradeType.isEmpty()) {
			url.append("tradeType=" + tradeType + "&");
		}

		if (tradeCategory != null) {
			url.append("tradeCategory=" + tradeCategory + "&");
		}

		if (tradeSubCategory != null) {
			url.append("tradeSubCategory=" + tradeSubCategory + "&");
		}

		if (legacy != null && !legacy.isEmpty()) {
			url.append("legacy=" + legacy + "&");
		}

		if (status != null) {
			url.append("status=" + status);
		} else {
			url.setLength(url.length() - 1);
		}

		TradeLicenseSearchResponse tradeLicenseSearchResponse = null;
		try {

			tradeLicenseSearchResponse = restTemplate.postForObject(url.toString(), requestMap,
					TradeLicenseSearchResponse.class);

		} catch (Exception e) {
			log.error("Error while connecting to the Tl-Indexer end point");
		}
		return tradeLicenseSearchResponse;
	}

	public ResponseInfo createResponseInfoFromRequestInfo(RequestInfo requestInfo, Boolean success) {
		ResponseInfo responseInfo = new ResponseInfo();
		String apiId = requestInfo.getApiId();
		responseInfo.setApiId(apiId);
		String ver = requestInfo.getVer();
		responseInfo.setVer(ver);
		Long ts = null;
		if (requestInfo.getTs() != null)
			ts = requestInfo.getTs();

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
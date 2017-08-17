package org.egov.tradelicense.domain.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.models.PropertyResponse;
import org.egov.tl.commons.web.contract.Category;
import org.egov.tl.commons.web.contract.CategoryDetail;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.contract.TradeLicenseSearchContract;
import org.egov.tl.commons.web.requests.CategoryResponse;
import org.egov.tl.commons.web.requests.DocumentTypeResponse;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tl.commons.web.requests.TradeLicenseSearchResponse;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.domain.exception.CustomBindException;
import org.egov.tradelicense.common.domain.exception.InvalidInputException;
import org.egov.tradelicense.common.util.TimeStampUtil;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.model.TradeLicenseSearch;
import org.egov.tradelicense.domain.repository.TradeLicenseRepository;
import org.egov.tradelicense.web.repository.BoundaryContractRepository;
import org.egov.tradelicense.web.repository.CategoryContractRepository;
import org.egov.tradelicense.web.repository.DocumentTypeContractRepository;
import org.egov.tradelicense.web.repository.PropertyContractRespository;
import org.egov.tradelicense.web.requests.BoundaryResponse;
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

	public List<TradeLicense> validateRelated(List<TradeLicense> tradeLicenses, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		for (TradeLicense tradeLicense : tradeLicenses) {

			if (tradeLicense.getIsLegacy()) {
				
				if( tradeLicense.getOldLicenseNumber() == null || tradeLicense.getOldLicenseNumber().trim().isEmpty() ){
					throw new InvalidInputException("Old License Number is Required Please enter Valid Old License No");
				}
				// check unique constraint
				tradeLicenseRepository.validateUniqueOldLicenseNumber(tradeLicense);
			}
			
			if( tradeLicense.getIsTradeOwner() ){
				if( ( tradeLicense.getAgreementNo() == null || tradeLicense.getAgreementNo().trim().isEmpty() ) ){
					throw new InvalidInputException("Agreement No is Required Please enter Valid Agreement No");
				}
				if( tradeLicense.getAgreementDate() == null || tradeLicense.getAgreementDate().trim().isEmpty() ){
					throw new InvalidInputException("Agreement Date is Required  Please enter valid date in dd/mm/yyyy");
				}
				
				
				
			}
			
			if (propertiesManager.getPtisValidation()) {
				
				if (tradeLicense.getPropertyAssesmentNo() == null){
					throw new InvalidInputException("Property assesment  is Required Please enter valid Property Assesment Number");
				}
				else{
					PropertyResponse propertyResponse = propertyContractRepository.findByAssesmentNo(tradeLicense,
							requestInfoWrapper);

					if (propertyResponse == null || propertyResponse.getProperties() == null
							|| propertyResponse.getProperties().size() == 0) {
						throw new InvalidInputException("Property assesment  is Required Please enter valid Property Assesment Number");
					}
				}
			}
			
			if( propertiesManager.getAdhaarValidation() ){
				if (tradeLicense.getAdhaarNumber() == null){
					throw new InvalidInputException("AdhaarNumber  is Required Please enter valid AdhaarNumber Number");
				}
			}
			
			// locality validation
			if (tradeLicense.getLocalityId() != null) {
				BoundaryResponse boundaryResponse = boundaryContractRepository.findByLocalityId(tradeLicense,
						requestInfoWrapper);

				if (boundaryResponse == null || boundaryResponse.getBoundarys() == null
						|| boundaryResponse.getBoundarys().size() == 0) {
					throw new InvalidInputException("Trade Locality  is Required Please enter valid Trade Locality ");
				}

			}

			// revenue ward validation
			if (tradeLicense.getRevenueWardId() != null) {
				BoundaryResponse boundaryResponse = boundaryContractRepository.findByRevenueWardId(tradeLicense,
						requestInfoWrapper);

				if (boundaryResponse == null || boundaryResponse.getBoundarys() == null
						|| boundaryResponse.getBoundarys().size() == 0) {
					throw new InvalidInputException("Trade Revenue Ward  is Required Please enter valid Trade Revenue Ward ");
				}

			}
			
			// admin ward validation
				if (tradeLicense.getAdminWardId() != null) {
					BoundaryResponse boundaryResponse = boundaryContractRepository.findByAdminWardId(tradeLicense,
							requestInfoWrapper);

					if (boundaryResponse == null || boundaryResponse.getBoundarys() == null
							|| boundaryResponse.getBoundarys().size() == 0) {
						throw new InvalidInputException("Trade Admin Ward  is Required Please enter valid Trade Admin Ward ");
					}

				}

			// category validation
			if (tradeLicense.getCategoryId() != null) {
				CategoryResponse categoryResponse = categoryContractRepository.findByCategoryId(tradeLicense,
						requestInfoWrapper);

				if (categoryResponse == null || categoryResponse.getCategories() == null
						|| categoryResponse.getCategories().size() == 0) {
					throw new InvalidInputException("Category is required ,  please enter Valid Category");
				}
			}

			// subCategory validation
			if (tradeLicense.getSubCategoryId() != null) {
				CategoryResponse categoryResponse = categoryContractRepository.findBySubCategoryId(tradeLicense,
						requestInfoWrapper);

				if (categoryResponse == null || categoryResponse.getCategories() == null
						|| categoryResponse.getCategories().size() == 0) {
					throw new InvalidInputException("Sub Category is required ,  please enter Valid Sub Category");
				} else {
					Long validityYears = categoryResponse.getCategories().get(0).getValidityYears();
					if(Long.valueOf(validityYears) != Long.valueOf(tradeLicense.getValidityYears())){
						throw new InvalidInputException("ValidityYears is required ,  please enter Valid years of SubCategory");
					}
				}
			}

			// supporting documents validation
			
			if( !tradeLicense.getIsLegacy()){
				//TODO check for all mdantory documents types for the given 
				//application type are filled are not.
			}
			
			if (tradeLicense.getSupportDocuments() != null) {
				for (SupportDocument supportDocument : tradeLicense.getSupportDocuments()) {

					DocumentTypeResponse documentTypeResponse = documentTypeContractRepository.findById(tradeLicense,
							supportDocument, requestInfoWrapper);

					if (documentTypeResponse == null || documentTypeResponse.getDocumentTypes() == null
							|| documentTypeResponse.getDocumentTypes().size() == 0) {
						throw new InvalidInputException("Document Type is required ,  please enter Valid Document Type");
					}
				}

			}

			// uom validation
			if (tradeLicense.getUomId() != null) {
				CategoryResponse categoryResponse = categoryContractRepository.findBySubCategoryUomId(tradeLicense,
						requestInfoWrapper);

				if (categoryResponse == null || categoryResponse.getCategories() == null
						|| categoryResponse.getCategories().size() == 0) {

					throw new InvalidInputException("UOM is required ,  please enter Valid UOM");
				} else {

					for (Category category : categoryResponse.getCategories()) {

						if (category.getDetails() == null && category.getDetails().size() == 0) {
							throw new InvalidInputException("UOM is required ,  please enter Valid UOM");
						} else {
							Boolean isExists = false;
							for (CategoryDetail categoryDetail : category.getDetails()) {
								if (categoryDetail.getUomId() == tradeLicense.getUomId()) {
									isExists = true;
								}
							}
							if (!isExists) {
								throw new InvalidInputException("UOM is required ,  please enter Valid UOM");
							}
						}
					}
				}

			}
			
			// feeDetails Validation
			if( tradeLicense.getFeeDetails() != null & tradeLicense.getFeeDetails().size() > 0 ){
				 String validFrom = tradeLicense.getLicenseValidFromDate();
				 if( validFrom != null ){
					 Timestamp validFromDate = TimeStampUtil.getTimeStamp(validFrom);
					 Timestamp currenDate = new Timestamp(System.currentTimeMillis());
					 // TODO 
					 // 1. find how many years between the validFromDate and currentDate
					 // 2. Figure out how many feeDetails ( including for which year ) based on the category.validityYears
					 // 3. validate wether input matches with data idenfieid in the step2, if not matching thow InvalidINput exception
				 }
			}
			
		}

		return tradeLicenses;
	}

	@Transactional
	public List<TradeLicense> add(List<TradeLicense> tradeLicenses, RequestInfo requestInfo, BindingResult errors) {

		validate(tradeLicenses, errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		// external end point validations
		validateRelated(tradeLicenses, requestInfo);

		// setting the id for the license and support document and fee details
		for (TradeLicense license : tradeLicenses) {

			license.setId(tradeLicenseRepository.getNextSequence());
			license.setStatus( new Long(1)); // Approved status id 1
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

	public TradeLicenseSearchResponse getTradeLicense(RequestInfo requestInfo, String tenantId, Integer pageSize,
			Integer pageNumber, String sort, String active, String tradeLicenseId, String applicationNumber,
			String licenseNumber, String oldLicenseNumber, String mobileNumber, String aadhaarNumber, String emailId,
			String propertyAssesmentNo, Integer adminWard, Integer locality, String ownerName, String tradeTitle,
			String tradeType, Integer tradeCategory, Integer tradeSubCategory, String legacy, Integer status) {

		TradeLicenseSearchResponse tradeLicenseSearchResponse = new TradeLicenseSearchResponse();

		tradeLicenseSearchResponse = getLicensesFromEs(tenantId, pageSize, pageNumber, sort, active, tradeLicenseId,
				applicationNumber, licenseNumber, oldLicenseNumber, mobileNumber, aadhaarNumber, emailId,
				propertyAssesmentNo, adminWard, locality, ownerName, tradeTitle, tradeType, tradeCategory,
				tradeSubCategory, legacy, status);

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);

		if (tradeLicenseSearchResponse != null && tradeLicenseSearchResponse.getLicenses() != null
				&& !(tradeLicenseSearchResponse.getLicenses().size() == 0)) {

			tradeLicenseSearchResponse.setResponseInfo(responseInfo);
			return tradeLicenseSearchResponse;
		}

		List<TradeLicenseSearch> licenses = tradeLicenseRepository.search(requestInfo, tenantId, pageSize, pageNumber,
				sort, active, tradeLicenseId, applicationNumber, licenseNumber, oldLicenseNumber, mobileNumber,
				aadhaarNumber, emailId, propertyAssesmentNo, adminWard, locality, ownerName, tradeTitle, tradeType,
				tradeCategory, tradeSubCategory, legacy, status);

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
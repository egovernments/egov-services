package org.egov.tradelicense.domain.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.models.PropertyResponse;
import org.egov.tl.commons.web.contract.CategoryDetailSearch;
import org.egov.tl.commons.web.contract.CategorySearch;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.contract.TradeLicenseSearchContract;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tl.commons.web.response.CategorySearchResponse;
import org.egov.tl.commons.web.response.DocumentTypeResponse;
import org.egov.tl.commons.web.response.TradeLicenseSearchResponse;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.domain.exception.AdhaarNotFoundException;
import org.egov.tradelicense.common.domain.exception.AgreeMentDateNotFoundException;
import org.egov.tradelicense.common.domain.exception.AgreeMentNotFoundException;
import org.egov.tradelicense.common.domain.exception.AgreeMentNotValidException;
import org.egov.tradelicense.common.domain.exception.CustomBindException;
import org.egov.tradelicense.common.domain.exception.EndPointException;
import org.egov.tradelicense.common.domain.exception.InvalidAdminWardException;
import org.egov.tradelicense.common.domain.exception.InvalidCategoryException;
import org.egov.tradelicense.common.domain.exception.InvalidDocumentTypeException;
import org.egov.tradelicense.common.domain.exception.InvalidFeeDetailException;
import org.egov.tradelicense.common.domain.exception.InvalidLocalityException;
import org.egov.tradelicense.common.domain.exception.InvalidPropertyAssesmentException;
import org.egov.tradelicense.common.domain.exception.InvalidRevenueWardException;
import org.egov.tradelicense.common.domain.exception.InvalidSubCategoryException;
import org.egov.tradelicense.common.domain.exception.InvalidUomException;
import org.egov.tradelicense.common.domain.exception.InvalidValidityYearsException;
import org.egov.tradelicense.common.domain.exception.LegacyFeeDetailNotFoundException;
import org.egov.tradelicense.common.domain.exception.OldLicenseNotFoundException;
import org.egov.tradelicense.common.domain.exception.PropertyAssesmentNotFoundException;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.model.TradeLicenseSearch;
import org.egov.tradelicense.domain.repository.TradeLicenseRepository;
import org.egov.tradelicense.web.contract.FinancialYearContract;
import org.egov.tradelicense.web.repository.BoundaryContractRepository;
import org.egov.tradelicense.web.repository.CategoryContractRepository;
import org.egov.tradelicense.web.repository.DocumentTypeContractRepository;
import org.egov.tradelicense.web.repository.FinancialYearContractRepository;
import org.egov.tradelicense.web.repository.PropertyContractRespository;
import org.egov.tradelicense.web.response.BoundaryResponse;
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

	public List<TradeLicense> validateRelated(List<TradeLicense> tradeLicenses, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		for (TradeLicense tradeLicense : tradeLicenses) {

			if (tradeLicense.getIsLegacy()) {

				if (tradeLicense.getOldLicenseNumber() == null || tradeLicense.getOldLicenseNumber().trim().isEmpty()) {
					throw new OldLicenseNotFoundException(propertiesManager.getOldLicenseNumberErrorMsg(), requestInfo);
				}
				// check unique constraint
				tradeLicenseRepository.validateUniqueOldLicenseNumber(tradeLicense, requestInfo);
			} else {
				// TODO Application Number and Application Date shoudl be
				// mandatory
			}

			if (tradeLicense.getIsPropertyOwner()) {

				if (tradeLicense.getAgreementNo() == null || tradeLicense.getAgreementNo().trim().isEmpty()) {
					throw new AgreeMentNotFoundException(propertiesManager.getAgreementNotFoundErrorMsg(), requestInfo);
				} else if (tradeLicense.getAgreementNo().trim().length() < 4
						|| tradeLicense.getAgreementNo().trim().length() > 20) {
					throw new AgreeMentNotValidException(propertiesManager.getAgreementNoErrorMsg(), requestInfo);
				}

				if (tradeLicense.getAgreementDate() == null) {
					throw new AgreeMentDateNotFoundException(propertiesManager.getAgreementDateErrorMsg(), requestInfo);
				}

			}

			if (propertiesManager.getPtisValidation()) {

				if (tradeLicense.getPropertyAssesmentNo() == null
						|| tradeLicense.getPropertyAssesmentNo().trim().isEmpty()) {
					throw new PropertyAssesmentNotFoundException(propertiesManager.getPropertyAssesmentNotFoundMsg(),
							requestInfo);
				} else if (tradeLicense.getPropertyAssesmentNo().trim().length() < 15
						|| tradeLicense.getPropertyAssesmentNo().trim().length() > 20) {
					throw new InvalidPropertyAssesmentException(
							propertiesManager.getPropertyAssesmentNoInvalidErrorMsg(), requestInfo);
				} else {
					PropertyResponse propertyResponse = propertyContractRepository.findByAssesmentNo(tradeLicense,
							requestInfoWrapper);

					if (propertyResponse == null || propertyResponse.getProperties() == null
							|| propertyResponse.getProperties().size() == 0) {
						throw new InvalidPropertyAssesmentException(
								propertiesManager.getPropertyAssesmentNoInvalidErrorMsg(), requestInfo);
					}
				}
			}

			if (propertiesManager.getAdhaarValidation()) {
				if (tradeLicense.getAdhaarNumber() == null) {
					throw new AdhaarNotFoundException(propertiesManager.getAadhaarNumberErrorMsg(), requestInfo);
				}
			}

			// locality validation
			if (tradeLicense.getLocalityId() != null) {
				BoundaryResponse boundaryResponse = boundaryContractRepository.findByLocalityId(tradeLicense,
						requestInfoWrapper);

				if (boundaryResponse == null || boundaryResponse.getBoundarys() == null
						|| boundaryResponse.getBoundarys().size() == 0) {
					throw new InvalidLocalityException(propertiesManager.getLocalityErrorMsg(), requestInfo);
				}

			}

			// revenue ward validation
			if (tradeLicense.getRevenueWardId() != null) {
				BoundaryResponse boundaryResponse = boundaryContractRepository.findByRevenueWardId(tradeLicense,
						requestInfoWrapper);

				if (boundaryResponse == null || boundaryResponse.getBoundarys() == null
						|| boundaryResponse.getBoundarys().size() == 0) {
					throw new InvalidRevenueWardException(propertiesManager.getRevenueWardErrorMsg(), requestInfo);
				}

			}

			// admin ward validation
			if (tradeLicense.getAdminWardId() != null) {
				BoundaryResponse boundaryResponse = boundaryContractRepository.findByAdminWardId(tradeLicense,
						requestInfoWrapper);

				if (boundaryResponse == null || boundaryResponse.getBoundarys() == null
						|| boundaryResponse.getBoundarys().size() == 0) {
					throw new InvalidAdminWardException(propertiesManager.getAdminWardErrorMsg(), requestInfo);
				}

			}

			// category validation
			if (tradeLicense.getCategoryId() != null) {
				CategorySearchResponse categoryResponse = categoryContractRepository.findByCategoryId(tradeLicense,
						requestInfoWrapper);

				if (categoryResponse == null || categoryResponse.getCategories() == null
						|| categoryResponse.getCategories().size() == 0) {
					throw new InvalidCategoryException(propertiesManager.getCategoryErrorMsg(), requestInfo);
				}
			}

			// subCategory validation
			if (tradeLicense.getSubCategoryId() != null) {
				CategorySearchResponse categoryResponse = categoryContractRepository.findBySubCategoryId(tradeLicense,
						requestInfoWrapper);

				if (categoryResponse == null || categoryResponse.getCategories() == null
						|| categoryResponse.getCategories().size() == 0) {
					throw new InvalidSubCategoryException(propertiesManager.getSubCategoryErrorMsg(), requestInfo);
				} else {
					Long validityYears = categoryResponse.getCategories().get(0).getValidityYears();
					if (Long.valueOf(validityYears) != Long.valueOf(tradeLicense.getValidityYears())) {
						throw new InvalidValidityYearsException(propertiesManager.getValidatiyYearsMatch(),
								requestInfo);
					}
				}
			}

			// supporting documents validation

			if (!tradeLicense.getIsLegacy()) {
				// TODO check for all mdantory documents types for the given
				// application type are filled are not.
			}

			if (tradeLicense.getSupportDocuments() != null && tradeLicense.getSupportDocuments().size() > 0) {
				for (SupportDocument supportDocument : tradeLicense.getSupportDocuments()) {

					DocumentTypeResponse documentTypeResponse = documentTypeContractRepository.findById(tradeLicense,
							supportDocument, requestInfoWrapper);

					if (documentTypeResponse == null || documentTypeResponse.getDocumentTypes() == null
							|| documentTypeResponse.getDocumentTypes().size() == 0) {
						throw new InvalidDocumentTypeException(propertiesManager.getDocumentTypeErrorMsg(),
								requestInfo);
					}
				}

			}

			// uom validation
			if (tradeLicense.getUomId() != null) {
				CategorySearchResponse categoryResponse = categoryContractRepository
						.findBySubCategoryUomId(tradeLicense, requestInfoWrapper);

				if (categoryResponse == null || categoryResponse.getCategories() == null
						|| categoryResponse.getCategories().size() == 0) {

					throw new InvalidUomException(propertiesManager.getUomErrorMsg(), requestInfo);
				} else {

					for (CategorySearch category : categoryResponse.getCategories()) {

						if (category.getDetails() == null && category.getDetails().size() == 0) {
							throw new InvalidUomException(propertiesManager.getUomErrorMsg(), requestInfo);
						} else {
							Boolean isExists = false;
							for (CategoryDetailSearch categoryDetail : category.getDetails()) {
								if (categoryDetail.getUomId() == tradeLicense.getUomId()) {
									isExists = true;
								}
							}
							if (!isExists) {
								throw new InvalidUomException(propertiesManager.getUomErrorMsg(), requestInfo);
							}
						}
					}
				}

			}

			// feeDetails Validation
			if (tradeLicense.getIsLegacy()) {
				if (tradeLicense.getFeeDetails() == null || tradeLicense.getFeeDetails().size() == 0) {
					throw new LegacyFeeDetailNotFoundException(propertiesManager.getLegacyFeeDetailsNotFoundMsg(),
							requestInfo);
				}
			}
			if (tradeLicense.getFeeDetails() != null && tradeLicense.getFeeDetails().size() > 0) {

				Long validFrom = tradeLicense.getLicenseValidFromDate();
				Integer validPeriod = null;
				String tenantId = tradeLicense.getTenantId();
				if (tradeLicense.getValidityYears() != null) {
					validPeriod = Integer.valueOf(tradeLicense.getValidityYears().toString());
				}

				Long currenDate = (System.currentTimeMillis());
				Calendar today = Calendar.getInstance();

				if (validFrom != null && validPeriod != null) {

					FinancialYearContract currentFYResponse = financialYearContractRepository
							.findFinancialYearIdByDate(tenantId, currenDate, requestInfoWrapper);
					FinancialYearContract licenseValidFYResponse = financialYearContractRepository
							.findFinancialYearIdByDate(tenantId, validFrom, requestInfoWrapper);

					String currentFinancialYear = null;
					String licenseValidFromFinancialYear = null;
					Integer currentFinancialFromValue = null;
					Integer licenseValidFinancialFromValue = null;
					Integer actualFeeDetailStartYear = null;
					Integer actualFeeDetailsEndYear = null;
					if (currentFYResponse != null && licenseValidFYResponse != null) {

						// current financial year details
						currentFinancialYear = currentFYResponse.getFinYearRange();
						currentFinancialFromValue = Integer.valueOf(currentFinancialYear.split("-")[0]);
						// license valid financial year details
						licenseValidFromFinancialYear = licenseValidFYResponse.getFinYearRange();
						licenseValidFinancialFromValue = Integer.valueOf(licenseValidFromFinancialYear.split("-")[0]);
						// get the fee details starting year
						if ((currentFinancialFromValue - licenseValidFinancialFromValue) >= 5) {
							actualFeeDetailStartYear = (currentFinancialFromValue - 5);
						} else {
							actualFeeDetailStartYear = licenseValidFinancialFromValue;
						}
						// getting fee details ending year
						actualFeeDetailsEndYear = currentFinancialFromValue;
					} else {

						throw new EndPointException(propertiesManager.getLocationEndPointError(), requestInfo);
					}
					// get the actual fee detail record count
					Integer actualFeeDetailCount = 0;
					for (int i = actualFeeDetailStartYear; i <= actualFeeDetailsEndYear;) {
						actualFeeDetailCount = actualFeeDetailCount + 1;
						i = i + validPeriod;
					}
					// validate given fee detail count
					if (actualFeeDetailCount != tradeLicense.getFeeDetails().size()) {
						throw new InvalidFeeDetailException(propertiesManager.getFeeDetailsErrorMsg(), requestInfo);
					}
					Date tradeValidFromDate = new Date(validFrom);
					today.setTimeInMillis(tradeValidFromDate.getTime());
					// validate the fee details
					for (int i = 0; i < actualFeeDetailCount; i++) {

						Boolean isFYExists = false;
						today.add(Calendar.YEAR, (i * validPeriod));

						FinancialYearContract feeDetailFYResponse = financialYearContractRepository
								.findFinancialYearIdByDate(tenantId, today.getTimeInMillis(), requestInfoWrapper);
						if (feeDetailFYResponse != null) {
							for (LicenseFeeDetail licenseFeeDetail : tradeLicense.getFeeDetails()) {
								if (licenseFeeDetail.getFinancialYear()
										.equalsIgnoreCase(feeDetailFYResponse.getFinYearRange())) {
									isFYExists = true;
									licenseFeeDetail.setFinancialYear(feeDetailFYResponse.getId().toString());
								}
							}
							if (!isFYExists) {
								throw new InvalidFeeDetailException(propertiesManager.getFeeDetailYearNotFound(),
										requestInfo);
							}
						} else {
							throw new InvalidFeeDetailException(propertiesManager.getFeeDetailYearNotFound(),
									requestInfo);
						}

						if (i == actualFeeDetailCount - 1) {
							today.setTime(feeDetailFYResponse.getEndingDate());
							today.add(Calendar.YEAR, (validPeriod - 1));
							feeDetailFYResponse = financialYearContractRepository.findFinancialYearIdByDate(tenantId,
									(today.getTimeInMillis()), requestInfoWrapper);
							if (feeDetailFYResponse != null) {
								tradeLicense.setExpiryDate(feeDetailFYResponse.getEndingDate().getTime());
							}
						}

					}
				}
			}

		}

		return tradeLicenses;
	}

	@Transactional
	public List<TradeLicense> add(List<TradeLicense> tradeLicenses, RequestInfo requestInfo, BindingResult errors) {

		validate(tradeLicenses, errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors, requestInfo);
		}
		// external end point validations
		validateRelated(tradeLicenses, requestInfo);

		// setting the id for the license and support document and fee details
		for (TradeLicense license : tradeLicenses) {

			license.setId(tradeLicenseRepository.getNextSequence());
			license.setStatus(new Long(1)); // Approved status id 1
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
			license.setLicenseNumber(licenseNumberGenerationService.generate(license.getTenantId(), requestInfo));
			// license.setApplicationNumber(
			// applNumberGenrationService.generate(license.getTenantId(),
			// requestInfo));
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
package org.egov.tradelicense.domain.service.validator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.egov.models.PropertyResponse;
import org.egov.tl.commons.web.contract.CategoryDetailSearch;
import org.egov.tl.commons.web.contract.CategorySearch;
import org.egov.tl.commons.web.contract.DocumentTypeContract;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.DocumentTypeV2Response;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.response.CategorySearchResponse;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.domain.exception.AdhaarNotFoundException;
import org.egov.tradelicense.common.domain.exception.AgreeMentDateNotFoundException;
import org.egov.tradelicense.common.domain.exception.AgreeMentNotFoundException;
import org.egov.tradelicense.common.domain.exception.AgreeMentNotValidException;
import org.egov.tradelicense.common.domain.exception.CustomInvalidInputException;
import org.egov.tradelicense.common.domain.exception.DuplicateTradeApplicationException;
import org.egov.tradelicense.common.domain.exception.DuplicateTradeLicenseException;
import org.egov.tradelicense.common.domain.exception.IdNotFoundException;
import org.egov.tradelicense.common.domain.exception.InvalidAdminWardException;
import org.egov.tradelicense.common.domain.exception.InvalidCategoryException;
import org.egov.tradelicense.common.domain.exception.InvalidDocumentTypeException;
import org.egov.tradelicense.common.domain.exception.InvalidFeeDetailException;
import org.egov.tradelicense.common.domain.exception.InvalidInputException;
import org.egov.tradelicense.common.domain.exception.InvalidLocalityException;
import org.egov.tradelicense.common.domain.exception.InvalidPropertyAssesmentException;
import org.egov.tradelicense.common.domain.exception.InvalidRevenueWardException;
import org.egov.tradelicense.common.domain.exception.InvalidSubCategoryException;
import org.egov.tradelicense.common.domain.exception.InvalidUomException;
import org.egov.tradelicense.common.domain.exception.InvalidValidityYearsException;
import org.egov.tradelicense.common.domain.exception.LegacyFeeDetailNotFoundException;
import org.egov.tradelicense.common.domain.exception.MandatoryDocumentNotFoundException;
import org.egov.tradelicense.common.domain.exception.OldLicenseNotFoundException;
import org.egov.tradelicense.common.domain.exception.PropertyAssesmentNotFoundException;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.LicenseSearch;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.model.TradePartner;
import org.egov.tradelicense.domain.model.TradeShift;
import org.egov.tradelicense.domain.repository.LicenseApplicationRepository;
import org.egov.tradelicense.domain.repository.LicenseFeeDetailRepository;
import org.egov.tradelicense.domain.repository.SupportDocumentRepository;
import org.egov.tradelicense.domain.repository.TradeLicenseRepository;
import org.egov.tradelicense.domain.repository.TradePartnerRepository;
import org.egov.tradelicense.domain.repository.TradeShiftRepository;
import org.egov.tradelicense.web.contract.FinancialYearContract;
import org.egov.tradelicense.web.repository.BoundaryContractRepository;
import org.egov.tradelicense.web.repository.CategoryContractRepository;
import org.egov.tradelicense.web.repository.DocumentTypeContractRepository;
import org.egov.tradelicense.web.repository.FinancialYearContractRepository;
import org.egov.tradelicense.web.repository.PropertyContractRespository;
import org.egov.tradelicense.web.response.BoundaryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TradeLicenseServiceValidator {

	@Autowired
	TradeLicenseRepository tradeLicenseRepository;

	@Autowired
	LicenseApplicationRepository licenseApplicationRepository;

	@Autowired
	SupportDocumentRepository supportDocumentRepository;

	@Autowired
	LicenseFeeDetailRepository licenseFeeDetailRepository;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	PropertyContractRespository propertyContractRepository;

	@Autowired
	BoundaryContractRepository boundaryContractRepository;

	@Autowired
	CategoryContractRepository categoryContractRepository;

	@Autowired
	DocumentTypeContractRepository documentTypeContractRepository;

	@Autowired
	FinancialYearContractRepository financialYearContractRepository;
	
	@Autowired
	TradeShiftRepository tradeShiftRepository;
	
	@Autowired
	TradePartnerRepository tradePartnerRepository;

	private static final Logger logger = LoggerFactory.getLogger(TradeLicenseServiceValidator.class);

	public void validateCreateTradeLicenseRelated(List<TradeLicense> tradeLicenses, RequestInfo requestInfo) {

		for (TradeLicense tradeLicense : tradeLicenses) {

			if (tradeLicense.getIsLegacy()) {

				validateCreateLegacyTradeLicense(tradeLicense, requestInfo);

			} else {

				validateCreateNewTradeLicense(tradeLicense, requestInfo);
			}
		}
	}

	public void validateUpdateTradeLicenseRelated(List<TradeLicense> tradeLicenses, RequestInfo requestInfo) {

		for (TradeLicense tradeLicense : tradeLicenses) {

			if (tradeLicense.getIsLegacy()) {

				validateUpdateLegacyTradeLicense(tradeLicense, requestInfo);

			} else {

				// checking the id existence of trade license in database
				validateTradeLicenseIdExistance(tradeLicense, requestInfo);
				// get the tradeLicense with id and bind non-modifiable fields
				bindTradeNonModifiableFields(tradeLicense, requestInfo);
			}
		}
	}

	private void validateCreateLegacyTradeLicense(TradeLicense tradeLicense, RequestInfo requestInfo) {

		// clearing the unnecessary details for legacy trade license
		clearUnwantedDetailsOfLegacyLicense(tradeLicense, requestInfo);
		// checking the valid from date existence
		validateTradeValidFromDate(tradeLicense, requestInfo);
		// checking the existence and uniqueness of old license number
		validateOldLicenseNumber(tradeLicense, requestInfo);
		// checking the agreement details
		validateLicenseAgreementDetails(tradeLicense, requestInfo);
		// checking the property details
		validatePtisDetails(tradeLicense, requestInfo);
		// checking the adhaar details
		validateAdhaarDetails(tradeLicense, requestInfo);
		// checking the trade location details
		validateTradeLocationDetails(tradeLicense, requestInfo);
		// checking revenue ward details
		validateRevenueWardDetails(tradeLicense, requestInfo);
		// checking admin ward details
		validateAdminWardDetails(tradeLicense, requestInfo);
		// checking category Details
		validateTradeCategoryDetails(tradeLicense, requestInfo);
		// checking subCategory Details
		validateTradeSubCategoryDetails(tradeLicense, requestInfo);
		// checking the uom details
		validateTradeUomDetails(tradeLicense, requestInfo);
		// checking support document details
		validateTradeSupportingDocuments(tradeLicense, requestInfo);
		// checking feeDetails
		validateTradeFeeDetails(tradeLicense, requestInfo);

	}

	private void validateCreateNewTradeLicense(TradeLicense tradeLicense, RequestInfo requestInfo) {

		// clearing the unnecessary details for new trade license
		if (tradeLicense.getApplication() != null) {
			tradeLicense.getApplication().setFeeDetails(new ArrayList<>());
		}
		tradeLicense.setOldLicenseNumber(null);
		tradeLicense.setLicenseNumber(null);
		// checking the valid from date existence
		validateTradeValidFromDate(tradeLicense, requestInfo);
		// checking the existence and uniqueness of old license number
		validateLicenseNumber(tradeLicense, requestInfo);
		// validate application and application type existence
		validateApplicationDetails(tradeLicense, requestInfo);
		// validate application details
		validateApplicationNumber(tradeLicense, requestInfo);
		// checking the agreement details
		validateLicenseAgreementDetails(tradeLicense, requestInfo);
		// checking the property details
		validatePtisDetails(tradeLicense, requestInfo);
		// checking the adhaar details
		validateAdhaarDetails(tradeLicense, requestInfo);
		// checking the trade location details
		validateTradeLocationDetails(tradeLicense, requestInfo);
		// checking revenue ward details
		validateRevenueWardDetails(tradeLicense, requestInfo);
		// checking admin ward details
		validateAdminWardDetails(tradeLicense, requestInfo);
		// checking category Details
		validateTradeCategoryDetails(tradeLicense, requestInfo);
		// checking subCategory Details
		validateTradeSubCategoryDetails(tradeLicense, requestInfo);
		// checking the uom details
		validateTradeUomDetails(tradeLicense, requestInfo);
		// checking support document details
		validateTradeSupportingDocuments(tradeLicense, requestInfo);
		// validate trade commencement date
		setTradeExpiryDateByValidatingCommencementDate(tradeLicense, requestInfo);

	}

	private void validateUpdateLegacyTradeLicense(TradeLicense tradeLicense, RequestInfo requestInfo) {

		// clearing the unnecessary details for legacy trade license
		clearUnwantedDetailsOfLegacyLicense(tradeLicense, requestInfo);
		// checking the valid from date existence
		validateTradeValidFromDate(tradeLicense, requestInfo);
		// checking the existence and uniqueness of license number
		validateOldLicenseNumber(tradeLicense, requestInfo);
		// checking the agreement details
		validateLicenseAgreementDetails(tradeLicense, requestInfo);
		// checking the property details
		validatePtisDetails(tradeLicense, requestInfo);
		// checking the adhaar details
		validateAdhaarDetails(tradeLicense, requestInfo);
		// checking the trade location details
		validateTradeLocationDetails(tradeLicense, requestInfo);
		// checking revenue ward details
		validateRevenueWardDetails(tradeLicense, requestInfo);
		// checking admin ward details
		validateAdminWardDetails(tradeLicense, requestInfo);
		// checking category Details
		validateTradeCategoryDetails(tradeLicense, requestInfo);
		// checking subCategory Details
		validateTradeSubCategoryDetails(tradeLicense, requestInfo);
		// checking the uom details
		validateTradeUomDetails(tradeLicense, requestInfo);
		// checking support document details
		validateTradeSupportingDocuments(tradeLicense, requestInfo);
		// checking feeDetails
		validateTradeFeeDetails(tradeLicense, requestInfo);
		// get the tradeLicense with id and bind non-modifiable fields
		bindTradeNonModifiableFields(tradeLicense, requestInfo);
	}

	private void validateUpdateNewTradeLicense(TradeLicense tradeLicense, RequestInfo requestInfo) {

		// clearing the unnecessary details for new trade license
		if (tradeLicense.getApplication() != null) {
			tradeLicense.getApplication().setFeeDetails(new ArrayList<>());
		}
		tradeLicense.setOldLicenseNumber(null);
		// checking the valid from date existence
		validateTradeValidFromDate(tradeLicense, requestInfo);
		// checking the id existence of trade license in database
		validateTradeLicenseIdExistance(tradeLicense, requestInfo);
		// validate application and application type existence
		validateApplicationDetails(tradeLicense, requestInfo);
		// validate application details
		validateApplicationNumber(tradeLicense, requestInfo);
		// checking the existence and uniqueness of license number
		validateLicenseNumber(tradeLicense, requestInfo);
		// checking the agreement details
		validateLicenseAgreementDetails(tradeLicense, requestInfo);
		// checking the property details
		validatePtisDetails(tradeLicense, requestInfo);
		// checking the adhaar details
		validateAdhaarDetails(tradeLicense, requestInfo);
		// checking the trade location details
		validateTradeLocationDetails(tradeLicense, requestInfo);
		// checking revenue ward details
		validateRevenueWardDetails(tradeLicense, requestInfo);
		// checking admin ward details
		validateAdminWardDetails(tradeLicense, requestInfo);
		// checking category Details
		validateTradeCategoryDetails(tradeLicense, requestInfo);
		// checking subCategory Details
		validateTradeSubCategoryDetails(tradeLicense, requestInfo);
		// checking the uom details
		validateTradeUomDetails(tradeLicense, requestInfo);
		// checking support document details
		validateTradeSupportingDocuments(tradeLicense, requestInfo);
		// validate trade commencement date
		setTradeExpiryDateByValidatingCommencementDate(tradeLicense, requestInfo);
	}

	private void clearUnwantedDetailsOfLegacyLicense(TradeLicense tradeLicense, RequestInfo requestInfo) {

		// clearing the unnecessary details for legacy trade license
		tradeLicense.setApplicationDate(null);

		if (tradeLicense.getApplication() != null) {

			tradeLicense.getApplication().setApplicationDate(null);
		}
	}

	/**
	 * validate trade valid from date, for legacy throw error if licenseValid
	 * from Date is null, set TradeCommencementDate to the validlicenseFromDate
	 * when validlicenseFromDate is null and license is not legacy
	 * 
	 * @param tradeLicense
	 * @param requestInfo
	 */
	private void validateTradeValidFromDate(TradeLicense tradeLicense, RequestInfo requestInfo) {

		if (tradeLicense.getLicenseValidFromDate() == null) {

			if (tradeLicense.getIsLegacy() && !tradeLicense.getIsDataPorting()) {

				throw new CustomInvalidInputException(propertiesManager.getLicenseValidFromDateNotNullCode(),
						propertiesManager.getLicenseValidFromDateNotNullMsg(), requestInfo);

			}
		}

	}

	private void validateTradeLicenseIdExistance(TradeLicense tradeLicense, RequestInfo requestInfo) {

		if (tradeLicense.getId() != null) {
			// check id existence in database
			if (!tradeLicenseRepository.idExistenceCheck(tradeLicense)) {

				throw new IdNotFoundException(propertiesManager.getOldLicenseIdNotValidCustomMsg(),
						propertiesManager.getIdField(), requestInfo);
			}

		} else {

			throw new IdNotFoundException(propertiesManager.getOldLicenseIdNotFoundCustomMsg(),
					propertiesManager.getIdField(), requestInfo);
		}
	}

	private void validateLicenseNumber(TradeLicense tradeLicense, RequestInfo requestInfo) {

		if (tradeLicense.getLicenseNumber() != null && !tradeLicense.getLicenseNumber().trim().isEmpty()) {

			// check unique constraint
			if (!tradeLicenseRepository.uniqueCheck("licenseNumber", tradeLicense)) {

				throw new DuplicateTradeLicenseException(propertiesManager.getDuplicateOldTradeLicenseMsg(),
						requestInfo);
			}

		}

	}

	private void validateApplicationDetails(TradeLicense tradeLicense, RequestInfo requestInfo) {

		if (tradeLicense.getApplication() == null) {

			throw new InvalidInputException(propertiesManager.getApplicationMissingErr(), requestInfo);

		} else if (tradeLicense.getApplication().getApplicationType() == null) {

			throw new InvalidInputException(propertiesManager.getApplicationTypeMissingErr(), requestInfo);

		}
	}

	private void validateApplicationNumber(TradeLicense tradeLicense, RequestInfo requestInfo) {

		if (tradeLicense.getApplication() != null && tradeLicense.getApplication().getApplicationNumber() != null
				&& !tradeLicense.getApplication().getApplicationNumber().isEmpty()) {

			// check unique constraint
			if (!licenseApplicationRepository.uniqueCheck("applicationNumber", tradeLicense.getApplication())) {

				throw new DuplicateTradeApplicationException(propertiesManager.getDuplicateTradeApplicationNumberMsg(),
						requestInfo);
			}
		}
	}

	private void validateOldLicenseNumber(TradeLicense tradeLicense, RequestInfo requestInfo) {

		if (tradeLicense.getOldLicenseNumber() == null || tradeLicense.getOldLicenseNumber().trim().isEmpty()) {

			throw new OldLicenseNotFoundException(propertiesManager.getOldLicenseNumberErrorMsg(), requestInfo);

		}
		// check unique constraint
		if (!tradeLicenseRepository.uniqueCheck("oldLicenseNumber", tradeLicense)) {

			throw new DuplicateTradeLicenseException(propertiesManager.getDuplicateOldTradeLicenseMsg(), requestInfo);
		}

	}

	private void validateLicenseAgreementDetails(TradeLicense tradeLicense, RequestInfo requestInfo) {

		if (tradeLicense.getIsPropertyOwner()) {

			if (tradeLicense.getAgreementNo() == null || tradeLicense.getAgreementNo().trim().isEmpty()) {

				throw new AgreeMentNotFoundException(propertiesManager.getAgreementNotFoundErrorMsg(), requestInfo);

			} else if (tradeLicense.getAgreementNo().trim().length() < 4
					|| tradeLicense.getAgreementNo().trim().length() > 30) {

				throw new AgreeMentNotValidException(propertiesManager.getAgreementNoErrorMsg(), requestInfo);
			}

			if (tradeLicense.getAgreementDate() == null) {

				throw new AgreeMentDateNotFoundException(propertiesManager.getAgreementDateErrorMsg(), requestInfo);

			} else if (!tradeLicense.getIsLegacy()) {

				if (tradeLicense.getTradeCommencementDate() != null) {

					if ((tradeLicense.getTradeCommencementDate().compareTo(tradeLicense.getAgreementDate())) < 0) {

						throw new CustomInvalidInputException(propertiesManager.getAgreementDateNotValidCode(),
								propertiesManager.getAgreementDateNotValidMsg(), requestInfo);

					}
				}
			}

		}
	}

	private void validatePtisDetails(TradeLicense tradeLicense, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		if (propertiesManager.getPtisValidation()) {

			if (tradeLicense.getPropertyAssesmentNo() == null
					|| tradeLicense.getPropertyAssesmentNo().trim().isEmpty()) {

				throw new PropertyAssesmentNotFoundException(propertiesManager.getPropertyAssesmentNotFoundMsg(),
						requestInfo);

			} else if (tradeLicense.getPropertyAssesmentNo().trim().length() < 12
					|| tradeLicense.getPropertyAssesmentNo().trim().length() > 12) {

				throw new InvalidPropertyAssesmentException(propertiesManager.getPropertyAssesmentNoInvalidErrorMsg(),
						requestInfo);

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
	}

	private void validateAdhaarDetails(TradeLicense tradeLicense, RequestInfo requestInfo) {

		if (propertiesManager.getAdhaarValidation()) {

			if (tradeLicense.getOwnerAadhaarNumber() == null) {

				throw new AdhaarNotFoundException(propertiesManager.getAadhaarNumberErrorMsg(), requestInfo);
			}
		}
	}

	private void validateTradeLocationDetails(TradeLicense tradeLicense, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		// locality validation
		if (tradeLicense.getLocality() != null && !tradeLicense.getLocality().isEmpty()) {

			BoundaryResponse boundaryResponse = boundaryContractRepository
					.findByLocalityCodes(tradeLicense.getTenantId(), tradeLicense.getLocality(), requestInfoWrapper);

			if (boundaryResponse == null || boundaryResponse.getBoundarys() == null
					|| boundaryResponse.getBoundarys().size() == 0) {

				
				if(!getLicenseIsUnderDataPorting(tradeLicense)){
					
					throw new InvalidLocalityException(propertiesManager.getLocalityErrorMsg(), requestInfo);
				}

			}
		}
	}

	private void validateRevenueWardDetails(TradeLicense tradeLicense, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		// revenue ward validation
		if (tradeLicense.getRevenueWard() != null && !tradeLicense.getRevenueWard().isEmpty()) {

			BoundaryResponse boundaryResponse = boundaryContractRepository.findByRevenueWardCodes(
					tradeLicense.getTenantId(), tradeLicense.getRevenueWard(), requestInfoWrapper);

			if (boundaryResponse == null || boundaryResponse.getBoundarys() == null
					|| boundaryResponse.getBoundarys().size() == 0) {

				if(!getLicenseIsUnderDataPorting(tradeLicense)){
					
					throw new InvalidRevenueWardException(propertiesManager.getRevenueWardErrorMsg(), requestInfo);
				}
			}
		}
	}

	private void validateAdminWardDetails(TradeLicense tradeLicense, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		// admin ward validation
		if (tradeLicense.getAdminWard() != null && !tradeLicense.getAdminWard().isEmpty()) {

			BoundaryResponse boundaryResponse = boundaryContractRepository
					.findByAdminWardCodes(tradeLicense.getTenantId(), tradeLicense.getAdminWard(), requestInfoWrapper);

			if (boundaryResponse == null || boundaryResponse.getBoundarys() == null
					|| boundaryResponse.getBoundarys().size() == 0) {

				if(!getLicenseIsUnderDataPorting(tradeLicense)){
					
					throw new InvalidAdminWardException(propertiesManager.getAdminWardErrorMsg(), requestInfo);
				}
			}
		}
	}

	private void validateTradeCategoryDetails(TradeLicense tradeLicense, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		// category validation
		if (tradeLicense.getCategory() != null && !tradeLicense.getCategory().isEmpty()) {

			CategorySearchResponse categoryResponse = categoryContractRepository.findByCategoryCode(tradeLicense,
					requestInfoWrapper);

			if (categoryResponse == null || categoryResponse.getCategories() == null
					|| categoryResponse.getCategories().size() == 0) {

				if(!getLicenseIsUnderDataPorting(tradeLicense)){
					
					throw new InvalidCategoryException(propertiesManager.getCategoryErrorMsg(), requestInfo);
				}
				
			}
		}
	}

	private void validateTradeSubCategoryDetails(TradeLicense tradeLicense, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		// subCategory validation
		if (tradeLicense.getSubCategory() != null && !tradeLicense.getSubCategory().isEmpty()) {

			CategorySearchResponse categoryResponse = categoryContractRepository.findBySubCategoryCode(tradeLicense,
					requestInfoWrapper);

			if (categoryResponse == null || categoryResponse.getCategories() == null
					|| categoryResponse.getCategories().size() == 0) {

				if(!getLicenseIsUnderDataPorting(tradeLicense)){
					
					throw new InvalidSubCategoryException(propertiesManager.getSubCategoryErrorMsg(), requestInfo);
				}
				

			} else {

				Long validityYears = categoryResponse.getCategories().get(0).getValidityYears();

				if (Long.valueOf(validityYears) != Long.valueOf(tradeLicense.getValidityYears())) {

					if(!getLicenseIsUnderDataPorting(tradeLicense)){
						
						throw new InvalidValidityYearsException(propertiesManager.getValidatiyYearsMatch(), requestInfo);
					}
					
				}
			}
		}
	}

	private void validateTradeSupportingDocuments(TradeLicense tradeLicense, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		logger.info(" tradeLicense Object for Support Document validation " + tradeLicense.toString());
		// supporting documents validation
		if (!tradeLicense.getIsLegacy()) {

			DocumentTypeV2Response documentTypeMandatoryResponse = documentTypeContractRepository
					.findTradeMandatoryDocuments(tradeLicense, requestInfoWrapper);

			// validating mandatory documents existence
			if (documentTypeMandatoryResponse != null && documentTypeMandatoryResponse.getDocumentTypes() != null
					&& documentTypeMandatoryResponse.getDocumentTypes().size() > 0) {

				logger.info(" Mandatory support documents are found "
						+ documentTypeMandatoryResponse.getDocumentTypes().size());
				logger.info(" Support documents in Trade license Application found "
						+ tradeLicense.getApplication().getSupportDocuments().size());
				for (DocumentTypeContract documentTypeContract : documentTypeMandatoryResponse.getDocumentTypes()) {

					Boolean isMandatoryDocumentExists = Boolean.FALSE;
					logger.info(" Mandatory docType Id " + documentTypeContract.getId().toString());
					if (tradeLicense.getApplication() != null
							&& tradeLicense.getApplication().getSupportDocuments() != null
							&& tradeLicense.getApplication().getSupportDocuments().size() > 0) {

						for (SupportDocument supportDocument : tradeLicense.getApplication().getSupportDocuments()) {
							logger.info(" Tradelicense Application support docType Id "
									+ supportDocument.getDocumentTypeId().toString());

							if (documentTypeContract.getId().equals(supportDocument.getDocumentTypeId())) {

								isMandatoryDocumentExists = Boolean.TRUE;
							}
						}

					} else {

						logger.info("mandatory document Id not found  " + documentTypeContract.getId().toString());
						throw new MandatoryDocumentNotFoundException(
								propertiesManager.getMandatoryDocumentNotFoundCustomMsg()
										+ documentTypeContract.getId().toString(),
								requestInfo);
					}

					if (!isMandatoryDocumentExists) {

						throw new MandatoryDocumentNotFoundException(
								propertiesManager.getMandatoryDocumentNotFoundCustomMsg(), requestInfo);
					}
				}
			}

			// validating request supporting documents
			if (tradeLicense.getApplication() != null && tradeLicense.getApplication().getSupportDocuments() != null
					&& tradeLicense.getApplication().getSupportDocuments().size() > 0) {

				for (SupportDocument supportDocument : tradeLicense.getApplication().getSupportDocuments()) {

					DocumentTypeV2Response documentTypeResponse = documentTypeContractRepository
							.findByIdAndTlValues(tradeLicense, supportDocument, requestInfoWrapper);

					if (documentTypeResponse == null || documentTypeResponse.getDocumentTypes() == null
							|| documentTypeResponse.getDocumentTypes().size() == 0) {

						throw new InvalidDocumentTypeException(propertiesManager.getDocumentTypeErrorMsg(),
								requestInfo);
					}
				}

			}

		} else {

			if (tradeLicense.getSupportDocuments() != null && tradeLicense.getSupportDocuments().size() > 0) {
				
				for (SupportDocument supportDocument : tradeLicense.getSupportDocuments()) {

					DocumentTypeV2Response documentTypeResponse = documentTypeContractRepository
							.findByIdAndTlValues(tradeLicense, supportDocument, requestInfoWrapper);

					if (documentTypeResponse == null || documentTypeResponse.getDocumentTypes() == null
							|| documentTypeResponse.getDocumentTypes().size() == 0) {

						if(!getLicenseIsUnderDataPorting(tradeLicense)){
							
							throw new InvalidDocumentTypeException(propertiesManager.getDocumentTypeErrorMsg(),
									requestInfo);
						}
						
					}
				}

			}
		}

	}

	private void validateTradeUomDetails(TradeLicense tradeLicense, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		// uom validation
		if (tradeLicense.getUom() != null && !tradeLicense.getUom().isEmpty()) {

			CategorySearchResponse categoryResponse = categoryContractRepository.findBySubCategoryUomCode(tradeLicense,
					requestInfoWrapper);

			if (categoryResponse == null || categoryResponse.getCategories() == null
					|| categoryResponse.getCategories().size() == 0) {

				if(!getLicenseIsUnderDataPorting(tradeLicense)){
					
					throw new InvalidUomException(propertiesManager.getUomErrorMsg(), requestInfo);
				}
				

			} else {

				for (CategorySearch category : categoryResponse.getCategories()) {

					if (category.getDetails() == null && category.getDetails().size() == 0) {

						if(!getLicenseIsUnderDataPorting(tradeLicense)){
							
							throw new InvalidUomException(propertiesManager.getUomErrorMsg(), requestInfo);
						}
						
					} else {

						Boolean isExists = false;

						for (CategoryDetailSearch categoryDetail : category.getDetails()) {

							if (categoryDetail.getUom() != null && !categoryDetail.getUom().isEmpty() 
									&& categoryDetail.getUom().equalsIgnoreCase(tradeLicense.getUom())) {

								isExists = true;
							}
						}

						if (!isExists) {

							if(!getLicenseIsUnderDataPorting(tradeLicense)){
								
								throw new InvalidUomException(propertiesManager.getUomErrorMsg(), requestInfo);
							}
							
						}
					}
				}
			}

		}
	}

	private void validateTradeFeeDetails(TradeLicense tradeLicense, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		// checking feeDetails existence
		if (tradeLicense.getFeeDetails() == null || tradeLicense.getFeeDetails().size() == 0) {

			//throw error if not data porting
			if(!getLicenseIsUnderDataPorting(tradeLicense)){
				
				throw new LegacyFeeDetailNotFoundException(propertiesManager.getLegacyFeeDetailsNotFoundMsg(), requestInfo);
			}
			
		}

		if (tradeLicense.getFeeDetails() != null && tradeLicense.getFeeDetails().size() > 0) {

			Long validFrom = tradeLicense.getLicenseValidFromDate();
			Integer validPeriod = null;
			String tenantId = tradeLicense.getTenantId();
			// get the license validity period
			if (tradeLicense.getValidityYears() != null) {

				validPeriod = Integer.valueOf(tradeLicense.getValidityYears().toString());
			}
			// get the system current date
			Long currenDate = (System.currentTimeMillis());
			Calendar today = Calendar.getInstance();
			// if both license valid from date and validity period exists then
			// only proceed for feeDetails validation
			if (validFrom != null && validPeriod != null) {

				FinancialYearContract currentFYResponse = financialYearContractRepository
						.findFinancialYearIdByDate(tenantId, currenDate, requestInfoWrapper);

				if (currentFYResponse == null) {

					throwFinancialYearNotFoundError(currenDate, requestInfo);
				}

				FinancialYearContract licenseValidFYResponse = financialYearContractRepository
						.findFinancialYearIdByDate(tenantId, validFrom, requestInfoWrapper);

				if (licenseValidFYResponse == null) {

					throwFinancialYearNotFoundError(validFrom, requestInfo);
				}

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
				// set the fee details calculation start date and year
				Date tradeValidFromDate = new Date(validFrom);
				today.setTimeInMillis(tradeValidFromDate.getTime());
				if ((actualFeeDetailStartYear - licenseValidFinancialFromValue) == 0) {
					today.add(Calendar.YEAR, 0);
				} else {
					today.add(Calendar.YEAR, (actualFeeDetailStartYear - licenseValidFinancialFromValue));
				}
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
								// update the new expire date based on fee paid
								if (isFYExists) {

									if (i == 0) {

										if (!licenseFeeDetail.getPaid()) {
											today.setTimeInMillis(tradeValidFromDate.getTime());
										}
										today.add(Calendar.YEAR, (validPeriod - 1));
										feeDetailFYResponse = financialYearContractRepository.findFinancialYearIdByDate(
												tenantId, (today.getTimeInMillis()), requestInfoWrapper);

										if (feeDetailFYResponse != null) {

											tradeLicense.setExpiryDate(feeDetailFYResponse.getEndingDate().getTime());

										} else {

											throwFinancialYearNotFoundError(today.getTimeInMillis(), requestInfo);
										}

									} else if (licenseFeeDetail.getPaid()) {

										today.add(Calendar.YEAR, (validPeriod - 1));
										feeDetailFYResponse = financialYearContractRepository.findFinancialYearIdByDate(
												tenantId, (today.getTimeInMillis()), requestInfoWrapper);

										if (feeDetailFYResponse != null) {

											tradeLicense.setExpiryDate(feeDetailFYResponse.getEndingDate().getTime());

										} else {

											throwFinancialYearNotFoundError(today.getTimeInMillis(), requestInfo);
										}
									}

								}
							}
						}

						if (!isFYExists) {

							throw new InvalidFeeDetailException(propertiesManager.getFeeDetailYearNotFound(),
									requestInfo);
						}

					} else {

						throwFinancialYearNotFoundError(today.getTimeInMillis(), requestInfo);

					}

					// reset the date to fee details calculation start date
					today.setTimeInMillis(tradeValidFromDate.getTime());
					if ((actualFeeDetailStartYear - licenseValidFinancialFromValue) == 0) {
						today.add(Calendar.YEAR, 0);
					} else {
						today.add(Calendar.YEAR, (actualFeeDetailStartYear - licenseValidFinancialFromValue));
					}
				}
				
			} else if(getLicenseIsUnderDataPorting(tradeLicense)){
				
				convertFeeDetailsFinancialYearRangeToId(tradeLicense, requestInfo);
			}
		}

	}
	
	private Boolean getLicenseIsUnderDataPorting(TradeLicense tradeLicense){
		
		if(tradeLicense.getIsLegacy() && tradeLicense.getIsDataPorting()){
			
			return true;
			
		} else {
			
			return false;
		}
	}
	
	//Conversion of data porting fee details financial year range to id
	private void convertFeeDetailsFinancialYearRangeToId(TradeLicense tradeLicense, RequestInfo requestInfo) {
		
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		
		// converting financialYear range to id for feeDetails existence
		if (tradeLicense.getFeeDetails() != null && tradeLicense.getFeeDetails().size() > 0) {

			for(LicenseFeeDetail licenseFeeDetail : tradeLicense.getFeeDetails()){
				
				FinancialYearContract financialYearContract = financialYearContractRepository.findFinancialYearByFinRange(tradeLicense.getTenantId(),
						licenseFeeDetail.getFinancialYear(), requestInfoWrapper);
				
				if (financialYearContract != null && financialYearContract.getId() != null) {
					
					licenseFeeDetail.setFinancialYear(financialYearContract.getId().toString());
				
				} else {
					
					throw new CustomInvalidInputException(propertiesManager.getFinancialYearNotFoundCode(),
							"financial year is not found for" + licenseFeeDetail.getFinancialYear() , requestInfo);
				}
			}
			
		}
		
	}

	private void throwFinancialYearNotFoundError(Long financialNotFoundDate, RequestInfo requestInfo) {

		String financialNotFoundErrorMsg = propertiesManager.getFinancialYearNotFoundMsg();

		if (financialNotFoundErrorMsg != null) {

			SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
			String asOnDate = sf.format(new Date(financialNotFoundDate));
			financialNotFoundErrorMsg = financialNotFoundErrorMsg.replace(":financialNotFoundDate", asOnDate);
		}

		throw new CustomInvalidInputException(propertiesManager.getFinancialYearNotFoundCode(),
				financialNotFoundErrorMsg, requestInfo);
	}

	private void validateTradeUpdateSupportDocuments(TradeLicense tradeLicense, RequestInfo requestInfo) {

		if (tradeLicense.getSupportDocuments() != null && tradeLicense.getSupportDocuments().size() > 0) {

			for (SupportDocument supportDocument : tradeLicense.getSupportDocuments()) {

				if (tradeLicense.getAuditDetails() != null) {
					supportDocument.setAuditDetails(tradeLicense.getAuditDetails());
				}

				if (supportDocument.getId() != null) {
					// check id existence in database
					if (!supportDocumentRepository.idExistenceCheck(supportDocument)) {

						throw new IdNotFoundException(propertiesManager.getSupportDocumentIdNotValidCustomMsg(),
								propertiesManager.getIdField(), requestInfo);
					}

				} else {
					// get the next sequence of support document id and set it
					supportDocument.setId(supportDocumentRepository.getNextSequence());
				}
			}
		}
	}

	private void validateTradeUpdateFeeDetails(TradeLicense tradeLicense, RequestInfo requestInfo) {

		if (tradeLicense.getFeeDetails() != null && tradeLicense.getFeeDetails().size() > 0) {

			for (LicenseFeeDetail licenseFeeDetail : tradeLicense.getFeeDetails()) {

				if (tradeLicense.getAuditDetails() != null) {
					licenseFeeDetail.setAuditDetails(tradeLicense.getAuditDetails());
				}

				if (licenseFeeDetail.getId() != null) {
					// check id existence in database
					if (!licenseFeeDetailRepository.idExistenceCheck(licenseFeeDetail)) {

						throw new IdNotFoundException(propertiesManager.getFeeDetailIdNotValidCustomMsg(),
								propertiesManager.getIdField(), requestInfo);
					}

				} else {
					// get the next sequence of fee detail id and set it
					licenseFeeDetail.setId(licenseFeeDetailRepository.getNextSequence());
				}
			}
		}
	}
	
	private void validateTradeShiftDetails(TradeLicense tradeLicense, RequestInfo requestInfo) {
		
		if (tradeLicense.getShifts() != null && tradeLicense.getShifts().size() > 0) {

			for (TradeShift tradeShift : tradeLicense.getShifts()) {

				if (tradeLicense.getAuditDetails() != null) {
					tradeShift.setAuditDetails(tradeLicense.getAuditDetails());
				}

				if (tradeShift.getId() != null) {
					// check id existence in database
					if (!tradeShiftRepository.idExistenceCheck(tradeShift)) {

						throw new IdNotFoundException("provided trade shift id is not valid",
								propertiesManager.getIdField(), requestInfo);
					}

				} else {
					// get the next sequence of fee detail id and set it
					tradeShift.setId(tradeShiftRepository.getNextSequence());
				}
			}
		}
	}

	private void validateTradePartnerDetails(TradeLicense tradeLicense, RequestInfo requestInfo) {
		
		if (tradeLicense.getPartners() != null && tradeLicense.getPartners().size() > 0) {

			for (TradePartner tradePartner : tradeLicense.getPartners()) {

				if (tradeLicense.getAuditDetails() != null) {
					tradePartner.setAuditDetails(tradeLicense.getAuditDetails());
				}

				if (tradePartner.getId() != null) {
					// check id existence in database
					if (!tradePartnerRepository.idExistenceCheck(tradePartner)) {

						throw new IdNotFoundException("provided trade partner id is not valid",
								propertiesManager.getIdField(), requestInfo);
					}

				} else {
					// get the next sequence of fee detail id and set it
					tradePartner.setId(tradePartnerRepository.getNextSequence());
				}
			}
		}
	}

	private void setTradeExpiryDateByValidatingCommencementDate(TradeLicense tradeLicense, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		String tenantId = tradeLicense.getTenantId();
		String currentFinancialYearRange = null;
		String commencementFinancialYearRange = null;
		Integer currentFinancialFromValue = null;
		Integer previousFinancialFromValue = null;
		Integer nextFinancialFromValue = null;
		Integer commencementDateFinancialFromValue = null;
		// get the trade commencement date
		Long commencementDate = tradeLicense.getTradeCommencementDate();
		// get the system current date
		Long currenDate = (System.currentTimeMillis());
		Calendar today = Calendar.getInstance();
		Date tradeValidFromDate = new Date(commencementDate);
		Integer validPeriod = null;

		// get the license validity period
		if (tradeLicense.getValidityYears() != null) {

			validPeriod = Integer.valueOf(tradeLicense.getValidityYears().toString());
		}

		today.setTimeInMillis(tradeValidFromDate.getTime());

		// get the current financial year
		FinancialYearContract currentFYResponse = financialYearContractRepository.findFinancialYearIdByDate(tenantId,
				currenDate, requestInfoWrapper);

		if (currentFYResponse != null) {

			currentFinancialYearRange = currentFYResponse.getFinYearRange();
			currentFinancialFromValue = Integer.valueOf(currentFinancialYearRange.split("-")[0]);
			previousFinancialFromValue = currentFinancialFromValue - Integer.valueOf(1);
			nextFinancialFromValue = currentFinancialFromValue + Integer.valueOf(1);

		} else {

			throwFinancialYearNotFoundError(currenDate, requestInfo);
		}

		// get the trade commencement date financial year
		FinancialYearContract commencementFYResponse = financialYearContractRepository
				.findFinancialYearIdByDate(tenantId, commencementDate, requestInfoWrapper);

		if (commencementFYResponse != null) {

			commencementFinancialYearRange = commencementFYResponse.getFinYearRange();
			commencementDateFinancialFromValue = Integer.valueOf(commencementFinancialYearRange.split("-")[0]);

			// validate trade commencement date
			if ((commencementDateFinancialFromValue.equals(currentFinancialFromValue))
					|| (commencementDateFinancialFromValue.equals(previousFinancialFromValue))
					|| (commencementDateFinancialFromValue.equals(nextFinancialFromValue))) {

				today.setTime(commencementFYResponse.getEndingDate());
				// setting license valid from date as commencement date for new
				// license
				tradeLicense.setLicenseValidFromDate(tradeLicense.getTradeCommencementDate());

			} else {

				throw new CustomInvalidInputException(propertiesManager.getTradeCommencementDateNotValidCode(),
						propertiesManager.getTradeCommencementDateNotValidMsg(), requestInfo);
			}

		} else {

			throwFinancialYearNotFoundError(commencementDate, requestInfo);
		}

		// setting expire date
		today.add(Calendar.YEAR, (validPeriod - 1));
		commencementFYResponse = financialYearContractRepository.findFinancialYearIdByDate(tenantId,
				(today.getTimeInMillis()), requestInfoWrapper);
		if (commencementFYResponse != null) {

			tradeLicense.setExpiryDate(commencementFYResponse.getEndingDate().getTime());

		} else {

			throwFinancialYearNotFoundError(today.getTimeInMillis(), requestInfo);
		}
	}

	private void bindTradeNonModifiableFields(TradeLicense tradeLicense, RequestInfo requestInfo) {

		if (tradeLicense.getIsLegacy()) {
			// checking support document id's
			validateTradeUpdateSupportDocuments(tradeLicense, requestInfo);
			// checking fee detail id's
			validateTradeUpdateFeeDetails(tradeLicense, requestInfo);
			// checking trade partner id's
			validateTradePartnerDetails(tradeLicense, requestInfo);
			// checking trade shifts id's
			validateTradeShiftDetails(tradeLicense, requestInfo);
		}

		Integer[] ids = new Integer[1];
		if (tradeLicense.getId() != null) {

			ids[0] = Integer.valueOf(tradeLicense.getId().toString());
		}
		LicenseSearch licenseSearch = LicenseSearch.builder().ids(ids).tenantId(tradeLicense.getTenantId()).build();
		TradeLicense license = tradeLicenseRepository.findLicense(licenseSearch);

		if (license != null) {

			tradeLicense.setApplicationType(license.getApplicationType());
			tradeLicense.setTenantId(license.getTenantId());
			tradeLicense.getApplication().setId(license.getApplication().getId());

			if (license.getIsLegacy()) {

				tradeLicense.setLicenseNumber(license.getLicenseNumber());
			}

			if (!license.getIsLegacy()) {

				tradeLicense.setApplicationNumber(license.getApplicationNumber());
				tradeLicense.getApplication().setApplicationNumber(license.getApplicationNumber());
				tradeLicense.setApplicationDate(license.getApplicationDate());

				if (requestInfo.getUserInfo() != null && requestInfo.getUserInfo().getId() != null
						&& license.getAuditDetails() != null && license.getAuditDetails().getCreatedBy() != null) {

					if (requestInfo.getUserInfo().getId().toString()
							.equalsIgnoreCase(license.getAuditDetails().getCreatedBy())) {

						tradeLicense.getApplication().setLicenseFee(license.getApplication().getLicenseFee());
						validateUpdateNewTradeLicense(tradeLicense, requestInfo);
						// checking support document id's
						validateTradeUpdateSupportDocuments(tradeLicense, requestInfo);
						// checking trade partner id's
						validateTradePartnerDetails(tradeLicense, requestInfo);
						// checking trade shifts id's
						validateTradeShiftDetails(tradeLicense, requestInfo);

					} else {

						tradeLicense.setActive(license.getActive());
						tradeLicense.setOwnerAadhaarNumber(license.getOwnerAadhaarNumber());
						tradeLicense.setAdminWard(license.getAdminWard());
						tradeLicense.setAgreementDate(license.getAgreementDate());
						tradeLicense.setAgreementNo(license.getAgreementNo());
						tradeLicense.setCategory(license.getCategory());
						tradeLicense.setOwnerEmailId(license.getOwnerEmailId());
						tradeLicense.setExpiryDate(license.getExpiryDate());
						tradeLicense.setFatherSpouseName(license.getFatherSpouseName());
						tradeLicense.setId(license.getId());
						tradeLicense.setIsLegacy(license.getIsLegacy());
						tradeLicense.setIsPropertyOwner(license.getIsPropertyOwner());
						tradeLicense.setLicenseValidFromDate(license.getLicenseValidFromDate());
						tradeLicense.setLocality(license.getLocality());
						tradeLicense.setOwnerMobileNumber(license.getOwnerMobileNumber());
						tradeLicense.setOldLicenseNumber(license.getOldLicenseNumber());
						tradeLicense.setOwnerAddress(license.getOwnerAddress());
						tradeLicense.setOwnerName(license.getOwnerName());
						tradeLicense.setOwnerShipType(license.getOwnerShipType());
						tradeLicense.setPropertyAssesmentNo(license.getPropertyAssesmentNo());
						tradeLicense.setRemarks(license.getRemarks());
						tradeLicense.setRevenueWard(license.getRevenueWard());
						tradeLicense.setSubCategory(license.getSubCategory());
						tradeLicense.setTradeAddress(license.getTradeAddress());
						tradeLicense.setTradeCommencementDate(license.getTradeCommencementDate());
						tradeLicense.setTradeTitle(license.getTradeTitle());
						tradeLicense.setTradeType(license.getTradeType());
						tradeLicense.setUom(license.getUom());
						tradeLicense.setValidityYears(license.getValidityYears());

						if(license.getPartners() != null && license.getPartners().size() > 0){
							
							tradeLicense.setPartners(license.getPartners());
							
							for(TradePartner tradePartner : tradeLicense.getPartners()){
								
								tradePartner.setAuditDetails(tradeLicense.getAuditDetails());
							}
						}
						
						if(license.getShifts() != null && license.getShifts().size() > 0){
							
							tradeLicense.setShifts(license.getShifts());
							
							for(TradeShift tradeShift : tradeLicense.getShifts()){
								
								tradeShift.setAuditDetails(tradeLicense.getAuditDetails());
							}
						}
						
						if (tradeLicense.getApplication() != null && license.getApplications() != null) {

							tradeLicense.getApplication().setFeeDetails(license.getApplication().getFeeDetails());
							tradeLicense.getApplication()
									.setSupportDocuments(license.getApplication().getSupportDocuments());
							tradeLicense.getApplication().setLicenseId(license.getApplication().getLicenseId());
							tradeLicense.getApplication().setTenantId(license.getApplication().getTenantId());
							tradeLicense.getApplication().setAuditDetails(tradeLicense.getAuditDetails());

							if (tradeLicense.getApplication().getSupportDocuments() != null
									&& tradeLicense.getApplication().getSupportDocuments().size() > 0) {

								for (SupportDocument supportDocument : tradeLicense.getApplication()
										.getSupportDocuments()) {

									supportDocument.setAuditDetails(tradeLicense.getAuditDetails());
								}
							}
						}
					}
				}
			}
		}
	}
}
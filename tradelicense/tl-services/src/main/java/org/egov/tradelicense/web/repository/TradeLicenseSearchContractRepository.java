package org.egov.tradelicense.web.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.CategorySearch;
import org.egov.tl.commons.web.contract.LicenseApplicationSearchContract;
import org.egov.tl.commons.web.contract.LicenseBill;
import org.egov.tl.commons.web.contract.LicenseBillSearchContract;
import org.egov.tl.commons.web.contract.LicenseFeeDetailContract;
import org.egov.tl.commons.web.contract.LicenseStatus;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.SupportDocumentSearchContract;
import org.egov.tl.commons.web.contract.TradeLicenseSearchContract;
import org.egov.tl.commons.web.contract.TradePartnerContract;
import org.egov.tl.commons.web.contract.TradeShiftContract;
import org.egov.tl.commons.web.contract.UOM;
import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tl.commons.web.contract.enums.BusinessNatureEnum;
import org.egov.tl.commons.web.contract.enums.Gender;
import org.egov.tl.commons.web.contract.enums.OwnerShipTypeEnum;
import org.egov.tl.commons.web.requests.DocumentTypeV2Response;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.response.CategorySearchResponse;
import org.egov.tl.commons.web.response.LicenseStatusResponse;
import org.egov.tl.commons.web.response.UOMResponse;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.domain.model.LicenseApplication;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.model.TradePartner;
import org.egov.tradelicense.domain.model.TradeShift;
import org.egov.tradelicense.web.contract.Boundary;
import org.egov.tradelicense.web.contract.FinancialYearContract;
import org.egov.tradelicense.web.response.BoundaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TradeLicenseSearchContractRepository {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private CategoryContractRepository categoryContractRepository;

	@Autowired
	private StatusRepository statusRepository;

	@Autowired
	private BoundaryContractRepository boundaryContractRepository;

	@Autowired
	private FinancialYearContractRepository financialYearRepository;

	@Autowired
	private DocumentTypeContractRepository documentTypeRepository;

	public List<TradeLicenseSearchContract> toSearchContractList(RequestInfo requestInfo,
			List<TradeLicense> domainLicenses) {

		List<TradeLicenseSearchContract> licenseSearchContracts = new ArrayList<TradeLicenseSearchContract>();

		if (domainLicenses != null && domainLicenses.size() > 0) {

			Map<String, Map<String, String>> uniqueFieldsMap = identifyDependencyFields(requestInfo, domainLicenses);

			for (TradeLicense domain : domainLicenses) {

				String categoryName = null, subCategoryName = null, uomName = null, statusName = null,
						localityName = null, adminWardName = null, revenueWardName = null;

				if (uniqueFieldsMap.get("categoryCodeAndNameMap") != null && domain.getCategory() != null) {
					categoryName = uniqueFieldsMap.get("categoryCodeAndNameMap").get(domain.getCategory().toString());
				}
				if (uniqueFieldsMap.get("subCategoryCodeAndNameMap") != null && domain.getSubCategory() != null) {
					subCategoryName = uniqueFieldsMap.get("subCategoryCodeAndNameMap")
							.get(domain.getSubCategory().toString());
				}
				if (uniqueFieldsMap.get("uomCodeAndNameMap") != null && domain.getUom() != null) {
					uomName = uniqueFieldsMap.get("uomCodeAndNameMap").get(domain.getUom().toString());
				}
				if (uniqueFieldsMap.get("statusCodeAndNameMap") != null && domain.getStatus() != null) {
					statusName = uniqueFieldsMap.get("statusCodeAndNameMap").get(domain.getStatus().toString());
				}
				if (uniqueFieldsMap.get("localityCodeAndNameMap") != null && domain.getLocality() != null) {
					localityName = uniqueFieldsMap.get("localityCodeAndNameMap").get(domain.getLocality().toString());
				}
				if (uniqueFieldsMap.get("adminWardCodeAndNameMap") != null && domain.getAdminWard() != null) {
					adminWardName = uniqueFieldsMap.get("adminWardCodeAndNameMap")
							.get(domain.getAdminWard().toString());
				}
				if (uniqueFieldsMap.get("revenueWardCodeAndNameMap") != null && domain.getRevenueWard() != null) {
					revenueWardName = uniqueFieldsMap.get("revenueWardCodeAndNameMap")
							.get(domain.getRevenueWard().toString());
				}

				TradeLicenseSearchContract licenseSearchContract = new TradeLicenseSearchContract();

				licenseSearchContract.setId(domain.getId());
				licenseSearchContract.setTenantId(domain.getTenantId());
				licenseSearchContract.setLicenseNumber(domain.getLicenseNumber());
				licenseSearchContract.setOldLicenseNumber(domain.getOldLicenseNumber());
				licenseSearchContract.setOwnerAadhaarNumber(domain.getOwnerAadhaarNumber());
				licenseSearchContract.setOwnerMobileNumber(domain.getOwnerMobileNumber());
				licenseSearchContract.setOwnerName(domain.getOwnerName());
				if(domain.getOwnerType() != null){
					licenseSearchContract.setOwnerType(domain.getOwnerType().toString());
				}
				if(domain.getOwnerGender() != null){
					licenseSearchContract.setOwnerGender(domain.getOwnerGender().toString());
				}
				licenseSearchContract.setOwnerBirthYear(domain.getOwnerBirthYear());
				licenseSearchContract.setOwnerCorrAddress(domain.getOwnerCorrAddress());
				licenseSearchContract.setOwnerCity(domain.getOwnerCity());
				licenseSearchContract.setOwnerPinCode(domain.getOwnerPinCode());
				licenseSearchContract.setOwnerEmailId(domain.getOwnerEmailId());
				licenseSearchContract.setOwnerPhoneNumber(domain.getOwnerPhoneNumber());
				licenseSearchContract.setOwnerPhoto(domain.getOwnerPhoto());
				licenseSearchContract.setFatherSpouseName(domain.getFatherSpouseName());
				licenseSearchContract.setOwnerAddress(domain.getOwnerAddress());
				if(domain.getEstablishmentType() != null){
					licenseSearchContract.setEstablishmentType(domain.getEstablishmentType().toString());
				}
				licenseSearchContract.setEstablishmentName(domain.getEstablishmentName());
				licenseSearchContract.setEstablishmentRegNo(domain.getEstablishmentRegNo());
				licenseSearchContract.setEstablishmentCorrAddress(domain.getEstablishmentCorrAddress());
				licenseSearchContract.setEstablishmentCity(domain.getEstablishmentCity());
				licenseSearchContract.setEstablishmentPinCode(domain.getEstablishmentPinCode());
				licenseSearchContract.setEstablishmentPhoneNo(domain.getEstablishmentPhoneNo());
				licenseSearchContract.setEstablishmentMobNo(domain.getEstablishmentMobNo());
				licenseSearchContract.setEstablishmentEmailId(domain.getEstablishmentEmailId());
				licenseSearchContract.setSurveyOrGatNo(domain.getSurveyOrGatNo());
				licenseSearchContract.setCtsOrFinalPlotNo(domain.getCtsOrFinalPlotNo());
				licenseSearchContract.setPlotNo(domain.getPlotNo());
				licenseSearchContract.setWaterConnectionNo(domain.getWaterConnectionNo());
				licenseSearchContract.setLandOwnerName(domain.getLandOwnerName());
				licenseSearchContract.setIsConsentLetterTaken(domain.getIsConsentLetterTaken());
				licenseSearchContract.setBusinessDescription(domain.getBusinessDescription());
				licenseSearchContract.setPrevLicenseNo(domain.getPrevLicenseNo());
				licenseSearchContract.setPrevLicenseDate(domain.getPrevLicenseDate());
				licenseSearchContract.setTotalEmployees(domain.getTotalEmployees());
				licenseSearchContract.setTotalMachines(domain.getTotalMachines());
				licenseSearchContract.setLicenseRejBefrForSamePremise(domain.getLicenseRejBefrForSamePremise());
				licenseSearchContract.setExplLicenseNo(domain.getExplLicenseNo());
				licenseSearchContract.setTotalShifts(domain.getTotalShifts());
				licenseSearchContract.setPropertyAssesmentNo(domain.getPropertyAssesmentNo());
				licenseSearchContract.setLocality(domain.getLocality());
				licenseSearchContract.setLocalityName(localityName);
				licenseSearchContract.setRevenueWard(domain.getRevenueWard());
				licenseSearchContract.setRevenueWardName(revenueWardName);
				licenseSearchContract.setAdminWard(domain.getAdminWard());
				licenseSearchContract.setAdminWardName(adminWardName);
				licenseSearchContract.setStatus(domain.getStatus());
				licenseSearchContract.setStatusName(statusName);
				licenseSearchContract.setTradeAddress(domain.getTradeAddress());
				licenseSearchContract.setTradeTitle(domain.getTradeTitle());
				if (domain.getOwnerShipType() != null) {
					licenseSearchContract
							.setOwnerShipType(OwnerShipTypeEnum.valueOf(domain.getOwnerShipType().toString()));
				}
				if (domain.getTradeType() != null) {
					licenseSearchContract.setTradeType(BusinessNatureEnum.valueOf(domain.getTradeType().toString()));
				}
				licenseSearchContract.setCategory(domain.getCategory());
				licenseSearchContract.setCategoryName(categoryName);
				licenseSearchContract.setSubCategory(domain.getSubCategory());
				licenseSearchContract.setSubCategoryName(subCategoryName);
				licenseSearchContract.setUom(domain.getUom());
				licenseSearchContract.setUomName(uomName);
				licenseSearchContract.setQuantity(domain.getQuantity());
				licenseSearchContract.setRemarks(domain.getRemarks());
				licenseSearchContract.setValidityYears(domain.getValidityYears());
				if (domain.getTradeCommencementDate() != null) {
					licenseSearchContract.setTradeCommencementDate(domain.getTradeCommencementDate().toString());
				}
				if (domain.getIssuedDate() != null) {
					licenseSearchContract.setIssuedDate(domain.getIssuedDate().toString());
				}
				if (domain.getLicenseValidFromDate() != null) {
					licenseSearchContract.setLicenseValidFromDate(domain.getLicenseValidFromDate().toString());
				}
				if (domain.getAgreementDate() != null) {
					licenseSearchContract.setAgreementDate(domain.getAgreementDate().toString());
				}
				if (domain.getExpiryDate() != null) {
					licenseSearchContract.setExpiryDate(domain.getExpiryDate().toString());
				}
				licenseSearchContract.setAgreementNo(domain.getAgreementNo());
				licenseSearchContract.setIsPropertyOwner(domain.getIsPropertyOwner());
				licenseSearchContract.setIsLegacy(domain.getIsLegacy());
				licenseSearchContract.setActive(domain.getActive());
				licenseSearchContract.setUserId(domain.getUserId());

				if(domain.getIsLegacy() && domain.getLicenseData() != null){
					
					licenseSearchContract.setLicenseData(domain.getLicenseData());
				}
				
				if (domain.getAuditDetails() != null) {

					AuditDetails auditDetails = new AuditDetails();
					auditDetails.setCreatedBy(domain.getAuditDetails().getCreatedBy());
					auditDetails.setCreatedTime(domain.getAuditDetails().getCreatedTime());
					auditDetails.setLastModifiedBy(domain.getAuditDetails().getLastModifiedBy());
					auditDetails.setLastModifiedTime(domain.getAuditDetails().getLastModifiedTime());
					licenseSearchContract.setAuditDetails(auditDetails);
				}
				
				if(domain.getPartners() != null && domain.getPartners().size() > 0){
					
					List<TradePartnerContract> partners = toLicensePartnerContract(requestInfo, domain.getPartners());
					licenseSearchContract.setPartners(partners);
				}
				
				if(domain.getShifts() != null && domain.getShifts().size() > 0){
					
					List<TradeShiftContract> shifts = toLicenseShiftContract(requestInfo, domain.getShifts());
					licenseSearchContract.setShifts(shifts);
				}

				if (domain.getApplications() != null && domain.getApplications().size() > 0) {

					List<LicenseApplicationSearchContract> licenseApplicationSearchContracts = toApplicationSearchContract(
							requestInfo, domain.getApplications());
					licenseSearchContract.setApplications(licenseApplicationSearchContracts);
				}

				if (licenseSearchContract.getApplications() != null
						&& licenseSearchContract.getApplications().size() > 0) {

					for (LicenseApplicationSearchContract licenseApplicationSearchContract : licenseSearchContract
							.getApplications()) {

						if (licenseSearchContract.getIsLegacy()) {

							licenseSearchContract.setFeeDetails(licenseApplicationSearchContract.getFeeDetails());
							licenseSearchContract
									.setSupportDocuments(licenseApplicationSearchContract.getSupportDocuments());

						}

						if (licenseApplicationSearchContract.getApplicationType() != null) {
							licenseSearchContract.setApplicationType(
									ApplicationTypeEnum.valueOf(licenseApplicationSearchContract.getApplicationType()));
						}

						if (licenseApplicationSearchContract.getApplicationDate() != null) {
							licenseSearchContract
									.setApplicationDate(licenseApplicationSearchContract.getApplicationDate());
						}

						licenseSearchContract
								.setApplicationNumber(licenseApplicationSearchContract.getApplicationNumber());

					}
				}

				licenseSearchContracts.add(licenseSearchContract);
			}
		}

		return licenseSearchContracts;
	}

	private List<TradeShiftContract> toLicenseShiftContract(RequestInfo requestInfo, List<TradeShift> shifts) {
		
		List<TradeShiftContract> tradeShiftContracts = new ArrayList<TradeShiftContract>();

		for (TradeShift tradeShift : shifts) {

			TradeShiftContract tradeShiftContract = new TradeShiftContract();

			tradeShiftContract.setId(tradeShift.getId());
			tradeShiftContract.setTenantId(tradeShift.getTenantId());
			tradeShiftContract.setLicenseId(tradeShift.getLicenseId());
			tradeShiftContract.setShiftNo(tradeShift.getShiftNo());
			tradeShiftContract.setFromTime(tradeShift.getFromTime());
			tradeShiftContract.setToTime(tradeShift.getToTime());
			tradeShiftContract.setRemarks(tradeShift.getRemarks());

			if (tradeShift.getAuditDetails() != null) {

				AuditDetails auditDetails = new AuditDetails();
				auditDetails.setCreatedBy(tradeShift.getAuditDetails().getCreatedBy());
				auditDetails.setCreatedTime(tradeShift.getAuditDetails().getCreatedTime());
				auditDetails.setLastModifiedBy(tradeShift.getAuditDetails().getLastModifiedBy());
				auditDetails.setLastModifiedTime(tradeShift.getAuditDetails().getLastModifiedTime());

				tradeShiftContract.setAuditDetails(auditDetails);
			}
			
			tradeShiftContracts.add(tradeShiftContract);
			
		}

		return tradeShiftContracts;
	}

	private List<TradePartnerContract> toLicensePartnerContract(RequestInfo requestInfo, List<TradePartner> partners) {
		
		List<TradePartnerContract> tradePartnerContracts = new ArrayList<TradePartnerContract>();

		for (TradePartner tradePartner : partners) {

			TradePartnerContract tradePartnerContract = new TradePartnerContract();

			tradePartnerContract.setId(tradePartner.getId());
			tradePartnerContract.setTenantId(tradePartner.getTenantId());
			tradePartnerContract.setLicenseId(tradePartner.getLicenseId());
			tradePartnerContract.setAadhaarNumber(tradePartner.getAadhaarNumber());;
			tradePartnerContract.setFullName(tradePartner.getFullName());
			if(tradePartner.getGender() != null){
				tradePartnerContract.setGender(Gender.valueOf(tradePartner.getGender().toString()));
			}
			tradePartnerContract.setBirthYear(tradePartner.getBirthYear());
			tradePartnerContract.setEmailId(tradePartner.getEmailId());
			tradePartnerContract.setDesignation(tradePartner.getDesignation());
			tradePartnerContract.setResidentialAddress(tradePartner.getResidentialAddress());
			tradePartnerContract.setCorrespondenceAddress(tradePartner.getCorrespondenceAddress());
			tradePartnerContract.setPhoneNumber(tradePartner.getPhoneNumber());
			tradePartnerContract.setMobileNumber(tradePartner.getMobileNumber());
			tradePartnerContract.setPhoto(tradePartner.getPhoto());

			if (tradePartner.getAuditDetails() != null) {

				AuditDetails auditDetails = new AuditDetails();
				auditDetails.setCreatedBy(tradePartner.getAuditDetails().getCreatedBy());
				auditDetails.setCreatedTime(tradePartner.getAuditDetails().getCreatedTime());
				auditDetails.setLastModifiedBy(tradePartner.getAuditDetails().getLastModifiedBy());
				auditDetails.setLastModifiedTime(tradePartner.getAuditDetails().getLastModifiedTime());

				tradePartnerContract.setAuditDetails(auditDetails);
			}
			
			tradePartnerContracts.add(tradePartnerContract);
			
		}

		return tradePartnerContracts;
	}
	
	private List<LicenseApplicationSearchContract> toApplicationSearchContract(RequestInfo requestInfo,
			List<LicenseApplication> licenseApplications) {

		List<LicenseApplicationSearchContract> licenseApplicationSearchContracts = new ArrayList<LicenseApplicationSearchContract>();

		for (LicenseApplication licenseApplication : licenseApplications) {

			LicenseApplicationSearchContract licenseApplicationSearchContract = new LicenseApplicationSearchContract();

			String statusCode = "";
			licenseApplicationSearchContract.setId(licenseApplication.getId());
			licenseApplicationSearchContract.setTenantId(licenseApplication.getTenantId());
			licenseApplicationSearchContract.setLicenseId(licenseApplication.getLicenseId());
			licenseApplicationSearchContract.setState_id(licenseApplication.getState_id());
			statusCode = licenseApplication.getStatus();

			if (statusCode != null && !statusCode.isEmpty()) {
				licenseApplicationSearchContract.setStatus(statusCode);
				licenseApplicationSearchContract.setStatusName(
						getApplicationStatusName(licenseApplication.getTenantId(), statusCode, requestInfo));
			}

			if (licenseApplication.getApplicationDate() != null) {
				licenseApplicationSearchContract.setApplicationDate(licenseApplication.getApplicationDate().toString());
			}
			licenseApplicationSearchContract.setApplicationNumber(licenseApplication.getApplicationNumber());
			licenseApplicationSearchContract.setApplicationType(licenseApplication.getApplicationType());
			licenseApplicationSearchContract.setFieldInspectionReport(licenseApplication.getFieldInspectionReport());
			licenseApplicationSearchContract.setLicenseFee(licenseApplication.getLicenseFee());

			if (licenseApplication.getAuditDetails() != null) {

				AuditDetails auditDetails = new AuditDetails();
				auditDetails.setCreatedBy(licenseApplication.getAuditDetails().getCreatedBy());
				auditDetails.setCreatedTime(licenseApplication.getAuditDetails().getCreatedTime());
				auditDetails.setLastModifiedBy(licenseApplication.getAuditDetails().getLastModifiedBy());
				auditDetails.setLastModifiedTime(licenseApplication.getAuditDetails().getLastModifiedTime());

				licenseApplicationSearchContract.setAuditDetails(auditDetails);
			}

			if (licenseApplication.getFeeDetails() != null && licenseApplication.getFeeDetails().size() > 0) {

				List<LicenseFeeDetailContract> licenseFeeDetailContracts = toFeeDetailsContract(requestInfo,
						licenseApplication.getFeeDetails());
				licenseApplicationSearchContract.setFeeDetails(licenseFeeDetailContracts);
			}

			if (licenseApplication.getSupportDocuments() != null
					&& licenseApplication.getSupportDocuments().size() > 0) {

				List<SupportDocumentSearchContract> supportDocumentSearchContracts = toSupportDocumentContract(
						requestInfo, licenseApplication.getSupportDocuments());
				licenseApplicationSearchContract.setSupportDocuments(supportDocumentSearchContracts);
			}
			
			
			if (licenseApplication.getLicenseBills() != null
					&& licenseApplication.getLicenseBills().size() > 0) {

				List<LicenseBillSearchContract> licenseBillSearchContracts = toLicenseBillContract(
						requestInfo, licenseApplication.getLicenseBills());
				licenseApplicationSearchContract.setLicenseBills(licenseBillSearchContracts);;
			}
			

			licenseApplicationSearchContracts.add(licenseApplicationSearchContract);
		}

		return licenseApplicationSearchContracts;
	}

	private List<LicenseBillSearchContract> toLicenseBillContract(RequestInfo requestInfo,
			List<LicenseBill> licenseBills) {
	
	
		List<LicenseBillSearchContract> licenseBillSearchContracts = new ArrayList<LicenseBillSearchContract>();

		for (LicenseBill licenseBill : licenseBills) {

			LicenseBillSearchContract licenseBillSearchContract = new LicenseBillSearchContract();

			licenseBillSearchContract.setId(licenseBill.getId());
			licenseBillSearchContract.setTenantId(licenseBill.getTenantId());
			licenseBillSearchContract.setApplicationId(licenseBill.getApplicationId());
			licenseBillSearchContract.setBillId(licenseBill.getBillId());
			

			if (licenseBill.getAuditDetails() != null) {

				AuditDetails auditDetails = new AuditDetails();
				auditDetails.setCreatedBy(licenseBill.getAuditDetails().getCreatedBy());
				auditDetails.setCreatedTime(licenseBill.getAuditDetails().getCreatedTime());
				auditDetails.setLastModifiedBy(licenseBill.getAuditDetails().getLastModifiedBy());
				auditDetails.setLastModifiedTime(licenseBill.getAuditDetails().getLastModifiedTime());

				licenseBillSearchContract.setAuditDetails(auditDetails);
			}

			licenseBillSearchContracts.add(licenseBillSearchContract);
		}

		return licenseBillSearchContracts;
	}

	private List<SupportDocumentSearchContract> toSupportDocumentContract(RequestInfo requestInfo,
			List<SupportDocument> supportDocuments) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		List<SupportDocumentSearchContract> supportDocumentSearchContracts = new ArrayList<SupportDocumentSearchContract>();

		for (SupportDocument supportDocument : supportDocuments) {

			SupportDocumentSearchContract supportDocumentSearchContract = new SupportDocumentSearchContract();

			supportDocumentSearchContract.setId(supportDocument.getId());
			supportDocumentSearchContract.setTenantId(supportDocument.getTenantId());
			supportDocumentSearchContract.setApplicationId(supportDocument.getApplicationId());
			supportDocumentSearchContract.setDocumentTypeId(supportDocument.getDocumentTypeId());
			supportDocumentSearchContract.setComments(supportDocument.getComments());
			supportDocumentSearchContract.setFileStoreId(supportDocument.getFileStoreId());

			if (supportDocument.getAuditDetails() != null) {

				AuditDetails auditDetails = new AuditDetails();
				auditDetails.setCreatedBy(supportDocument.getAuditDetails().getCreatedBy());
				auditDetails.setCreatedTime(supportDocument.getAuditDetails().getCreatedTime());
				auditDetails.setLastModifiedBy(supportDocument.getAuditDetails().getLastModifiedBy());
				auditDetails.setLastModifiedTime(supportDocument.getAuditDetails().getLastModifiedTime());

				supportDocumentSearchContract.setAuditDetails(auditDetails);
			}

			DocumentTypeV2Response documentTypeResponse = documentTypeRepository.findById(requestInfoWrapper,
					supportDocument.getTenantId(), supportDocument.getDocumentTypeId());
			if (documentTypeResponse != null && documentTypeResponse.getDocumentTypes().size() > 0) {
				supportDocumentSearchContract
						.setDocumentTypeName(documentTypeResponse.getDocumentTypes().get(0).getName());
			}

			supportDocumentSearchContracts.add(supportDocumentSearchContract);
		}

		return supportDocumentSearchContracts;
	}
	
	private List<LicenseFeeDetailContract> toFeeDetailsContract(RequestInfo requestInfo,
			List<LicenseFeeDetail> licenseFeeDetails) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		List<LicenseFeeDetailContract> licenseFeeDetailContracts = new ArrayList<LicenseFeeDetailContract>();

		for (LicenseFeeDetail licenseFeeDetail : licenseFeeDetails) {

			LicenseFeeDetailContract licenseFeeDetailContract = new LicenseFeeDetailContract();

			licenseFeeDetailContract.setAmount(licenseFeeDetail.getAmount());
			licenseFeeDetailContract.setApplicationId(licenseFeeDetail.getApplicationId());
			licenseFeeDetailContract.setFinancialYear(licenseFeeDetail.getFinancialYear());
			licenseFeeDetailContract.setId(licenseFeeDetail.getId());
			licenseFeeDetailContract.setPaid(licenseFeeDetail.getPaid());
			licenseFeeDetailContract.setTenantId(licenseFeeDetail.getTenantId());

			if (licenseFeeDetail.getAuditDetails() != null) {

				AuditDetails auditDetails = new AuditDetails();
				auditDetails.setCreatedBy(licenseFeeDetail.getAuditDetails().getCreatedBy());
				auditDetails.setCreatedTime(licenseFeeDetail.getAuditDetails().getCreatedTime());
				auditDetails.setLastModifiedBy(licenseFeeDetail.getAuditDetails().getLastModifiedBy());
				auditDetails.setLastModifiedTime(licenseFeeDetail.getAuditDetails().getLastModifiedTime());

				licenseFeeDetailContract.setAuditDetails(auditDetails);
			}

			FinancialYearContract finYearContract = financialYearRepository.findFinancialYearById(
					licenseFeeDetail.getTenantId(), licenseFeeDetail.getFinancialYear(), requestInfoWrapper);
			if (finYearContract != null) {
				licenseFeeDetailContract.setFinancialYear(finYearContract.getFinYearRange());
			}

			licenseFeeDetailContracts.add(licenseFeeDetailContract);
		}

		return licenseFeeDetailContracts;
	}

	private String getApplicationStatusName(String tenantId, String codes, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		String appStatus = "";
		LicenseStatusResponse licenseStatusResponse = statusRepository.findByCodes(tenantId, codes, requestInfoWrapper);
		if (licenseStatusResponse != null && licenseStatusResponse.getLicenseStatuses() != null
				&& licenseStatusResponse.getLicenseStatuses().size() > 0) {

			appStatus = licenseStatusResponse.getLicenseStatuses().get(0).getName();

		}

		return appStatus;

	}

	private Map<String, Map<String, String>> identifyDependencyFields(RequestInfo requestInfo,
			List<TradeLicense> tradeLicenses) {

		// preparing request info wrapper for the rest api calls
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		// getting the list of unique field codes
		Map<String, List<String>> uniqueCodes = getDependentFieldsUniqueIds(tradeLicenses);
		// map holds all field maps
		Map<String, Map<String, String>> uniqueFieldsCodeAndNameMap = new HashMap<String, Map<String, String>>();
		// map holds individual field maps with unique codes and corresponding
		// names
		Map<String, String> categoryCodeAndNameMap = new HashMap<String, String>();
		Map<String, String> subCategoryCodeAndNameMap = new HashMap<String, String>();
		Map<String, String> uomCodeAndNameMap = new HashMap<String, String>();
		Map<String, String> statusCodeAndNameMap = new HashMap<String, String>();
		Map<String, String> localityCodeAndNameMap = new HashMap<String, String>();
		Map<String, String> adminWardCodeAndNameMap = new HashMap<String, String>();
		Map<String, String> revenueWardCodeAndNameMap = new HashMap<String, String>();
		String tenantId = null;

		if (tradeLicenses != null && tradeLicenses.size() > 0) {
			tenantId = tradeLicenses.get(0).getTenantId();
		}

		// building category unique codes map
		if (uniqueCodes.get("categoryCodes") != null) {

			String codes = uniqueCodes.get("categoryCodes").toString();
			codes = codes.replace("[", "").replace("]", "");
			CategorySearchResponse categoryResponse = categoryContractRepository.findByCategoryCodes(tenantId, codes,
					requestInfoWrapper);
			if (categoryResponse != null && categoryResponse.getCategories() != null
					&& categoryResponse.getCategories().size() > 0) {

				for (CategorySearch category : categoryResponse.getCategories()) {
					categoryCodeAndNameMap.put(category.getCode().toString(), category.getName());
				}

			}
			uniqueFieldsCodeAndNameMap.put("categoryCodeAndNameMap", categoryCodeAndNameMap);
		}
		// building sub category unique codes map
		if (uniqueCodes.get("subCategoryCodes") != null) {

			String codes = uniqueCodes.get("subCategoryCodes").toString();
			codes = codes.replace("[", "").replace("]", "");
			CategorySearchResponse categoryResponse = categoryContractRepository.findBySubCategoryCodes(tenantId, codes,
					requestInfoWrapper);
			if (categoryResponse != null && categoryResponse.getCategories() != null
					&& categoryResponse.getCategories().size() > 0) {

				for (CategorySearch category : categoryResponse.getCategories()) {
					subCategoryCodeAndNameMap.put(category.getCode().toString(), category.getName());
				}

			}
			uniqueFieldsCodeAndNameMap.put("subCategoryCodeAndNameMap", subCategoryCodeAndNameMap);
		}
		// building uom unique codes map
		if (uniqueCodes.get("uomCodes") != null) {

			String codes = uniqueCodes.get("uomCodes").toString();
			codes = codes.replace("[", "").replace("]", "");
			UOMResponse uomResponse = categoryContractRepository.findByUomCodes(tenantId, codes, requestInfoWrapper);
			if (uomResponse != null && uomResponse.getUoms() != null && uomResponse.getUoms().size() > 0) {

				for (UOM uom : uomResponse.getUoms()) {
					uomCodeAndNameMap.put(uom.getCode().toString(), uom.getName());
				}

			}
			uniqueFieldsCodeAndNameMap.put("uomCodeAndNameMap", uomCodeAndNameMap);
		}
		// building status unique codes map
		if (uniqueCodes.get("statusCodes") != null) {

			String codes = uniqueCodes.get("statusCodes").toString();
			codes = codes.replace("[", "").replace("]", "");
			LicenseStatusResponse licenseStatusResponse = statusRepository.findByCodes(tenantId, codes, requestInfoWrapper);
			if (licenseStatusResponse != null && licenseStatusResponse.getLicenseStatuses() != null
					&& licenseStatusResponse.getLicenseStatuses().size() > 0) {

				for (LicenseStatus licenseStatus : licenseStatusResponse.getLicenseStatuses()) {
					statusCodeAndNameMap.put(licenseStatus.getCode().toString(), licenseStatus.getName());
				}

			}
			uniqueFieldsCodeAndNameMap.put("statusCodeAndNameMap", statusCodeAndNameMap);
		}
		// building locality unique codes map
		if (uniqueCodes.get("localityCodes") != null) {

			String codes = uniqueCodes.get("localityCodes").toString();
			codes = codes.replace("[", "").replace("]", "");
			BoundaryResponse boundaryResponse = boundaryContractRepository.findByLocalityCodes(tenantId, codes,
					requestInfoWrapper);
			if (boundaryResponse != null && boundaryResponse.getBoundarys() != null
					&& boundaryResponse.getBoundarys().size() > 0) {

				for (Boundary boundary : boundaryResponse.getBoundarys()) {
					localityCodeAndNameMap.put(boundary.getCode().toString(), boundary.getName());
				}

			}
			uniqueFieldsCodeAndNameMap.put("localityCodeAndNameMap", localityCodeAndNameMap);
		}
		// building adminWard unique codes map
		if (uniqueCodes.get("adminWardCodes") != null) {

			String codes = uniqueCodes.get("adminWardCodes").toString();
			codes = codes.replace("[", "").replace("]", "");
			BoundaryResponse boundaryResponse = boundaryContractRepository.findByAdminWardCodes(tenantId, codes,
					requestInfoWrapper);
			if (boundaryResponse != null && boundaryResponse.getBoundarys() != null
					&& boundaryResponse.getBoundarys().size() > 0) {

				for (Boundary boundary : boundaryResponse.getBoundarys()) {
					adminWardCodeAndNameMap.put(boundary.getCode().toString(), boundary.getName());
				}

			}
			uniqueFieldsCodeAndNameMap.put("adminWardCodeAndNameMap", adminWardCodeAndNameMap);
		}
		// building revenueWard unique codes map
		if (uniqueCodes.get("revenueWardCodes") != null) {

			String ids = uniqueCodes.get("revenueWardCodes").toString();
			ids = ids.replace("[", "").replace("]", "");
			BoundaryResponse boundaryResponse = boundaryContractRepository.findByRevenueWardCodes(tenantId, ids,
					requestInfoWrapper);
			if (boundaryResponse != null && boundaryResponse.getBoundarys() != null
					&& boundaryResponse.getBoundarys().size() > 0) {

				for (Boundary boundary : boundaryResponse.getBoundarys()) {
					revenueWardCodeAndNameMap.put(boundary.getCode().toString(), boundary.getName());
				}

			}
			uniqueFieldsCodeAndNameMap.put("revenueWardCodeAndNameMap", revenueWardCodeAndNameMap);
		}

		return uniqueFieldsCodeAndNameMap;
	}

	private Map<String, List<String>> getDependentFieldsUniqueIds(List<TradeLicense> tradeLicenses) {

		Map<String, List<String>> uniqueCodes = new HashMap<String, List<String>>();
		List<String> categoryCodes = new ArrayList<>();
		List<String> subCategoryCodes = new ArrayList<>();
		List<String> uomCodes = new ArrayList<>();
		List<String> statusCodes = new ArrayList<>();
		List<String> localityCodes = new ArrayList<>();
		List<String> adminWardCodes = new ArrayList<>();
		List<String> revenueWardCodes = new ArrayList<>();

		for (TradeLicense tradeLicense : tradeLicenses) {

			String category = tradeLicense.getCategory() != null ? tradeLicense.getCategory() : null;
			String subCategory = tradeLicense.getSubCategory() != null ? tradeLicense.getSubCategory() : null;
			String uom = tradeLicense.getUom() != null ? tradeLicense.getUom() : null;
			String status = tradeLicense.getStatus() != null ? tradeLicense.getStatus() : null;
			String locality = tradeLicense.getLocality() != null
					? tradeLicense.getLocality().toString() : null;
			String adminWard = tradeLicense.getAdminWard() != null
					? tradeLicense.getAdminWard().toString() : null;
			String revenueWard = tradeLicense.getRevenueWard() != null
					? tradeLicense.getRevenueWard().toString() : null;

			// category codes
			if (categoryCodes.size() > 0) {
				if (category != null && !categoryCodes.contains(category)) {
					categoryCodes.add(category);
				}
			} else if (category != null) {
				categoryCodes.add(category);
			}
			// sub Category codes
			if (subCategoryCodes.size() > 0) {
				if (subCategory != null && !subCategoryCodes.contains(subCategory)) {
					subCategoryCodes.add(subCategory);
				}
			} else if (subCategory != null) {
				subCategoryCodes.add(subCategory);
			}
			// uom codes
			if (uomCodes.size() > 0) {
				if (uom != null && !uomCodes.contains(uom)) {
					uomCodes.add(uom);
				}
			} else if (uom != null) {
				uomCodes.add(uom);
			}
			// status codes
			if (statusCodes.size() > 0) {
				if (status != null && !statusCodes.contains(status)) {
					statusCodes.add(status);
				}
			} else if (status != null) {
				statusCodes.add(status);
			}
			// locality Codes
			if (localityCodes.size() > 0) {
				if (locality != null && !localityCodes.contains(locality)) {
					localityCodes.add(locality);
				}
			} else if (locality != null) {
				localityCodes.add(locality);
			}
			// adminWard Codes
			if (adminWardCodes.size() > 0) {
				if (adminWard != null && !adminWardCodes.contains(adminWard)) {
					adminWardCodes.add(adminWard);
				}
			} else if (adminWard != null) {
				adminWardCodes.add(adminWard);
			}
			// revenueWard Codes
			if (revenueWardCodes.size() > 0) {
				if (revenueWard != null && !revenueWardCodes.contains(revenueWard)) {
					revenueWardCodes.add(revenueWard);
				}
			} else if (revenueWard != null) {
				revenueWardCodes.add(revenueWard);
			}
		}
		if (categoryCodes.size() > 0) {
			uniqueCodes.put("categoryCodes", categoryCodes);
		}
		if (subCategoryCodes.size() > 0) {
			uniqueCodes.put("subCategoryCodes", subCategoryCodes);
		}
		if (uomCodes.size() > 0) {
			uniqueCodes.put("uomCodes", uomCodes);
		}
		if (statusCodes.size() > 0) {
			uniqueCodes.put("statusCodes", statusCodes);
		}
		if (localityCodes.size() > 0) {
			uniqueCodes.put("localityCodes", localityCodes);
		}
		if (adminWardCodes.size() > 0) {
			uniqueCodes.put("adminWardCodes", adminWardCodes);
		}
		if (revenueWardCodes.size() > 0) {
			uniqueCodes.put("revenueWardCodes", revenueWardCodes);
		}

		return uniqueCodes;
	}
}
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
import org.egov.tl.commons.web.contract.UOM;
import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tl.commons.web.contract.enums.BusinessNatureEnum;
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

				if (uniqueFieldsMap.get("categoryIdAndNameMap") != null && domain.getCategoryId() != null) {
					categoryName = uniqueFieldsMap.get("categoryIdAndNameMap").get(domain.getCategoryId().toString());
				}
				if (uniqueFieldsMap.get("subCategoryIdAndNameMap") != null && domain.getSubCategoryId() != null) {
					subCategoryName = uniqueFieldsMap.get("subCategoryIdAndNameMap")
							.get(domain.getSubCategoryId().toString());
				}
				if (uniqueFieldsMap.get("uomIdAndNameMap") != null && domain.getUomId() != null) {
					uomName = uniqueFieldsMap.get("uomIdAndNameMap").get(domain.getUomId().toString());
				}
				if (uniqueFieldsMap.get("statusIdAndNameMap") != null && domain.getStatus() != null) {
					statusName = uniqueFieldsMap.get("statusIdAndNameMap").get(domain.getStatus().toString());
				}
				if (uniqueFieldsMap.get("localityIdAndNameMap") != null && domain.getLocalityId() != null) {
					localityName = uniqueFieldsMap.get("localityIdAndNameMap").get(domain.getLocalityId().toString());
				}
				if (uniqueFieldsMap.get("adminWardIdAndNameMap") != null && domain.getAdminWardId() != null) {
					adminWardName = uniqueFieldsMap.get("adminWardIdAndNameMap")
							.get(domain.getAdminWardId().toString());
				}
				if (uniqueFieldsMap.get("revenueWardIdAndNameMap") != null && domain.getRevenueWardId() != null) {
					revenueWardName = uniqueFieldsMap.get("revenueWardIdAndNameMap")
							.get(domain.getRevenueWardId().toString());
				}

				TradeLicenseSearchContract licenseSearchContract = new TradeLicenseSearchContract();

				licenseSearchContract.setId(domain.getId());
				licenseSearchContract.setTenantId(domain.getTenantId());
				licenseSearchContract.setLicenseNumber(domain.getLicenseNumber());
				licenseSearchContract.setOldLicenseNumber(domain.getOldLicenseNumber());
				licenseSearchContract.setAdhaarNumber(domain.getAdhaarNumber());
				licenseSearchContract.setMobileNumber(domain.getMobileNumber());
				licenseSearchContract.setOwnerName(domain.getOwnerName());
				licenseSearchContract.setFatherSpouseName(domain.getFatherSpouseName());
				licenseSearchContract.setEmailId(domain.getEmailId());
				licenseSearchContract.setOwnerAddress(domain.getOwnerAddress());
				licenseSearchContract.setPropertyAssesmentNo(domain.getPropertyAssesmentNo());
				licenseSearchContract.setLocalityId(domain.getLocalityId());
				licenseSearchContract.setLocalityName(localityName);
				licenseSearchContract.setRevenueWardId(domain.getRevenueWardId());
				licenseSearchContract.setRevenueWardName(revenueWardName);
				licenseSearchContract.setAdminWardId(domain.getAdminWardId());
				licenseSearchContract.setAdminWardName(adminWardName);
				licenseSearchContract.setStatus(domain.getStatus());
				licenseSearchContract.setStatusName(statusName);
				licenseSearchContract.setTradeAddress(domain.getTradeAddress());
				if (domain.getOwnerShipType() != null) {
					licenseSearchContract
							.setOwnerShipType(OwnerShipTypeEnum.valueOf(domain.getOwnerShipType().toString()));
				}
				licenseSearchContract.setTradeTitle(domain.getTradeTitle());
				if (domain.getTradeType() != null) {
					licenseSearchContract.setTradeType(BusinessNatureEnum.valueOf(domain.getTradeType().toString()));
				}
				licenseSearchContract.setCategoryId(domain.getCategoryId());
				licenseSearchContract.setCategory(categoryName);
				licenseSearchContract.setSubCategoryId(domain.getSubCategoryId());
				licenseSearchContract.setSubCategory(subCategoryName);
				licenseSearchContract.setUomId(domain.getUomId());
				licenseSearchContract.setUom(uomName);
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

				if (domain.getAuditDetails() != null) {

					AuditDetails auditDetails = new AuditDetails();
					auditDetails.setCreatedBy(domain.getAuditDetails().getCreatedBy());
					auditDetails.setCreatedTime(domain.getAuditDetails().getCreatedTime());
					auditDetails.setLastModifiedBy(domain.getAuditDetails().getLastModifiedBy());
					auditDetails.setLastModifiedTime(domain.getAuditDetails().getLastModifiedTime());
					licenseSearchContract.setAuditDetails(auditDetails);
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

	private List<LicenseApplicationSearchContract> toApplicationSearchContract(RequestInfo requestInfo,
			List<LicenseApplication> licenseApplications) {

		List<LicenseApplicationSearchContract> licenseApplicationSearchContracts = new ArrayList<LicenseApplicationSearchContract>();

		for (LicenseApplication licenseApplication : licenseApplications) {

			LicenseApplicationSearchContract licenseApplicationSearchContract = new LicenseApplicationSearchContract();

			String statusId = "";
			licenseApplicationSearchContract.setId(licenseApplication.getId());
			licenseApplicationSearchContract.setTenantId(licenseApplication.getTenantId());
			licenseApplicationSearchContract.setLicenseId(licenseApplication.getLicenseId());
			licenseApplicationSearchContract.setState_id(licenseApplication.getState_id());
			statusId = licenseApplication.getStatus();

			if (statusId != null && !statusId.isEmpty()) {
				licenseApplicationSearchContract.setStatus(statusId);
				licenseApplicationSearchContract.setStatusName(
						getApplicationStatusName(licenseApplication.getTenantId(), statusId, requestInfo));
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

	private String getApplicationStatusName(String tenantId, String ids, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		String appStatus = "";
		LicenseStatusResponse licenseStatusResponse = statusRepository.findByIds(tenantId, ids, requestInfoWrapper);
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
		// getting the list of unique field ids
		Map<String, List<Long>> uniqueIds = getDependentFieldsUniqueIds(tradeLicenses);
		// map holds all field maps
		Map<String, Map<String, String>> uniqueFieldsIdAndNameMap = new HashMap<String, Map<String, String>>();
		// map holds individual field maps with unique ids and corresponding
		// names
		Map<String, String> categoryIdAndNameMap = new HashMap<String, String>();
		Map<String, String> subCategoryIdAndNameMap = new HashMap<String, String>();
		Map<String, String> uomIdAndNameMap = new HashMap<String, String>();
		Map<String, String> statusIdAndNameMap = new HashMap<String, String>();
		Map<String, String> localityIdAndNameMap = new HashMap<String, String>();
		Map<String, String> adminWardIdAndNameMap = new HashMap<String, String>();
		Map<String, String> revenueWardIdAndNameMap = new HashMap<String, String>();
		String tenantId = null;

		if (tradeLicenses != null && tradeLicenses.size() > 0) {
			tenantId = tradeLicenses.get(0).getTenantId();
		}

		// building category unique ids map
		if (uniqueIds.get("categoryIds") != null) {

			String ids = uniqueIds.get("categoryIds").toString();
			ids = ids.replace("[", "").replace("]", "");
			CategorySearchResponse categoryResponse = categoryContractRepository.findByCategoryIds(tenantId, ids,
					requestInfoWrapper);
			if (categoryResponse != null && categoryResponse.getCategories() != null
					&& categoryResponse.getCategories().size() > 0) {

				for (CategorySearch category : categoryResponse.getCategories()) {
					categoryIdAndNameMap.put(category.getId().toString(), category.getName());
				}

			}
			uniqueFieldsIdAndNameMap.put("categoryIdAndNameMap", categoryIdAndNameMap);
		}
		// building sub category unique ids map
		if (uniqueIds.get("subCategoryIds") != null) {

			String ids = uniqueIds.get("subCategoryIds").toString();
			ids = ids.replace("[", "").replace("]", "");
			CategorySearchResponse categoryResponse = categoryContractRepository.findByCategoryIds(tenantId, ids,
					requestInfoWrapper);
			if (categoryResponse != null && categoryResponse.getCategories() != null
					&& categoryResponse.getCategories().size() > 0) {

				for (CategorySearch category : categoryResponse.getCategories()) {
					subCategoryIdAndNameMap.put(category.getId().toString(), category.getName());
				}

			}
			uniqueFieldsIdAndNameMap.put("subCategoryIdAndNameMap", subCategoryIdAndNameMap);
		}
		// building uom unique ids map
		if (uniqueIds.get("uomIds") != null) {

			String ids = uniqueIds.get("uomIds").toString();
			ids = ids.replace("[", "").replace("]", "");
			UOMResponse uomResponse = categoryContractRepository.findByUomIds(tenantId, ids, requestInfoWrapper);
			if (uomResponse != null && uomResponse.getUoms() != null && uomResponse.getUoms().size() > 0) {

				for (UOM uom : uomResponse.getUoms()) {
					uomIdAndNameMap.put(uom.getId().toString(), uom.getName());
				}

			}
			uniqueFieldsIdAndNameMap.put("uomIdAndNameMap", uomIdAndNameMap);
		}
		// building status unique ids map
		if (uniqueIds.get("statusIds") != null) {

			String ids = uniqueIds.get("statusIds").toString();
			ids = ids.replace("[", "").replace("]", "");
			LicenseStatusResponse licenseStatusResponse = statusRepository.findByIds(tenantId, ids, requestInfoWrapper);
			if (licenseStatusResponse != null && licenseStatusResponse.getLicenseStatuses() != null
					&& licenseStatusResponse.getLicenseStatuses().size() > 0) {

				for (LicenseStatus licenseStatus : licenseStatusResponse.getLicenseStatuses()) {
					statusIdAndNameMap.put(licenseStatus.getId().toString(), licenseStatus.getName());
				}

			}
			uniqueFieldsIdAndNameMap.put("statusIdAndNameMap", statusIdAndNameMap);
		}
		// building locality unique ids map
		if (uniqueIds.get("localityIds") != null) {

			String ids = uniqueIds.get("localityIds").toString();
			ids = ids.replace("[", "").replace("]", "");
			BoundaryResponse boundaryResponse = boundaryContractRepository.findByBoundaryIds(tenantId, ids,
					requestInfoWrapper);
			if (boundaryResponse != null && boundaryResponse.getBoundarys() != null
					&& boundaryResponse.getBoundarys().size() > 0) {

				for (Boundary boundary : boundaryResponse.getBoundarys()) {
					localityIdAndNameMap.put(boundary.getId().toString(), boundary.getName());
				}

			}
			uniqueFieldsIdAndNameMap.put("localityIdAndNameMap", localityIdAndNameMap);
		}
		// building adminWard unique ids map
		if (uniqueIds.get("adminWardIds") != null) {

			String ids = uniqueIds.get("adminWardIds").toString();
			ids = ids.replace("[", "").replace("]", "");
			BoundaryResponse boundaryResponse = boundaryContractRepository.findByBoundaryIds(tenantId, ids,
					requestInfoWrapper);
			if (boundaryResponse != null && boundaryResponse.getBoundarys() != null
					&& boundaryResponse.getBoundarys().size() > 0) {

				for (Boundary boundary : boundaryResponse.getBoundarys()) {
					adminWardIdAndNameMap.put(boundary.getId().toString(), boundary.getName());
				}

			}
			uniqueFieldsIdAndNameMap.put("adminWardIdAndNameMap", adminWardIdAndNameMap);
		}
		// building revenueWard unique ids map
		if (uniqueIds.get("revenueWardIds") != null) {

			String ids = uniqueIds.get("revenueWardIds").toString();
			ids = ids.replace("[", "").replace("]", "");
			BoundaryResponse boundaryResponse = boundaryContractRepository.findByBoundaryIds(tenantId, ids,
					requestInfoWrapper);
			if (boundaryResponse != null && boundaryResponse.getBoundarys() != null
					&& boundaryResponse.getBoundarys().size() > 0) {

				for (Boundary boundary : boundaryResponse.getBoundarys()) {
					revenueWardIdAndNameMap.put(boundary.getId().toString(), boundary.getName());
				}

			}
			uniqueFieldsIdAndNameMap.put("revenueWardIdAndNameMap", revenueWardIdAndNameMap);
		}

		return uniqueFieldsIdAndNameMap;
	}

	private Map<String, List<Long>> getDependentFieldsUniqueIds(List<TradeLicense> tradeLicenses) {

		Map<String, List<Long>> uniqueIds = new HashMap<String, List<Long>>();
		List<Long> categoryIds = new ArrayList<>();
		List<Long> subCategoryIds = new ArrayList<>();
		List<Long> uomIds = new ArrayList<>();
		List<Long> statusIds = new ArrayList<>();
		List<Long> localityIds = new ArrayList<>();
		List<Long> adminWardIds = new ArrayList<>();
		List<Long> revenueWardIds = new ArrayList<>();

		for (TradeLicense tradeLicense : tradeLicenses) {

			Long categoryId = tradeLicense.getCategoryId() != null ? tradeLicense.getCategoryId() : null;
			Long subCategoryId = tradeLicense.getSubCategoryId() != null ? tradeLicense.getSubCategoryId() : null;
			Long uomId = tradeLicense.getUomId() != null ? tradeLicense.getUomId() : null;
			Long statusId = tradeLicense.getStatus() != null ? tradeLicense.getStatus() : null;
			Long localityId = tradeLicense.getLocalityId() != null
					? Long.parseLong(tradeLicense.getLocalityId().toString()) : null;
			Long adminWardId = tradeLicense.getAdminWardId() != null
					? Long.parseLong(tradeLicense.getAdminWardId().toString()) : null;
			Long revenueWardId = tradeLicense.getRevenueWardId() != null
					? Long.parseLong(tradeLicense.getRevenueWardId().toString()) : null;

			// category ids
			if (categoryIds.size() > 0) {
				if (categoryId != null && !categoryIds.contains(categoryId)) {
					categoryIds.add(categoryId);
				}
			} else if (categoryId != null) {
				categoryIds.add(categoryId);
			}
			// sub Category ids
			if (subCategoryIds.size() > 0) {
				if (subCategoryId != null && !subCategoryIds.contains(subCategoryId)) {
					subCategoryIds.add(subCategoryId);
				}
			} else if (subCategoryId != null) {
				subCategoryIds.add(subCategoryId);
			}
			// uom ids
			if (uomIds.size() > 0) {
				if (uomId != null && !uomIds.contains(uomId)) {
					uomIds.add(uomId);
				}
			} else if (uomId != null) {
				uomIds.add(uomId);
			}
			// status ids
			if (statusIds.size() > 0) {
				if (statusId != null && !statusIds.contains(statusId)) {
					statusIds.add(statusId);
				}
			} else if (statusId != null) {
				statusIds.add(statusId);
			}
			// locality ids
			if (localityIds.size() > 0) {
				if (localityId != null && !localityIds.contains(localityId)) {
					localityIds.add(localityId);
				}
			} else if (localityId != null) {
				localityIds.add(localityId);
			}
			// adminWard ids
			if (adminWardIds.size() > 0) {
				if (adminWardId != null && !adminWardIds.contains(adminWardId)) {
					adminWardIds.add(adminWardId);
				}
			} else if (adminWardId != null) {
				adminWardIds.add(adminWardId);
			}
			// revenueWard ids
			if (revenueWardIds.size() > 0) {
				if (revenueWardId != null && !revenueWardIds.contains(revenueWardId)) {
					revenueWardIds.add(revenueWardId);
				}
			} else if (revenueWardId != null) {
				revenueWardIds.add(revenueWardId);
			}
		}
		if (categoryIds.size() > 0) {
			uniqueIds.put("categoryIds", categoryIds);
		}
		if (subCategoryIds.size() > 0) {
			uniqueIds.put("subCategoryIds", subCategoryIds);
		}
		if (uomIds.size() > 0) {
			uniqueIds.put("uomIds", uomIds);
		}
		if (statusIds.size() > 0) {
			uniqueIds.put("statusIds", statusIds);
		}
		if (localityIds.size() > 0) {
			uniqueIds.put("localityIds", localityIds);
		}
		if (adminWardIds.size() > 0) {
			uniqueIds.put("adminWardIds", adminWardIds);
		}
		if (revenueWardIds.size() > 0) {
			uniqueIds.put("revenueWardIds", revenueWardIds);
		}

		return uniqueIds;
	}
}
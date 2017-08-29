package org.egov.tradelicense.persistence.repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.CategorySearch;
import org.egov.tl.commons.web.contract.LicenseStatus;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.UOM;
import org.egov.tl.commons.web.requests.DocumentTypeV2Response;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.response.CategorySearchResponse;
import org.egov.tl.commons.web.response.LicenseStatusResponse;
import org.egov.tl.commons.web.response.UOMResponse;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.persistense.repository.JdbcRepository;
import org.egov.tradelicense.persistence.entity.LicenseApplicationSearchEntity;
import org.egov.tradelicense.persistence.entity.LicenseFeeDetailSearchEntity;
import org.egov.tradelicense.persistence.entity.SupportDocumentSearchEntity;
import org.egov.tradelicense.persistence.entity.TradeLicenseEntity;
import org.egov.tradelicense.persistence.entity.TradeLicenseSearchEntity;
import org.egov.tradelicense.web.contract.Boundary;
import org.egov.tradelicense.web.contract.FinancialYearContract;
import org.egov.tradelicense.web.repository.BoundaryContractRepository;
import org.egov.tradelicense.web.repository.CategoryContractRepository;
import org.egov.tradelicense.web.repository.DocumentTypeContractRepository;
import org.egov.tradelicense.web.repository.FinancialYearContractRepository;
import org.egov.tradelicense.web.repository.StatusRepository;
import org.egov.tradelicense.web.response.BoundaryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TradeLicenseJdbcRepository extends JdbcRepository {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private CategoryContractRepository categoryContractRepository;
	
	@Autowired
	private StatusRepository statusRepository;

	@Autowired
	private	BoundaryContractRepository boundaryContractRepository;

	@Autowired
	private	FinancialYearContractRepository financialYearRepository;

	@Autowired
	private	DocumentTypeContractRepository documentTypeRepository;
	
	

	private static final Logger LOG = LoggerFactory.getLogger(TradeLicenseJdbcRepository.class);
	static {
		LOG.debug("init accountCodePurpose");
		init(TradeLicenseEntity.class);
		LOG.debug("end init accountCodePurpose");
	}

	public TradeLicenseEntity create(TradeLicenseEntity entity) {

		super.create(entity);
		
		return entity;
	}

	public TradeLicenseEntity update(TradeLicenseEntity entity) {

		super.update(entity);
		
		return entity;
	}

	public TradeLicenseSearchEntity searchById(RequestInfo requestInfo, Long licenseId) {

		MapSqlParameterSource parameter = new MapSqlParameterSource();
		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + "egtl_license" + " where ");
		searchSql.append(" id = :id ");
		parameter.addValue("id", licenseId);
		List<TradeLicenseSearchEntity> searchEntities = executeSearchQuery(requestInfo, searchSql.toString(),
				parameter);
		TradeLicenseSearchEntity tradeLicenseSearchEntity = null;
		if (searchEntities != null && searchEntities.size() > 0) {
			tradeLicenseSearchEntity = searchEntities.get(0);
		}
		return tradeLicenseSearchEntity;
	}

	public List<TradeLicenseSearchEntity> search(RequestInfo requestInfo, String tenantId, Integer pageSize,
			Integer pageNumber, String sort, String active, Integer[] ids, String applicationNumber,
			String licenseNumber, String oldLicenseNumber, String mobileNumber, String aadhaarNumber, String emailId,
			String propertyAssesmentNo, Integer adminWard, Integer locality, String ownerName, String tradeTitle,
			String tradeType, Integer tradeCategory, Integer tradeSubCategory, String isLegacy, Integer status) {

		MapSqlParameterSource parameter = new MapSqlParameterSource();

		String query = buildSearchQuery(tenantId, pageSize, pageNumber, sort, active, ids, applicationNumber,
				licenseNumber, oldLicenseNumber, mobileNumber, aadhaarNumber, emailId, propertyAssesmentNo, adminWard,
				locality, ownerName, tradeTitle, tradeType, tradeCategory, tradeSubCategory, isLegacy, status, parameter);

		return executeSearchQuery(requestInfo, query, parameter);

	}

	public List<TradeLicenseSearchEntity> executeSearchQuery(RequestInfo requestInfo, String query,
			MapSqlParameterSource parameter) {

		List<TradeLicenseSearchEntity> tradeLicenses = new ArrayList<TradeLicenseSearchEntity>();
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(query, parameter);

		Map<String, Map<String, String>> uniqueFieldsMap = new HashMap<String, Map<String, String>>();
		if (rows != null && rows.size() > 0) {
			uniqueFieldsMap = identifyDependencyFields(requestInfo, rows);
		}

		for (Map<String, Object> row : rows) {

			String categoryName = null, subCategoryName = null, uomName = null, statusName = null, localityName = null,
					adminWardName = null, revenueWardName = null;

			if (uniqueFieldsMap.get("categoryIdAndNameMap") != null) {
				categoryName = uniqueFieldsMap.get("categoryIdAndNameMap").get(getString(row.get("categoryId")));
			}
			if (uniqueFieldsMap.get("subCategoryIdAndNameMap") != null) {
				subCategoryName = uniqueFieldsMap.get("subCategoryIdAndNameMap")
						.get(getString(row.get("subCategoryId")));
			}
			if (uniqueFieldsMap.get("uomIdAndNameMap") != null) {
				uomName = uniqueFieldsMap.get("uomIdAndNameMap").get(getString(row.get("uomId")));
			}
			if (uniqueFieldsMap.get("statusIdAndNameMap") != null) {
				statusName = uniqueFieldsMap.get("statusIdAndNameMap").get(getString(row.get("status")));
			}
			if (uniqueFieldsMap.get("localityIdAndNameMap") != null) {
				localityName = uniqueFieldsMap.get("localityIdAndNameMap").get(getString(row.get("localityId")));
			}
			if (uniqueFieldsMap.get("adminWardIdAndNameMap") != null) {
				adminWardName = uniqueFieldsMap.get("adminWardIdAndNameMap").get(getString(row.get("adminWardId")));
			}
			if (uniqueFieldsMap.get("revenueWardIdAndNameMap") != null) {
				revenueWardName = uniqueFieldsMap.get("revenueWardIdAndNameMap")
						.get(getString(row.get("revenueWardId")));
			}

			TradeLicenseSearchEntity license = new TradeLicenseSearchEntity();

			license.setId(getLong(row.get("id")));
			license.setTenantId(getString(row.get("tenantId")));
			
//			license.setApplicationType(getString(row.get("applicationType")));
//			license.setApplicationDate(TimeStampUtil.getTimeStampFromDB(getString(row.get("applicationDate"))));
//			license.setApplicationNumber(getString(row.get("applicationNumber")));
			
			if( getBoolean(row.get("isLegacy"))){
				Long applicationId = this.getLicenseApplication(license, requestInfo);
				license.setFeeDetailEntitys(getFeeDetails( applicationId,license.getTenantId(), requestInfo));
				license.setSupportDocumentEntitys(getSupportDocuments(applicationId,license.getTenantId(), requestInfo));
			}
			
			license.setLicenseApplicationSearchEntitys(this.getLicenseApplicationEntitys( license.getId(), requestInfo));
			
			
			
			license.setLicenseNumber(getString(row.get("licenseNumber")));
			license.setOldLicenseNumber(getString(row.get("oldLicenseNumber")));
			license.setAdhaarNumber(getString(row.get("adhaarNumber")));
			license.setMobileNumber(getString(row.get("mobileNumber")));
			license.setOwnerName(getString(row.get("ownerName")));
			license.setFatherSpouseName(getString(row.get("fatherSpouseName")));
			license.setEmailId(getString(row.get("emailId")));
			license.setOwnerAddress(getString(row.get("ownerAddress")));
			license.setPropertyAssesmentNo(getString(row.get("propertyAssesmentNo")));
			license.setLocalityId(Integer.valueOf(getString((row.get("localityId")))));
			license.setLocalityName(getString(localityName));
			license.setRevenueWardId(Integer.valueOf((getString(row.get("revenueWardId")))));
			license.setRevenueWardName(getString(revenueWardName));
			license.setAdminWardId(Integer.valueOf((getString(row.get("adminWardId")))));
			license.setAdminWardName(getString(adminWardName));
			license.setStatus(Long.valueOf((getString(row.get("status")))));
			license.setStatusName(getString(statusName));
			license.setTradeAddress(getString(row.get("tradeAddress")));
			license.setOwnerShipType(getString(row.get("ownerShipType")));
			license.setTradeTitle(getString(row.get("tradeTitle")));
			license.setTradeType((getString(row.get("tradeType"))));
			license.setCategoryId(getLong(row.get("categoryId")));
			license.setCategory(getString(categoryName));
			license.setSubCategoryId(getLong(row.get("subCategoryId")));
			license.setSubCategory(getString(subCategoryName));
			license.setUomId(getLong(row.get("uomId")));
			license.setUom(getString(uomName));
			license.setQuantity(getDouble(row.get("quantity")));
			license.setRemarks(getString(row.get("remarks")));
			license.setValidityYears(getLong(row.get("validityyears")));
			license.setTradeCommencementDate(((Timestamp)row.get("tradeCommencementDate")));
			license.setLicenseValidFromDate(
					((Timestamp)row.get("licenseValidFromDate")));
			license.setAgreementDate(((Timestamp)row.get("agreementDate")));
			license.setAgreementNo(getString(row.get("agreementNo")));
			license.setIsLegacy(getBoolean(row.get("isLegacy")));
			license.setActive(getBoolean(row.get("active")));
			license.setExpiryDate(((Timestamp)row.get("expiryDate")));
			license.setCreatedBy(getString(row.get("createdBy")));
			license.setLastModifiedBy(getString(row.get("lastModifiedBy")));
			license.setLastModifiedTime(getLong(row.get("lastModifiedTime")));
			license.setCreatedTime(getLong(row.get("createdTime")));
			tradeLicenses.add(license);
		}

		return tradeLicenses;
	}

	public List<LicenseFeeDetailSearchEntity> getFeeDetails(Long applicationId,String tenantId, RequestInfo requestInfo) {

		String query = buildFeeDetailsSearchQuery(applicationId.intValue());
		List<LicenseFeeDetailSearchEntity> licenses = executeFeeDetailsQuery(query, tenantId, requestInfo);

		return licenses;
	}

	private List<LicenseFeeDetailSearchEntity> executeFeeDetailsQuery(String query, String tenantId,
			RequestInfo requestInfo) {

		// preparing request info wrapper for the rest api calls
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		FinancialYearContract finYearContract;
		MapSqlParameterSource parameter = new MapSqlParameterSource();
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(query, parameter);
		List<LicenseFeeDetailSearchEntity> feeDetails = new ArrayList<LicenseFeeDetailSearchEntity>();
		for (Map<String, Object> row : rows) {
			LicenseFeeDetailSearchEntity feeDetail = new LicenseFeeDetailSearchEntity();
			feeDetail.setId(getLong(row.get("id")));
			feeDetail.setApplicationId( getLong(row.get("applicationId")));
			feeDetail.setAmount(getDouble(row.get("amount")));
			feeDetail.setTenantId( getString(row.get("tenantId")));
			feeDetail.setPaid(getBoolean(row.get("paid")));
//			feeDetail.setLicenseId(getLong(row.get("licenseid")));
			feeDetail.setFinancialYear(getString(row.get("financialyear")));
			feeDetail.setCreatedBy(getString(row.get("createdby")));
			feeDetail.setCreatedTime(getLong(row.get("createdtime")));
			feeDetail.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			feeDetail.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));

			finYearContract = financialYearRepository.findFinancialYearById(tenantId,
					getString(row.get("financialyear")), requestInfoWrapper);
			if (finYearContract != null) {
				feeDetail.setFinancialYear(finYearContract.getFinYearRange());
			}

			feeDetails.add(feeDetail);

		}
		return feeDetails;
	}

	private String buildFeeDetailsSearchQuery(Integer licenseId) {
		StringBuilder builder = new StringBuilder("select * from egtl_fee_details where ");
		builder.append("applicationid = ");
		builder.append(licenseId);
		return builder.toString();
	}

	public Long getLicenseApplication(TradeLicenseSearchEntity license,
			RequestInfo requestInfo){
		
		StringBuilder builder = new StringBuilder("select * from egtl_license_application where ");
		builder.append("licenseid = ");
		builder.append(license.getId().intValue());
		MapSqlParameterSource parameter = new MapSqlParameterSource();
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(builder.toString(), parameter);
		for (Map<String, Object> row : rows) {
			
			license.setApplicationDate(((Timestamp)row.get("applicationDate")));
			license.setApplicationNumber( getString( row.get("applicationNumber")));
			license.setApplicationType( getString(row.get("applicationType")));
		}
		
		return (getLong(rows.get(0).get("id")));
	}
	
	public Long getLegacyLicenseApplicationId(Long licenseId){
		
		StringBuilder builder = new StringBuilder("select * from egtl_license_application where ");
		builder.append("licenseid = ");
		if( licenseId == null){
			return null;
		}
		builder.append(licenseId.intValue());
		MapSqlParameterSource parameter = new MapSqlParameterSource();
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(builder.toString(), parameter);
		
		
		return (getLong(rows.get(0).get("id")));
	}
	
	public List<LicenseApplicationSearchEntity> getLicenseApplicationEntitys(Long licenseId,
			RequestInfo requestInfo){
		
		StringBuilder builder = new StringBuilder("select * from egtl_license_application where ");
		builder.append("licenseid = ");
		builder.append(licenseId.intValue());
		
		MapSqlParameterSource parameter = new MapSqlParameterSource();
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(builder.toString(), parameter);
		List<LicenseApplicationSearchEntity> licenseApplications = new ArrayList<LicenseApplicationSearchEntity>();
		for (Map<String, Object> row : rows) {
			
			LicenseApplicationSearchEntity licenseApplication = new LicenseApplicationSearchEntity(); 
			licenseApplication.setId( getLong(row.get("id")));
			licenseApplication.setTenantId(getString(row.get("tenantId")));
			licenseApplication.setApplicationDate(((Timestamp)row.get("applicationDate")));
			licenseApplication.setApplicationNumber( getString( row.get("applicationNumber")));
			licenseApplication.setApplicationType( getString(row.get("applicationType")));
			licenseApplication.setCreatedBy( getString(row.get("createdBy")));
			licenseApplication.setCreatedTime( getLong(row.get("createdTime")));
			licenseApplication.setLastModifiedBy(getString(row.get("lastModifiedBy")));
			licenseApplication.setLastModifiedTime( getLong(row.get("lastModifiedTime")));
			licenseApplication.setFeeDetailEntitys(getFeeDetails(licenseApplication.getId(),licenseApplication.getTenantId(), requestInfo));
			licenseApplication.setSupportDocumentEntitys(getSupportDocuments(licenseApplication.getId(),licenseApplication.getTenantId(), requestInfo));
			licenseApplications.add(licenseApplication);
		}
		
		return licenseApplications;
	}
	public List<SupportDocumentSearchEntity> getSupportDocuments(Long applicationId, String tenantId,
			RequestInfo requestInfo) {

		String query = buildSupportedSearchQuery(applicationId.intValue());
		List<SupportDocumentSearchEntity> supportedDocs = executeSupportedDocumentQuery(query, tenantId, requestInfo);

		return supportedDocs;
	}

	private List<SupportDocumentSearchEntity> executeSupportedDocumentQuery(String query,
			String tenatId, RequestInfo requestInfo) {

		// preparing request info wrapper for the rest api calls
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		DocumentTypeV2Response documentTypeResponse;
		MapSqlParameterSource parameter = new MapSqlParameterSource();
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(query, parameter);
		List<SupportDocumentSearchEntity> supportedDocuments = new ArrayList<SupportDocumentSearchEntity>();
		for (Map<String, Object> row : rows) {
			SupportDocumentSearchEntity supportedDocument = new SupportDocumentSearchEntity();
			supportedDocument.setId(getLong(row.get("id")));
			supportedDocument.setTenantId(getString(row.get("tenantId")));
			supportedDocument.setApplicationId( getLong(row.get("applicationid")));
			supportedDocument.setDocumentTypeId(getLong(row.get("documenttypeid")));
			supportedDocument.setFileStoreId(getString(row.get("filestoreid")));
			supportedDocument.setComments(getString(row.get("comments")));
//			supportedDocument.setLicenseId(getLong(row.get("licenseid")));
			supportedDocument.setCreatedBy(getString(row.get("createdby")));
			supportedDocument.setCreatedTime(getLong(row.get("createdtime")));
			supportedDocument.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			supportedDocument.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));

			documentTypeResponse = documentTypeRepository.findById(requestInfoWrapper, tenatId,
					supportedDocument.getDocumentTypeId());
			if (documentTypeResponse != null && documentTypeResponse.getDocumentTypes().size() > 0) {
				supportedDocument.setDocumentTypeName(documentTypeResponse.getDocumentTypes().get(0).getName());
			}
			supportedDocuments.add(supportedDocument);

		}
		return supportedDocuments;
	}

	private String buildSupportedSearchQuery(Integer licenseId) {

		StringBuilder builder = new StringBuilder("select * from egtl_support_document where ");
		builder.append("applicationid = ");
		builder.append(licenseId);

		return builder.toString();
	}

	public String buildSearchQuery(String tenantId, Integer pageSize, Integer pageNumber, String sort, String active,
			Integer[] ids, String applicationNumber, String licenseNumber, String oldLicenseNumber, String mobileNumber,
			String aadhaarNumber, String emailId, String propertyAssesmentNo, Integer adminWard, Integer locality,
			String ownerName, String tradeTitle, String tradeType, Integer tradeCategory, Integer tradeSubCategory,
			String isLegacy, Integer status, MapSqlParameterSource parameter) {

		StringBuffer searchSql = new StringBuffer();
		searchSql.append("select * from " + "egtl_license" + " where ");
		searchSql.append(" tenantId = :tenantId ");
		parameter.addValue("tenantId", tenantId);

		if (active != null && !active.trim().isEmpty()) {

			if (active.equalsIgnoreCase("true")) {
				searchSql.append(" AND active = :active ");
				parameter.addValue("active", true);
			} else if (active.equalsIgnoreCase("false")) {
				searchSql.append(" AND active = :active ");
				parameter.addValue("active", false);
			}

		}
		
		if (isLegacy != null && !isLegacy.trim().isEmpty()) {

			if (isLegacy.equalsIgnoreCase("true")) {
				searchSql.append(" AND isLegacy = :isLegacy ");
				parameter.addValue("isLegacy", true);
			} else if (isLegacy.equalsIgnoreCase("false")) {
				searchSql.append(" AND isLegacy = :isLegacy ");
				parameter.addValue("isLegacy", false);
			}

		}

		if (ids != null && ids.length > 0) {

			String searchIds = "";
			int count = 1;
			for (Integer id : ids) {

				if (count < ids.length)
					searchIds = searchIds + id + ",";
				else
					searchIds = searchIds + id;

				count++;
			}
			searchSql.append(" AND id IN (" + searchIds + ") ");
		}

		if (applicationNumber != null && !applicationNumber.trim().isEmpty()) {
			searchSql.append(" AND upper(applicationNumber)  like :applicationNumber");
			parameter.addValue("applicationNumber", '%' + applicationNumber.toUpperCase() + '%');
		}

		if (licenseNumber != null && !licenseNumber.trim().isEmpty()) {
			searchSql.append(" AND upper(licenseNumber)  like  :licenseNumber ");
			parameter.addValue("licenseNumber", '%' +  licenseNumber.toUpperCase() + '%');
		}

		if (oldLicenseNumber != null && !oldLicenseNumber.trim().isEmpty()) {
			searchSql.append(" AND upper(oldLicenseNumber) like :oldLicenseNumber ");
			parameter.addValue("oldLicenseNumber", '%' + oldLicenseNumber.toUpperCase() + '%');
		}

		if (mobileNumber != null && !mobileNumber.trim().isEmpty()) {
			searchSql.append(" AND mobileNumber = :mobileNumber ");
			parameter.addValue("mobileNumber", mobileNumber);
		}

		if (aadhaarNumber != null && !aadhaarNumber.trim().isEmpty()) {
			searchSql.append(" AND adhaarNumber = :adhaarNumber ");
			parameter.addValue("adhaarNumber", aadhaarNumber);
		}

		if (emailId != null && !emailId.trim().isEmpty()) {
			searchSql.append(" AND emailId = :emailId ");
			parameter.addValue("emailId", emailId);
		}

		if (propertyAssesmentNo != null && !propertyAssesmentNo.trim().isEmpty()) {
			searchSql.append(" AND upper(propertyAssesmentNo) like :propertyAssesmentNo ");
			parameter.addValue("propertyAssesmentNo", '%' + propertyAssesmentNo.toUpperCase() + '%');
		}

		if (adminWard != null) {
			searchSql.append(" AND adminWardId = :adminWardId ");
			parameter.addValue("adminWardId", adminWard);
		}

		if (locality != null) {
			searchSql.append(" AND localityId = :localityId ");
			parameter.addValue("localityId", locality);
		}

		if (ownerName != null && !ownerName.trim().isEmpty()) {
			searchSql.append(" AND upper(ownerName) like :ownerName ");
			parameter.addValue("ownerName", '%' + ownerName.toUpperCase() + '%');
		}

		if (tradeTitle != null && !tradeTitle.trim().isEmpty()) {
			searchSql.append(" AND upper(tradeTitle) like :tradeTitle ");
			parameter.addValue("tradeTitle", '%' + tradeTitle.toUpperCase() + '%');
		}

		if (tradeType != null && !tradeType.trim().isEmpty()) {
			searchSql.append(" AND tradeType = :tradeType ");
			parameter.addValue("tradeType", tradeType);
		}
		if (tradeCategory != null) {
			searchSql.append(" AND categoryId = :categoryId ");
			parameter.addValue("categoryId", tradeCategory);
		}

		if (tradeSubCategory != null) {
			searchSql.append(" AND subCategoryId = :subCategoryId ");
			parameter.addValue("subCategoryId", tradeSubCategory);
		}

		if (status != null) {
			searchSql.append(" AND status = :status ");
			parameter.addValue("status", status);
		}

		if (pageSize == null) {
			pageSize = Integer.valueOf(propertiesManager.getPageSize());
		}

		if (pageNumber == null) {
			pageNumber = Integer.valueOf(propertiesManager.getPageNumber());
		}

		if (sort == null || sort.isEmpty()) {

			searchSql.append("  ORDER BY licenseNumber ASC");
		} else if (sort != null && sort.trim().isEmpty()) {

			searchSql.append("  ORDER BY licenseNumber ASC");
		} else {
			searchSql.append("  ORDER BY licenseNumber,tradeTitle,ownerName ASC");
		}

		searchSql.append(" offset :offset ");
		parameter.addValue("offset", ((pageNumber - 1) * pageSize));

		searchSql.append(" limit :limit ");
		parameter.addValue("limit", pageSize);

		return searchSql.toString();
	}

	private Map<String, Map<String, String>> identifyDependencyFields(RequestInfo requestInfo,
			List<Map<String, Object>> rows) {

		// preparing request info wrapper for the rest api calls
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		// getting the list of unique field ids
		Map<String, List<Long>> uniqueIds = getDependentFieldsUniqueIds(rows);
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

		if (rows != null && rows.size() > 0) {
			tenantId = getString(rows.get(0).get("tenantId"));
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
			LicenseStatusResponse licenseStatusResponse = statusRepository.findByIds(tenantId, ids,
					requestInfoWrapper);
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

	private Map<String, List<Long>> getDependentFieldsUniqueIds(List<Map<String, Object>> rows) {

		Map<String, List<Long>> uniqueIds = new HashMap<String, List<Long>>();
		List<Long> categoryIds = new ArrayList<>();
		List<Long> subCategoryIds = new ArrayList<>();
		List<Long> uomIds = new ArrayList<>();
		List<Long> statusIds = new ArrayList<>();
		List<Long> localityIds = new ArrayList<>();
		List<Long> adminWardIds = new ArrayList<>();
		List<Long> revenueWardIds = new ArrayList<>();

		for (Map<String, Object> row : rows) {

			Long categoryId = getLong(row.get("categoryId"));
			Long subCategoryId = getLong(row.get("subCategoryId"));
			Long uomId = getLong(row.get("uomId"));
			Long statusId = getLong(row.get("status"));
			Long localityId = getLong(row.get("localityId"));
			Long adminWardId = getLong(row.get("adminWardId"));
			Long revenueWardId = getLong(row.get("revenueWardId"));

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
				if (uomIds != null && !uomIds.contains(uomId)) {
					uomIds.add(uomId);
				}
			} else if (uomIds != null) {
				uomIds.add(uomId);
			}
			// status ids
			if (statusIds.size() > 0) {
				if (statusIds != null && !statusIds.contains(statusId)) {
					statusIds.add(statusId);
				}
			} else if (statusIds != null) {
				statusIds.add(statusId);
			}
			// locality ids
			if (localityIds.size() > 0) {
				if (localityIds != null && !localityIds.contains(localityId)) {
					localityIds.add(localityId);
				}
			} else if (localityIds != null) {
				localityIds.add(localityId);
			}
			// adminWard ids
			if (adminWardIds.size() > 0) {
				if (adminWardIds != null && !adminWardIds.contains(adminWardId)) {
					adminWardIds.add(adminWardId);
				}
			} else if (adminWardIds != null) {
				adminWardIds.add(adminWardId);
			}
			// revenueWard ids
			if (revenueWardIds.size() > 0) {
				if (revenueWardIds != null && !revenueWardIds.contains(revenueWardId)) {
					revenueWardIds.add(revenueWardId);
				}
			} else if (revenueWardIds != null) {
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

	/**
	 * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String}
	 */
	private String getString(Object object) {
		return object == null ? "" : object.toString();
	}

	/**
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Long getLong(Object object) {
		return object == null ? 0 : Long.parseLong(object.toString());
	}

	/**
	 * This method will cast the given object to double
	 * 
	 * @param object
	 *            that need to be cast to Double
	 * @return {@link Double}
	 */
	@SuppressWarnings("unused")
	private Double getDouble(Object object) {
		return object == null ? 0.0 : Double.parseDouble(object.toString());
	}

	/**
	 * This method will cast the given object to Boolean
	 * 
	 * @param object
	 *            that need to be cast to Boolean
	 * @return {@link boolean}
	 */
	private Boolean getBoolean(Object object) {
		return object == null ? Boolean.FALSE : (Boolean) object;
	}

}
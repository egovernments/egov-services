package org.egov.tradelicense.domain.repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.domain.exception.CustomInvalidInputException;
import org.egov.tradelicense.common.domain.exception.DuplicateTradeApplicationException;
import org.egov.tradelicense.common.domain.exception.DuplicateTradeLicenseException;
import org.egov.tradelicense.common.domain.exception.IdNotFoundException;
import org.egov.tradelicense.domain.enums.NewLicenseStatus;
import org.egov.tradelicense.domain.model.LicenseApplication;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.LicenseSearch;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.model.TradeLicenseSearch;
import org.egov.tradelicense.domain.repository.builder.LicenseBillQueryBuilder;
import org.egov.tradelicense.persistence.entity.LicenseApplicationEntity;
import org.egov.tradelicense.persistence.entity.LicenseFeeDetailEntity;
import org.egov.tradelicense.persistence.entity.SupportDocumentEntity;
import org.egov.tradelicense.persistence.entity.TradeLicenseEntity;
import org.egov.tradelicense.persistence.entity.TradeLicenseSearchEntity;
import org.egov.tradelicense.persistence.queue.TradeLicenseQueueRepository;
import org.egov.tradelicense.persistence.repository.LicenseApplicationJdbcRepository;
import org.egov.tradelicense.persistence.repository.LicenseFeeDetailJdbcRepository;
import org.egov.tradelicense.persistence.repository.SupportDocumentJdbcRepository;
import org.egov.tradelicense.persistence.repository.TradeLicenseJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TradeLicenseRepository {

	@Autowired
	TradeLicenseQueueRepository tradeLicenseQueueRepository;

	@Autowired
	TradeLicenseJdbcRepository tradeLicenseJdbcRepository;

	@Autowired
	SupportDocumentJdbcRepository supportDocumentJdbcRepository;

	@Autowired
	LicenseFeeDetailJdbcRepository licenseFeeDetailJdbcRepository;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	LicenseApplicationJdbcRepository licenseApplicationJdbcRepository;

	public Long getNextSequence() {

		String id = tradeLicenseJdbcRepository.getSequence(TradeLicenseEntity.SEQUENCE_NAME);
		return Long.valueOf(id);
	}

	public Long getApplicationNextSequence() {
		String id = licenseApplicationJdbcRepository.getSequence(LicenseApplicationEntity.SEQUENCE_NAME);
		return Long.valueOf(id);
	}

	public void validateUniqueLicenseNumber(TradeLicense tradeLicense, Boolean isNewRecord, RequestInfo requestInfo) {

		String sql = getUniqueTenantLicenseQuery(tradeLicense, isNewRecord);
		Integer count = null;

		try {
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			count = (Integer) namedParameterJdbcTemplate.queryForObject(sql, parameters, Integer.class);
		} catch (Exception e) {
			log.error("error while executing the query :" + sql + " , error message : " + e.getMessage());
		}

		if (count != 0) {
			throw new DuplicateTradeLicenseException(propertiesManager.getDuplicateOldTradeLicenseMsg(), requestInfo);
		}
	}

	public void validateUniqueApplicationNumber(TradeLicense tradeLicense, Boolean isNewRecord,
			RequestInfo requestInfo) {

		String sql = getUniqueTenantApplicationQuery(tradeLicense, isNewRecord);
		Integer count = null;

		try {
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			count = (Integer) namedParameterJdbcTemplate.queryForObject(sql, parameters, Integer.class);
		} catch (Exception e) {
			log.error("error while executing the query :" + sql + " , error message : " + e.getMessage());
		}

		if (count != 0) {
			throw new DuplicateTradeApplicationException(propertiesManager.getDuplicateTradeApplicationNumberMsg(),
					requestInfo);
		}
	}

	private String getUniqueTenantLicenseQuery(TradeLicense tradeLicense, Boolean isNewRecord) {

		String tenantId = tradeLicense.getTenantId();
		Long id = tradeLicense.getId();
		String licNumber = "";

		StringBuffer uniqueQuery = new StringBuffer("select count(*) from egtl_license");
		uniqueQuery.append("  where tenantId = '" + tenantId + "'");

		if (tradeLicense.getIsLegacy() && isNewRecord) {
			licNumber = tradeLicense.getOldLicenseNumber();
			uniqueQuery.append(" AND  oldLicenseNumber = '" + licNumber + "'");
		} else {
			licNumber = tradeLicense.getLicenseNumber();
			uniqueQuery.append(" AND  licenseNumber = '" + licNumber + "'");
		}

		if (id != null && !isNewRecord) {
			uniqueQuery.append(" AND id != " + id);
		}
		return uniqueQuery.toString();
	}

	private String getUniqueTenantApplicationQuery(TradeLicense tradeLicense, Boolean isNewRecord) {

		String tenantId = tradeLicense.getTenantId();
		Long id = tradeLicense.getApplication().getId();
		String appNumber = tradeLicense.getApplication().getApplicationNumber();

		StringBuffer uniqueQuery = new StringBuffer("select count(*) from egtl_license_application");
		uniqueQuery.append("  where tenantId = '" + tenantId + "' AND  applicationnumber = '" + appNumber + "'");

		if (id != null && !isNewRecord) {
			uniqueQuery.append(" AND id != " + id);
		}
		return uniqueQuery.toString();
	}

	public void validateTradeLicenseId(TradeLicense tradeLicense, RequestInfo requestInfo) {

		String tableName = "egtl_license";
		Long id = tradeLicense.getId();
		String sql = getLicenseIdQuery(tableName, id);
		Integer count = null;

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		count = (Integer) namedParameterJdbcTemplate.queryForObject(sql, parameters, Integer.class);

		if (count == 0) {
			throw new IdNotFoundException(propertiesManager.getOldLicenseIdNotValidCustomMsg(),
					propertiesManager.getIdField(), requestInfo);
		}
	}

	public void validateTradeLicenseSupportDocumentId(SupportDocument supportDocument, RequestInfo requestInfo) {

		String tableName = "egtl_support_document";
		Long id = supportDocument.getId();
		String sql = getLicenseIdQuery(tableName, id);
		Integer count = null;

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		count = (Integer) namedParameterJdbcTemplate.queryForObject(sql, parameters, Integer.class);

		if (count == 0) {
			throw new IdNotFoundException(propertiesManager.getSupportDocumentIdNotValidCustomMsg(),
					propertiesManager.getIdField(), requestInfo);
		}
	}

	public void validateTradeLicenseFeeDetailId(LicenseFeeDetail licenseFeeDetail, RequestInfo requestInfo) {

		String tableName = "egtl_fee_details";
		Long id = licenseFeeDetail.getId();
		String sql = getLicenseIdQuery(tableName, id);
		Integer count = null;

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		count = (Integer) namedParameterJdbcTemplate.queryForObject(sql, parameters, Integer.class);

		if (count == 0) {
			throw new IdNotFoundException(propertiesManager.getFeeDetailIdNotValidCustomMsg(),
					propertiesManager.getIdField(), requestInfo);
		}
	}

	public Boolean validateUpdateTradeSupportDocumentId(SupportDocument supportDocument) {

		String tableName = "egtl_support_document";
		Long id = supportDocument.getId();
		String sql = getLicenseIdQuery(tableName, id);
		Integer count = null;
		Boolean isExists = Boolean.TRUE;
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		count = (Integer) namedParameterJdbcTemplate.queryForObject(sql, parameters, Integer.class);

		if (count == 0) {
			isExists = Boolean.FALSE;
		}

		return isExists;
	}

	public Boolean validateUpdateTradeApplicationId(LicenseApplication licenseApplication) {

		String tableName = "egtl_license_application";
		Long id = licenseApplication.getId();
		String sql = getLicenseIdQuery(tableName, id);
		Integer count = null;
		Boolean isExists = Boolean.TRUE;
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		count = (Integer) namedParameterJdbcTemplate.queryForObject(sql, parameters, Integer.class);

		if (count == 0) {
			isExists = Boolean.FALSE;
		}

		return isExists;
	}

	public Boolean validateUpdateTradeLicenseFeeDetailId(LicenseFeeDetail licenseFeeDetail) {

		String tableName = "egtl_fee_details";
		Long id = licenseFeeDetail.getId();
		String sql = getLicenseIdQuery(tableName, id);
		Integer count = null;
		Boolean isExists = Boolean.TRUE;
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		count = (Integer) namedParameterJdbcTemplate.queryForObject(sql, parameters, Integer.class);

		if (count == 0) {
			isExists = Boolean.FALSE;
		}

		return isExists;
	}

	private String getLicenseIdQuery(String tableName, Long id) {

		StringBuffer uniqueQuery = new StringBuffer("select count(*) from " + tableName);
		uniqueQuery.append(" where id = " + id);

		return uniqueQuery.toString();
	}

	/*
	 * public void validateUniqueAgreeMentNumber(TradeLicense tradeLicense) {
	 * 
	 * String sql = getUniqueTenantAgreementQuery(tradeLicense); Integer count =
	 * null; try { MapSqlParameterSource parameters = new
	 * MapSqlParameterSource(); count = (Integer)
	 * namedParameterJdbcTemplate.queryForObject(sql, parameters,
	 * Integer.class); } catch (Exception e) { log.error(
	 * "error while executing the query :" + sql + " , error message : " +
	 * e.getMessage()); }
	 * 
	 * if (count != 0) { throw new InvalidInputException(
	 * "agreementNumber number already exists"); } }
	 * 
	 * private String getUniqueTenantAgreementQuery(TradeLicense tradeLicense) {
	 * 
	 * String tenantId = tradeLicense.getTenantId().toLowerCase(); String
	 * agreement = tradeLicense.getAgreementNo().toLowerCase();
	 * 
	 * StringBuffer uniqueQuery = new StringBuffer(
	 * "select count(*) from egtl_license"); uniqueQuery.append(
	 * " where LOWER(agreementNo) = '" + agreement + "'"); uniqueQuery.append(
	 * " AND LOWER(tenantId) = '" + tenantId + "'");
	 * 
	 * return uniqueQuery.toString(); }
	 */

	public Long getSupportDocumentNextSequence() {

		String id = tradeLicenseJdbcRepository.getSequence(SupportDocumentEntity.SEQUENCE_NAME);
		return Long.valueOf(id);
	}

	public Long getFeeDetailNextSequence() {

		String id = tradeLicenseJdbcRepository.getSequence(LicenseFeeDetailEntity.SEQUENCE_NAME);
		return Long.valueOf(id);
	}

	public void add(TradeLicenseRequest request, Boolean isNewRecord) {

		if (request != null && request.getLicenses() != null && request.getLicenses().size() > 0) {

			if (isNewRecord) {

				if (request.getLicenses().get(0).getIsLegacy()) {

					request.getRequestInfo().setAction("legacy-create");

				} else {

					request.getRequestInfo().setAction("new-create");
				}

			} else {

				if (request.getLicenses().get(0).getIsLegacy()) {

					request.getRequestInfo().setAction("legacy-update");

				} else {

					request.getRequestInfo().setAction("new-update");
				}
			}

		}

		tradeLicenseQueueRepository.add(request);
	}

	@Transactional
	public TradeLicense save(TradeLicense tradeLicense) {

		TradeLicenseEntity entity = tradeLicenseJdbcRepository.create(new TradeLicenseEntity().toEntity(tradeLicense));
		// TODO : Application Id is fetched assuming there will be only one
		// applicaiton for given license
		LicenseApplicationEntity applicationEntity = new LicenseApplicationEntity();
		// if( tradeLicense.getIsLegacy() ){
		// applicationEntity = entity.getLicenseApplicationEntity();
		// }else{
		// applicationEntity =
		// applicationEntity.toEntity(tradeLicense.getApplication());
		// }

		// applicationEntity.setId(
		// Long.valueOf(licenseApplicationJdbcRepository.getSequence(LicenseApplicationEntity.SEQUENCE_NAME)));

		tradeLicense.getApplication().setAuditDetails(tradeLicense.getAuditDetails());
		licenseApplicationJdbcRepository.create(applicationEntity.toEntity(tradeLicense.getApplication()));

		SupportDocumentEntity supportDocumentEntity;
		if (tradeLicense.getApplication() != null && tradeLicense.getApplication().getSupportDocuments() != null
				&& tradeLicense.getApplication().getSupportDocuments().size() > 0) {

			for (SupportDocument supportDocument : tradeLicense.getApplication().getSupportDocuments()) {
				supportDocument.setTeantId(tradeLicense.getTenantId());
				supportDocument.setApplicationId(applicationEntity.getId());
				supportDocumentEntity = new SupportDocumentEntity().toEntity(supportDocument);
				supportDocumentJdbcRepository.create(supportDocumentEntity);
			}
		}

		LicenseFeeDetailEntity licenseFeeDetailEntity;
		if (tradeLicense.getFeeDetails() != null && tradeLicense.getFeeDetails().size() > 0) {

			for (LicenseFeeDetail feeDetail : tradeLicense.getFeeDetails()) {
				feeDetail.setTenantId(tradeLicense.getTenantId());
				feeDetail.setApplicationId(applicationEntity.getId());
				licenseFeeDetailEntity = new LicenseFeeDetailEntity().toEntity(feeDetail);

				licenseFeeDetailJdbcRepository.create(licenseFeeDetailEntity);
			}
		}

		return entity.toDomain();
	}

	@Transactional
	public TradeLicense update(TradeLicense tradeLicense) {
		TradeLicenseEntity entity = tradeLicenseJdbcRepository.update(new TradeLicenseEntity().toEntity(tradeLicense));

		if (tradeLicense.getApplication() != null) {

			tradeLicense.getApplication().setAuditDetails(tradeLicense.getAuditDetails());
			LicenseApplicationEntity applicationEntity = new LicenseApplicationEntity();
			Boolean isApplicationExists = validateUpdateTradeApplicationId(tradeLicense.getApplication());
			tradeLicense.getApplication().setLicenseId(tradeLicense.getId());
			if (!isApplicationExists) {
				throw new CustomInvalidInputException("tl.error.invalid.aplication.id", "Invalid application id",
						new RequestInfo());
			}
			licenseApplicationJdbcRepository.update(applicationEntity.toEntity(tradeLicense.getApplication()));
		}

		if (tradeLicense.getApplication() != null && tradeLicense.getApplication().getSupportDocuments() != null
				&& tradeLicense.getApplication().getSupportDocuments().size() > 0) {

			for (SupportDocument supportDocument : tradeLicense.getApplication().getSupportDocuments()) {

				Boolean isDocumentExists = validateUpdateTradeSupportDocumentId(supportDocument);
				supportDocument.setTeantId(tradeLicense.getTenantId());
				// TODO : Application Id is fetched assuming there will be only
				// one applicaiton for given license
				Long applicationId = tradeLicenseJdbcRepository.getLegacyLicenseApplicationId(tradeLicense.getId());
				supportDocument.setApplicationId(applicationId);

				if (isDocumentExists) {

					supportDocumentJdbcRepository.update(new SupportDocumentEntity().toEntity(supportDocument));

				} else {

					supportDocumentJdbcRepository.create(new SupportDocumentEntity().toEntity(supportDocument));
				}
			}
		}

		if (tradeLicense.getFeeDetails() != null && tradeLicense.getFeeDetails().size() > 0) {

			for (LicenseFeeDetail feeDetail : tradeLicense.getFeeDetails()) {

				Boolean isFeeDetailExists = validateUpdateTradeLicenseFeeDetailId(feeDetail);
				feeDetail.setTenantId(tradeLicense.getTenantId());
				// TODO : Application Id is fetched assuming there will be only
				// one applicaiton for given license
				Long applicationId = tradeLicenseJdbcRepository.getLegacyLicenseApplicationId(tradeLicense.getId());
				feeDetail.setApplicationId(applicationId);
				if (isFeeDetailExists) {

					licenseFeeDetailJdbcRepository.update(new LicenseFeeDetailEntity().toEntity(feeDetail));
				} else {

					licenseFeeDetailJdbcRepository.create(new LicenseFeeDetailEntity().toEntity(feeDetail));
				}
			}
		}
		return entity.toDomain();
	}

	public TradeLicenseSearch getByLicenseId(TradeLicense tradeLicense, RequestInfo requestInfo) {

		Long licenseId = tradeLicense.getId();
		TradeLicenseSearchEntity tradeLicenseSearchEntity = tradeLicenseJdbcRepository.searchById(requestInfo,
				licenseId);
		TradeLicenseSearch tradeLicenseSearch = tradeLicenseSearchEntity.toDomain();

		return tradeLicenseSearch;
	}

	@Transactional
	public List<TradeLicenseSearch> search(RequestInfo requestInfo, LicenseSearch domain) {

		List<TradeLicenseSearch> tradeLicenseSearchList = new ArrayList<TradeLicenseSearch>();

		List<TradeLicenseSearchEntity> licenses = tradeLicenseJdbcRepository.search(requestInfo, domain);

		for (TradeLicenseSearchEntity tradeLicenseSearchEntity : licenses) {
			TradeLicenseSearch tradeLicenseSearch = tradeLicenseSearchEntity.toDomain();
			tradeLicenseSearchList.add(tradeLicenseSearch);
		}

		return tradeLicenseSearchList;
	}

	@Transactional
	public void createLicenseBill(final String query, final Object[] objValue) {
		jdbcTemplate.update(LicenseBillQueryBuilder.insertLicenseBill(), objValue);
	}

	@Transactional
	public void updateTradeLicenseAfterWorkFlowQuery(String consumerCode, String status) {
		String insertquery = LicenseBillQueryBuilder.updateTradeLicenseAfterWorkFlowQuery();
		Object[] obj = new Object[] { new java.util.Date().getTime(), status,
				consumerCode };
		jdbcTemplate.update(insertquery, obj);
	}

	public TradeLicense searchByApplicationNumber(RequestInfo requestInfo, String applicationNumber) {
		return tradeLicenseJdbcRepository.searchByApplicationNumber(requestInfo, applicationNumber);
	}
}
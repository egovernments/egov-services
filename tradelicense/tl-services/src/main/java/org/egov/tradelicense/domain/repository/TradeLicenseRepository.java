package org.egov.tradelicense.domain.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.domain.exception.InvalidInputException;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.persistence.entity.LicenseFeeDetailEntity;
import org.egov.tradelicense.persistence.entity.SupportDocumentEntity;
import org.egov.tradelicense.persistence.entity.TradeLicenseEntity;
import org.egov.tradelicense.persistence.queue.TradeLicenseQueueRepository;
import org.egov.tradelicense.persistence.repository.LicenseFeeDetailJdbcRepository;
import org.egov.tradelicense.persistence.repository.SupportDocumentJdbcRepository;
import org.egov.tradelicense.persistence.repository.TradeLicenseJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
	JdbcTemplate jdbcTemplate;

	public Long getNextSequence() {

		String id = tradeLicenseJdbcRepository.getSequence(TradeLicenseEntity.SEQUENCE_NAME);
		return Long.valueOf(id);
	}

	public void validateUniqueOldLicenseNumber(TradeLicense tradeLicense) {

		String sql = getUniqueTenantLicenseQuery(tradeLicense);
		Integer count = null;
		try {
			count = (Integer) jdbcTemplate.queryForObject(sql, Integer.class);
		} catch (Exception e) {
			log.error("error while executing the query :" + sql + " , error message : " + e.getMessage());
		}

		if (count != 0) {
			throw new InvalidInputException("tradeLicense number already exists");
		}
	}

	private String getUniqueTenantLicenseQuery(TradeLicense tradeLicense) {

		String tenantId = tradeLicense.getTenantId();
		String licNumber = tradeLicense.getOldLicenseNumber();

		StringBuffer uniqueQuery = new StringBuffer("select count(*) from egtl_license");
		uniqueQuery.append(" where oldLicenseNumber = '" + licNumber + "'");
		uniqueQuery.append(" AND tenantId = '" + tenantId + "'");

		return uniqueQuery.toString();
	}

	public Long getSupportDocumentNextSequence() {

		String id = tradeLicenseJdbcRepository.getSequence(SupportDocumentEntity.SEQUENCE_NAME);
		return Long.valueOf(id);
	}

	public Long getFeeDetailNextSequence() {

		String id = tradeLicenseJdbcRepository.getSequence(LicenseFeeDetailEntity.SEQUENCE_NAME);
		return Long.valueOf(id);
	}

	public void add(TradeLicenseRequest request) {

		Map<String, Object> message = new HashMap<>();
		message.put(propertiesManager.getCreateLegacyTradeValidated(), request);
		tradeLicenseQueueRepository.add(message);
	}

	@Transactional
	public TradeLicense save(TradeLicense tradeLicense) {

		TradeLicenseEntity entity = tradeLicenseJdbcRepository.create(new TradeLicenseEntity().toEntity(tradeLicense));

		if (tradeLicense.getSupportDocuments() != null && tradeLicense.getSupportDocuments().size() > 0) {
			for (SupportDocument supportDocument : tradeLicense.getSupportDocuments()) {
				SupportDocumentEntity documentEntity = supportDocumentJdbcRepository
						.create(new SupportDocumentEntity().toEntity(supportDocument));
			}
		}

		if (tradeLicense.getFeeDetails() != null && tradeLicense.getFeeDetails().size() > 0) {
			for (LicenseFeeDetail feeDetail : tradeLicense.getFeeDetails()) {
				LicenseFeeDetailEntity LicenseFeeEntity = licenseFeeDetailJdbcRepository
						.create(new LicenseFeeDetailEntity().toEntity(feeDetail));
			}
		}

		return entity.toDomain();
	}

	@Transactional
	public List<TradeLicense> search(String tenantId, Integer pageSize, Integer pageNumber, String sort, String active,
			String tradeLicenseId, String applicationNumber, String licenseNumber, String oldLicenseNumber, String mobileNumber,
			String aadhaarNumber, String emailId, String propertyAssesmentNo, Integer adminWard, Integer locality,
			String ownerName, String tradeTitle, String tradeType, Integer tradeCategory, Integer tradeSubCategory,
			String legacy, Integer status) {

		List<TradeLicense> tradeLicenselst = new ArrayList<TradeLicense>();
		List<TradeLicenseEntity> license = tradeLicenseJdbcRepository.search(tenantId, pageSize, pageNumber, sort,
				active, tradeLicenseId, applicationNumber, licenseNumber,  oldLicenseNumber, mobileNumber, aadhaarNumber, emailId,
				propertyAssesmentNo, adminWard, locality, ownerName, tradeTitle, tradeType, tradeCategory,
				tradeSubCategory, legacy, status);

		for (TradeLicenseEntity tradeLicenseEntity : license) {
			TradeLicense tradeLicense = tradeLicenseEntity.toDomain();
			tradeLicenselst.add(tradeLicense);
		}

		return tradeLicenselst;

	}

}
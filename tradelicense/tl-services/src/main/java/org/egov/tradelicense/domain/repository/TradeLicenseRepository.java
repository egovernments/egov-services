package org.egov.tradelicense.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.domain.exception.CustomInvalidInputException;
import org.egov.tradelicense.domain.enums.ApplicationType;
import org.egov.tradelicense.domain.model.LicenseApplication;
import org.egov.tradelicense.domain.model.LicenseApplicationSearch;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.LicenseFeeDetailSearch;
import org.egov.tradelicense.domain.model.LicenseSearch;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.SupportDocumentSearch;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.repository.builder.LicenseBillQueryBuilder;
import org.egov.tradelicense.persistence.entity.LicenseApplicationEntity;
import org.egov.tradelicense.persistence.entity.LicenseFeeDetailEntity;
import org.egov.tradelicense.persistence.entity.SupportDocumentEntity;
import org.egov.tradelicense.persistence.entity.TradeLicenseEntity;
import org.egov.tradelicense.persistence.queue.TradeLicenseQueueRepository;
import org.egov.tradelicense.persistence.repository.LicenseApplicationJdbcRepository;
import org.egov.tradelicense.persistence.repository.LicenseFeeDetailJdbcRepository;
import org.egov.tradelicense.persistence.repository.SupportDocumentJdbcRepository;
import org.egov.tradelicense.persistence.repository.TradeLicenseJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
	LicenseApplicationRepository licenseApplicationRepository;

	@Autowired
	LicenseFeeDetailRepository licenseFeeDetailRepository;

	@Autowired
	SupportDocumentRepository supportDocumentRepository;

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

	public Boolean uniqueCheck(String fieldName, TradeLicense tradeLicense) {

		return tradeLicenseJdbcRepository.uniqueCheck(fieldName, new TradeLicenseEntity().toEntity(tradeLicense));
	}

	public Boolean idExistenceCheck(TradeLicense tradeLicense) {

		return tradeLicenseJdbcRepository.idExistenceCheck(new TradeLicenseEntity().toEntity(tradeLicense));
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
		LicenseApplicationEntity applicationEntity = new LicenseApplicationEntity();
		tradeLicense.getApplication().setAuditDetails(tradeLicense.getAuditDetails());
		licenseApplicationJdbcRepository.create(applicationEntity.toEntity(tradeLicense.getApplication()));

		SupportDocumentEntity supportDocumentEntity;
		if (tradeLicense.getApplication() != null && tradeLicense.getApplication().getSupportDocuments() != null
				&& tradeLicense.getApplication().getSupportDocuments().size() > 0) {

			for (SupportDocument supportDocument : tradeLicense.getApplication().getSupportDocuments()) {
				supportDocument.setTenantId(tradeLicense.getTenantId());
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
			Boolean isApplicationExists = licenseApplicationJdbcRepository
					.idExistenceCheck(applicationEntity.toEntity(tradeLicense.getApplication()));
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

				Boolean isDocumentExists = supportDocumentJdbcRepository
						.idExistenceCheck(new SupportDocumentEntity().toEntity(supportDocument));
				supportDocument.setTenantId(tradeLicense.getTenantId());
				Long applicationId = tradeLicenseJdbcRepository.getLicenseApplicationId(tradeLicense.getId());
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

				Boolean isFeeDetailExists = licenseFeeDetailJdbcRepository
						.idExistenceCheck(new LicenseFeeDetailEntity().toEntity(feeDetail));
				feeDetail.setTenantId(tradeLicense.getTenantId());
				Long applicationId = tradeLicenseJdbcRepository.getLicenseApplicationId(tradeLicense.getId());
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

	public TradeLicense findLicense(LicenseSearch domain) {

		List<TradeLicense> tradeLicenses = search(domain);

		if (tradeLicenses != null && tradeLicenses.size() > 0) {

			TradeLicense tradeLicense = tradeLicenses.get(0);

			if (tradeLicense.getApplications() != null && tradeLicense.getApplications().size() > 0) {

				tradeLicense.setApplication(tradeLicense.getApplications().get(0));
				tradeLicense.setApplicationDate(tradeLicense.getApplication().getApplicationDate());
				if (tradeLicense.getApplication().getApplicationType() != null) {

					tradeLicense.setApplicationType(
							ApplicationType.valueOf(tradeLicense.getApplication().getApplicationType().toString()));
				}
				tradeLicense.setApplicationNumber(tradeLicense.getApplication().getApplicationNumber());
			}

			return tradeLicense;

		} else {

			return null;
		}
	}

	@Transactional
	public List<TradeLicense> search(LicenseSearch domain) {

		List<TradeLicense> tradeLicenses = new ArrayList<TradeLicense>();

		List<TradeLicenseEntity> licenseEntities = tradeLicenseJdbcRepository.search(domain);

		for (TradeLicenseEntity tradeLicenseEntity : licenseEntities) {

			TradeLicense tradeLicense = tradeLicenseEntity.toDomain();
			// get the application related to the license
			LicenseApplicationSearch licenseApplicationSearch = new LicenseApplicationSearch();
			licenseApplicationSearch.setLicenseId(tradeLicense.getId());
			licenseApplicationSearch.setTenantId(tradeLicense.getTenantId());
			List<LicenseApplication> licenseApplications = licenseApplicationRepository
					.search(licenseApplicationSearch);

			for (LicenseApplication licenseApplication : licenseApplications) {
				// get the fee details of the application
				LicenseFeeDetailSearch licenseFeeDetailSearch = new LicenseFeeDetailSearch();
				licenseFeeDetailSearch.setApplicationId(licenseApplication.getId());
				licenseFeeDetailSearch.setTenantId(licenseApplication.getTenantId());
				List<LicenseFeeDetail> LicenseFeeDetails = licenseFeeDetailRepository.search(licenseFeeDetailSearch);
				licenseApplication.setFeeDetails(LicenseFeeDetails);
				// get the support documents of the application
				SupportDocumentSearch supportDocumentSearch = new SupportDocumentSearch();
				supportDocumentSearch.setApplicationId(licenseApplication.getId());
				supportDocumentSearch.setTenantId(licenseApplication.getTenantId());
				List<SupportDocument> supportDocuments = supportDocumentRepository.search(supportDocumentSearch);
				licenseApplication.setSupportDocuments(supportDocuments);
			}

			tradeLicense.setApplications(licenseApplications);
			tradeLicenses.add(tradeLicense);
		}

		return tradeLicenses;
	}

	@Transactional
	public void createLicenseBill(final String query, final Object[] objValue) {
		jdbcTemplate.update(LicenseBillQueryBuilder.insertLicenseBill(), objValue);
	}

	@Transactional
	public void updateTradeLicenseAfterWorkFlowQuery(String consumerCode, String status) {
		String insertquery = LicenseBillQueryBuilder.updateTradeLicenseAfterWorkFlowQuery();
		Object[] obj = new Object[] { new java.util.Date().getTime(), status, consumerCode };
		jdbcTemplate.update(insertquery, obj);
	}

}
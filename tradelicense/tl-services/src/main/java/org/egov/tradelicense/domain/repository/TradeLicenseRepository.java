package org.egov.tradelicense.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.tl.commons.web.contract.LicenseBill;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.domain.exception.CustomInvalidInputException;
import org.egov.tradelicense.domain.enums.ApplicationType;
import org.egov.tradelicense.domain.model.LicenseApplication;
import org.egov.tradelicense.domain.model.LicenseApplicationSearch;
import org.egov.tradelicense.domain.model.LicenseBillSearch;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.LicenseFeeDetailSearch;
import org.egov.tradelicense.domain.model.LicenseSearch;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.SupportDocumentSearch;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.model.TradePartner;
import org.egov.tradelicense.domain.model.TradePartnerSearch;
import org.egov.tradelicense.domain.model.TradeShift;
import org.egov.tradelicense.domain.model.TradeShiftSearch;
import org.egov.tradelicense.persistence.entity.LicenseApplicationEntity;
import org.egov.tradelicense.persistence.entity.LicenseBillEntity;
import org.egov.tradelicense.persistence.entity.LicenseFeeDetailEntity;
import org.egov.tradelicense.persistence.entity.SupportDocumentEntity;
import org.egov.tradelicense.persistence.entity.TradeLicenseEntity;
import org.egov.tradelicense.persistence.entity.TradePartnerEntity;
import org.egov.tradelicense.persistence.entity.TradeShiftEntity;
import org.egov.tradelicense.persistence.queue.TradeLicenseQueueRepository;
import org.egov.tradelicense.persistence.repository.LicenseApplicationJdbcRepository;
import org.egov.tradelicense.persistence.repository.LicenseBillJdbcRepository;
import org.egov.tradelicense.persistence.repository.LicenseFeeDetailJdbcRepository;
import org.egov.tradelicense.persistence.repository.SupportDocumentJdbcRepository;
import org.egov.tradelicense.persistence.repository.TradeLicenseJdbcRepository;
import org.egov.tradelicense.persistence.repository.TradePartnerJdbcRepository;
import org.egov.tradelicense.persistence.repository.TradeShiftJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
	LicenseBillRepository licenseBillRepository;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	LicenseBillJdbcRepository licenseBillJdbcRepository;

	@Autowired
	TradePartnerJdbcRepository tradePartnerJdbcRepository;

	@Autowired
	TradeShiftJdbcRepository tradeShiftJdbcRepository;

	@Autowired
	LicenseApplicationJdbcRepository licenseApplicationJdbcRepository;

	@Autowired
	TradeShiftRepository tradeShiftRepository;

	@Autowired
	TradePartnerRepository tradePartnerRepository;

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

		TradePartnerEntity tradePartnerEntity;
		if (tradeLicense.getPartners() != null && tradeLicense.getPartners().size() > 0) {

			for (TradePartner tradePartner : tradeLicense.getPartners()) {

				tradePartner.setTenantId(tradeLicense.getTenantId());
				tradePartner.setAuditDetails(tradeLicense.getAuditDetails());
				tradePartnerEntity = new TradePartnerEntity().toEntity(tradePartner);

				tradePartnerJdbcRepository.create(tradePartnerEntity);
			}
		}

		TradeShiftEntity tradeShiftEntity;
		if (tradeLicense.getShifts() != null && tradeLicense.getShifts().size() > 0) {

			for (TradeShift tradeShift : tradeLicense.getShifts()) {

				tradeShift.setTenantId(tradeLicense.getTenantId());
				tradeShift.setAuditDetails(tradeLicense.getAuditDetails());
				tradeShiftEntity = new TradeShiftEntity().toEntity(tradeShift);

				tradeShiftJdbcRepository.create(tradeShiftEntity);
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
		
		if (tradeLicense.getShifts() != null && tradeLicense.getShifts().size() > 0) {

			for (TradeShift tradeShift : tradeLicense.getShifts()) {

				Boolean isShiftExists = tradeShiftJdbcRepository
						.idExistenceCheck(new TradeShiftEntity().toEntity(tradeShift));
				tradeShift.setTenantId(tradeLicense.getTenantId());
				tradeShift.setLicenseId(tradeLicense.getId());
				
				if (isShiftExists) {

					tradeShiftJdbcRepository.update(new TradeShiftEntity().toEntity(tradeShift));
					
				} else {

					tradeShiftJdbcRepository.create(new TradeShiftEntity().toEntity(tradeShift));
				}
			}
		}
		
		if (tradeLicense.getPartners() != null && tradeLicense.getPartners().size() > 0) {

			for (TradePartner tradePartner : tradeLicense.getPartners()) {

				Boolean isPartnerExists = tradePartnerJdbcRepository
						.idExistenceCheck(new TradePartnerEntity().toEntity(tradePartner));
				tradePartner.setTenantId(tradeLicense.getTenantId());
				tradePartner.setLicenseId(tradeLicense.getId());
				
				if (isPartnerExists) {

					tradePartnerJdbcRepository.update(new TradePartnerEntity().toEntity(tradePartner));
					
				} else {

					tradePartnerJdbcRepository.create(new TradePartnerEntity().toEntity(tradePartner));
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
			// get the shifts related to the license
			TradeShiftSearch tradeShiftSearch = new TradeShiftSearch();
			tradeShiftSearch.setLicenseId(tradeLicense.getId());
			tradeShiftSearch.setTenantId(tradeLicense.getTenantId());
			List<TradeShift> tradeShifts = tradeShiftRepository.search(tradeShiftSearch);
			tradeLicense.setShifts(tradeShifts);
			// get the partners related to the license
			TradePartnerSearch tradePartnerSearch = new TradePartnerSearch();
			tradePartnerSearch.setLicenseId(tradeLicense.getId());
			tradePartnerSearch.setTenantId(tradeLicense.getTenantId());
			List<TradePartner> tradePartners = tradePartnerRepository.search(tradePartnerSearch);
			tradeLicense.setPartners(tradePartners);

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

				// pull the Bill details
				LicenseBillSearch licenseBillSearch = new LicenseBillSearch();
				licenseBillSearch.setApplicationId(licenseApplication.getId());
				licenseBillSearch.setTenantId(licenseApplication.getTenantId());
				List<LicenseBill> licenseBills = licenseBillRepository.search(licenseBillSearch);
				licenseApplication.setLicenseBills(licenseBills);

			}

			tradeLicense.setApplications(licenseApplications);
			tradeLicenses.add(tradeLicense);
		}

		return tradeLicenses;
	}

	@Transactional
	public void createLicenseBill(LicenseBill licenseBill) {
		LicenseBillEntity licenseBillEntity = new LicenseBillEntity().toEntity(licenseBill);
		licenseBillJdbcRepository.create(licenseBillEntity);
	}

	@Transactional
	public void updateTradeLicenseAfterWorkFlowQuery(String consumerCode, String status) {
		licenseBillJdbcRepository.updateTradeLicenseAfterWorkFlowQuery(consumerCode, status);
	}

	public Long getLicenseBillId(Long licenseId) {
		return tradeLicenseJdbcRepository.getLicenseBillId(licenseId);
	}

	public Long getApplicationBillId(Long licenseId) {
		return tradeLicenseJdbcRepository.getApplicationBillId(licenseId);
	}
}
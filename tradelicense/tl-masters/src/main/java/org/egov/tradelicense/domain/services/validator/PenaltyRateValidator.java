package org.egov.tradelicense.domain.services.validator;

import java.util.ArrayList;
import java.util.List;

import org.egov.models.AuditDetails;
import org.egov.models.PenaltyRate;
import org.egov.models.PenaltyRateRequest;
import org.egov.models.RequestInfo;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.domain.exception.InvalidRangeException;
import org.egov.tradelicense.persistence.repository.PenaltyRateRepository;
import org.egov.tradelicense.persistence.repository.helper.UtilityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PenaltyRateValidator {

	@Autowired
	UtilityHelper utilityHelper;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	PenaltyRateRepository penaltyRateRepository;

	public void validatePenaltyRange(String tenantId, PenaltyRateRequest penaltyRateRequest, boolean validateNew) {

		RequestInfo requestInfo = penaltyRateRequest.getRequestInfo();
		// Assuming ApplicationType in all PenalityRate objects will be same,
		// considering the first one.
		String applicationType = penaltyRateRequest.getPenaltyRates().get(0).getApplicationType().toString();
		List<PenaltyRate> penaltyRates = new ArrayList<PenaltyRate>();
		if (tenantId == null) {
			tenantId = penaltyRateRequest.getPenaltyRates().get(0).getTenantId();
		}
		try {
			penaltyRates = penaltyRateRepository.searchPenaltyRate(tenantId, null, applicationType, null, null);
		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}

		if (validateNew) {
			for (PenaltyRate penaltyRate : penaltyRateRequest.getPenaltyRates()) {
				AuditDetails auditDetails = utilityHelper.getCreateMasterAuditDetails(requestInfo);
				penaltyRate.setAuditDetails(auditDetails);
				penaltyRates.add(penaltyRate);
			}
		} else {
			for (PenaltyRate penaltyRate : penaltyRateRequest.getPenaltyRates()) {
				AuditDetails auditDetails = penaltyRate.getAuditDetails();
				auditDetails = utilityHelper.getUpdateMasterAuditDetails(auditDetails, requestInfo);
				penaltyRate.setAuditDetails(auditDetails);
				for (int i = 0; i < penaltyRates.size(); i++) {
					Long id = penaltyRates.get(i).getId();
					if (penaltyRate.getId() != null && id == penaltyRate.getId()) {
						penaltyRates.set(i, penaltyRate);
					}
				}
			}
		}

		penaltyRates.sort((s1, s2) -> s1.getFromRange().compareTo(s2.getFromRange()));
		String oldApplicationType = null;
		Long oldToRange = null;
		Long fromRange = null;
		int count = 0;
		for (PenaltyRate penaltyRate : penaltyRates) {
			if (count > 0) {
				applicationType = penaltyRate.getApplicationType().toString();
				fromRange = penaltyRate.getFromRange();
				if (applicationType.equalsIgnoreCase(oldApplicationType)) {
					if (!fromRange.equals(oldToRange)) {
						throw new InvalidRangeException(requestInfo);
					}
				} else {
					throw new InvalidInputException(requestInfo);
				}
			}
			oldApplicationType = penaltyRate.getApplicationType().toString();
			oldToRange = penaltyRate.getToRange();
			count++;
		}
	}
}
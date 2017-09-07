package org.egov.tradelicense.domain.services.validator;

import java.util.ArrayList;
import java.util.List;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.PenaltyRate;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.PenaltyRateRequest;
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

		{
			RequestInfo requestInfo = penaltyRateRequest.getRequestInfo();
			// Assuming ApplicationType in all PenalityRate objects will be
			// same,
			// considering the first one.

			String applicationType = null;
			if (penaltyRateRequest.getPenaltyRates().get(0).getApplicationType() != null) {
				applicationType = penaltyRateRequest.getPenaltyRates().get(0).getApplicationType().toString();
			}

			List<PenaltyRate> penaltyRates = new ArrayList<PenaltyRate>();
			if (tenantId == null) {
				tenantId = penaltyRateRequest.getPenaltyRates().get(0).getTenantId();
			}

			try {
				penaltyRates = penaltyRateRepository.searchPenaltyRate(tenantId, null, applicationType, null, null);
			} catch (Exception e) {
				throw new InvalidInputException(e.getLocalizedMessage(), requestInfo);
			}

			if (validateNew) {
				for (PenaltyRate penaltyRate : penaltyRateRequest.getPenaltyRates()) {
					AuditDetails auditDetails = utilityHelper.getCreateMasterAuditDetails(requestInfo);
					penaltyRate.setAuditDetails(auditDetails);
					validateDuplicateAndRate(penaltyRates, penaltyRate, requestInfo);
					penaltyRates.add(penaltyRate);

				}
				validatePenaltyRate(penaltyRates, applicationType, requestInfo);
			} else {
				validatePenaltyRate(penaltyRateRequest.getPenaltyRates(), applicationType, requestInfo);
				List<PenaltyRate> pentalties = penaltyRateRequest.getPenaltyRates();
				for (int i = 0; i < pentalties.size(); i ++) {
					validateDuplicateAndRateInUpdate(pentalties, pentalties.get(i), i , requestInfo); 
				}
				
			}

		}

	}

	public void validateDuplicateAndRate(List<PenaltyRate> penaltyRates, PenaltyRate penaltyRate,
			RequestInfo requestInfo) {
		for (PenaltyRate penalty : penaltyRates) {
			if ((penalty.getToRange().longValue() == penaltyRate.getToRange().longValue())
					&& (penalty.getFromRange().longValue() == penaltyRate.getFromRange().longValue())) {
				throw new InvalidRangeException(propertiesManager.getDuplicatePenaltyRate(), requestInfo);
			}
			if (Double.compare(penalty.getRate(), penaltyRate.getRate()) == 0) {
				throw new InvalidInputException(propertiesManager.getInvalidRateWithapp(), requestInfo);

			}
		}
	}
	
	public void validateDuplicateAndRateInUpdate(List<PenaltyRate> penaltyRates, PenaltyRate penaltyRate,
			int index, RequestInfo requestInfo) {
		
		for (int i = 0; i < penaltyRates.size(); i ++) {
		 
		if(!(index == i)){
			
					if ((penaltyRates.get(i).getToRange().longValue() == penaltyRate.getToRange().longValue())
							&& (penaltyRates.get(i).getFromRange().longValue() == penaltyRate.getFromRange().longValue())) {
						throw new InvalidRangeException(propertiesManager.getDuplicatePenaltyRate(), requestInfo);
					}
					
					if (Double.compare(penaltyRates.get(i).getRate(), penaltyRate.getRate()) == 0) {
						throw new InvalidInputException(propertiesManager.getInvalidRateWithapp(), requestInfo);

				
			}
			
		}
		}
	}

	public void validatePenaltyRate(List<PenaltyRate> penaltyRates, String applicationType, RequestInfo requestInfo) {
		penaltyRates.sort((s1, s2) -> s1.getFromRange().compareTo(s2.getFromRange()));
		Long oldToRange = null;
		Long fromRange = null;
		int count = 0;
		for (PenaltyRate penaltyRate : penaltyRates) {
			
			if (penaltyRate.getToRange() <= penaltyRate.getFromRange()) {
				throw new InvalidRangeException(propertiesManager.getInvalidfromRangeCode(), requestInfo);
			}
			
			if (count > 0) {
				fromRange = penaltyRate.getFromRange();

				if (!fromRange.equals(oldToRange)) {
					throw new InvalidRangeException(propertiesManager.getInvalidSequenceRangeMsg(), requestInfo);
				}
			}
			
			oldToRange = penaltyRate.getToRange();
			count++;
		}
	}
}
package org.egov.lams.web.validator;

import java.util.Date;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.Demand;
import org.egov.lams.model.DemandDetails;
import org.egov.lams.model.enums.Status;
import org.egov.lams.repository.DemandRepository;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.DemandSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class CancelAgreementValidator {

	@Autowired
	private DemandRepository demandRepository;

	public void validateCancel(AgreementRequest agreementRequest, Errors errors) {

		Agreement agreement = agreementRequest.getAgreement();

		if (!agreement.getStatus().equals(Status.ACTIVE))
			errors.rejectValue("Agreement.Status", "", "Agreement must be in active state to initiate cancellation");

		DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
		demandSearchCriteria.setDemandId(Long.getLong(agreement.getDemands().get(0)));
		Demand demand = demandRepository.getDemandBySearch(demandSearchCriteria, agreementRequest.getRequestInfo())
				.getDemands().get(0);
		if (demand == null)
			errors.rejectValue("Agreement.demands", "", "No Demands found for the given agreement");
		else {
			Date today = new Date();
			for (DemandDetails demandDetails : demand.getDemandDetails()) {
				if (today.compareTo(demandDetails.getPeriodStartDate()) >= 0
						&& (!demandDetails.getTaxAmount().subtract(demandDetails.getCollectionAmount()).equals(0)))
					errors.rejectValue("Demands unpaid", "",
							"all due must be paid till current month to initiate cancellation");
			}
		}
	}
}

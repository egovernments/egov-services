package org.egov.demand.web.validator;

import java.util.List;

import org.egov.demand.model.TaxHeadMaster;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.egov.demand.service.TaxHeadMasterService;
import org.egov.demand.web.contract.DemandRequest;
import org.egov.demand.web.contract.TaxHeadMasterRequest;
import org.egov.demand.web.contract.TaxHeadMasterResponse;
import org.egov.demand.web.contract.TaxPeriodCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TaxHeadMasterValidator implements Validator {

	@Autowired
	private TaxHeadMasterService taxHeadMasterService;

	@Override
	public boolean supports(Class<?> clazz) {

		return TaxHeadMasterRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		TaxHeadMasterRequest taxHeadMasterRequest = null;
		if (target instanceof TaxHeadMasterRequest)
			taxHeadMasterRequest = (TaxHeadMasterRequest) target;
		else
			throw new RuntimeException("Invalid Object type for GlCodeMaster validator");
		validateTaxHeads(taxHeadMasterRequest, errors);
	}

	public void validateTaxHeads(final TaxHeadMasterRequest taxHeadsRequest, Errors error) {
		log.debug(":::::in validator class:::::::" + taxHeadsRequest);
		List<TaxHeadMaster> taxHeads = taxHeadsRequest.getTaxHeadMasters();
		TaxHeadMasterCriteria taxHeadMasterCriteria = null;

		for (TaxHeadMaster master : taxHeads) {
			if (!master.getTenantId().equalsIgnoreCase(taxHeads.get(0).getTenantId()))
				error.rejectValue("TaxHeadMasters", "", "Tenant id should be same in all objects");
			taxHeadMasterCriteria = TaxHeadMasterCriteria.builder().tenantId(master.getTenantId())
					.name(master.getName()).build();
			final TaxHeadMasterResponse taxHeadMasterResponse = taxHeadMasterService.getTaxHeads(taxHeadMasterCriteria,
					taxHeadsRequest.getRequestInfo());
			if (!taxHeadMasterResponse.getTaxHeadMasters().isEmpty())
				error.rejectValue("TaxHeadMasters", "", "Record Already exist");
		}
	}
}

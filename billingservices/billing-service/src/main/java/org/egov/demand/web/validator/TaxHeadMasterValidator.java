package org.egov.demand.web.validator;

import java.util.List;

import org.egov.demand.model.TaxHeadMaster;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.egov.demand.model.TaxPeriod;
import org.egov.demand.service.TaxHeadMasterService;
import org.egov.demand.service.TaxPeriodService;
import org.egov.demand.web.contract.TaxHeadMasterRequest;
import org.egov.demand.web.contract.TaxHeadMasterResponse;
import org.egov.demand.web.contract.TaxPeriodCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class TaxHeadMasterValidator {

	@Autowired
	private TaxHeadMasterService taxHeadMasterService;
	
	@Autowired
	private TaxPeriodService taxPeriodService;
	
	public void validateTaxHeads(final TaxHeadMasterRequest taxHeadsRequest) {
		log.debug(":::::in validator class:::::::"+taxHeadsRequest);
		System.out.println("::::::inside validator:::::::::::::::");
		TaxHeadMasterCriteria taxHeadMasterCriteria=new TaxHeadMasterCriteria();
		List<TaxHeadMaster> taxHeads = taxHeadsRequest.getTaxHeadMasters();
		TaxPeriodCriteria taxPeriodCriteria=new TaxPeriodCriteria();
		int taxHeadsCount=taxHeads.size();
		for (TaxHeadMaster master:taxHeads) {
			taxHeadMasterCriteria.setTenantId(master.getTenantId());
			taxHeadMasterCriteria.setName(master.getName());
		final TaxHeadMasterResponse taxHeadMasterResponse = taxHeadMasterService.getTaxHeads(taxHeadMasterCriteria, taxHeadsRequest.getRequestInfo());
	    if(! taxHeadMasterResponse.getTaxHeadMasters().isEmpty())
	    	throw new RuntimeException("Record Already exist");
		}
	}
}

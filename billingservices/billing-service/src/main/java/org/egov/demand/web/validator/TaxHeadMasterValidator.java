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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TaxHeadMasterValidator {

	private static final Logger logger = LoggerFactory.getLogger(TaxHeadMasterValidator.class);
	
	@Autowired
	private TaxHeadMasterService taxHeadMasterService;
	
	@Autowired
	private TaxPeriodService taxPeriodService;
	
	public void validateTaxHeads(final TaxHeadMasterRequest taxHeadsRequest) {
		System.out.println(":::::in validator class:::::::");
		TaxHeadMasterCriteria taxHeadMasterCriteria=new TaxHeadMasterCriteria();
		List<TaxHeadMaster> taxHeads = taxHeadsRequest.getTaxHeadMasters();
		TaxPeriodCriteria taxPeriodCriteria=new TaxPeriodCriteria();
		int taxHeadsCount=taxHeads.size();
		for (int i = 0; i < taxHeadsCount; i++) {
			taxHeadMasterCriteria.setTenantId(taxHeads.get(i).getTenantId());
			taxHeadMasterCriteria.setName(taxHeads.get(i).getName());
			taxPeriodCriteria.setTenantId(taxHeads.get(i).getTenantId());
			taxPeriodCriteria.setService(taxHeads.get(i).getService());
			List<TaxPeriod> taxPeriod= taxPeriodService.searchTaxPeriods(taxPeriodCriteria, taxHeadsRequest.getRequestInfo()).getTaxPeriods();
		if(taxPeriod.isEmpty())
			throw new RuntimeException("There are no tax period details");
		
		final TaxHeadMasterResponse taxHeadMasterResponse = taxHeadMasterService.getTaxHeads(taxHeadMasterCriteria, taxHeadsRequest.getRequestInfo());
	    if(! taxHeadMasterResponse.getTaxHeadMasters().isEmpty())
	    	throw new RuntimeException("Record Already exist");
		}
	}
}

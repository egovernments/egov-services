package org.egov.demand.web.validator;

import java.util.List;

import org.egov.demand.model.TaxHeadMaster;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.egov.demand.service.TaxHeadMasterService;
import org.egov.demand.web.contract.TaxHeadMasterRequest;
import org.egov.demand.web.contract.TaxHeadMasterResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TaxHeadMasterValidator {

	private static final Logger logger = LoggerFactory.getLogger(TaxHeadMasterValidator.class);
	
	@Autowired
	private TaxHeadMasterService taxHeadMasterService;
	
	public void validateTaxHeads(final TaxHeadMasterRequest taxHeadsRequest) {
		
		TaxHeadMasterCriteria taxHeadMasterCriteria=new TaxHeadMasterCriteria();
		List<TaxHeadMaster> taxHeads = taxHeadsRequest.getTaxHeadMasters();
		int taxHeadsCount=taxHeads.size();
		for (int i = 0; i < taxHeadsCount; i++) {
			taxHeadMasterCriteria.setTenantId(taxHeads.get(i).getTenantId());
			taxHeadMasterCriteria.setName(taxHeads.get(i).getName());
		final TaxHeadMasterResponse taxHeadMasterResponse = taxHeadMasterService.getTaxHeads(taxHeadMasterCriteria, taxHeadsRequest.getRequestInfo());
	    if(! taxHeadMasterResponse.getTaxHeadMasters().isEmpty())
	    	throw new RuntimeException("Record Already exist");
		}
	}
}

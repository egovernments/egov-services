package org.egov.demand.web.validator;

import java.util.List;

import org.egov.demand.model.GlCodeMaster;
import org.egov.demand.model.GlCodeMasterCriteria;
import org.egov.demand.service.GlCodeMasterService;
import org.egov.demand.web.contract.GlCodeMasterRequest;
import org.egov.demand.web.contract.GlCodeMasterResponse;
import org.egov.demand.web.contract.TaxHeadMasterResponse;
import org.egov.demand.web.contract.TaxPeriodCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GlCodeMasterValidator {

	@Autowired
	private GlCodeMasterService glCodeMasterService;

	public void validateTaxHeads(final GlCodeMasterRequest glCodeMasterRequest) {
		log.debug(":::::in validator class:::::::"+glCodeMasterRequest);
		GlCodeMasterCriteria glCodeMasterCriteria=new GlCodeMasterCriteria();
		List<GlCodeMaster> glCodes = glCodeMasterRequest.getGlCodeMasters();
		TaxPeriodCriteria taxPeriodCriteria=new TaxPeriodCriteria();
		int taxHeadsCount=glCodes.size();
		for (int i = 0; i < taxHeadsCount; i++) {
			glCodeMasterCriteria.setTenantId(glCodes.get(i).getTenantId());
			glCodeMasterCriteria.setService(glCodes.get(i).getService());
		final GlCodeMasterResponse glCodeMasterResponse = glCodeMasterService.getGlCodes(glCodeMasterCriteria, glCodeMasterRequest.getRequestInfo());
	    if(! glCodeMasterResponse.getGlCodeMasters().isEmpty())
	    	throw new RuntimeException("Record Already exist");
		}
	}
}

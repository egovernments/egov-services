package org.egov.demand.web.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.demand.model.GlCodeMaster;
import org.egov.demand.model.GlCodeMasterCriteria;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.egov.demand.service.GlCodeMasterService;
import org.egov.demand.service.TaxHeadMasterService;
import org.egov.demand.web.contract.GlCodeMasterRequest;
import org.egov.demand.web.contract.GlCodeMasterResponse;
import org.egov.demand.web.contract.TaxHeadMasterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GlCodeMasterValidator {

	@Autowired
	private GlCodeMasterService glCodeMasterService;

	@Autowired
	private TaxHeadMasterService taxHeadMasterService;

	public void validateGlCodeMaster(final GlCodeMasterRequest glCodeMasterRequest) {
		log.debug(":::::in validator class:::::::" + glCodeMasterRequest);
		GlCodeMasterCriteria glCodeMasterCriteria = new GlCodeMasterCriteria();
		TaxHeadMasterCriteria taxHeadCriteria = new TaxHeadMasterCriteria();
		List<GlCodeMaster> glCodes = glCodeMasterRequest.getGlCodeMasters();
		int taxHeadsCount = glCodes.size();
		for (GlCodeMaster master : glCodes) {
			glCodeMasterCriteria.setTenantId(master.getTenantId());
			glCodeMasterCriteria.setService(master.getService());

			Set<String> codes = new HashSet<String>();
			codes.add(master.getTaxHead());
			glCodeMasterCriteria.setTaxHead(codes);
			glCodeMasterCriteria.setFromDate(master.getFromDate());
			glCodeMasterCriteria.setToDate(master.getToDate());
			glCodeMasterCriteria.setGlCode(master.getGlCode());

			final GlCodeMasterResponse glCodeMasterResponse = glCodeMasterService.getGlCodes(glCodeMasterCriteria,
					glCodeMasterRequest.getRequestInfo());

			if (!glCodeMasterResponse.getGlCodeMasters().isEmpty())
				throw new RuntimeException("Record Already exist");

			taxHeadCriteria.setCode(codes);
			taxHeadCriteria.setTenantId(master.getTenantId());
			taxHeadCriteria.setService(master.getService());
			TaxHeadMasterResponse taxHeadMasterResponse = taxHeadMasterService.getTaxHeads(taxHeadCriteria,
					glCodeMasterRequest.getRequestInfo());
			if (taxHeadMasterResponse.getTaxHeadMasters().isEmpty())
				throw new RuntimeException("Invalid taxHead");
		}
	}
}

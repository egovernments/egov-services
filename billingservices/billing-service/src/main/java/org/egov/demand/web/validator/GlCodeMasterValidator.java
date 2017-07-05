package org.egov.demand.web.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.demand.model.GlCodeMaster;
import org.egov.demand.model.GlCodeMasterCriteria;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.egov.demand.service.GlCodeMasterService;
import org.egov.demand.service.TaxHeadMasterService;
import org.egov.demand.web.contract.DemandRequest;
import org.egov.demand.web.contract.GlCodeMasterRequest;
import org.egov.demand.web.contract.GlCodeMasterResponse;
import org.egov.demand.web.contract.TaxHeadMasterRequest;
import org.egov.demand.web.contract.TaxHeadMasterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GlCodeMasterValidator implements Validator {

	@Autowired
	private GlCodeMasterService glCodeMasterService;

	@Autowired
	private TaxHeadMasterService taxHeadMasterService;
	
	@Override
	public boolean supports(Class<?> clazz) {

		return GlCodeMasterRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		GlCodeMasterRequest glCodeMasterRequest = null;
		if (target instanceof GlCodeMasterRequest)
			glCodeMasterRequest = (GlCodeMasterRequest) target;
		else
			throw new RuntimeException("Invalid Object type for GlCodeMaster validator");
		validateGlCodeMaster(glCodeMasterRequest, errors);
	}
	
	
	

	public void validateGlCodeMaster(final GlCodeMasterRequest glCodeMasterRequest,Errors error) {
		log.debug(":::::in validator class:::::::" + glCodeMasterRequest);
		GlCodeMasterCriteria glCodeMasterCriteria = new GlCodeMasterCriteria();
		TaxHeadMasterCriteria taxHeadCriteria = new TaxHeadMasterCriteria();
		List<GlCodeMaster> glCodes = glCodeMasterRequest.getGlCodeMasters();
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
				error.rejectValue("GlCodeMasters","","Record Already exist");
//				throw new RuntimeException("Record Already exist");

			taxHeadCriteria.setCode(codes);
			taxHeadCriteria.setTenantId(master.getTenantId());
			taxHeadCriteria.setService(master.getService());
			TaxHeadMasterResponse taxHeadMasterResponse = taxHeadMasterService.getTaxHeads(taxHeadCriteria,
					glCodeMasterRequest.getRequestInfo());
			if (taxHeadMasterResponse.getTaxHeadMasters().isEmpty())
				error.rejectValue("GlCodeMasters","","The TaxHead provided is invalid");
		}
	}
}

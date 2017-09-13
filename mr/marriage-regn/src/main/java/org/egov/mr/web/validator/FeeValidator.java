package org.egov.mr.web.validator;

import java.util.List;

import org.egov.mr.model.Fee;
import org.egov.mr.service.FeeService;
import org.egov.mr.web.contract.FeeCriteria;
import org.egov.mr.web.contract.FeeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FeeValidator implements Validator {

	@Autowired
	private FeeService feeService;

	@Override
	public boolean supports(Class<?> clazz) {
		return FeeRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		FeeRequest feeRequest = null;
		if (target instanceof FeeRequest)
			feeRequest = (FeeRequest) target;
		else
			throw new RuntimeException("Invalid Object type for Fee validator");
		log.info("::::inside validator::::::");
		validateFee(feeRequest, errors);
	}

	public void validateFee(FeeRequest request,Errors errors){
		List<Fee> fees=request.getFees();
		for (Fee fee : fees) {
			
			FeeCriteria feeCriteria = FeeCriteria.builder().tenantId(fee.getTenantId())
					.feeCriteria(fee.getFeeCriteria()).fromDate(fee.getFromDate()).toDate(fee.getToDate()).build();
			List<Fee> respFees = feeService.getFee(feeCriteria, request.getRequestInfo()).getFees();
			
			if (!respFees.isEmpty())
				errors.rejectValue("fees", "",
						"Fee with tenantId:" + fee.getTenantId() + ",criteria:" + fee.getFeeCriteria() + ",fromDate:"
								+ fee.getFromDate() + " and toDate:" + fee.getToDate() + " is already exist");
		}
	}
}

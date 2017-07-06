package org.egov.demand.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.demand.domain.service.InstallmentService;
import org.egov.demand.persistence.entity.Installment;
import org.egov.demand.web.contract.InstallmentResponse;
import org.egov.demand.web.contract.InstallmentSearchCriteria;
import org.egov.demand.web.contract.RequestInfo;
import org.egov.demand.web.contract.ResponseInfo;
import org.egov.demand.web.contract.factory.ResponseInfoFactory;
import org.egov.demand.web.errorhandler.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("demand/installment")
public class InstallmentController {

	public static final Logger LOGGER = LoggerFactory.getLogger(InstallmentController.class);
	
	@Autowired
	private InstallmentService installmentService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private ErrorHandler errHandler;

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid InstallmentSearchCriteria installmentSearchCriteria,
			@RequestBody RequestInfo requestInfo, BindingResult bindingResult) {
		List<Installment> installments = null;
		org.egov.demand.web.contract.Installment installment = null;
		List<org.egov.demand.web.contract.Installment> insts = new ArrayList<org.egov.demand.web.contract.Installment>();
		if (bindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForBindingErrors(bindingResult, requestInfo);
		}
		try {
			/*
			 * fetching current installment based on only current date,installment type,module,tenantid
			 * installment fromDate and toDate not required
			 */

			if (installmentSearchCriteria.getCurrentDate() != null) {
				LOGGER.info("current date  is : " + installmentSearchCriteria.getCurrentDate());

				installments = installmentService
						.findCurrentInstallmentByCurrentDateAndInstallmentTypeAndModuleAndTenantId(
								installmentSearchCriteria);
				LOGGER.info("current installment is : " + installments.get(0).getDescription());
			} else {
				installments = installmentService.search(installmentSearchCriteria);
			}
			for (Installment inst : installments) {
				installment = new org.egov.demand.web.contract.Installment();
				installment.setFromDate(inst.getFromDate());
				installment.setToDate(inst.getToDate());
				installment.setDescription(inst.getDescription());
				installment.setModule(inst.getModule());
				installment.setInstallmentType(inst.getInstallmentType());
				installment.setFinancialYear(inst.getFinYearRange());
				installment.setTenantId(inst.getTenantId());
				insts.add(installment);
			}
		} catch (Exception e) {
			LOGGER.info("exception while fetching installments", e);
		}
		return getSuccessResponseForSearch(insts, requestInfo);
	}

	/**
	 * Populate Response object and return Installment List
	 * 
	 * @param installmentList
	 * @return
	 */
	private ResponseEntity<?> getSuccessResponse(org.egov.demand.web.contract.Installment installment,
			RequestInfo requestInfo) {
		InstallmentResponse installmentRes = new InstallmentResponse();
		List<org.egov.demand.web.contract.Installment> installments = new ArrayList<org.egov.demand.web.contract.Installment>();
		installments.add(installment);
		installmentRes.setInstallments(installments);

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		installmentRes.setResponseInfo(responseInfo);
		return new ResponseEntity<InstallmentResponse>(installmentRes, HttpStatus.OK);
	}

	private ResponseEntity<?> getSuccessResponseForSearch(
			List<org.egov.demand.web.contract.Installment> installmentList, RequestInfo requestInfo) {
		InstallmentResponse installmentRes = new InstallmentResponse();
		installmentRes.setInstallments(installmentList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		installmentRes.setResponseInfo(responseInfo);
		return new ResponseEntity<InstallmentResponse>(installmentRes, HttpStatus.OK);
	}
}

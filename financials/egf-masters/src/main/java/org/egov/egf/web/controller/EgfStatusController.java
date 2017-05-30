package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.EgfStatus;
import org.egov.egf.persistence.queue.contract.EgfStatusContract;
import org.egov.egf.persistence.queue.contract.EgfStatusContractRequest;
import org.egov.egf.persistence.queue.contract.EgfStatusContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.EgfStatusService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/egfstatus")
public class EgfStatusController {

	@Autowired
	private EgfStatusService egfStatusService;

	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public EgfStatusContractResponse view(@ModelAttribute EgfStatusContractRequest egfStatusContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {
		egfStatusService.validate(egfStatusContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		egfStatusService.fetchRelatedContracts(egfStatusContractRequest);
		EgfStatus egfStatusFromDb = egfStatusService.findOne(uniqueId);
		EgfStatusContract egfStatus = egfStatusContractRequest.getEgfStatus();

		ModelMapper model = new ModelMapper();
		model.map(egfStatusFromDb, egfStatus);

		EgfStatusContractResponse egfStatusContractResponse = new EgfStatusContractResponse();
		egfStatusContractResponse.setEgfStatus(egfStatus);
		egfStatusContractResponse.setResponseInfo(getResponseInfo(egfStatusContractRequest.getRequestInfo()));
		egfStatusContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return egfStatusContractResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public EgfStatusContractResponse search(@ModelAttribute EgfStatusContract egfStatusContracts,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {
		EgfStatusContractRequest egfStatusContractRequest = new EgfStatusContractRequest();
		egfStatusContractRequest.setEgfStatus(egfStatusContracts);
		egfStatusContractRequest.setRequestInfo(requestInfo);
		egfStatusService.validate(egfStatusContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		egfStatusService.fetchRelatedContracts(egfStatusContractRequest);
		EgfStatusContractResponse egfStatusContractResponse = new EgfStatusContractResponse();
		egfStatusContractResponse.setEgfStatuses(new ArrayList<EgfStatusContract>());
		egfStatusContractResponse.setPage(new Pagination());
		Page<EgfStatus> allEgfStatuses;
		ModelMapper model = new ModelMapper();

		allEgfStatuses = egfStatusService.search(egfStatusContractRequest);
		EgfStatusContract egfStatusContract = null;
		for (EgfStatus b : allEgfStatuses) {
			egfStatusContract = new EgfStatusContract();
			model.map(b, egfStatusContract);
			egfStatusContractResponse.getEgfStatuses().add(egfStatusContract);
		}
		egfStatusContractResponse.getPage().map(allEgfStatuses);
		egfStatusContractResponse.setResponseInfo(getResponseInfo(egfStatusContractRequest.getRequestInfo()));
		egfStatusContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return egfStatusContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}
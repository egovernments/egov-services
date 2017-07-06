package org.egov.egf.master.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.common.web.contract.CommonResponse;
import org.egov.common.web.contract.PaginationContract;
import org.egov.common.web.contract.RequestInfo;
import org.egov.common.web.contract.ResponseInfo;
import org.egov.egf.master.domain.model.EgfStatus;
import org.egov.egf.master.domain.model.EgfStatusSearch;
import org.egov.egf.master.domain.service.EgfStatusService;
import org.egov.egf.master.web.contract.EgfStatusContract;
import org.egov.egf.master.web.contract.EgfStatusSearchContract;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/egfstatuses")
public class EgfStatusController {

	@Autowired
	private EgfStatusService egfStatusService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<EgfStatusContract> create(
			@RequestBody @Valid CommonRequest<EgfStatusContract> egfStatusContractRequest, BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		egfStatusContractRequest.getRequestInfo().setAction("create");

		ModelMapper model = new ModelMapper();
		CommonResponse<EgfStatusContract> egfStatusResponse = new CommonResponse<>();
		List<EgfStatus> egfstatuses = new ArrayList<>();
		EgfStatus egfStatus = null;
		List<EgfStatusContract> egfStatusContracts = new ArrayList<EgfStatusContract>();
		EgfStatusContract contract = null;

		egfStatusContractRequest.getRequestInfo().setAction("create");

		for (EgfStatusContract egfStatusContract : egfStatusContractRequest.getData()) {
			egfStatus = new EgfStatus();
			model.map(egfStatusContract, egfStatus);
			egfStatus.setCreatedBy(egfStatusContractRequest.getRequestInfo().getUserInfo());
			egfStatus.setLastModifiedBy(egfStatusContractRequest.getRequestInfo().getUserInfo());
			egfstatuses.add(egfStatus);
		}

		egfstatuses = egfStatusService.add(egfstatuses, errors);

		for (EgfStatus f : egfstatuses) {
			contract = new EgfStatusContract();
			model.map(f, contract);
			egfStatusContracts.add(contract);
		}

		egfStatusContractRequest.setData(egfStatusContracts);
		egfStatusService.addToQue(egfStatusContractRequest);
		egfStatusResponse.setData(egfStatusContracts);

		return egfStatusResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<EgfStatusContract> update(
			@RequestBody @Valid CommonRequest<EgfStatusContract> egfStatusContractRequest, BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		egfStatusContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<EgfStatusContract> egfStatusResponse = new CommonResponse<>();
		List<EgfStatus> egfstatuses = new ArrayList<>();
		EgfStatus egfStatus;
		EgfStatusContract contract;
		List<EgfStatusContract> egfStatusContracts = new ArrayList<>();

		for (EgfStatusContract egfStatusContract : egfStatusContractRequest.getData()) {
			egfStatus = new EgfStatus();
			model.map(egfStatusContract, egfStatus);
			egfStatus.setLastModifiedBy(egfStatusContractRequest.getRequestInfo().getUserInfo());
			egfstatuses.add(egfStatus);
		}

		egfstatuses = egfStatusService.update(egfstatuses, errors);

		for (EgfStatus egfStatusObj : egfstatuses) {
			contract = new EgfStatusContract();
			model.map(egfStatusObj, contract);
			egfStatusContracts.add(contract);
		}

		egfStatusContractRequest.setData(egfStatusContracts);
		egfStatusService.addToQue(egfStatusContractRequest);
		egfStatusResponse.setData(egfStatusContracts);

		return egfStatusResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<EgfStatusContract> search(@ModelAttribute EgfStatusSearchContract egfStatusSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		EgfStatusSearch domain = new EgfStatusSearch();
		mapper.map(egfStatusSearchContract, domain);
		EgfStatusContract contract = null;
		ModelMapper model = new ModelMapper();
		List<EgfStatusContract> egfStatusContracts = new ArrayList<EgfStatusContract>();

		Pagination<EgfStatus> egfstatuses = egfStatusService.search(domain);

		for (EgfStatus egfStatus : egfstatuses.getPagedData()) {
			contract = new EgfStatusContract();
			model.map(egfStatus, contract);
			egfStatusContracts.add(contract);
		}

		CommonResponse<EgfStatusContract> response = new CommonResponse<>();
		response.setData(egfStatusContracts);
		response.setPage(new PaginationContract(egfstatuses));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}
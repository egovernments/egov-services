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
import org.egov.egf.master.domain.model.FiscalPeriod;
import org.egov.egf.master.domain.model.FiscalPeriodSearch;
import org.egov.egf.master.domain.service.FiscalPeriodService;
import org.egov.egf.master.web.contract.FiscalPeriodContract;
import org.egov.egf.master.web.contract.FiscalPeriodSearchContract;
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
@RequestMapping("/fiscalperiods")
public class FiscalPeriodController {

	@Autowired
	private FiscalPeriodService fiscalPeriodService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<FiscalPeriodContract> create(
			@RequestBody @Valid CommonRequest<FiscalPeriodContract> fiscalPeriodContractRequest, BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<FiscalPeriodContract> fiscalPeriodResponse = new CommonResponse<>();
		List<FiscalPeriod> fiscalperiods = new ArrayList<>();
		FiscalPeriod fiscalPeriod = null;
		List<FiscalPeriodContract> fiscalPeriodContracts = new ArrayList<FiscalPeriodContract>();
		FiscalPeriodContract contract = null;

		fiscalPeriodContractRequest.getRequestInfo().setAction("create");

		for (FiscalPeriodContract fiscalPeriodContract : fiscalPeriodContractRequest.getData()) {
			fiscalPeriod = new FiscalPeriod();
			model.map(fiscalPeriodContract, fiscalPeriod);
			fiscalPeriod.setCreatedBy(fiscalPeriodContractRequest.getRequestInfo().getUserInfo());
			fiscalPeriod.setLastModifiedBy(fiscalPeriodContractRequest.getRequestInfo().getUserInfo());
			fiscalperiods.add(fiscalPeriod);
		}

		fiscalperiods = fiscalPeriodService.add(fiscalperiods, errors);

		for (FiscalPeriod f : fiscalperiods) {
			contract = new FiscalPeriodContract();
			model.map(f, contract);
			fiscalPeriodContracts.add(contract);
		}

		fiscalPeriodContractRequest.setData(fiscalPeriodContracts);
		fiscalPeriodService.addToQue(fiscalPeriodContractRequest);
		fiscalPeriodResponse.setData(fiscalPeriodContracts);

		return fiscalPeriodResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<FiscalPeriodContract> update(
			@RequestBody @Valid CommonRequest<FiscalPeriodContract> fiscalPeriodContractRequest, BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		fiscalPeriodContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<FiscalPeriodContract> fiscalPeriodResponse = new CommonResponse<>();
		List<FiscalPeriod> fiscalperiods = new ArrayList<>();
		FiscalPeriod fiscalPeriod = null;
		FiscalPeriodContract contract = null;
		List<FiscalPeriodContract> fiscalPeriodContracts = new ArrayList<FiscalPeriodContract>();

		for (FiscalPeriodContract fiscalPeriodContract : fiscalPeriodContractRequest.getData()) {
			fiscalPeriod = new FiscalPeriod();
			model.map(fiscalPeriodContract, fiscalPeriod);
			fiscalPeriod.setLastModifiedBy(fiscalPeriodContractRequest.getRequestInfo().getUserInfo());
			fiscalperiods.add(fiscalPeriod);
		}

		fiscalperiods = fiscalPeriodService.update(fiscalperiods, errors);

		for (FiscalPeriod fiscalPeriodObj : fiscalperiods) {
			contract = new FiscalPeriodContract();
			model.map(fiscalPeriodObj, contract);
			fiscalPeriodContracts.add(contract);
		}

		fiscalPeriodContractRequest.setData(fiscalPeriodContracts);
		fiscalPeriodService.addToQue(fiscalPeriodContractRequest);
		fiscalPeriodResponse.setData(fiscalPeriodContracts);

		return fiscalPeriodResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<FiscalPeriodContract> search(
			@ModelAttribute FiscalPeriodSearchContract fiscalPeriodSearchContract, RequestInfo requestInfo,
			BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		FiscalPeriodSearch domain = new FiscalPeriodSearch();
		mapper.map(fiscalPeriodSearchContract, domain);
		FiscalPeriodContract contract = null;
		ModelMapper model = new ModelMapper();
		List<FiscalPeriodContract> fiscalPeriodContracts = new ArrayList<FiscalPeriodContract>();

		Pagination<FiscalPeriod> fiscalperiods = fiscalPeriodService.search(domain);

		for (FiscalPeriod fiscalPeriod : fiscalperiods.getPagedData()) {
			contract = new FiscalPeriodContract();
			model.map(fiscalPeriod, contract);
			fiscalPeriodContracts.add(contract);
		}

		CommonResponse<FiscalPeriodContract> response = new CommonResponse<>();
		response.setData(fiscalPeriodContracts);
		response.setPage(new PaginationContract(fiscalperiods));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}
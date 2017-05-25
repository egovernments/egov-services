package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.FiscalPeriod;
import org.egov.egf.persistence.queue.contract.FiscalPeriodContract;
import org.egov.egf.persistence.queue.contract.FiscalPeriodContractRequest;
import org.egov.egf.persistence.queue.contract.FiscalPeriodContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.FiscalPeriodService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	public FiscalPeriodContractResponse create(
			@RequestBody @Valid FiscalPeriodContractRequest fiscalPeriodContractRequest, BindingResult errors) {
		fiscalPeriodService.validate(fiscalPeriodContractRequest, "create", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		fiscalPeriodContractRequest.getRequestInfo().setAction("create");
		fiscalPeriodService.fetchRelatedContracts(fiscalPeriodContractRequest);
		fiscalPeriodService.push(fiscalPeriodContractRequest);
		FiscalPeriodContractResponse fiscalPeriodContractResponse = new FiscalPeriodContractResponse();
		fiscalPeriodContractResponse.setFiscalPeriods(new ArrayList<FiscalPeriodContract>());
		if (fiscalPeriodContractRequest.getFiscalPeriods() != null
				&& !fiscalPeriodContractRequest.getFiscalPeriods().isEmpty()) {
			for (FiscalPeriodContract fiscalPeriodContract : fiscalPeriodContractRequest.getFiscalPeriods()) {

				fiscalPeriodContractResponse.getFiscalPeriods().add(fiscalPeriodContract);
			}
		} else if (fiscalPeriodContractRequest.getFiscalPeriod() != null) {
			fiscalPeriodContractResponse.setFiscalPeriod(fiscalPeriodContractRequest.getFiscalPeriod());
		}
		fiscalPeriodContractResponse.setResponseInfo(getResponseInfo(fiscalPeriodContractRequest.getRequestInfo()));
		fiscalPeriodContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return fiscalPeriodContractResponse;
	}

	@PostMapping(value = "/{uniqueId}/_update")
	@ResponseStatus(HttpStatus.OK)
	public FiscalPeriodContractResponse update(
			@RequestBody @Valid FiscalPeriodContractRequest fiscalPeriodContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {

		fiscalPeriodService.validate(fiscalPeriodContractRequest, "update", errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		fiscalPeriodContractRequest.getRequestInfo().setAction("update");
		fiscalPeriodService.fetchRelatedContracts(fiscalPeriodContractRequest);
		fiscalPeriodContractRequest.getFiscalPeriod().setId(uniqueId);
		fiscalPeriodService.push(fiscalPeriodContractRequest);
		FiscalPeriodContractResponse fiscalPeriodContractResponse = new FiscalPeriodContractResponse();
		fiscalPeriodContractResponse.setFiscalPeriod(fiscalPeriodContractRequest.getFiscalPeriod());
		fiscalPeriodContractResponse.setResponseInfo(getResponseInfo(fiscalPeriodContractRequest.getRequestInfo()));
		fiscalPeriodContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return fiscalPeriodContractResponse;
	}

	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public FiscalPeriodContractResponse view(@ModelAttribute FiscalPeriodContractRequest fiscalPeriodContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {
		fiscalPeriodService.validate(fiscalPeriodContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		fiscalPeriodService.fetchRelatedContracts(fiscalPeriodContractRequest);
		FiscalPeriod fiscalPeriodFromDb = fiscalPeriodService.findOne(uniqueId);
		FiscalPeriodContract fiscalPeriod = fiscalPeriodContractRequest.getFiscalPeriod();

		ModelMapper model = new ModelMapper();
		model.map(fiscalPeriodFromDb, fiscalPeriod);

		FiscalPeriodContractResponse fiscalPeriodContractResponse = new FiscalPeriodContractResponse();
		fiscalPeriodContractResponse.setFiscalPeriod(fiscalPeriod);
		fiscalPeriodContractResponse.setResponseInfo(getResponseInfo(fiscalPeriodContractRequest.getRequestInfo()));
		fiscalPeriodContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return fiscalPeriodContractResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.OK)
	public FiscalPeriodContractResponse updateAll(
			@RequestBody @Valid FiscalPeriodContractRequest fiscalPeriodContractRequest, BindingResult errors) {
		fiscalPeriodService.validate(fiscalPeriodContractRequest, "updateAll", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		fiscalPeriodContractRequest.getRequestInfo().setAction("updateAll");
		fiscalPeriodService.fetchRelatedContracts(fiscalPeriodContractRequest);
		fiscalPeriodService.push(fiscalPeriodContractRequest);
		FiscalPeriodContractResponse fiscalPeriodContractResponse = new FiscalPeriodContractResponse();
		fiscalPeriodContractResponse.setFiscalPeriods(new ArrayList<FiscalPeriodContract>());
		for (FiscalPeriodContract fiscalPeriodContract : fiscalPeriodContractRequest.getFiscalPeriods()) {
			fiscalPeriodContractResponse.getFiscalPeriods().add(fiscalPeriodContract);
		}

		fiscalPeriodContractResponse.setResponseInfo(getResponseInfo(fiscalPeriodContractRequest.getRequestInfo()));
		fiscalPeriodContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());

		return fiscalPeriodContractResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public FiscalPeriodContractResponse search(@ModelAttribute FiscalPeriodContract fiscalPeriodContracts,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {
		FiscalPeriodContractRequest fiscalPeriodContractRequest = new FiscalPeriodContractRequest();
		fiscalPeriodContractRequest.setFiscalPeriod(fiscalPeriodContracts);
		fiscalPeriodContractRequest.setRequestInfo(requestInfo);
		fiscalPeriodService.validate(fiscalPeriodContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		fiscalPeriodService.fetchRelatedContracts(fiscalPeriodContractRequest);
		FiscalPeriodContractResponse fiscalPeriodContractResponse = new FiscalPeriodContractResponse();
		fiscalPeriodContractResponse.setFiscalPeriods(new ArrayList<FiscalPeriodContract>());
		fiscalPeriodContractResponse.setPage(new Pagination());
		Page<FiscalPeriod> allFiscalPeriods;
		ModelMapper model = new ModelMapper();

		allFiscalPeriods = fiscalPeriodService.search(fiscalPeriodContractRequest);
		FiscalPeriodContract fiscalPeriodContract = null;
		for (FiscalPeriod b : allFiscalPeriods) {
			fiscalPeriodContract = new FiscalPeriodContract();
			model.map(b, fiscalPeriodContract);
			fiscalPeriodContractResponse.getFiscalPeriods().add(fiscalPeriodContract);
		}
		fiscalPeriodContractResponse.getPage().map(allFiscalPeriods);
		fiscalPeriodContractResponse.setResponseInfo(getResponseInfo(fiscalPeriodContractRequest.getRequestInfo()));
		fiscalPeriodContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return fiscalPeriodContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}
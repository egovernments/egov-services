package org.egov.egf.instrument.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.PaginationContract;
import org.egov.egf.instrument.domain.model.SurrenderReason;
import org.egov.egf.instrument.domain.model.SurrenderReasonSearch;
import org.egov.egf.instrument.domain.service.SurrenderReasonService;
import org.egov.egf.instrument.persistence.queue.repository.SurrenderReasonQueueRepository;
import org.egov.egf.instrument.web.contract.SurrenderReasonContract;
import org.egov.egf.instrument.web.contract.SurrenderReasonSearchContract;
import org.egov.egf.instrument.web.requests.SurrenderReasonRequest;
import org.egov.egf.instrument.web.requests.SurrenderReasonResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/surrenderreasons")
public class SurrenderReasonController {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String PLACEHOLDER = "placeholder";

	@Value("${persist.through.kafka}")
	private static String persistThroughKafka;

	@Autowired
	private SurrenderReasonQueueRepository surrenderReasonQueueRepository;

	@Autowired
	private SurrenderReasonService surrenderReasonService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public SurrenderReasonResponse create(@RequestBody SurrenderReasonRequest surrenderReasonRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		SurrenderReasonResponse surrenderReasonResponse = new SurrenderReasonResponse();
		surrenderReasonResponse.setResponseInfo(getResponseInfo(surrenderReasonRequest.getRequestInfo()));
		List<SurrenderReason> surrenderreasons = new ArrayList<>();
		SurrenderReason surrenderReason;
		List<SurrenderReasonContract> surrenderReasonContracts = new ArrayList<>();
		SurrenderReasonContract contract;

		surrenderReasonRequest.getRequestInfo().setAction("create");

		for (SurrenderReasonContract surrenderReasonContract : surrenderReasonRequest.getSurrenderReasons()) {
			surrenderReason = new SurrenderReason();
			model.map(surrenderReasonContract, surrenderReason);
			surrenderReason.setCreatedDate(new Date());
			surrenderReason.setCreatedBy(surrenderReasonRequest.getRequestInfo().getUserInfo());
			surrenderReason.setLastModifiedBy(surrenderReasonRequest.getRequestInfo().getUserInfo());
			surrenderreasons.add(surrenderReason);
		}

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			surrenderreasons = surrenderReasonService.fetchAndValidate(surrenderreasons, errors, ACTION_CREATE);

			for (SurrenderReason sr : surrenderreasons) {
				contract = new SurrenderReasonContract();
				contract.setCreatedDate(new Date());
				model.map(sr, contract);
				surrenderReasonContracts.add(contract);
			}

			surrenderReasonRequest.setSurrenderReasons(surrenderReasonContracts);
			surrenderReasonQueueRepository.addToQue(surrenderReasonRequest);

		} else {

			surrenderreasons = surrenderReasonService.save(surrenderreasons, errors);

			for (SurrenderReason sr : surrenderreasons) {
				contract = new SurrenderReasonContract();
				contract.setCreatedDate(new Date());
				model.map(sr, contract);
				surrenderReasonContracts.add(contract);
			}

			surrenderReasonRequest.setSurrenderReasons(surrenderReasonContracts);
			surrenderReasonQueueRepository.addToSearchQue(surrenderReasonRequest);

		}

		surrenderReasonResponse.setSurrenderReasons(surrenderReasonContracts);

		return surrenderReasonResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public SurrenderReasonResponse update(@RequestBody SurrenderReasonRequest surrenderReasonRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		surrenderReasonRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		SurrenderReasonResponse surrenderReasonResponse = new SurrenderReasonResponse();
		List<SurrenderReason> surrenderreasons = new ArrayList<>();
		surrenderReasonResponse.setResponseInfo(getResponseInfo(surrenderReasonRequest.getRequestInfo()));
		SurrenderReason surrenderReason;
		SurrenderReasonContract contract;
		List<SurrenderReasonContract> surrenderReasonContracts = new ArrayList<>();

		for (SurrenderReasonContract surrenderReasonContract : surrenderReasonRequest.getSurrenderReasons()) {
			surrenderReason = new SurrenderReason();
			model.map(surrenderReasonContract, surrenderReason);
			surrenderReason.setLastModifiedBy(surrenderReasonRequest.getRequestInfo().getUserInfo());
			surrenderReason.setLastModifiedDate(new Date());
			surrenderreasons.add(surrenderReason);
		}

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			surrenderreasons = surrenderReasonService.fetchAndValidate(surrenderreasons, errors, ACTION_UPDATE);

			for (SurrenderReason sr : surrenderreasons) {
				contract = new SurrenderReasonContract();
				contract.setCreatedDate(new Date());
				model.map(sr, contract);
				surrenderReasonContracts.add(contract);
			}

			surrenderReasonRequest.setSurrenderReasons(surrenderReasonContracts);
			surrenderReasonQueueRepository.addToQue(surrenderReasonRequest);

		} else {

			surrenderreasons = surrenderReasonService.update(surrenderreasons, errors);

			for (SurrenderReason sr : surrenderreasons) {
				contract = new SurrenderReasonContract();
				contract.setCreatedDate(new Date());
				model.map(sr, contract);
				surrenderReasonContracts.add(contract);
			}

			surrenderReasonRequest.setSurrenderReasons(surrenderReasonContracts);
			surrenderReasonQueueRepository.addToSearchQue(surrenderReasonRequest);

		}

		surrenderReasonResponse.setSurrenderReasons(surrenderReasonContracts);

		return surrenderReasonResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SurrenderReasonResponse search(@ModelAttribute SurrenderReasonSearchContract surrenderReasonSearchContract,
			RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		SurrenderReasonSearch domain = new SurrenderReasonSearch();
		mapper.map(surrenderReasonSearchContract, domain);
		SurrenderReasonContract contract;
		ModelMapper model = new ModelMapper();
		List<SurrenderReasonContract> surrenderReasonContracts = new ArrayList<>();
		Pagination<SurrenderReason> surrenderreasons = surrenderReasonService.search(domain);

		if (surrenderreasons.getPagedData() != null) {
			for (SurrenderReason surrenderReason : surrenderreasons.getPagedData()) {
				contract = new SurrenderReasonContract();
				model.map(surrenderReason, contract);
				surrenderReasonContracts.add(contract);
			}
		}

		SurrenderReasonResponse response = new SurrenderReasonResponse();
		response.setSurrenderReasons(surrenderReasonContracts);
		response.setPage(new PaginationContract(surrenderreasons));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}
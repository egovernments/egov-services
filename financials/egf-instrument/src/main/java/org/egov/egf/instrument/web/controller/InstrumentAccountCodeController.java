package org.egov.egf.instrument.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.PaginationContract;
import org.egov.egf.instrument.domain.model.InstrumentAccountCode;
import org.egov.egf.instrument.domain.model.InstrumentAccountCodeSearch;
import org.egov.egf.instrument.domain.service.InstrumentAccountCodeService;
import org.egov.egf.instrument.persistence.queue.repository.InstrumentAccountCodeQueueRepository;
import org.egov.egf.instrument.web.contract.InstrumentAccountCodeContract;
import org.egov.egf.instrument.web.contract.InstrumentAccountCodeSearchContract;
import org.egov.egf.instrument.web.requests.InstrumentAccountCodeRequest;
import org.egov.egf.instrument.web.requests.InstrumentAccountCodeResponse;
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
@RequestMapping("/instrumentaccountcodes")
public class InstrumentAccountCodeController {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String PLACEHOLDER = "placeholder";

	@Value("${persist.through.kafka}")
	private static String persistThroughKafka;

	@Autowired
	private InstrumentAccountCodeService instrumentAccountCodeService;

	@Autowired
	private InstrumentAccountCodeQueueRepository instrumentAccountCodeQueueRepository;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public InstrumentAccountCodeResponse create(@RequestBody InstrumentAccountCodeRequest instrumentAccountCodeRequest,
			BindingResult errors) {

		ModelMapper model = new ModelMapper();
		InstrumentAccountCodeResponse instrumentAccountCodeResponse = new InstrumentAccountCodeResponse();
		instrumentAccountCodeResponse.setResponseInfo(getResponseInfo(instrumentAccountCodeRequest.getRequestInfo()));
		List<InstrumentAccountCode> instrumentaccountcodes = new ArrayList<>();
		InstrumentAccountCode instrumentAccountCode;
		List<InstrumentAccountCodeContract> instrumentAccountCodeContracts = new ArrayList<>();
		InstrumentAccountCodeContract contract;

		instrumentAccountCodeRequest.getRequestInfo().setAction(ACTION_CREATE);

		for (InstrumentAccountCodeContract instrumentAccountCodeContract : instrumentAccountCodeRequest
				.getInstrumentAccountCodes()) {
			instrumentAccountCode = new InstrumentAccountCode();
			model.map(instrumentAccountCodeContract, instrumentAccountCode);
			instrumentAccountCode.setCreatedDate(new Date());
			instrumentAccountCode.setCreatedBy(instrumentAccountCodeRequest.getRequestInfo().getUserInfo());
			instrumentAccountCode.setLastModifiedBy(instrumentAccountCodeRequest.getRequestInfo().getUserInfo());
			instrumentaccountcodes.add(instrumentAccountCode);
		}

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			instrumentaccountcodes = instrumentAccountCodeService.fetchAndValidate(instrumentaccountcodes, errors,
					ACTION_CREATE);

			for (InstrumentAccountCode iac : instrumentaccountcodes) {
				contract = new InstrumentAccountCodeContract();
				contract.setCreatedDate(new Date());
				model.map(iac, contract);
				instrumentAccountCodeContracts.add(contract);
			}

			instrumentAccountCodeRequest.setInstrumentAccountCodes(instrumentAccountCodeContracts);

			instrumentAccountCodeQueueRepository.addToQue(instrumentAccountCodeRequest);

		} else {

			instrumentaccountcodes = instrumentAccountCodeService.save(instrumentaccountcodes, errors);

			for (InstrumentAccountCode iac : instrumentaccountcodes) {
				contract = new InstrumentAccountCodeContract();
				model.map(iac, contract);
				instrumentAccountCodeContracts.add(contract);
			}

			instrumentAccountCodeRequest.setInstrumentAccountCodes(instrumentAccountCodeContracts);

			instrumentAccountCodeQueueRepository.addToSearchQue(instrumentAccountCodeRequest);

		}

		instrumentAccountCodeResponse.setInstrumentAccountCodes(instrumentAccountCodeContracts);

		return instrumentAccountCodeResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public InstrumentAccountCodeResponse update(@RequestBody InstrumentAccountCodeRequest instrumentAccountCodeRequest,
			BindingResult errors) {

		instrumentAccountCodeRequest.getRequestInfo().setAction(ACTION_UPDATE);
		ModelMapper model = new ModelMapper();
		InstrumentAccountCodeResponse instrumentAccountCodeResponse = new InstrumentAccountCodeResponse();
		List<InstrumentAccountCode> instrumentaccountcodes = new ArrayList<>();
		instrumentAccountCodeResponse.setResponseInfo(getResponseInfo(instrumentAccountCodeRequest.getRequestInfo()));
		InstrumentAccountCode instrumentAccountCode;
		InstrumentAccountCodeContract contract;
		List<InstrumentAccountCodeContract> instrumentAccountCodeContracts = new ArrayList<>();

		for (InstrumentAccountCodeContract instrumentAccountCodeContract : instrumentAccountCodeRequest
				.getInstrumentAccountCodes()) {
			instrumentAccountCode = new InstrumentAccountCode();
			model.map(instrumentAccountCodeContract, instrumentAccountCode);
			instrumentAccountCode.setLastModifiedBy(instrumentAccountCodeRequest.getRequestInfo().getUserInfo());
			instrumentAccountCode.setLastModifiedDate(new Date());
			instrumentaccountcodes.add(instrumentAccountCode);
		}

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			instrumentaccountcodes = instrumentAccountCodeService.fetchAndValidate(instrumentaccountcodes, errors,
					ACTION_UPDATE);

			for (InstrumentAccountCode iac : instrumentaccountcodes) {
				contract = new InstrumentAccountCodeContract();
				contract.setCreatedDate(new Date());
				model.map(iac, contract);
				instrumentAccountCodeContracts.add(contract);
			}

			instrumentAccountCodeRequest.setInstrumentAccountCodes(instrumentAccountCodeContracts);

			instrumentAccountCodeQueueRepository.addToQue(instrumentAccountCodeRequest);

		} else {

			instrumentaccountcodes = instrumentAccountCodeService.update(instrumentaccountcodes, errors);

			for (InstrumentAccountCode iac : instrumentaccountcodes) {
				contract = new InstrumentAccountCodeContract();
				model.map(iac, contract);
				instrumentAccountCodeContracts.add(contract);
			}

			instrumentAccountCodeRequest.setInstrumentAccountCodes(instrumentAccountCodeContracts);

			instrumentAccountCodeQueueRepository.addToSearchQue(instrumentAccountCodeRequest);

		}
		
		instrumentAccountCodeResponse.setInstrumentAccountCodes(instrumentAccountCodeContracts);

		return instrumentAccountCodeResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public InstrumentAccountCodeResponse search(
			@ModelAttribute InstrumentAccountCodeSearchContract instrumentAccountCodeSearchContract,
			RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		InstrumentAccountCodeSearch domain = new InstrumentAccountCodeSearch();
		mapper.map(instrumentAccountCodeSearchContract, domain);
		InstrumentAccountCodeContract contract;
		ModelMapper model = new ModelMapper();
		List<InstrumentAccountCodeContract> instrumentAccountCodeContracts = new ArrayList<>();
		Pagination<InstrumentAccountCode> instrumentaccountcodes = instrumentAccountCodeService.search(domain);

		if (instrumentaccountcodes.getPagedData() != null) {
			for (InstrumentAccountCode instrumentAccountCode : instrumentaccountcodes.getPagedData()) {
				contract = new InstrumentAccountCodeContract();
				model.map(instrumentAccountCode, contract);
				instrumentAccountCodeContracts.add(contract);
			}
		}

		InstrumentAccountCodeResponse response = new InstrumentAccountCodeResponse();
		response.setInstrumentAccountCodes(instrumentAccountCodeContracts);
		response.setPage(new PaginationContract(instrumentaccountcodes));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.ts(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())).resMsgId(requestInfo.getMsgId())
				.resMsgId(PLACEHOLDER).status(PLACEHOLDER).build();
	}

}
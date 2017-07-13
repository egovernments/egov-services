package org.egov.egf.voucher.web.controller;

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
import org.egov.egf.voucher.domain.model.Voucher;
import org.egov.egf.voucher.domain.model.VoucherSearch;
import org.egov.egf.voucher.domain.service.VoucherService;
import org.egov.egf.voucher.web.contract.VoucherContract;
import org.egov.egf.voucher.web.contract.VoucherSearchContract;
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
@RequestMapping("/vouchers")
public class VoucherController {

	@Autowired
	private VoucherService voucherService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<VoucherContract> create(@RequestBody @Valid CommonRequest<VoucherContract> voucherContractRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<VoucherContract> voucherResponse = new CommonResponse<>();
		List<Voucher> vouchers = new ArrayList<>();
		Voucher voucher = null;
		List<VoucherContract> voucherContracts = new ArrayList<VoucherContract>();
		VoucherContract contract = null;

		voucherContractRequest.getRequestInfo().setAction("create");

		for (VoucherContract voucherContract : voucherContractRequest.getData()) {
			voucher = new Voucher();
			model.map(voucherContract, voucher);
			voucher.setCreatedBy(voucherContractRequest.getRequestInfo().getUserInfo());
			voucher.setLastModifiedBy(voucherContractRequest.getRequestInfo().getUserInfo());
			vouchers.add(voucher);
		}

		vouchers = voucherService.add(vouchers, errors);

		for (Voucher f : vouchers) {
			contract = new VoucherContract();
			model.map(f, contract);
			voucherContracts.add(contract);
		}

		voucherContractRequest.setData(voucherContracts);
		voucherService.addToQue(voucherContractRequest);
		voucherResponse.setData(voucherContracts);

		return voucherResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<VoucherContract> update(@RequestBody @Valid CommonRequest<VoucherContract> voucherContractRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		voucherContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<VoucherContract> voucherResponse = new CommonResponse<>();
		List<Voucher> vouchers = new ArrayList<>();
		Voucher voucher = null;
		VoucherContract contract = null;
		List<VoucherContract> voucherContracts = new ArrayList<VoucherContract>();

		for (VoucherContract voucherContract : voucherContractRequest.getData()) {
			voucher = new Voucher();
			model.map(voucherContract, voucher);
			voucher.setLastModifiedBy(voucherContractRequest.getRequestInfo().getUserInfo());
			vouchers.add(voucher);
		}

		vouchers = voucherService.update(vouchers, errors);

		for (Voucher voucherObj : vouchers) {
			contract = new VoucherContract();
			model.map(voucherObj, contract);
			voucherContracts.add(contract);
		}

		voucherContractRequest.setData(voucherContracts);
		voucherService.addToQue(voucherContractRequest);
		voucherResponse.setData(voucherContracts);

		return voucherResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<VoucherContract> search(@ModelAttribute VoucherSearchContract voucherSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		VoucherSearch domain = new VoucherSearch();
		mapper.map(voucherSearchContract, domain);
		VoucherContract contract = null;
		ModelMapper model = new ModelMapper();
		List<VoucherContract> voucherContracts = new ArrayList<VoucherContract>();

		Pagination<Voucher> vouchers = voucherService.search(domain);

		for (Voucher voucher : vouchers.getPagedData()) {
			contract = new VoucherContract();
			model.map(voucher, contract);
			voucherContracts.add(contract);
		}

		CommonResponse<VoucherContract> response = new CommonResponse<>();
		response.setData(voucherContracts);
		response.setPage(new PaginationContract(vouchers));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}
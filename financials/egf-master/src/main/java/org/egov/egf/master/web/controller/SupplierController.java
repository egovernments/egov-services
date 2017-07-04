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
import org.egov.egf.master.domain.model.Supplier;
import org.egov.egf.master.domain.model.SupplierSearch;
import org.egov.egf.master.domain.service.SupplierService;
import org.egov.egf.master.web.contract.SupplierContract;
import org.egov.egf.master.web.contract.SupplierSearchContract;
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
@RequestMapping("/suppliers")
public class SupplierController {

	@Autowired
	private SupplierService supplierService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<SupplierContract> create(
			@RequestBody @Valid CommonRequest<SupplierContract> supplierContractRequest, BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<SupplierContract> supplierResponse = new CommonResponse<>();
		List<Supplier> suppliers = new ArrayList<>();
		Supplier supplier = null;
		List<SupplierContract> supplierContracts = new ArrayList<SupplierContract>();
		SupplierContract contract = null;

		supplierContractRequest.getRequestInfo().setAction("create");

		for (SupplierContract supplierContract : supplierContractRequest.getData()) {
			supplier = new Supplier();
			model.map(supplierContract, supplier);
			supplier.setCreatedBy(supplierContractRequest.getRequestInfo().getUserInfo());
			supplier.setLastModifiedBy(supplierContractRequest.getRequestInfo().getUserInfo());
			suppliers.add(supplier);
		}

		suppliers = supplierService.add(suppliers, errors);

		for (Supplier f : suppliers) {
			contract = new SupplierContract();
			model.map(f, contract);
			supplierContracts.add(contract);
		}

		supplierContractRequest.setData(supplierContracts);
		supplierService.addToQue(supplierContractRequest);
		supplierResponse.setData(supplierContracts);

		return supplierResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<SupplierContract> update(
			@RequestBody @Valid CommonRequest<SupplierContract> supplierContractRequest, BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<SupplierContract> supplierResponse = new CommonResponse<>();
		List<Supplier> suppliers = new ArrayList<>();
		Supplier supplier = null;
		SupplierContract contract = null;
		List<SupplierContract> supplierContracts = new ArrayList<SupplierContract>();

		for (SupplierContract supplierContract : supplierContractRequest.getData()) {
			supplier = new Supplier();
			model.map(supplierContract, supplier);
			supplier.setLastModifiedBy(supplierContractRequest.getRequestInfo().getUserInfo());
			suppliers.add(supplier);
		}

		suppliers = supplierService.update(suppliers, errors);

		for (Supplier supplierObj : suppliers) {
			contract = new SupplierContract();
			model.map(supplierObj, contract);
			supplierContracts.add(contract);
		}

		supplierContractRequest.setData(supplierContracts);
		supplierService.addToQue(supplierContractRequest);
		supplierResponse.setData(supplierContracts);

		return supplierResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<SupplierContract> search(@ModelAttribute SupplierSearchContract supplierSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		SupplierSearch domain = new SupplierSearch();
		mapper.map(supplierSearchContract, domain);
		SupplierContract contract = null;
		ModelMapper model = new ModelMapper();
		List<SupplierContract> supplierContracts = new ArrayList<SupplierContract>();

		Pagination<Supplier> suppliers = supplierService.search(domain);

		for (Supplier supplier : suppliers.getPagedData()) {
			contract = new SupplierContract();
			model.map(supplier, contract);
			supplierContracts.add(contract);
		}

		CommonResponse<SupplierContract> response = new CommonResponse<>();
		response.setData(supplierContracts);
		response.setPage(new PaginationContract(suppliers));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}
package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.Supplier;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.queue.contract.SupplierContract;
import org.egov.egf.persistence.queue.contract.SupplierContractRequest;
import org.egov.egf.persistence.queue.contract.SupplierContractResponse;
import org.egov.egf.persistence.service.SupplierService;
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
@RequestMapping("/suppliers")
public class SupplierController {
	@Autowired
	private SupplierService supplierService;

	@PostMapping("_create")
	@ResponseStatus(HttpStatus.CREATED)
	public SupplierContractResponse create(@RequestBody @Valid SupplierContractRequest supplierContractRequest,
			BindingResult errors) {
		supplierService.validate(supplierContractRequest, "create", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		supplierContractRequest.getRequestInfo().setAction("create");
		supplierService.fetchRelatedContracts(supplierContractRequest);
		supplierService.push(supplierContractRequest);
		SupplierContractResponse supplierContractResponse = new SupplierContractResponse();
		supplierContractResponse.setSuppliers(new ArrayList<SupplierContract>());
		if (supplierContractRequest.getSuppliers() != null && !supplierContractRequest.getSuppliers().isEmpty()) {
			for (SupplierContract supplierContract : supplierContractRequest.getSuppliers()) {
				supplierContractResponse.getSuppliers().add(supplierContract);
			}
		} else if (supplierContractRequest.getSupplier() != null) {
			supplierContractResponse.setSupplier(supplierContractRequest.getSupplier());
		}
		supplierContractResponse.setResponseInfo(getResponseInfo(supplierContractRequest.getRequestInfo()));
		supplierContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return supplierContractResponse;
	}

	@PostMapping(value = "/{uniqueId}/_update")
	@ResponseStatus(HttpStatus.OK)
	public SupplierContractResponse update(@RequestBody @Valid SupplierContractRequest supplierContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {

		supplierService.validate(supplierContractRequest, "update", errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		supplierContractRequest.getRequestInfo().setAction("update");
		supplierService.fetchRelatedContracts(supplierContractRequest);
		supplierContractRequest.getSupplier().setId(uniqueId);
		supplierService.push(supplierContractRequest);
		SupplierContractResponse supplierContractResponse = new SupplierContractResponse();
		supplierContractResponse.setSupplier(supplierContractRequest.getSupplier());
		supplierContractResponse.setResponseInfo(getResponseInfo(supplierContractRequest.getRequestInfo()));
		supplierContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return supplierContractResponse;
	}

	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public SupplierContractResponse view(@ModelAttribute SupplierContractRequest supplierContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {
		supplierService.validate(supplierContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		supplierService.fetchRelatedContracts(supplierContractRequest);
		Supplier supplierFromDb = supplierService.findOne(uniqueId);
		SupplierContract supplier = supplierContractRequest.getSupplier();

		ModelMapper model = new ModelMapper();
		model.map(supplierFromDb, supplier);

		SupplierContractResponse supplierContractResponse = new SupplierContractResponse();
		supplierContractResponse.setSupplier(supplier);
		supplierContractResponse.setResponseInfo(getResponseInfo(supplierContractRequest.getRequestInfo()));
		supplierContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return supplierContractResponse;
	}

	@PostMapping("/update")
	@ResponseStatus(HttpStatus.OK)
	public SupplierContractResponse updateAll(@RequestBody @Valid SupplierContractRequest supplierContractRequest,
			BindingResult errors) {
		supplierService.validate(supplierContractRequest, "updateAll", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		supplierContractRequest.getRequestInfo().setAction("updateAll");
		supplierService.fetchRelatedContracts(supplierContractRequest);
		supplierService.push(supplierContractRequest);
		SupplierContractResponse supplierContractResponse = new SupplierContractResponse();
		supplierContractResponse.setSuppliers(new ArrayList<SupplierContract>());
		for (SupplierContract supplierContract : supplierContractRequest.getSuppliers()) {
			supplierContractResponse.getSuppliers().add(supplierContract);
		}

		supplierContractResponse.setResponseInfo(getResponseInfo(supplierContractRequest.getRequestInfo()));
		supplierContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());

		return supplierContractResponse;
	}

	@PostMapping("_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SupplierContractResponse search(@ModelAttribute SupplierContractRequest supplierContractRequest,
			BindingResult errors) {
		supplierService.validate(supplierContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		supplierService.fetchRelatedContracts(supplierContractRequest);
		SupplierContractResponse supplierContractResponse = new SupplierContractResponse();
		supplierContractResponse.setSuppliers(new ArrayList<SupplierContract>());
		supplierContractResponse.setPage(new Pagination());
		Page<Supplier> allSuppliers;
		ModelMapper model = new ModelMapper();

		allSuppliers = supplierService.search(supplierContractRequest);
		SupplierContract supplierContract = null;
		for (Supplier b : allSuppliers) {
			supplierContract = new SupplierContract();
			model.map(b, supplierContract);
			supplierContractResponse.getSuppliers().add(supplierContract);
		}
		supplierContractResponse.getPage().map(allSuppliers);
		supplierContractResponse.setResponseInfo(getResponseInfo(supplierContractRequest.getRequestInfo()));
		supplierContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return supplierContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}
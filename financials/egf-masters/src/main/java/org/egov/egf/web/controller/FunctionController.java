package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.Function;
import org.egov.egf.persistence.queue.contract.FunctionContract;
import org.egov.egf.persistence.queue.contract.FunctionContractRequest;
import org.egov.egf.persistence.queue.contract.FunctionContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.FunctionService;
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
@RequestMapping("/functions")
public class FunctionController {
    @Autowired
    private FunctionService functionService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public FunctionContractResponse create(@RequestBody @Valid final FunctionContractRequest functionContractRequest,
            final BindingResult errors) {
        final ModelMapper modelMapper = new ModelMapper();
        functionService.validate(functionContractRequest, "create", errors);
        if (errors.hasErrors())
            throw new CustomBindException(errors);
        functionService.fetchRelatedContracts(functionContractRequest);
        final FunctionContractResponse functionContractResponse = new FunctionContractResponse();
        functionContractResponse.setFunctions(new ArrayList<FunctionContract>());
        for (final FunctionContract functionContract : functionContractRequest.getFunctions()) {

            Function functionEntity = modelMapper.map(functionContract, Function.class);
            functionEntity = functionService.create(functionEntity);
            final FunctionContract resp = modelMapper.map(functionEntity, FunctionContract.class);
            functionContract.setId(functionEntity.getId());
            functionContractResponse.getFunctions().add(resp);
        }

        functionContractResponse.setResponseInfo(getResponseInfo(functionContractRequest.getRequestInfo()));

        return functionContractResponse;
    }

    @PostMapping(value = "/{uniqueId}/_update")
    @ResponseStatus(HttpStatus.OK)
    public FunctionContractResponse update(@RequestBody @Valid final FunctionContractRequest functionContractRequest,
            final BindingResult errors, @PathVariable final Long uniqueId) {
        functionService.validate(functionContractRequest, "update", errors);
        if (errors.hasErrors())
            throw new CustomBindException(errors);
        functionService.fetchRelatedContracts(functionContractRequest);
        Function functionFromDb = functionService.findOne(uniqueId);

        final FunctionContract function = functionContractRequest.getFunction();
        // ignoring internally passed id if the put has id in url
        function.setId(uniqueId);
        final ModelMapper model = new ModelMapper();
        model.map(function, functionFromDb);
        functionFromDb = functionService.update(functionFromDb);
        final FunctionContractResponse functionContractResponse = new FunctionContractResponse();
        functionContractResponse.setFunction(function);
        functionContractResponse.setResponseInfo(getResponseInfo(functionContractRequest.getRequestInfo()));
        functionContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
        return functionContractResponse;
    }

    @GetMapping(value = "/{uniqueId}")
    @ResponseStatus(HttpStatus.OK)
    public FunctionContractResponse view(@ModelAttribute final FunctionContractRequest functionContractRequest,
            final BindingResult errors,
            @PathVariable final Long uniqueId) {
        functionService.validate(functionContractRequest, "view", errors);
        if (errors.hasErrors())
            throw new CustomBindException(errors);
        functionService.fetchRelatedContracts(functionContractRequest);
        functionContractRequest.getRequestInfo();
        final Function functionFromDb = functionService.findOne(uniqueId);
        final FunctionContract function = functionContractRequest.getFunction();

        final ModelMapper model = new ModelMapper();
        model.map(functionFromDb, function);

        final FunctionContractResponse functionContractResponse = new FunctionContractResponse();
        functionContractResponse.setFunction(function);
        functionContractResponse.setResponseInfo(getResponseInfo(functionContractRequest.getRequestInfo()));
        functionContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
        return functionContractResponse;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public FunctionContractResponse updateAll(@RequestBody @Valid final FunctionContractRequest functionContractRequest,
            final BindingResult errors) {
        functionService.validate(functionContractRequest, "updateAll", errors);
        if (errors.hasErrors())
            throw new CustomBindException(errors);
        functionService.fetchRelatedContracts(functionContractRequest);

        final FunctionContractResponse functionContractResponse = new FunctionContractResponse();
        functionContractResponse.setFunctions(new ArrayList<FunctionContract>());
        for (final FunctionContract functionContract : functionContractRequest.getFunctions()) {
            Function functionFromDb = functionService.findOne(functionContract.getId());

            final ModelMapper model = new ModelMapper();
            model.map(functionContract, functionFromDb);
            functionFromDb = functionService.update(functionFromDb);
            model.map(functionFromDb, functionContract);
            functionContractResponse.getFunctions().add(functionContract);
        }

        functionContractResponse.setResponseInfo(getResponseInfo(functionContractRequest.getRequestInfo()));
        functionContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());

        return functionContractResponse;
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public FunctionContractResponse search(@ModelAttribute final FunctionContract functionContracts,
            @RequestBody final RequestInfo requestInfo, final BindingResult errors) {
        String tenantId = functionContracts.getTenantId();
        final FunctionContractRequest functionContractRequest = new FunctionContractRequest();
        functionContractRequest.setFunction(functionContracts);
        functionContractRequest.setRequestInfo(requestInfo);
        functionService.validate(functionContractRequest, "search", errors);
        if (errors.hasErrors())
            throw new CustomBindException(errors);
        functionService.fetchRelatedContracts(functionContractRequest);
        final FunctionContractResponse functionContractResponse = new FunctionContractResponse();
        functionContractResponse.setFunctions(new ArrayList<FunctionContract>());
        functionContractResponse.setPage(new Pagination());
        Page<Function> allFunctions;
        final ModelMapper model = new ModelMapper();

        allFunctions = functionService.search(functionContractRequest);
        FunctionContract functionContract = null;
        for (final Function b : allFunctions) {
            functionContract = new FunctionContract();
            model.map(b, functionContract);
            functionContractResponse.getFunctions().add(functionContract);
        }
        functionContractResponse.getPage().map(allFunctions);
        functionContractResponse.setResponseInfo(getResponseInfo(functionContractRequest.getRequestInfo()));
        functionContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
        return functionContractResponse;
    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        new ResponseInfo();
        return ResponseInfo.builder()
                .apiId(requestInfo.getApiId())
                .ver(requestInfo.getVer())
                .ts(new Date())
                .resMsgId(requestInfo.getMsgId())
                .resMsgId("placeholder")
                .status("placeholder")
                .build();
    }

}
package org.egov.egf.voucher.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.PaginationContract;
import org.egov.egf.voucher.domain.model.VoucherSubType;
import org.egov.egf.voucher.domain.model.VoucherSubTypeSearch;
import org.egov.egf.voucher.domain.service.VoucherSubTypeService;
import org.egov.egf.voucher.web.contract.VoucherSubTypeContract;
import org.egov.egf.voucher.web.contract.VoucherSubTypeSearchContract;
import org.egov.egf.voucher.web.requests.VoucherSubTypeRequest;
import org.egov.egf.voucher.web.requests.VoucherSubTypeResponse;
import org.egov.egf.voucher.web.util.VoucherConstants;
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
@RequestMapping("/vouchersubtype")
public class VoucherSubTypeController {

    @Autowired
    private VoucherSubTypeService voucherSubTypeService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public VoucherSubTypeResponse create(
            @RequestBody VoucherSubTypeRequest voucherSubTypeRequest,
            BindingResult errors) {

        ModelMapper model = new ModelMapper();
        VoucherSubTypeResponse voucherSubTypeResponse = new VoucherSubTypeResponse();
        voucherSubTypeResponse
                .setResponseInfo(getResponseInfo(voucherSubTypeRequest
                        .getRequestInfo()));
        List<VoucherSubType> voucherSubTypes = new ArrayList<>();
        VoucherSubType voucherSubType;
        List<VoucherSubTypeContract> voucherSubTypeContracts = new ArrayList<>();
        VoucherSubTypeContract contract;

        voucherSubTypeRequest.getRequestInfo().setAction(
                Constants.ACTION_CREATE);

        for (VoucherSubTypeContract voucherSubTypeContract : voucherSubTypeRequest
                .getVoucherSubTypes()) {
            voucherSubType = new VoucherSubType();
            model.map(voucherSubTypeContract, voucherSubType);
            voucherSubType.setCreatedDate(new Date());
            voucherSubType.setCreatedBy(voucherSubTypeRequest.getRequestInfo()
                    .getUserInfo());
            voucherSubType.setLastModifiedBy(voucherSubTypeRequest
                    .getRequestInfo().getUserInfo());
            voucherSubTypes.add(voucherSubType);
        }

        voucherSubTypes = voucherSubTypeService.create(voucherSubTypes, errors,
                voucherSubTypeRequest.getRequestInfo());

        for (VoucherSubType f : voucherSubTypes) {
            contract = new VoucherSubTypeContract();
            model.map(f, contract);
            voucherSubTypeContracts.add(contract);
        }

        voucherSubTypeResponse.setVoucherSubTypes(voucherSubTypeContracts);

        return voucherSubTypeResponse;
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public VoucherSubTypeResponse update(@RequestBody VoucherSubTypeRequest voucherSubTypeRequest, BindingResult errors) {

        voucherSubTypeRequest.getRequestInfo().setAction(Constants.ACTION_UPDATE);
        ModelMapper model = new ModelMapper();
        VoucherSubTypeResponse voucherSubTypeResponse = new VoucherSubTypeResponse();
        List<VoucherSubType> voucherSubTypes = new ArrayList<>();
        voucherSubTypeResponse.setResponseInfo(getResponseInfo(voucherSubTypeRequest.getRequestInfo()));
        VoucherSubType voucherSubType;
        VoucherSubTypeContract contract;
        List<VoucherSubTypeContract> voucherSubTypeContracts = new ArrayList<>();

        for (VoucherSubTypeContract voucherSubTypeContract : voucherSubTypeRequest.getVoucherSubTypes()) {
            voucherSubType = new VoucherSubType();
            model.map(voucherSubTypeContract, voucherSubType);
            voucherSubType.setLastModifiedBy(voucherSubTypeRequest.getRequestInfo().getUserInfo());
            voucherSubType.setLastModifiedDate(new Date());
            voucherSubTypes.add(voucherSubType);
        }

        voucherSubTypes = voucherSubTypeService.update(voucherSubTypes, errors, voucherSubTypeRequest.getRequestInfo());

        for (VoucherSubType voucherSubTypeObj : voucherSubTypes) {
            contract = new VoucherSubTypeContract();
            model.map(voucherSubTypeObj, contract);
            voucherSubTypeContracts.add(contract);
        }

        voucherSubTypeResponse.setVoucherSubTypes(voucherSubTypeContracts);

        return voucherSubTypeResponse;
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public VoucherSubTypeResponse search(@ModelAttribute VoucherSubTypeSearchContract voucherSubTypeSearchContract,
            RequestInfo requestInfo,
            BindingResult errors) {

        ModelMapper mapper = new ModelMapper();
        VoucherSubTypeSearch domain = new VoucherSubTypeSearch();
        mapper.map(voucherSubTypeSearchContract, domain);
        VoucherSubTypeContract contract;
        ModelMapper model = new ModelMapper();
        List<VoucherSubTypeContract> voucherSubTypeContracts = new ArrayList<>();
        Pagination<VoucherSubType> voucherSubTypes = voucherSubTypeService.search(domain);

        if (voucherSubTypes.getPagedData() != null) {
            for (VoucherSubType voucherSubType : voucherSubTypes.getPagedData()) {
                contract = new VoucherSubTypeContract();
                model.map(voucherSubType, contract);
                voucherSubTypeContracts.add(contract);
            }
        }

        VoucherSubTypeResponse response = new VoucherSubTypeResponse();
        response.setVoucherSubTypes(voucherSubTypeContracts);
        response.setPage(new PaginationContract(voucherSubTypes));
        response.setResponseInfo(getResponseInfo(requestInfo));

        return response;

    }

    private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId())
                .ver(requestInfo.getVer()).resMsgId(requestInfo.getMsgId())
                .resMsgId(VoucherConstants.PLACEHOLDER).status(VoucherConstants.PLACEHOLDER).build();
    }
}

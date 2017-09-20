package org.egov.egf.voucher.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.PaginationContract;
import org.egov.egf.voucher.domain.model.Voucher;
import org.egov.egf.voucher.domain.model.VoucherSearch;
import org.egov.egf.voucher.domain.service.VoucherService;
import org.egov.egf.voucher.web.contract.VoucherContract;
import org.egov.egf.voucher.web.contract.VoucherSearchContract;
import org.egov.egf.voucher.web.requests.VoucherRequest;
import org.egov.egf.voucher.web.requests.VoucherResponse;
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
    public VoucherResponse create(@RequestBody VoucherRequest voucherRequest, BindingResult errors) {
        if (errors.hasErrors()) {
            throw new CustomBindException(errors);
        }

        ModelMapper model = new ModelMapper();
        VoucherResponse voucherResponse = new VoucherResponse();
        voucherResponse.setResponseInfo(getResponseInfo(voucherRequest.getRequestInfo()));
        List<Voucher> vouchers = new ArrayList<>();
        Voucher voucher;
        List<VoucherContract> voucherContracts = new ArrayList<>();
        VoucherContract contract;

        voucherRequest.getRequestInfo().setAction(Constants.ACTION_CREATE);

        for (VoucherContract voucherContract : voucherRequest.getVouchers()) {
            voucher = new Voucher();
            model.map(voucherContract, voucher);
            voucher.setCreatedDate(new Date());
            voucher.setCreatedBy(voucherRequest.getRequestInfo().getUserInfo());
            voucher.setLastModifiedBy(voucherRequest.getRequestInfo().getUserInfo());
            vouchers.add(voucher);
        }

        vouchers = voucherService.create(vouchers, errors, voucherRequest.getRequestInfo());

        for (Voucher f : vouchers) {
            contract = new VoucherContract();
            contract.setCreatedDate(new Date());
            model.map(f, contract);
            voucherContracts.add(contract);
        }

        voucherResponse.setVouchers(voucherContracts);

        return voucherResponse;
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public VoucherResponse update(@RequestBody VoucherRequest voucherRequest, BindingResult errors) {

        if (errors.hasErrors()) {
            throw new CustomBindException(errors);
        }
        voucherRequest.getRequestInfo().setAction(Constants.ACTION_UPDATE);
        ModelMapper model = new ModelMapper();
        VoucherResponse voucherResponse = new VoucherResponse();
        List<Voucher> vouchers = new ArrayList<>();
        voucherResponse.setResponseInfo(getResponseInfo(voucherRequest.getRequestInfo()));
        Voucher voucher;
        VoucherContract contract;
        List<VoucherContract> voucherContracts = new ArrayList<>();

        for (VoucherContract voucherContract : voucherRequest.getVouchers()) {
            voucher = new Voucher();
            model.map(voucherContract, voucher);
            voucher.setLastModifiedBy(voucherRequest.getRequestInfo().getUserInfo());
            voucher.setLastModifiedDate(new Date());
            vouchers.add(voucher);
        }

        vouchers = voucherService.update(vouchers, errors, voucherRequest.getRequestInfo());

        for (Voucher voucherObj : vouchers) {
            contract = new VoucherContract();
            model.map(voucherObj, contract);
            voucherObj.setLastModifiedDate(new Date());
            voucherContracts.add(contract);
        }

        voucherResponse.setVouchers(voucherContracts);

        return voucherResponse;
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public VoucherResponse search(@ModelAttribute VoucherSearchContract voucherSearchContract, RequestInfo requestInfo,
            BindingResult errors) {

        ModelMapper mapper = new ModelMapper();
        VoucherSearch domain = new VoucherSearch();
        mapper.map(voucherSearchContract, domain);
        VoucherContract contract;
        ModelMapper model = new ModelMapper();
        List<VoucherContract> voucherContracts = new ArrayList<>();
        Pagination<Voucher> vouchers = voucherService.search(domain, errors);

        if (vouchers.getPagedData() != null) {
            for (Voucher voucher : vouchers.getPagedData()) {
                contract = new VoucherContract();
                model.map(voucher, contract);
                voucherContracts.add(contract);
            }
        }

        VoucherResponse response = new VoucherResponse();
        response.setVouchers(voucherContracts);
        response.setPage(new PaginationContract(vouchers));
        response.setResponseInfo(getResponseInfo(requestInfo));

        return response;

    }

    @PostMapping("/_reverse")
    @ResponseStatus(HttpStatus.CREATED)
    public VoucherResponse reverse(@RequestBody VoucherRequest voucherRequest, BindingResult errors) {
        if (errors.hasErrors()) {
            throw new CustomBindException(errors);
        }

        ModelMapper model = new ModelMapper();
        VoucherResponse voucherResponse = new VoucherResponse();
        voucherResponse.setResponseInfo(getResponseInfo(voucherRequest.getRequestInfo()));
        List<Voucher> vouchers = new ArrayList<>();
        Voucher voucher;
        List<VoucherContract> voucherContracts = new ArrayList<>();
        VoucherContract contract;

        voucherRequest.getRequestInfo().setAction(Constants.ACTION_CREATE);

        for (VoucherContract voucherContract : voucherRequest.getVouchers()) {
            voucher = new Voucher();
            model.map(voucherContract, voucher);
            voucher.setCreatedDate(new Date());
            voucher.setCreatedBy(voucherRequest.getRequestInfo().getUserInfo());
            voucher.setLastModifiedBy(voucherRequest.getRequestInfo().getUserInfo());
            vouchers.add(voucher);
        }

        vouchers = voucherService.reverse(vouchers, errors, voucherRequest.getRequestInfo());

        for (Voucher f : vouchers) {
            contract = new VoucherContract();
            contract.setCreatedDate(new Date());
            model.map(f, contract);
            voucherContracts.add(contract);
        }

        voucherResponse.setVouchers(voucherContracts);

        return voucherResponse;
    }

    private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }

}
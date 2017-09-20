package org.egov.egf.voucher.web.requests;

import java.util.List;

import lombok.Data;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.web.contract.PaginationContract;
import org.egov.egf.voucher.web.contract.VoucherSubTypeContract;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public @Data class VoucherSubTypeResponse {
    private ResponseInfo responseInfo;
    private List<VoucherSubTypeContract> voucherSubTypes;
    private PaginationContract page;
}
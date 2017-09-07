package org.egov.egf.bill.web.requests;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.web.contract.PaginationContract;
import org.egov.egf.bill.web.contract.BillRegisterContract;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
public @Data class BillRegisterResponse {
    private ResponseInfo responseInfo;
    private List<BillRegisterContract> billRegisters;
    private PaginationContract page;
}
package org.egov.egf.bill.web.requests;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.egf.bill.domain.model.Pagination;
import org.egov.egf.bill.web.contract.AccountDetailKey;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
public @Data class AccountDetailKeyResponse {
    private ResponseInfo responseInfo;
    private List<AccountDetailKey> accountDetailKeys;
    private Pagination page;
}
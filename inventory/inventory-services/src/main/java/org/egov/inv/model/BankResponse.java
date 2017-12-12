package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@JsonInclude(value = Include.NON_NULL)
public @Data
class BankResponse {
    private ResponseInfo responseInfo;
    private List<BankContract> banks;
}
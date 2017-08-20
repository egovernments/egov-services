package org.egov.asset.contract;

import java.util.List;

import org.egov.asset.model.Fund;
import org.egov.asset.model.Pagination;
import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
@Data
public class FundResponse {
    private ResponseInfo responseInfo;
    private List<Fund> funds;
    private Fund fund;
    private Pagination page;
}
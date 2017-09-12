package org.egov.asset.contract;

import java.util.List;

import org.egov.asset.model.Function;
import org.egov.asset.model.Pagination;
import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
@Data
public class FunctionResponse {
    private ResponseInfo responseInfo;
    private List<Function> functions;
    private Function function;
    private Pagination page;
}

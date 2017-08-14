package org.egov.asset.contract;

import java.util.List;

import org.egov.asset.model.Pagination;
import org.egov.asset.model.Scheme;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
@Data
public class SchemeResponse {
    private ResponseInfo responseInfo;
    private List<Scheme> schemes;
    private Scheme scheme;
    private Pagination page;
}

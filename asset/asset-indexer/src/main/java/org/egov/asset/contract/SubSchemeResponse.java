package org.egov.asset.contract;

import java.util.List;

import org.egov.asset.model.Pagination;
import org.egov.asset.model.SubScheme;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
@Data
public class SubSchemeResponse {
    private ResponseInfo responseInfo;
    private List<SubScheme> subSchemes;
    private SubScheme subScheme;
    private Pagination page;

}

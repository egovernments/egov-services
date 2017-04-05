package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;

@Getter
public class DepartmentRes {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo = null;

    @JsonProperty("Department")
    private Department department = null;


}

package org.egov.swm.web.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class EmployeeResponse {

    @JsonProperty("ResponseInfo")
    private final ResponseInfo responseInfo = null;

    @JsonProperty("Employee")
    private final List<Employee> employees = new ArrayList<>();

}

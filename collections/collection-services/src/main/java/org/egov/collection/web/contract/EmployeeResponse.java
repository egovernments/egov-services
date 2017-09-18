package org.egov.collection.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@Setter
@Getter
public class EmployeeResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    @JsonProperty("Employee")
    private List<Employee> employees;
}

package org.egov.swm.web.contract;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class EmployeeResponse {

    @JsonProperty("Employee")
    private final List<Employee> employees = new ArrayList<>();

}

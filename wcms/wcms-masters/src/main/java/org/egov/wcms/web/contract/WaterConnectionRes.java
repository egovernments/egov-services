package org.egov.wcms.web.contract;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.model.Connection;
import org.egov.wcms.model.WorkflowDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class WaterConnectionRes {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    @JsonProperty("Connection")
    private Connection connection;

    @JsonProperty("WorkflowDetails")
    private WorkflowDetails workflowDetails;
}

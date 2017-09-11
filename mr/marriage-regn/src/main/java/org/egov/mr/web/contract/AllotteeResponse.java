package org.egov.mr.web.contract;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.mr.model.Allottee;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AllotteeResponse {
    @JsonProperty("responseInfo")
    ResponseInfo responseInfo;

    @JsonProperty("user")
    List<Allottee> allottee;
}

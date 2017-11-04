package org.egov.contract;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.model.Depreciation;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class DepreciationResponse {
    
    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;
    
    @JsonProperty("Depreciation")
    private Depreciation depreciation;
}

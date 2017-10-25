package org.egov.inv.web.contract;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.inv.domain.model.Store;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class StoreResponse {
    @JsonProperty("responseInfo")
    private ResponseInfo responseInfo;
    
    @JsonProperty("stores")
    private List<Store> stores;

}

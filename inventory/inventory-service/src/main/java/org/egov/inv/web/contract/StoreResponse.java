package org.egov.inv.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.inv.domain.model.Store;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StoreResponse {
    @JsonProperty("responseInfo")
    private ResponseInfo responseInfo;

    @JsonProperty("stores")
    private List<Store> stores;

}

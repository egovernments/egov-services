package org.egov.inv.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.inv.domain.model.Store;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class StoreRequest {
    @NotNull
    @JsonProperty("requestInfo")
    public RequestInfo requestInfo = new RequestInfo();

    @NotNull
    @JsonProperty("stores")
    public List<Store> stores = new ArrayList<Store>();

}

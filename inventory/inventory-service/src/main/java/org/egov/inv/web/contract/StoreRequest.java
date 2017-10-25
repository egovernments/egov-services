package org.egov.inv.web.contract;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.inv.domain.model.Store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public  class StoreRequest {
    @Valid
    private RequestInfo requestInfo = new RequestInfo();
    
    @Valid
    private List<Store> stores = new ArrayList<Store>();

}

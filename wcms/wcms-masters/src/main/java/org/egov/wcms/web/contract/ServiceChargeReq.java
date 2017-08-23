package org.egov.wcms.web.contract;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.model.ServiceCharge;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class ServiceChargeReq {
    
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
    
    @JsonProperty("ServiceCharges")
    private List<ServiceCharge> serviceCharge;

}

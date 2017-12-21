package org.egov.egf.bill.web.contract;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class RequestInfoWrapper {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

}

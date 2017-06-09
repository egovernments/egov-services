package org.egov.pgrrest.master.web.contract;

import javax.validation.constraints.NotNull;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgrrest.master.model.ReceivingModeType;

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
public class ReceivingModeTypeReq {
	
	   @NotNull
	    @JsonProperty("RequestInfo")
	    private RequestInfo requestInfo;

	    @JsonProperty("ReceivingModeType")
	    private ReceivingModeType modeType;

}

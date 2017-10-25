package org.egov.lams.common.web.request;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lams.common.web.contract.LandRegister;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LandRegisterRequest {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("landRegisters")
	@Valid
	private List<LandRegister> landRegisters;
}

package org.egov.lams.common.web.response;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.lams.common.web.contract.LandRegister;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LandRegisterResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("landRegisters")
	private List<LandRegister> landRegisters;
}

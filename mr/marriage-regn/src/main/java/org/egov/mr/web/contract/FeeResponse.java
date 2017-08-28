package org.egov.mr.web.contract;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.mr.model.Fee;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class FeeResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("Fees")
	private List<Fee> fees;
	
}

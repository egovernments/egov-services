package org.egov.wcms.web.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.model.Gapcode;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GapcodeResponse {
	
	 	@JsonProperty("ResponseInfo")
	    private ResponseInfo responseInfo;
	    @JsonProperty("Gapcodes")
	    private List<Gapcode> gapcodeResponse = new ArrayList<>();;
	
	

}

package org.egov.wcms.web.contract;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.model.Gapcode;

import com.fasterxml.jackson.annotation.JsonProperty;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class GapcodeRequest {
	
	 	@NotNull
	    @JsonProperty("RequestInfo")
	    private RequestInfo requestInfo;

	    @JsonProperty("Gapcodes")
	    private List<Gapcode> gapcode= new ArrayList<>();

}

package org.egov.lams.web.contract;

import javax.validation.Valid;

import lombok.*;
import org.egov.lams.model.Agreement;

import com.fasterxml.jackson.annotation.JsonProperty;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class AgreementRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	
	@JsonProperty("Agreement")
	@Valid
	private Agreement agreement;
}

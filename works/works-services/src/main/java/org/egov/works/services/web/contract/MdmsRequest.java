package org.egov.works.services.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.works.commons.web.contract.MdmsCriteria;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MdmsRequest {

	@JsonProperty(value="RequestInfo")
	private RequestInfo requestInfo;

	@JsonProperty(value="MdmsCriteria")
	private MdmsCriteria mdmsCriteria;

}
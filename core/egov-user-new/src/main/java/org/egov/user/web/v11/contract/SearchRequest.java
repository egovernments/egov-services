package org.egov.user.web.v11.contract;

import javax.validation.constraints.NotNull;
import org.egov.common.contract.request.RequestInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@ToString
public class SearchRequest {

	@NotNull
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@NotNull
	@JsonProperty("searchCriteria")
	private Object searchCriteria;
}

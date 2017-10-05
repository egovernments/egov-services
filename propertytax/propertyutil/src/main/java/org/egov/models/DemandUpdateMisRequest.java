package org.egov.models;

import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DemandUpdateMisRequest {

	@NotNull
	private String tenantId;

	@NotNull
	private Set<String> id;

	@NotNull
	private String consumerCode;

	private RequestInfo requestInfo;
}

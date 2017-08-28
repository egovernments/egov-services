package org.egov.mr.web.contract;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.model.RegistrationUnit;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class RegnUnitRequest {

	@Valid
	@NotNull
	private RequestInfo requestInfo;

	@Valid
	@NotNull
	private RegistrationUnit regnUnit;

}

package org.egov.mr.web.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
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
public class RegnUnitResponse {
	private ResponseInfo responseInfo;

	private List<RegistrationUnit> regnUnits = new ArrayList<RegistrationUnit>();
}

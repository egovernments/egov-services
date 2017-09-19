package org.egov.lams.web.contract;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.egov.lams.model.Demand;

import com.fasterxml.jackson.annotation.JsonProperty;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class DemandResponse {
	@JsonProperty("ResponseInfo")
	ResponseInfo responseInfo;

	@JsonProperty("Demand")
	private List<Demand> demands = new ArrayList<Demand>();
}

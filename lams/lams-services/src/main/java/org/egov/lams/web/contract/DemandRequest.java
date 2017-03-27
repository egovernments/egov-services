package org.egov.lams.web.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.Demand;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class DemandRequest {
	@JsonProperty("RequestInfo")
	RequestInfo requestInfo;

	@JsonProperty("Demand")
	private List<Demand> demand = new ArrayList<Demand>();
}

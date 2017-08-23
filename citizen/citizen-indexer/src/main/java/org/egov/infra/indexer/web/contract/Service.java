package org.egov.infra.indexer.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString

public class Service {
	@JsonProperty("ServiceMaps")
	private ServiceMaps serviceMaps;
}

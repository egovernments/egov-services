package org.egov.web.indexer.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Boundary {
	
	private Long id;
	private String name;
	private Float longitude;
    private Float latitude;
    private Long boundaryNum;
}

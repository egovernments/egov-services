package org.egov.web.indexer.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class City {
	// TODO : remove default values once the dependant rest service is ready.
	private String name="Kurnool";
	private String code="KC";
	private String districtCode="KC01";
	private String districtName="Kurnool District";
	private String grade="Grade A";
	private String domainURL="Localhost";
	private String regionName="Kurnool Region";
}

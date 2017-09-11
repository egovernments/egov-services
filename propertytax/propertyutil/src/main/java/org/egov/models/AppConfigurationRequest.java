package org.egov.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppConfigurationRequest {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("appConfigurations")
	private List<AppConfiguration> appConfigurations;
}

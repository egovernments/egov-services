package org.egov.lams.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PropertiesManager {
	
	@Value("${egov.services.asset_service.hostname}")
	private String assetServiceHostName;

	@Value("${egov.services.allottee_service.hostname}")
	private String allotteeServiceHostName;
}

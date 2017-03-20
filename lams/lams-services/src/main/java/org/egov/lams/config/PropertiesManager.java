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

	@Value("${egov.services.asset_service.basepath}")
	private String assetServiceBasePAth;

	@Value("${egov.services.asset_service.searchpath}")
	private String assetServiceSearchPath;

	@Value("${egov.services.allottee_service.hostname}")
	private String allotteeServiceHostName;

	@Value("${egov.services.allottee_service.basepath}")
	private String allotteeServiceBasePAth;

	@Value("${egov.services.allottee_service.searchpath}")
	private String allotteeServiceSearchPath;

	@Value("${egov.services.allottee_service.createpath}")
	private String allotteeServiceCreatePAth;
	
	@Value("${egov.services.lams.ulb_number}")
	private String ulbNumber;

	@Value("${egov.services.lams.agreementnumber_sequence}")
	private String agreementNumberSequence;

	@Value("${egov.services.lams.agreementnumber_prefix}")
	private String lamsPrefix;

	@Value("${egov.services.lams.acknowledgementnumber_sequence}")
	private String acknowledgementNumberSequence;

}

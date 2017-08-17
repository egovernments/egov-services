package org.egov.tradelicense.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.ToString;

@Configuration
@Getter
@ToString
public class PropertiesManager {

	@Value("${app.timezone}")
	private String appTimeZone;

	@Value("${egov.services.tl-masters_v1.hostname}")
	private String tradeLicenseMasterServiceHostName;

	@Value("${egov.services.tl-masters_v1.basepath}")
	private String tradeLicenseMasterServiceBasePath;

	@Value("${egov.services.tl-indexer_v1.basepath}")
	private String tradeLicenseIndexerServiceBasePath;

	@Value("${egov.services.tl-indexer_v1.license.searchpath}")
	private String tradeLicenseIndexerLicenseSearchPath;

	@Value("${aadhaar.validation.required}")
	private Boolean adhaarValidation;

	@Value("${egov.services.tl-indexer_v1.hostname}")
	private String tradeLicenseIndexerServiceHostName;

	@Value("${ptis.validation.required}")
	private Boolean ptisValidation;

	@Value("${egov.services.pt_property.hostname}")
	private String propertyHostname;

	@Value("${egov.services.pt_property.basepath}")
	private String propertyBasepath;

	@Value("${egov.services.pt_property.searchpath}")
	private String propertySearchpath;

	@Value("${egov.services.tl-masters_v1.documenttype.searchpath}")
	private String documentServiceSearchPath;

	@Value("${egov.services.tl-masters_v1.category.searchpath}")
	private String categoryServiceSearchPath;
	
	@Value("${egov.services.tl-masters_v1.uom.searchpath}")
	private String uomServiceSearchPath;
	
	@Value("${egov.services.tl-masters_v1.status.searchpath}")
	private String statusServiceSearchPath;

	@Value("${egov.services.egov-location.hostname}")
	private String locationServiceHostName;

	@Value("${egov.services.egov-location.basepath}")
	private String locationServiceBasePath;

	@Value("${egov.services.egov-location.searchpath}")
	private String locationServiceSearchPath;

	@Value("${egov.services.tl-services.create.legacy.tradelicense.validated}")
	private String createLegacyTradeValidated;

	@Value("${egov.services.egf-masters.hostname}")
	private String financialYearServiceHostName;

	@Value("${egov.services.egf-masters.basepath}")
	private String financialYearServiceBasePath;

	@Value("${egov.services.egf-masters.searchpath}")
	private String financialYearServiceSearchPath;

	@Value("${egov.services.tl-services.pageSize.default}")
	private String pageSize;

	@Value("${egov.services.tl-services.pageNumber.default}")
	private String pageNumber;

	@Value("${egov.services.id_service.hostname}")
	private String idGenServiceBasePathTopic;

	@Value("${egov.services.id_service.createpath}")
	private String idGenServiceCreatePathTopic;

	@Value("${id.tlnName}")
	private String idTLNumberGenNameServiceTopic;

	@Value("${id.tlnFormat}")
	private String idTLNumberGenFormatServiceTopic;

	@Value("${id.anName}")
	private String idApplicationNumberGenNameServiceTopic;

	@Value("${id.anFormat}")
	private String idApplicationNumberGenFormatServiceTopic;
}
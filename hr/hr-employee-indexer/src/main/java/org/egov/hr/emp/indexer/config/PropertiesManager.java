package org.egov.hr.emp.indexer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class PropertiesManager {
	
	@Value("${egov.services.boundary.host}")
	private String boundaryServiceSearchUri;
	
	@Value("${egov.services.boundary.search.contextpath}")
	private String boundarySearchContextPath;
	
	@Value("${egov.services.eis.commonmaster.host}")
	private String commonMasterServiceSearchUri;
	
	@Value("${egov.services.language.search.contextpath}")
	private String languageSearchContextPath;
	
	@Value("${egov.services.religion.search.contextpath}")
	private String religionSearchContextPath;
	
	@Value("${egov.services.category.search.contextpath}")
	private String categoryrSearchContextPath;
	
	@Value("${egov.services.community.search.contextpath}")
	private String communitySearchContextPath;
	
	@Value("${egov.services.eis.hrmaster.host}")
	private String hrMasterServiceSearchUri;
	
	@Value("${egov.services.employeetype.search.contextpath}")
	private String employeetypeSearchContextPath;
	
	@Value("${egov.services.recruitmentmode.search.contextpath}")
	private String recruitmentmodeSearchContextPath;
	
	@Value("${egov.services.recruitmentquota.search.contextpath}")
	private String recruitmentquotaSearchContextPath;
	
	@Value("${egov.services.recruitmenttype.search.contextpath}")
	private String recruitmenttypeSearchContextPath;
	

}

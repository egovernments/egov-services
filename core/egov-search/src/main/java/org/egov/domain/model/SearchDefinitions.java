package org.egov.domain.model;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.egov.swagger.model.SearchDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
@ConfigurationProperties 
@EnableConfigurationProperties(SearchDefinitions.class)
public class SearchDefinitions {
	
	
	@JsonProperty("SearchDefinition")
    public List<SearchDefinition> searchDefinition = new ArrayList<>();
	
	@JsonProperty("moduleKey")
	private String moduleKey;

	private HashMap<String, SearchDefinition> definitionMap = new HashMap<>();
	

	
	public String getModuleKey() {
		return moduleKey;
	}
	public void setModuleKey(String moduleKey) {
		this.moduleKey = moduleKey;
	}


	private HashMap<String, SearchDefinition> duplicateReportKeys = new HashMap<>();
	
	
	public SearchDefinition getSearchDefinition(String name){
		return definitionMap.get(name);
	}
	public List<SearchDefinition> getDuplicateSearchDefinition(){
		List<SearchDefinition> localreportDefinitions = new ArrayList<>(duplicateReportKeys.values());
		return localreportDefinitions;
	}
	
	public List<SearchDefinition> getReportDefinitions() {
		return searchDefinition;
	}

	public void setReportDefinitions(List<SearchDefinition> reportDefinitions) {
		this.searchDefinition = reportDefinitions;
		System.out.println("Coming in to the set report definition method");
		for(SearchDefinition rd : reportDefinitions){
			String reportKey = "";
			if(rd.getModuleName() != null){
				reportKey = rd.getModuleName()+" " +rd.getReportName();
				}
				else {
					reportKey = rd.getReportName();
				}
			if(definitionMap.get(rd.getReportName()) == null) {
				definitionMap.put(reportKey, rd);

				
					}
			else{
				definitionMap.put(reportKey, rd);

					}
			
			
		}
		System.out.println("Hash Map Keys are :"+definitionMap.keySet());
		
	}

	@Override
	public String toString() {
		return "ReportDefinitions [moduleKey="+moduleKey+" reportDefinitions=" + searchDefinition + "]";
	}
	
}



package org.egov.domain.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.egov.swagger.model.SearchDefinition;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportDefinitionCollection {
	@JsonProperty("moduleName")
	public String moduleName;
	
	@JsonProperty("ReportDefinitions")
    public List<SearchDefinition> reportDefinitions = new ArrayList<>();

	private HashMap<String, SearchDefinition> definitionMap = new HashMap<>();
	
	private HashMap<String, SearchDefinition> duplicateReportKeys = new HashMap<>();
	
	
	
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public SearchDefinition getReportDefinition(String name){
		return definitionMap.get(name);
	}
	public List<SearchDefinition> getDuplicateReportDefinition(){
		List<SearchDefinition> localreportDefinitions = new ArrayList<>(duplicateReportKeys.values());
		return localreportDefinitions;
	}
	
	public List<SearchDefinition> getReportDefinitions() {
		return reportDefinitions;
	}

	public void setReportDefinitions(List<SearchDefinition> reportDefinitions) {
		this.reportDefinitions = reportDefinitions;
		for(SearchDefinition rd : reportDefinitions){
			String reportKey = rd.getReportName();
					
			if(definitionMap.get(rd.getReportName()) == null) 
					{
				definitionMap.put(reportKey, rd);
					}
			else{
				duplicateReportKeys.put(reportKey, rd);
			}
			
		}
	}

	@Override
	public String toString() {
		return "ReportDefinitions [moduleName="+moduleName+ " reportDefinitions=" + reportDefinitions + "]";
	}
}

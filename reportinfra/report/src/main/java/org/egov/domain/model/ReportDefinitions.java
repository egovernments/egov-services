package org.egov.domain.model;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.egov.swagger.model.ReportDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
@ConfigurationProperties 
@EnableConfigurationProperties(ReportDefinitions.class)
public class ReportDefinitions {
	@JsonProperty("ReportDefinitions")
    public List<ReportDefinition> reportDefinitions = new ArrayList<>();

	private HashMap<String, ReportDefinition> definitionMap = new HashMap<>();
    
	
	
	public ReportDefinition getReportDefinition(String name){
		return definitionMap.get(name);
	}
	
	public List<ReportDefinition> getReportDefinitions() {
		return reportDefinitions;
	}
  


	public void setReportDefinitions(List<ReportDefinition> reportDefinitions) {
		this.reportDefinitions = reportDefinitions;
		for(ReportDefinition rd : reportDefinitions){
			definitionMap.put(rd.getReportName(), rd);
		}
	}



	@Override
	public String toString() {
		return "ReportDefinitions [reportDefinitions=" + reportDefinitions + "]";
	}
    
	
}



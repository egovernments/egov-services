package org.egov.report.repository.builder;

import java.util.List;

import org.egov.domain.model.ReportYamlMetaData;
import org.egov.swagger.model.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportQueryBuilder {
		
	public String buildQuery(List<SearchParam> searchParams, String tenantId, ReportYamlMetaData reportYamlMetaData){
		
		System.out.println("searchParams:" + searchParams);
		
		String baseQuery = reportYamlMetaData.getQuery();
		baseQuery = baseQuery.replaceAll("\\$tenantid","'"+tenantId+"'");
		for(SearchParam searchParam : searchParams){
				
			Object value = searchParam.getValue();
			if(value instanceof Number)
			baseQuery = baseQuery.replaceAll("\\$"+searchParam.getName(),value.toString());
			
			if(value instanceof String)
			baseQuery = baseQuery.replaceAll("\\$"+searchParam.getName(),"'"+value.toString()+"'");
			
		}
		System.out.println("baseQuery :"+baseQuery);
		return baseQuery;
	}

}

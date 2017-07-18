package org.egov.report.repository.builder;

import java.util.List;

import org.egov.report.repository.ReportRepository;
import org.egov.swagger.model.ReportDefinition;
import org.egov.swagger.model.SearchParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportQueryBuilder {
		 
	public static final Logger LOGGER = LoggerFactory.getLogger(ReportQueryBuilder.class);
	   
	public String buildQuery(List<SearchParam> searchParams, String tenantId, ReportDefinition reportDefinition){
		
		LOGGER.info("searchParams:" + searchParams);
		
		String baseQuery = reportDefinition.getQuery();
		baseQuery = baseQuery.replaceAll("\\$tenantid","'"+tenantId+"'");
		
		for(SearchParam searchParam : searchParams){
				
			Object value = searchParam.getInput();
			//Object value = null;
			if(value instanceof Number)
			baseQuery = baseQuery.replaceAll("\\$"+searchParam.getName(),value.toString());
			
			if(value instanceof String)
			baseQuery = baseQuery.replaceAll("\\$"+searchParam.getName(),"'"+value.toString()+"'");
			
		}
		LOGGER.info("baseQuery :"+baseQuery);
		return baseQuery;
	}

}

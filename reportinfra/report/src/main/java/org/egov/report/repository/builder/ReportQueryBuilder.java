package org.egov.report.repository.builder;

import java.util.List;

import org.egov.report.repository.ReportRepository;
import org.egov.swagger.model.ReportDefinition;
import org.egov.swagger.model.SearchColumn;
import org.egov.swagger.model.SearchParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportQueryBuilder {
		 
	public static final Logger LOGGER = LoggerFactory.getLogger(ReportQueryBuilder.class);
	   
	public String buildQuery(List<SearchParam> searchParams, String tenantId, ReportDefinition reportDefinition){
		
		String baseQuery;
		
		LOGGER.info("searchParams:" + searchParams);
		if(reportDefinition.getQuery().contains("UNION")){
			baseQuery = generateUnionQuery(searchParams, tenantId, reportDefinition);
		} else {
			
			baseQuery = generateQuery(searchParams, tenantId, reportDefinition);
			
		}
		
		baseQuery = baseQuery.replaceAll("\\$tenantid","'"+tenantId+"'");
		
		for(SearchParam searchParam : searchParams){
				
			Object value = searchParam.getInput();
			
			if(value instanceof Number)
			baseQuery = baseQuery.replaceAll("\\$"+searchParam.getName(),value.toString());
			
			if(value instanceof String)
			baseQuery = baseQuery.replaceAll("\\$"+searchParam.getName(),"'"+value.toString()+"'");
			
		}
		LOGGER.info("baseQuery :"+baseQuery);
		return baseQuery;
	}
public String generateQuery(List<SearchParam> searchParams, String tenantId, ReportDefinition reportDefinition){
		
		LOGGER.info("searchParams:" + searchParams);
		
		StringBuffer baseQuery = new StringBuffer(reportDefinition.getQuery());
		
		String orderByQuery = reportDefinition.getOrderByQuery();
		
		String groupByQuery = reportDefinition.getGroupByQuery();
		
		for(SearchParam searchParam : searchParams){
			
			Object name = searchParam.getName();
			
		    for (SearchColumn sc : reportDefinition.getSearchParams()) 
		    {
		            if(name.equals(sc.getName()) && !sc.getIsMandatory()){
		            	if(sc.getSearchClause() != null) {
		            	baseQuery.append(" " +sc.getSearchClause());
		            	}
		            }
		    }
			
		
	}
	if(groupByQuery != null){
    baseQuery.append(" "+ groupByQuery);
	}
	if(orderByQuery != null) {
		
		baseQuery.append(" "+ orderByQuery);
		
	}
    LOGGER.info("generate baseQuery :"+baseQuery);
    return baseQuery.toString();
}

public String generateUnionQuery(List<SearchParam> searchParams, String tenantId, ReportDefinition reportDefinition){
	
	LOGGER.info("searchParams:" + searchParams);
	
	String baseQuery = reportDefinition.getQuery();
	
	String[] unionQueries = baseQuery.split("UNION");
	
	StringBuffer query = new StringBuffer();
	StringBuffer finalQuery = new StringBuffer();
	
	for(int i=0; i<unionQueries.length; i++) {
		
		query = new StringBuffer(unionQueries[i]);
		
		for(SearchParam searchParam : searchParams){
			
			Object name = searchParam.getName();
			
		    for (SearchColumn sc : reportDefinition.getSearchParams()) 
		    {
		            if(name.equals(sc.getName()) && !sc.getIsMandatory()){
		            	if(sc.getSearchClause() != null) {
		            	query.append(" " +sc.getSearchClause());
		            	}
		            }
		    }
			
		
	}
		String groupByQuery = reportDefinition.getGroupByQuery(); 
		if(groupByQuery != null){
			query.append(" "+ groupByQuery);
	    }
		if(i > 0) {
		finalQuery.append(" UNION "+query.toString()+ " ");
		} else {
			finalQuery.append(query.toString());
		}
	}
	String orderByQuery = reportDefinition.getOrderByQuery(); 
	if(orderByQuery != null){
		finalQuery.append(" "+ orderByQuery);
    }
	
	finalQuery.toString();
LOGGER.info("generate baseUnionQuery :"+finalQuery);
return finalQuery.toString();
}
}

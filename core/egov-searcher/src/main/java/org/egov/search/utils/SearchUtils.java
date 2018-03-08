package org.egov.search.utils;

import org.egov.search.model.Params;
import org.egov.search.model.Query;
import org.egov.search.model.SearchParams;
import org.egov.search.model.SearchRequest;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

@Component
public class SearchUtils {
	
	public static final Logger logger = LoggerFactory.getLogger(SearchUtils.class);

	public String buildQuery(SearchRequest searchRequest, SearchParams searchParam, Query query){
		StringBuilder queryString = new StringBuilder();
		StringBuilder where = new StringBuilder();
		queryString.append(query.getBaseQuery());
		String whereClause = null;
		try{
			whereClause = buildWhereClause(searchRequest, searchParam);
		}catch(CustomException e){
			throw e;
		}
		if(null == whereClause){
			return whereClause;
		}
		where.append(" where ( ")
		           .append(whereClause.toString()+ " ) ");	
		
		if(null != query.getGroupBy()){
			queryString.append(" group by ")
						.append(query.getGroupBy());
		}
		
		if(null != query.getOrderBy()){
			where.append(" order by ")
						.append(query.getOrderBy().split(",")[0])
						.append(" ")
						.append(query.getOrderBy().split(",")[1]);
		}
		
		if(null != query.getSort()){
			queryString.append(" "+query.getSort());
		}
		
		String finalQuery = queryString.toString().replace("$where", where.toString());
		logger.info("Final Query: "+finalQuery);
		
		return finalQuery;
	}
	
	public 	String buildWhereClause(SearchRequest searchRequest, SearchParams searchParam){
		StringBuilder whereClause = new StringBuilder();
		ObjectMapper mapper = new ObjectMapper();
		if(null != searchParam){
			String condition = searchParam.getCondition();
			for(Params param: searchParam.getParams()){
				Object paramValue = null ;
				try{
					paramValue = JsonPath.read(mapper.writeValueAsString(searchRequest), param.getJsonPath());
				}catch(Exception e){
					logger.error("param: "+param.getName()+" is not provided");
					logger.error("Exception: ",e);
					if(param.getIsMandatory()){
						throw new CustomException(HttpStatus.BAD_REQUEST.toString(), 
								"Mandatory param for search not provided. Param: "+param.getName());					
					}else
						continue;
				}
				if(null == paramValue){
					if(param.getIsMandatory()){
						logger.error("param: "+param.getName()+" is not provided");
						throw new CustomException(HttpStatus.BAD_REQUEST.toString(), 
								"Mandatory param for search not provided. Param: "+param.getName());	
					}else{
						continue;
					}
				}else if(paramValue instanceof net.minidev.json.JSONArray){ //TODO: might need to add some more types
					logger.info("param: "+param.getName());
					JSONArray array = (JSONArray)paramValue;
					StringBuilder paramBuilder = new StringBuilder();
					for (Object object : array) {
						paramBuilder.append("'"+object.toString()+"'");
						if(array.indexOf(object)!=array.size()-1)
							paramBuilder.append(",");
					}
					whereClause.append(param.getName())
					  .append(" IN ")
					  .append("(")
					  .append(paramBuilder.toString())
					  .append(")");
				}else{
					logger.info("param: "+param.getName());
					String operator = " = ";
					if(param.getJsonPath().contains("startDate")) {
						operator = " >= ";
					}else if(param.getJsonPath().contains("endDate")) {
						operator = " <= ";
					}
					whereClause.append(param.getName())
					   .append(operator)
					   .append("'"+paramValue.toString()+"'");
				}
			    whereClause.append(" "+condition+" ");
			}
		}
		Integer index = whereClause.toString().lastIndexOf(searchParam.getCondition());
		String where = whereClause.toString().substring(0, index);

		logger.info("WHERE clause: "+where);
		return where;
	}
	
	
}

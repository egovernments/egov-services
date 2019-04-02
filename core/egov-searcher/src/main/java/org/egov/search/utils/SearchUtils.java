package org.egov.search.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.search.model.Definition;
import org.egov.search.model.Pagination;
import org.egov.search.model.Params;
import org.egov.search.model.Query;
import org.egov.search.model.SearchDefinition;
import org.egov.search.model.SearchParams;
import org.egov.search.model.SearchRequest;
import org.egov.tracer.model.CustomException;
import org.json.JSONArray;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@Component
public class SearchUtils {

	public static final Logger logger = LoggerFactory.getLogger(SearchUtils.class);

	@Value("${pagination.default.page.size}")
	private Integer defaultPageSize;

	@Value("${pagination.default.offset}")
	private Integer defaultOffset;

	public String buildQuery(SearchRequest searchRequest, SearchParams searchParam, Query query,Map<String, Object> preparedSt ) {
		StringBuilder queryString = new StringBuilder();
		StringBuilder where = new StringBuilder();
		String finalQuery = null;
		queryString.append(query.getBaseQuery());
		String whereClause = buildWhereClause(searchRequest, searchParam,preparedSt);
		String paginationClause = getPaginationClause(searchRequest, searchParam.getPagination(),preparedSt);
		if (null == whereClause) {
			return whereClause;
		}
		where.append(" where ( ").append(whereClause.toString() + " ) ");
		if (null != query.getGroupBy()) {
			queryString.append(" group by ").append(query.getGroupBy());
		}
		if (null != query.getOrderBy()) {
			where.append(" order by ").append(query.getOrderBy().split(",")[0]).append(" ")
					.append(query.getOrderBy().split(",")[1]);
		}
		if (null != query.getSort()) {
			queryString.append(" " + query.getSort());
		}
		finalQuery = queryString.toString().replace("$where", where.toString());
		finalQuery = finalQuery.replace("$pagination", paginationClause);
		logger.info("Final Query: " + finalQuery);

		return finalQuery;
	}

	public String buildWhereClause(SearchRequest searchRequest, SearchParams searchParam,Map<String, Object> preparedSt) {
		StringBuilder whereClause = new StringBuilder();
		ObjectMapper mapper = new ObjectMapper();
		String condition = searchParam.getCondition();
		for (Params param : searchParam.getParams()) {
			Object paramValue = null;
			try {
				paramValue = JsonPath.read(mapper.writeValueAsString(searchRequest), param.getJsonPath());
				if (null == paramValue) {
					continue;
				}
				} catch (Exception e) {
					continue;
				}
			if (paramValue instanceof net.minidev.json.JSONArray) { 
				net.minidev.json.JSONArray array = (net.minidev.json.JSONArray) paramValue;
				List<String>paramValueList=new ArrayList();
				for (int i = 0; i < array.size(); i++) {
				paramValueList.add(array.get(i).toString());
				}
				whereClause.append(param.getName()).append(" IN ").append("(").append(":"+param.getName())
						.append(")");
				preparedSt.put(param.getName(),paramValueList);
			} else {
				logger.debug("param: " + param.getName());
				String operator = (null != param.getOperator() && !param.getOperator().isEmpty()) ? param.getOperator()
						: "=";
				if (operator.equals("GE"))
					operator = ">=";
				if (operator.equals("LE"))
					operator = "<=";
				if (operator.equals("LIKE")) {
					paramValue = "%" + paramValue + "%";
					whereClause.append(param.getName()).append(" " + operator + " ").append(":"+param.getName());
					preparedSt.put(param.getName(), paramValue);
				} else {
					whereClause.append(param.getName()).append(" " + operator + " ").append(":"+param.getName());
					preparedSt.put(param.getName(), paramValue);
				}
			}
			whereClause.append(" " + condition + " ");
		}
		Integer index = whereClause.toString().lastIndexOf(searchParam.getCondition());
		String where = whereClause.toString().substring(0, index);
		return where;
	}

	public String getPaginationClause(SearchRequest searchRequest, Pagination pagination,Map<String, Object> preparedSt) {
		StringBuilder paginationClause = new StringBuilder();
		ObjectMapper mapper = new ObjectMapper();
		Integer limit = null;
		Integer offset = null;
		Integer limitValue=null;
		Integer offsetValue=null;
		if (null != pagination) {
			try {
				limit = JsonPath.read(mapper.writeValueAsString(searchRequest), pagination.getNoOfRecords());
				offset = JsonPath.read(mapper.writeValueAsString(searchRequest), pagination.getOffset());
			} catch (Exception e) {
				logger.error("Error while fetching limit and offset, using default values.");
			}
		}
		if(limit!=null)
			limitValue=limit;
		else
			limitValue=defaultPageSize;
		if(offset!=null)
			offsetValue=offset;
		else
			offsetValue=defaultOffset;
			
		paginationClause.append(" LIMIT ").append(":Limit");
		preparedSt.put("Limit", limitValue);
		paginationClause.append(" OFFSET ").append(":Offset");
		preparedSt.put("Offset", offsetValue);
		return paginationClause.toString();
	}

	public Definition getSearchDefinition(Map<String, SearchDefinition> searchDefinitionMap, String moduleName,
			String searchName) {
		logger.debug("Fetching Definitions for module: " + moduleName + " and search feature: " + searchName);
		List<Definition> definitions = null;
		try {
			definitions = searchDefinitionMap.get(moduleName).getDefinitions().parallelStream()
					.filter(def -> (def.getName().equals(searchName))).collect(Collectors.toList());
		} catch (Exception e) {
			throw new CustomException(HttpStatus.BAD_REQUEST.toString(),
					"There's no Search Definition provided for this search feature");
		}
		if (0 == definitions.size()) {
			throw new CustomException(HttpStatus.BAD_REQUEST.toString(),
					"There's no Search Definition provided for this search feature");
		}
		return definitions.get(0);

	}

	public List<String> convertPGOBjects(List<PGobject> maps){
		List<String> result = new ArrayList<>();
		if(null != maps || !maps.isEmpty()) {
			for(PGobject obj: maps){
				if(null == obj.getValue())
					break;
				String tuple = obj.toString();
				if(tuple.startsWith("[") && tuple.endsWith("]")){
					try {
						JSONArray jsonArray = new JSONArray(tuple);
						for(int i = 0; i < jsonArray.length();  i++){
							result.add(jsonArray.get(i).toString());
						}
					}catch(Exception e) {
						logger.error("Error while building json array!", e);
					}
				}else{
					try{
						result.add(obj.getValue());
					}catch(Exception e){
						throw e;
					}
				}
			}
		}
		
		return result;
	}

}

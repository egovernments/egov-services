package org.egov.report.repository.builder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swagger.model.ExternalService;
import org.egov.swagger.model.ReportDefinition;
import org.egov.swagger.model.SearchColumn;
import org.egov.swagger.model.SearchParam;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@Component
public class ReportQueryBuilder {
	
	@Autowired
	private RestTemplate restTemplate;
		 
	public static final Logger LOGGER = LoggerFactory.getLogger(ReportQueryBuilder.class);
	   
	public String buildQuery(List<SearchParam> searchParams, String tenantId, ReportDefinition reportDefinition){
		
		String baseQuery = null;
		
		
		StringBuffer csinput = new StringBuffer();
		
		if(reportDefinition.getQuery().contains("UNION")){
			baseQuery = generateUnionQuery(searchParams, tenantId, reportDefinition);
		}else if(reportDefinition.getQuery().contains("FULLJOIN")){
			baseQuery = generateJoinQuery(searchParams, tenantId, reportDefinition);
		} 
		else {
			
			baseQuery = generateQuery(searchParams, tenantId, reportDefinition,baseQuery);
			
		}
		
		try {
			if(reportDefinition.getExternalService().size() > 0) {
				baseQuery = populateExternalServiceValues(reportDefinition, baseQuery,tenantId);
			} } catch(JSONException e){
				e.printStackTrace();
			}
		
		baseQuery = baseQuery.replaceAll("\\$tenantid","'"+tenantId+"'");
		
		for(SearchParam searchParam : searchParams){
				
			Object value = searchParam.getInput();
			
			if(value instanceof Number)
			{
			baseQuery = baseQuery.replaceAll("\\$"+searchParam.getName(),value.toString());
			}
			
			if(value instanceof String ){
				
			baseQuery = baseQuery.replaceAll("\\$"+searchParam.getName(),"'"+value.toString()+"'");
			} if (value instanceof Boolean ){
				
				baseQuery = baseQuery.replaceAll("\\$"+searchParam.getName(),value.toString());
				
			}
			if(value instanceof ArrayList<?>) {
				
				List<String> arrayInput = (ArrayList)value;
			
			    for(int i=0;i<arrayInput.size();i++) {
			    	if (i < (arrayInput.size()-1)) {
			    	csinput.append("'"+arrayInput.get(i)+"',");
			    	} else {
			    		csinput.append("'"+arrayInput.get(i)+"'");
			    	}
			    	
			    }
				baseQuery = baseQuery.replaceAll("\\$"+searchParam.getName(),csinput.toString());
			}
				
		}
		
		LOGGER.info("baseQuery :"+baseQuery);
		return baseQuery;
	}
	
	private String populateExternalServiceValues(ReportDefinition reportDefinition, String baseQuery,String tenantid)
			throws JSONException {
		String url;
		String res = "";
		String replacetableQuery = baseQuery;
		
		for (ExternalService es : reportDefinition.getExternalService()) {
			
			url = es.getApiURL();
			url = url.replaceAll("\\$currentTime", Long.toString(getCurrentTime()));
			 String[] stateid = null;
			if(es.getStateData() && (!tenantid.equals("default"))) {
				stateid = tenantid.split("\\.");
				url = url.replaceAll("\\$tenantid",stateid[0]);
			} else {
			
			url = url.replaceAll("\\$tenantid",tenantid);
			}
			
			
			
			
			
			URI uri = URI.create(url);
			try {
			res = restTemplate.postForObject(uri, getRInfo(),String.class);
			} catch(Exception e){
				e.printStackTrace();
			}

			Object jsonObject = JsonPath.read(res,es.getEntity());
			
			JSONArray mdmsArray = new JSONArray(jsonObject.toString());
			 
			 StringBuffer finalString = new StringBuffer();
			 
			 for (int i = 0; i < mdmsArray.length(); i++) {
					JSONObject obj = mdmsArray.getJSONObject(i);

					StringBuffer sb = new StringBuffer();
					sb.append("(");

					String[] jsonKeys = es.getKeyOrder().split(",");

					for (int k=0; k<jsonKeys.length; k++) {

						String value = "";
						if(obj.has(jsonKeys[k])){
					    value = String.valueOf(obj.get(jsonKeys[k]));
						} 

						sb.append("'" + value + "'");
						if ((k != jsonKeys.length-1)) {
							sb.append(",");
						}
					}
					sb.append(")");
					if (i != (mdmsArray.length() - 1)) {
						sb.append(",");
					}

					finalString.append(sb);
				
				}
			 
			 if(mdmsArray.length() == 0) {
				 StringBuffer sb = new StringBuffer();
				 sb.append("(");
				 int i = 0;
				 for(String key: es.getKeyOrder().split(",")){
					 if(i != es.getKeyOrder().split(",").length-1) {
					 sb.append("'',");
					 } else {
						 sb.append("''");
					 }
					 i++;
				 }
				 sb.append(")");
				 finalString.append(sb);
			 }
		       
			 replacetableQuery = replacetableQuery.replace(es.getTableName(), finalString.toString());
			
		}
		return replacetableQuery;
	}
public String generateQuery(List<SearchParam> searchParams, String tenantId, ReportDefinition reportDefinition,String inlineQuery){
		
		
		
		StringBuffer query = new StringBuffer();
		
		if(inlineQuery != null){
		
			query = new StringBuffer(inlineQuery);
		} else {
			query = new StringBuffer(reportDefinition.getQuery());
		}
		String orderByQuery = reportDefinition.getOrderByQuery();
		String groupByQuery = reportDefinition.getGroupByQuery();
		
		query = addSearchClause(searchParams, reportDefinition, query);
	
	if(groupByQuery != null){
		query.append(" "+ groupByQuery);
	}

	if(orderByQuery != null) {
		
		query.append(" "+ orderByQuery);
		
	}
    LOGGER.info("generate baseQuery :"+query);
    return query.toString();
}

public String generateUnionQuery(List<SearchParam> searchParams, String tenantId, ReportDefinition reportDefinition){
	
	
	
	String baseQuery = reportDefinition.getQuery();
	
	String[] unionQueries = baseQuery.split("UNION");
	
	StringBuffer query = new StringBuffer();
	StringBuffer finalQuery = new StringBuffer();
	StringBuffer finalUnionQuery = new StringBuffer();
	
	for(int i=0; i<unionQueries.length; i++) {
		
		query = new StringBuffer(unionQueries[i]);
	   
		query = addSearchClause(searchParams, reportDefinition, query);
	
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
     
     String alteredOrderByQuery = "";
      if(finalQuery.toString().trim().contains("  UNION  ALL ")){
      String[] unionall = finalQuery.toString().split("  UNION  ALL ");
      for(int j=0; j<unionall.length; j++) {
    	  
    	  
    	  if(j < unionall.length-1) {
    		  finalUnionQuery.append(unionall[j] );
    		  if(orderByQuery != null){
        	  		
    			  finalUnionQuery.append(" "+ orderByQuery);
        	      } 
    		  finalUnionQuery.append(" UNION ALL ");
    		  
    	  } else{
    		  finalUnionQuery.append(unionall[j] );
    		  if(orderByQuery != null){
      	  		
    			  finalUnionQuery.append(" "+ orderByQuery);
        	      } 
    		  
    		  
    	      }
      }
      }else {
      
  	if(orderByQuery != null){
  		finalUnionQuery.append(" "+ finalQuery);
  		finalUnionQuery.append(" "+ orderByQuery);
      }
      }
      
      
      return finalQuery.toString();
    }
	public String generateJoinQuery(List<SearchParam> searchParams, String tenantId, ReportDefinition reportDefinition){
		
		
		
		String baseQuery = reportDefinition.getQuery();
		
		String[] joinQueries = baseQuery.split("FULLJOIN");
		
		StringBuffer query = new StringBuffer();
		StringBuffer finalQuery = new StringBuffer();
		
		for(int i=0; i<joinQueries.length; i++) {
			
			query = new StringBuffer(joinQueries[i]);
			
			query = addSearchClause(searchParams, reportDefinition, query);
			String groupByQuery = reportDefinition.getGroupByQuery(); 
			if(groupByQuery != null){
				if (i==0){
					String[] group = groupByQuery.split("using");
					groupByQuery = group[i];
					 
				}
				groupByQuery = groupByQuery.replaceAll("\\$result", ("result"+i));
				query.append(" "+ groupByQuery);
		    }
			if(i > 0) {
			finalQuery.append(" JOIN "+query.toString()+ " ");
			} else {
				finalQuery.append(query.toString());
			}
		}
		String orderByQuery = reportDefinition.getOrderByQuery(); 
		if(orderByQuery != null){
			finalQuery.append(" "+ orderByQuery);
	    }
		
		finalQuery.toString();
	
	return finalQuery.toString();
	}

	private StringBuffer addSearchClause(List<SearchParam> searchParams, ReportDefinition reportDefinition,
			StringBuffer query) {
		for(SearchParam searchParam : searchParams){
			
			Object name = searchParam.getName();
			
		    for (SearchColumn sc : reportDefinition.getSearchParams()) 
		    {
		            if(name.equals(sc.getName()) && !sc.getIsMandatory()){
		            	if(sc.getSearchClause() != null) {
		            		if(searchParam.getInput() instanceof ArrayList<?>){
		            			LOGGER.info("Coming in to the instance of ArrayList ");
			            		ArrayList<?> list = new ArrayList<>();
			            		list = (ArrayList)(searchParam.getInput());
			            		LOGGER.info("Check the list is empty "+list.size());
			            		if(list.size() > 0) {
			            			
			            			query.append(" " +sc.getSearchClause());
			            			
			            		}
			            		
			            	} else {
		            	query.append(" " +sc.getSearchClause());
			            	}
		            	
		            	}
		            }
		    }
			
		
}
		return query;
	}
	
	
	public String buildInlineQuery(Object json) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		json = mapper.writeValueAsString(json);
		StringBuilder inlineQuery = new StringBuilder();
		if(json.toString().startsWith("[") && json.toString().endsWith("]")){
			LOGGER.info("Building inline query for JSONArray.....");
			JSONArray array = new JSONArray(json.toString());
			try{
				Map<String, Object> map = new HashMap<>();
				map = mapper.readValue(array.getString(0).toString(), new TypeReference<Map<String, String>>(){});
				StringBuilder table = new StringBuilder();
				StringBuilder values = new StringBuilder();
				table.append("table (");
				values.append("(VALUES (");
				for(Map.Entry<String, Object> row: map.entrySet()){
					table.append(row.getKey()+",");
				}
				for(int i = 0; i < array.length(); i++){
					Map<String, Object> jsonMap = new HashMap<>();
					jsonMap = mapper.readValue(array.getString(i).toString(), new TypeReference<Map<String, String>>(){});
					values.append("(");
					for(Map.Entry<String, Object> row: jsonMap.entrySet()){
						values.append("'"+row.getValue()+"'"+",");	
					}
					values.replace(values.length() - 1, values.length(), "),");
				}
				table.replace(table.length() - 1, table.length(), ")");
				LOGGER.info("tables: "+table.toString());
				
				values.replace(values.length() - 1, values.length(), ")");
				
				
				inlineQuery.append(values.toString())
				.append(" AS ")
				.append(table.toString());
			}catch(Exception e){
				LOGGER.error("Exception while building inline query, Valid Data format: [{},{}]. Please verify: ",e);
			}
			
			
			
		}else{
			LOGGER.info("Building inline query for a JSON Object.....");
			try{
				Map<String, Object> map = new HashMap<>();
				map = mapper.readValue(json.toString(), new TypeReference<Map<String, Object>>(){});
				StringBuilder table = new StringBuilder();
				StringBuilder values = new StringBuilder();
				table.append("table (");
				values.append("(VALUES (");
				for(Map.Entry<String, Object> row: map.entrySet()){
					table.append(row.getKey()+",");
					values.append("'"+row.getValue()+"'"+",");	
				}
				table.replace(table.length() - 1, table.length(), ")");
				LOGGER.info("tables: "+table.toString());
				
				values.replace(values.length() - 1, values.length(), "))");
				LOGGER.info("values: "+values.toString());
				
				inlineQuery.append(values.toString())
				.append(" AS ")
				.append(table.toString());
			}catch(Exception e){
				LOGGER.error("Exception while building inline query: ",e);
			}
			
			
		}
		
		return inlineQuery.toString();
	}
	
	public RequestInfo getRInfo()
	{
		// TODO Auto-generated method stub
				RequestInfo ri = new RequestInfo();
				ri.setAction("action");
				ri.setAuthToken("a487e887-cafd-41cf-bb8a-2245acbb6c01");
				/*ri.setTs(new Date());*/
				ri.setApiId("apiId");
				ri.setVer("version");
				ri.setDid("did");
				ri.setKey("key");
				ri.setMsgId("msgId");
				ri.setRequesterId("requestId");
		return ri;
	}
   
	public long getCurrentTime(){
		  Calendar calendar = Calendar.getInstance();
		  calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		  return calendar.getTimeInMillis();
		}

}

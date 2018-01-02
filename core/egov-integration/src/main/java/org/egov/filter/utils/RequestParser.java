package org.egov.filter.utils;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.zuul.context.RequestContext;

@Component
public class RequestParser {
	
	public HashMap<String, Object> hashMap;
	
	public DocumentContext getParsedRequestBody(String jsonBody) {
		DocumentContext documentContext = JsonPath.parse(jsonBody);
		return documentContext;
	}
	
	public boolean hasRequestInfo() {
		if(hashMap.containsKey(FilterConstant.REQUEST_INFO_PASCAL_CASE) || 
				hashMap.containsKey(FilterConstant.REQUEST_INFO_CAMEL_CASE)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setAuthToken(DocumentContext documentContext, RequestContext ctx) {
		if(isPascalCasePresent())
			ctx.set(FilterConstant.REQ_TOKEN_KEY, documentContext.read("$.".concat(FilterConstant.REQUEST_INFO_PASCAL_CASE).
					concat(FilterConstant.DOT).concat(FilterConstant.AUTH_TOKEN_KEY)));
		else  
			ctx.set(FilterConstant.REQ_TOKEN_KEY, documentContext.read("$.".concat(FilterConstant.REQUEST_INFO_CAMEL_CASE).
					concat(FilterConstant.DOT).concat(FilterConstant.AUTH_TOKEN_KEY)));
	}
	
	public boolean isCamelCasePresent() {
		return hashMap.containsKey(FilterConstant.REQUEST_INFO_CAMEL_CASE);
	}
	
	public boolean isPascalCasePresent() {
		return hashMap.containsKey(FilterConstant.REQUEST_INFO_PASCAL_CASE);
	}
	
	public void setReqAsMap(String jsonBody) {
		ObjectMapper mapper = new ObjectMapper();
         try {
        	 hashMap = mapper.readValue(jsonBody,
			    new TypeReference<HashMap<String, Object>>() { });
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

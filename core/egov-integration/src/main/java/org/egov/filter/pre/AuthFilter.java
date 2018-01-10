package org.egov.filter.pre;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.egov.filter.utils.APIAuthTokenService;
import org.egov.filter.utils.FilterConstant;
import org.egov.filter.utils.RequestParser;
import org.egov.filter.utils.RequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthFilter extends ZuulFilter {

	@Autowired
	private RequestParser requestParser;
	
	private static Map<String, String> authKey = new HashMap<String, String>();
	
	public static void setAuthToken(String authToken) {
		authKey.put(FilterConstant.REQ_TOKEN_KEY, authToken);
	}
	
	public static String getAuthToken() {
		return authKey.get(FilterConstant.REQ_TOKEN_KEY);
	}
	
	@Autowired
	private APIAuthTokenService apiAuthTokenService; 

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		System.out.println("Request Method : " + request.getMethod() + " Request URL : " + request.getRequestURL().toString());
		String body = readRequestBody(request);
	
		if(getAuthToken() == null) 
			setAuthToken(apiAuthTokenService.getAuthToken());
		
		/*requestParser.setReqAsMap(body);
		if (requestParser.hasRequestInfo()) {
			System.out.println("has req info");
			requestParser.setAuthToken(requestParser.getParsedRequestBody(body), ctx);
		}*/
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	private String readRequestBody(HttpServletRequest request) {
		RequestWrapper requestWrapper = new RequestWrapper(request);
		String body = requestWrapper.getPayload();
		log.info("body:" + body);
		return body;
	}
	
}

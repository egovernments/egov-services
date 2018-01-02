package org.egov.filter.post;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.egov.filter.utils.MdmsReqResConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MdmsResponseFilter extends ZuulFilter {

	@Autowired
	private MdmsReqResConstructor mdmsReqResConstructor;
	
	@Value("#{'${egov.request_filter.should_not_filter}'.split(',')}")
	private String[] shouldFilter;
	
	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		String reqUri = ctx.getRequest().getRequestURI();
		if(Arrays.asList(shouldFilter).contains(reqUri))
			return true;
		
		return false;
	}

	@Override
	public Object run() {
		
		log.info("Inside MdmsResponseFilter");
		RequestContext ctx = RequestContext.getCurrentContext();
		String responseBody = readResponseBody(ctx);
		log.info("respBody:" + responseBody);
		try {
			mdmsReqResConstructor.constructRequest(ctx, responseBody);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 10;
	}

	private String readResponseBody(RequestContext ctx) {
		String responseBody = null;
		try (final InputStream responseDataStream = ctx.getResponseDataStream()) {
			responseBody = CharStreams.toString(new InputStreamReader(responseDataStream, "UTF-8"));
			ctx.setResponseBody(responseBody);
		} catch (IOException e) {
			log.info("Error reading body", e);
		}
		return responseBody;
	}

}

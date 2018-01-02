package org.egov.filter.pre;

import javax.servlet.http.HttpServletRequest;

import org.egov.filter.utils.RequestParser;
import org.egov.filter.utils.RequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthFilter extends ZuulFilter {

	@Autowired
	private RequestParser requestParser;

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		System.out.println("Request Method : " + request.getMethod() + " Request URL : " + request.getRequestURL().toString());
		String body = readRequestBody(request);
		
		requestParser.setReqAsMap(body);
		if (requestParser.hasRequestInfo()) {
			System.out.println("has req info");
			requestParser.setAuthToken(requestParser.getParsedRequestBody(body), ctx);
		}
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

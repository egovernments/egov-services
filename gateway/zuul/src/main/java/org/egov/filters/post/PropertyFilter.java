package org.egov.filters.post;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.egov.UrlProvider;
import org.egov.model.PostFilterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.common.io.CharStreams;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PropertyFilter extends ZuulFilter {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public Object run() {

		 RequestContext ctx = RequestContext.getCurrentContext();
		 String resBody = readResponseBody(ctx);
		 if(StringUtils.isEmpty(resBody)) return null;
		 
		 DocumentContext resDc = JsonPath.parse(resBody);
		 DocumentContext reqDc = parseRequest(ctx);
		String uri = ctx.getRequest().getRequestURI();
		PostFilterRequest req = PostFilterRequest.builder().Request(reqDc.jsonString()).Response(resDc.jsonString()).build();
		String response = null;
		try {
			response = updateFireCess(req, uri);
		} catch (HttpClientErrorException e) {
			System.err.println(" the error : " + e.getResponseBodyAsString());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ctx.setResponseBody(response);
		return null;
	}

	private String updateFireCess(PostFilterRequest req, String uri) {

		return restTemplate.postForObject(UrlProvider.getUrlMap().get(uri), req,
				String.class);
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		String uri = ctx.getRequest().getRequestURI();
		return UrlProvider.getUrlMap().get(uri) != null;
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public String filterType() {
		return "post";
	}
	
	private String readResponseBody(RequestContext ctx) {
		String responseBody = null;
		try (final InputStream responseDataStream = ctx.getResponseDataStream()) {
			responseBody = CharStreams.toString(new InputStreamReader(responseDataStream, "UTF-8"));
			//ctx.setResponseBody(responseBody);
		} catch (IOException e) {
			log.info("Error reading body", e);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return responseBody;
	}
	
	private DocumentContext parseRequest(RequestContext ctx) {
		
		String payload = null;
		  try {
			  InputStream is = ctx.getRequest().getInputStream();
	            payload = IOUtils.toString(is);
	            //request.getRequestURI();
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }
		return JsonPath.parse(payload);
	}

}

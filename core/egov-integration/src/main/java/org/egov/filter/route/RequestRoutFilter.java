package org.egov.filter.route;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.egov.filter.utils.RequestWrapper;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;
/*
@Slf4j
public class RequestRoutFilter extends ZuulFilter{

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		System.out.println("route filter");
		RequestContext ctx = RequestContext.getCurrentContext();
	    HttpServletRequest request = ctx.getRequest();
	    System.out.println("before:"+ ctx.getRouteHost().getPort());
	    StringBuffer str = ctx.getRequest().getRequestURL();
	    System.out.println("str:"+str);
	    String str1 =ctx.getRequest().getRequestURI();
	    System.out.println("str1:"+str1);
	    ctx.getRequest();
	    
	    System.out.println("before path:");
	    URL url = ctx.getRouteHost();
		try {
			System.out.println("before path:"+url.getPath());
			//new URL("http", "example.com", "/pages/page1.html");
			//url = new URL("http","localhost",8082,"/wf-service/_create");
			
			url = new URL("http://localhost:8082/");
			//RequestWrapper requestWrapper = new RequestWrapper(ctx.getRequest());
			//ctx.setRequest(requestWrapper);
			
			
			 Object originalRequestPath = ctx.get("REQUEST_URI_KEY");
		        String modifiedRequestPath = "/api/microservicePath" + originalRequestPath;
		        ctx.put("REQUEST_URI_KEY", modifiedRequestPath);
			 StringBuffer str2 = ctx.getRequest().getRequestURL();
			    System.out.println("str2:"+str);
			    String str3 =ctx.getRequest().getRequestURI();
			    
			    System.out.println("str3:"+str1);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   // ctx.setRouteHost(url);
	    System.out.println("after path:"+url.getPath());
	    System.out.println("after:"+ ctx.getRouteHost().getPort());
		return null;
	}

	@Override
	public String filterType() {
		return null;
	}

	@Override
	public int filterOrder() {
		return 3;
	}
	
	private String getUri(HttpServletRequest request) {
		RequestWrapper  requestWrapper = new RequestWrapper(request);
		log.info("uri::"+requestWrapper.getRequestURI());	
		return null;
	}
*/
//}

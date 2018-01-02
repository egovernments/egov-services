package org.egov.filter.pre;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.egov.filter.model.Request;
import org.egov.filter.model.Service;
import org.egov.filter.model.ServiceMap;
import org.egov.filter.utils.ReqResConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class RequestFilter extends ZuulFilter {

	@Autowired
	private ServiceMap serviceMap;

	@Autowired
	private ReqResConstructor reqResConstructor;
	   
    @Value("#{'${egov.request_filter.should_not_filter}'.split(',')}")
	private String[] shouldNotFilter;

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		System.out.println("RequestFilter");
		System.out.println(
				"Request Method : " + request.getMethod() + " Request URL : " + request.getRequestURL().toString());

		List<Service> services = serviceMap.getServices();
		Map<String, Service> uriServiceMap = services.stream()
				.collect(Collectors.toMap(Service::getFromEndPont, s -> s));

		try {
			if (uriServiceMap.containsKey(ctx.getRequest().getRequestURI()))
				reqResConstructor.constructRequest(uriServiceMap.get(ctx.getRequest().getRequestURI()), ctx);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		ctx.setRouteHost(null);

		return null;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		String reqUri = ctx.getRequest().getRequestURI();
		if(Arrays.asList(shouldNotFilter).contains(reqUri))
			return false;
		
		return true;
	}

	@Override
	public int filterOrder() {
		return 6;
	}

	@Override
	public String filterType() {
		return "pre";
	}

}

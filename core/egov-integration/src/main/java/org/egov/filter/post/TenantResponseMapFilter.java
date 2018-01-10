package org.egov.filter.post;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.egov.filter.model.ServiceMap;
import org.egov.filter.utils.FilterConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

@Slf4j
public class TenantResponseMapFilter extends ZuulFilter {

	@Autowired
	public ResourceLoader resourceLoader;
	
	@Value("${egov.tenant.filter.uris}")
	private String shouldFilter;
	
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		System.out.println("TenantResponseMapFilter");
		DocumentContext resDc = JsonPath.parse((String)ctx.get(FilterConstant.RESPONSE_BODY));
		
		JSONArray jsonArray = null;
		boolean isTenantMdms = ctx.getBoolean(FilterConstant.TENANT_MODULE_MASTER);
		if(isTenantMdms) {
			jsonArray = resDc.read("$.MdmsRes.tenant.tenants");
		} else {
			jsonArray = resDc.read("$.tenant");
		}
		
		Map<String, Map<String,String>> tenantMap = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			  Resource resource = resourceLoader.getResource("classpath:TenantMap.json"); 
			  File file = resource.getFile(); 
			  tenantMap= mapper.readValue(file, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String,String> tenant = tenantMap.get("tenantMap");
		log.info("jsonArray.size():"+jsonArray.size());
		for(int i=0; i<jsonArray.size(); i++) {
			LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap<String, Object>)jsonArray.get(i);
			String internalTenant = tenant.get(Long.toString(((new Double((double)linkedHashMap.get("code"))).longValue())));
			if(internalTenant != null)
			linkedHashMap.put("code", internalTenant);
			jsonArray.set(i, linkedHashMap);
		}
		
		
		if(isTenantMdms) {
			//resDc.read(".tenants");
			resDc.put("$.MdmsRes.tenant", "tenants", jsonArray);
		} else {
			resDc.put("$", "tenant", jsonArray);
		}
		
		ctx.set(FilterConstant.RESPONSE_BODY, resDc.jsonString());
		return null;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		String reqUri = ctx.getRequest().getRequestURI();
		if(reqUri.equals(shouldFilter))
			return true;
		else if(ctx.getBoolean(FilterConstant.TENANT_MODULE_MASTER))
			return true;
		
		return false;
	}

	@Override
	public int filterOrder() {
		return 15;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "post";
	}
}
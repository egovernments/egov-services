package org.egov.filter.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.ReadConfiguration;
import org.egov.filter.model.FinalResponse;
import org.egov.filter.model.MasterDetail;
import org.egov.filter.model.Parameter;
import org.egov.filter.model.Request;
import org.egov.filter.model.Response;
import org.egov.filter.model.ResponseParam;
import org.egov.filter.model.SourceInEnum;
import org.egov.filter.model.TypeEnum;
import org.egov.filter.pre.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.zuul.context.RequestContext;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

@Component
@Slf4j
public class MdmsReqResConstructor {

	private static final String MDMS_REQ_MODULE_DETAILS_JSONPATH = "$.MdmsCriteria.moduleDetails";
	private static final String MDMS_MODULE_NAME = "moduleName";
	private static final String MDMS_MASTER_DETAILS = "masterDetails";
	private static final String MDMS_MASTER_NAME = "name";
	private static final String MDMS_MASTER_FILTER_NAME = "filter";

	private DocumentContext resBodyDc;

	private DocumentContext reqBodyDc;

	private Map<String, List<String>> queryParamMap;

	private Map<String, Map<String, JSONArray>> mdmsResMap;

	private String authToken;

	@Value("${egov.innowave.host}")
	private String innowaveHost;
	
	@Autowired
	private ResponseFieldDataConverterUtil responseFieldDataConverterUtil;

	public void constructRequest(RequestContext ctx, String resBody) {
		resToDc(resBody);
		resDcToMap();
		parseRequest(ctx);
		// Map<String, Map<String, MasterDetail>> mdmsConfigModuleMap =
		// ReadConfiguration.getMdmsConfigMap();

		Map<String, List<RequestMaster>> integrationModuleMasterMap = filterReqMasterForIntegration();
		findConfigForMaster(integrationModuleMasterMap, ctx);
		
	}

	private void resToDc(String resBody) {
		resBodyDc = new RequestParser().getParsedRequestBody(resBody);
	}

	private void resDcToMap() {
		mdmsResMap = resBodyDc.read("$.MdmsRes");
	}

	@Setter
	@Getter
	@ToString
	class RequestMaster {
		String masterName;
		String filter;
	}

	private Map<String, List<RequestMaster>> filterReqMasterForIntegration() {
		Map<String, Map<String, MasterDetail>> mdmsConfigModuleMap = ReadConfiguration.getMdmsConfigMap();
		Map<String, List<RequestMaster>> integrationModuleMasterMap = new HashMap<>();
		JSONArray moduleArray = reqBodyDc.read(MDMS_REQ_MODULE_DETAILS_JSONPATH);

		for (int i = 0; i < moduleArray.size(); i++) {
			LinkedHashMap<String, Object> moduleMap = (LinkedHashMap<String, Object>) moduleArray.get(i);
			String moduleName = (String) moduleMap.get(MDMS_MODULE_NAME);

			Map<String, MasterDetail> mdmsConfigMasterMap = mdmsConfigModuleMap.get(moduleName);

			if (mdmsConfigMasterMap == null)
				continue;

			JSONArray masterArray = (JSONArray) moduleMap.get(MDMS_MASTER_DETAILS);
			List<RequestMaster> requestMasters = new ArrayList<>();
			for (int j = 0; j < masterArray.size(); j++) {
				LinkedHashMap<String, String> masterObj = (LinkedHashMap<String, String>) masterArray.get(j);
				RequestMaster requestMaster = new MdmsReqResConstructor().new RequestMaster();
				String masterName = (String) masterObj.get(MDMS_MASTER_NAME);
				requestMaster.setMasterName(masterName);
				requestMaster.setFilter((String) masterObj.get(MDMS_MASTER_FILTER_NAME));

				if (mdmsConfigMasterMap.get(masterName) != null) {
					requestMasters.add(requestMaster);
					integrationModuleMasterMap.put(moduleName, requestMasters);
				}
			}

		}
		return integrationModuleMasterMap;
	}

	private void parseRequest(RequestContext ctx) {
		RequestWrapper requestWrapper = new RequestWrapper(ctx.getRequest());
		reqBodyDc = new RequestParser().getParsedRequestBody(requestWrapper.getPayload());
	}

	private void findConfigForMaster(Map<String, List<RequestMaster>> integrationModuleMasterMap, RequestContext ctx) {

		authToken = AuthFilter.getAuthToken();;

		Map<String, Map<String, MasterDetail>> mdmsConfigModuleMap = ReadConfiguration.getMdmsConfigMap();

		for (Map.Entry<String, List<RequestMaster>> reqMdules : integrationModuleMasterMap.entrySet()) {
			String moduleName = reqMdules.getKey();
			Map<String, MasterDetail> mdmsConfigMasterMap = mdmsConfigModuleMap.get(moduleName);
			List<RequestMaster> reqstedMasters = reqMdules.getValue();
			for (RequestMaster requestMaster : reqstedMasters) {
				MasterDetail masterDetail = mdmsConfigMasterMap.get(requestMaster.getMasterName());
				constructRequest(moduleName, masterDetail, ctx);
				String moduleMasterKey = moduleName +"-"+ masterDetail.getMasterName();
				if(moduleMasterKey.equals(FilterConstant.TENANT_MODULE_MASTER)) {
					ctx.set(FilterConstant.TENANT_MODULE_MASTER, true);
				} else {
					ctx.set(FilterConstant.TENANT_MODULE_MASTER, false);
				}
				
			}
		}
	}

	public void constructRequest(String moduleName, MasterDetail masterDetail, RequestContext ctx) {

		List<Request> requests = masterDetail.getRequests();
		for (Request request : requests) {
			String basePath = request.getBasePath();

			if (basePath == null || !basePath.contains("*")) {
				/*
				 * basePath = basePath.replace("*", "length()"); int objLength =
				 * reqBodyDc.read(basePath); for (int i = 0; i < objLength; i++) {
				 */
				String url = constructReqUrl(request, ctx, null);
				String body = constructReqBody(request, null);
				String response = doServiceCall(url, body);
				String jsonResponse = covertResponse(request, masterDetail.getFinalResponse(), response, moduleName, masterDetail);
				ctx.set(FilterConstant.RESPONSE_BODY, jsonResponse);
				System.out.println("final Response:" + masterDetail.getFinalResponse());
				// }

			} else {
				basePath = basePath.replace("*", "length()");
				int objLength = reqBodyDc.read(basePath);
				for (int i = 0; i < objLength; i++) {
					String url = constructReqUrl(request, ctx, String.valueOf(i));
					String body = constructReqBody(request, String.valueOf(i));
					String response = doServiceCall(url, body);
					//parseResponse(request, service.getFinalResponse(), response);
					//System.out.println("final Response:" + service.getFinalResponse());
				}
			}
		}
	}

	private void parseQueryString(RequestContext ctx) {
		queryParamMap = ctx.getRequestQueryParams();
	}

	private String constructReqUrl(Request request, RequestContext ctx, String index) {
		String finalUrl = null;
		
		//http://103.249.96.234:8080/
		String[] urlVar = innowaveHost.split(":");
		try {
			/*URL url = new URL(ctx.getRouteHost().getProtocol(), ctx.getRouteHost().getHost(),
					ctx.getRouteHost().getPort(), request.getUrl());*/
			
			URL url = new URL(ctx.getRouteHost().getProtocol(), urlVar[0],
					Integer.parseInt(urlVar[1]), request.getUrl());
			finalUrl = url.toString();
			System.out.println("url2::" + url.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// String finalUrl =
		String queryStr = constructQueryParam(request.getQueryParams(), ctx, index);
		if (queryStr != null)
			finalUrl = finalUrl.concat(queryStr);

		log.info("constructReqUrl url:" + finalUrl);
		return finalUrl;
	}

	private String constructQueryParam(List<Parameter> queryParams, RequestContext ctx, String index) {

		if (queryParams == null)
			return null;

		String queryStr = "?";

		int i = 1;
		for (Parameter parameter : queryParams) {
			SourceInEnum inEnum = parameter.getIn();
			if (inEnum.toString().equals(SourceInEnum.query.toString())) {
				List<String> values = queryParamMap.get(parameter.getSource());

				if (values != null) {
					String strList = values.stream().collect(Collectors.joining(","));
					queryStr = queryStr.concat(parameter.getName()).concat("=").concat(strList);
				}

			} else if (inEnum.toString().equals(SourceInEnum.body.toString())) {
				String value = null;

				if (parameter.getSource() != null && parameter.getSource().contains("*") && index != null)
					value = reqBodyDc.read(parameter.getSource().replaceFirst("*", index));
				else
					value = reqBodyDc.read(parameter.getSource());

				if (value != null)
					queryStr = queryStr.concat(parameter.getName().concat("=").concat(value));
			}
			if (i++ < queryParams.size())
				queryStr = queryStr.concat("&");

		}
		return queryStr;
	}

	private String constructReqHeader(RequestContext ctx) {
		return null;
	}

	private String constructReqBody(Request request, String index) {
		List<Parameter> bodyParameters = request.getBodyParams();

		String reqBody = request.getBody();

		if (bodyParameters == null)
			return reqBody;

		int i = 0;
		for (Parameter parameter : bodyParameters) {
			String regEx = "{" + i++ + "}";
			log.info("regEx:" + regEx);
			log.info("prepareReqBody parameter:" + parameter);
			reqBody = parseOutgoingReqBody(parameter, reqBody, regEx, index);
		}
		return reqBody;
	}

	private String parseOutgoingReqBody(Parameter parameter, String body, String regex, String index) {

		Object value = null;
		if (parameter.getIn().toString().equals(SourceInEnum.query.toString())) {
			List<String> values = queryParamMap.get(parameter.getSource());
			if (values != null && values.size() == 1)
				value = values.stream().collect(Collectors.joining(","));
			else if (values != null && values.size() > 1)
				value = values.toString();
			else
				value = null;
		} else if (parameter.getIn().toString().equals(SourceInEnum.body.toString())) {

			if (parameter.getSource().contains("*")) {
				String jsonPath = parameter.getSource().replaceFirst("*", index);
				value = reqBodyDc.read(jsonPath);
			} else {
				value = reqBodyDc.read(parameter.getSource());
			}
		}

		if (value != null)
			body = body.replace(regex, value.toString());
		else
			body = body.replace(regex, "null");

		return body;
	}

	public String doServiceCall(String url, String body) {

		log.info("ulr:" + url + "  ,  " + "body:" + body);
		HttpHeaders headers = new HttpHeaders();
		
		headers.set("Authorization", "Bearer " + authToken);
		HttpEntity<String> entity = null;
		if(body != null) {
			headers.setContentType(MediaType.APPLICATION_JSON);
			entity = new HttpEntity<String>(body, headers);
		} else {
			entity = new HttpEntity<String>(headers);
		}
		ResponseEntity<String> res = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			res = restTemplate.postForEntity(url, entity, String.class);
		} catch (HttpClientErrorException ex) {
			ex.printStackTrace();
			String excep = ex.getResponseBodyAsString();
			log.info("HttpClientErrorException:" + excep);
			// throw new ModuleServiceCallException(excep);
		} catch (Exception ex) {
			log.info("Exception:" + ex.getMessage());
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}

		log.info("res:" + res.getBody());
		// System.out.println("documentContext:" + documentContext.jsonString());
		return res.getBody();
	}
	
	private String covertResponse(Request request, FinalResponse finalResponse ,String response, String moduleName, MasterDetail masterDetail) {
		DocumentContext finalResponseDc = JsonPath.parse(finalResponse.getBody());
		DocumentContext responseDc = JsonPath.parse(response);
		
		Response responsePath = request.getResponse();
		String resBasePath = responsePath.getBasePath();
		log.info("parseResponse resBasePath:"+resBasePath);
		
		log.info("parseResponse resBasePath.replaceFirst.length():"+resBasePath.replaceFirst("\\[\\*\\]", ".length()"));
		int objLength = responseDc.read(resBasePath.replaceFirst("\\[\\*\\]", ".length()"));
		List<Object> list = new ArrayList<>();
		
		for (int i = 0; i < objLength; i++) {
			log.info("finalResponseDc i:"+i+finalResponseDc.jsonString());
			for (ResponseParam responseParam : request.getResponse().getResponseParams()) {
				
				log.info("Destination:"+responseParam.getDestination());
			//	log.info("responseParam.getSource().replace(\"*\", String.valueOf(i))):"+responseParam.getSource().replace("*", String.valueOf(i)));
			//	log.info("Source::"+responseDc.read(responseParam.getSource().replace("*", String.valueOf(i))));
				responseFieldDataConverterUtil.setResponse(finalResponseDc, responseParam, finalResponse, responseDc, i);
				log.info("parseResponse finalResObjDc:"+finalResponseDc.jsonString());
				
			}
			//finalResArray.add(finalResObjDc.json());
			list.add(finalResponseDc.json());
			try {
				finalResponseDc = JsonPath.parse(new String(finalResponse.getBody()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("i:"+i+","+"list:"+list);
			
		}
		log.info("final response json array:"+list);
		log.info("final response resBodyDc before:"+resBodyDc.jsonString());
		resBodyDc.put("$.MdmsRes.".concat(moduleName), masterDetail.getMasterName(), list);
		log.info("final response resBodyDc after:"+resBodyDc.jsonString());
		
		return resBodyDc.jsonString();
	}
	
	
}

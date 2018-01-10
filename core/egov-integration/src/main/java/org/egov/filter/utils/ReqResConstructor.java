package org.egov.filter.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.egov.filter.model.FinalResponse;
import org.egov.filter.model.Parameter;
import org.egov.filter.model.Request;
import org.egov.filter.model.Response;
import org.egov.filter.model.ResponseParam;
import org.egov.filter.model.Service;
import org.egov.filter.model.SourceInEnum;
import org.egov.filter.model.TypeEnum;
import org.egov.filter.pre.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
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

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@Component
@Slf4j
public class ReqResConstructor {

	/*
	 * @Autowired private RestTemplate restTemplate;
	 */
	private DocumentContext reqBodyDc;

	private Map<String, List<String>> queryParamMap;

	private String authToken;
	
	@Autowired
	private ResponseFieldDataConverterUtil responseFieldDataConverterUtil;

	public void constructRequest(Service service, RequestContext ctx) {

		authToken = AuthFilter.getAuthToken();

		parseRequest(ctx);
		parseQueryString(ctx);
		log.info(reqBodyDc.jsonString());
		log.info("queryParamMap" + queryParamMap);
		List<Request> requests = service.getRequests();
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
				String jsonResponse = parseResponse(request, service.getFinalResponse(), response);
				ctx.set(FilterConstant.RESPONSE_BODY, jsonResponse);
				System.out.println("final Response:" + service.getFinalResponse());
				// }

			} else {
				basePath = basePath.replace("*", "length()");
				int objLength = reqBodyDc.read(basePath);
				for (int i = 0; i < objLength; i++) {
					String url = constructReqUrl(request, ctx, String.valueOf(i));
					String body = constructReqBody(request, String.valueOf(i));
					String response = doServiceCall(url, body);
					parseResponse(request, service.getFinalResponse(), response);
					System.out.println("final Response:" + service.getFinalResponse());
				}
			}
		}
	}

	private void parseRequest(RequestContext ctx) {
		RequestWrapper requestWrapper = new RequestWrapper(ctx.getRequest());
		reqBodyDc = new RequestParser().getParsedRequestBody(requestWrapper.getPayload());
	}

	private void parseQueryString(RequestContext ctx) {
		queryParamMap = ctx.getRequestQueryParams();
	}

	private String constructReqUrl(Request request, RequestContext ctx, String index) {
		String finalUrl = null;
		try {
			URL url = new URL(ctx.getRouteHost().getProtocol(), ctx.getRouteHost().getHost(),
					ctx.getRouteHost().getPort(), request.getUrl());
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
		if (body != null) {
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

	private String parseResponse(Request request, FinalResponse finalResponse, String response) {

		log.info("parseResponse.." + response);
		DocumentContext finalResDc = JsonPath.parse(finalResponse.getBody());
		JSONArray finalResArray = finalResDc.read(finalResponse.getBasePath());
		// String finalResJson = (String) finalResArray.get(0);
		DocumentContext finalResObjDc = JsonPath.parse((LinkedHashMap<String, Object>) finalResArray.get(0));

		DocumentContext responseDc = JsonPath.parse(response);
		Response responsePath = request.getResponse();
		String resBasePath = responsePath.getBasePath();
		log.info("parseResponse resBasePath:" + resBasePath);
		if (resBasePath.contains("*")) {
			// resBasePath = resBasePath.replace("[*]", "length()");

			log.info("parseResponse resBasePath.replaceFirst.length():"
					+ resBasePath.replaceFirst("\\[\\*\\]", ".length()"));
			int objLength = responseDc.read(resBasePath.replaceFirst("\\[\\*\\]", ".length()"));
			List<Object> list = new ArrayList<>();

			// finalResArray.clear();
			for (int i = 0; i < objLength; i++) {
				for (ResponseParam responseParam : request.getResponse().getResponseParams()) {
					log.info("Destination:" + responseParam.getDestination().replace(finalResponse.getBasePath(), "$"));
					log.info("responseParam.getSource().replace(\"*\", String.valueOf(i))):"
							+ responseParam.getSource().replace("*", String.valueOf(i)));
					log.info("Source::" + responseDc.read(responseParam.getSource().replace("*", String.valueOf(i))));
					responseFieldDataConverterUtil.setResponse(finalResObjDc, responseParam, finalResponse, responseDc, i);

				}
				// finalResArray.add(finalResObjDc.json());
				list.add(finalResObjDc.json());
				try {
					finalResObjDc = JsonPath
							.parse(new String(new ObjectMapper().writeValueAsString(finalResArray.get(0))));
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("i:" + i + "," + "list:" + list);

			}
			// finalResArray.add(list);

			log.info("list:" + list);
			log.info("finalResArray:" + finalResArray);
			log.info("finalResObjDc::" + finalResObjDc.jsonString());
			String key = finalResponse.getBasePath().replace("$.", "");
			key = key.replaceFirst("\\[\\*\\]", "");
			log.info("key::" + key);
			finalResDc.put("$", key, list);

			log.info("finalResDc: " + finalResDc.jsonString());

		}

		/*
		 * for(ResponseParam responseParam : responseParams) {
		 * finalResDc.set(responseParam.getDestination(),
		 * responseDc.read(responseParam.getSource())); }
		 */
		return finalResDc.jsonString();
	}

}

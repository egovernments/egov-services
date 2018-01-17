package org.egov.wf.service;

import java.util.List;

import org.egov.tracer.model.CustomException;
import org.egov.wf.model.Parameter;
import org.egov.wf.model.ReplaceableParameter;
import org.egov.wf.model.ServiceMap;
import org.egov.wf.model.TopicMap;
import org.egov.wf.model.TypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WfService {

	@Autowired
	private ServiceMap serviceMap;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	public void makeWfCall(String fromTopic, String body) {
		System.out.println("WfService fromTopic" + fromTopic);
		DocumentContext documentContext = JsonPath.parse(body);
		log.info("WfService documentContext" + documentContext.jsonString());
		List<TopicMap> topicMaps = serviceMap.getTopicMap();

		for (TopicMap topicMap : topicMaps) {
			if (topicMap.getFromTopic().equals(fromTopic)) {
				jsonPathCreator(topicMap, documentContext);
				sendToQueue(documentContext, topicMap.getToTopic());
			}
		}
	}
	
	public void jsonPathCreator(TopicMap topicMap, DocumentContext documentContext) {

		String basePath = topicMap.getBasePath();
		String jsonBody = null;
		if (topicMap.getRequest().getBody().equals("processInstanceBody"))
			jsonBody = serviceMap.getProcessInstanceBody();
		else if (topicMap.getRequest().getBody().equals("taskBody"))
			jsonBody = serviceMap.getTaskBody();
		else
			jsonBody = topicMap.getRequest().getBody();

		if (basePath.contains("*")) {
			basePath = basePath.replace("*", "length()");
			int objLength = documentContext.read(basePath);
			for (int i = 0; i < objLength; i++) {
				String url = prepareUrlPathParam(documentContext, topicMap, String.valueOf(i));
				String body = prepareReqBody(jsonBody, documentContext, topicMap.getRequest().getBodyParameters(),
						String.valueOf(i));

				log.info("jsonPathCreator url:" + url);
				log.info("jsonPathCreator body:" + body);

				String wfResponse = makeModuleCall(url, body, documentContext);
				setResponse(wfResponse, documentContext, topicMap, String.valueOf(i));
				System.out.println("final Response"+documentContext.jsonString());

			}
		} else {

			String url = prepareUrlPathParam(documentContext, topicMap, null);
			String body = prepareReqBody(jsonBody, documentContext, topicMap.getRequest().getBodyParameters(), null);

			log.info("jsonPathCreator url:" + url);
			log.info("jsonPathCreator body:" + body);

			String wfResponse = makeModuleCall(url, body, documentContext);
			setResponse(wfResponse, documentContext, topicMap, null);
		}
		
		

	}

	public String makeModuleCall(String url, String body, DocumentContext documentContext) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<String>(body, headers);
		ResponseEntity<String> res = null;
		try {
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

		if (res == null) {
			throw new CustomException("workflow.response.null", "Failed while making call to work flow");
		}
		System.out.println("res:" + res.getBody());
		System.out.println("documentContext:" + documentContext.jsonString());

		return res.getBody();
	}

	public String prepareUrlPathParam(DocumentContext documentContext, TopicMap topicMap, String curObj) {

		String url = topicMap.getRequest().getUrl();
		List<Parameter> pathParameter = topicMap.getRequest().getPathParameters();

		if (pathParameter != null) {
			for (Parameter parameter : pathParameter) {
				// Parameter parameter = pathParameter.get(i);
				String defaultValue = parameter.getDefaultValue();
				String name = parameter.getName();
				if (defaultValue == null && parameter.getJsonPath() != null && curObj != null)
					url = url.replace("{" + name + "}",
							documentContext.read(parameter.getJsonPath().replace("*", curObj)));
				else if (defaultValue == null && parameter.getJsonPath() != null && curObj == null)
					url = url.replace("{" + name + "}", documentContext.read(parameter.getJsonPath()));
				else
					url = url.replace("{" + name + "}", defaultValue);

				log.info("pathParameter:" + url);
			}
		}
		return url;
	}

	public String prepareReqBody(String jsonBody, DocumentContext documentContext, List<Parameter> bodyParameters,
			String curObj) {

		for (int i = 0; i < bodyParameters.size(); i++) {
			Parameter parameter = bodyParameters.get(i);

			String regEx = "{" + i + "}";
			System.out.println("regEx:" + regEx);
			log.info("prepareReqBody parameter:" + parameter);
			String jsonPath = parameter.getJsonPath();

			if (curObj != null && jsonPath != null)
				jsonPath = jsonPath.replace("*", curObj);

			TypeEnum type = parameter.getType();
			log.info("prepareReqBody jsonPath:" + jsonPath);
			Object jsonValue = null;
			if (jsonPath != null)
				jsonValue = documentContext.read(jsonPath);

			log.info("prepareReqBody jsonValue:" + jsonValue);
			ObjectMapper objectMapper = new ObjectMapper();
			String defaultValue = parameter.getDefaultValue();
			Object valueStr = null;
			if (parameter.getType() != null && parameter.getType().toString().equals(TypeEnum.JSON.toString())) {
				try {
					valueStr = objectMapper.writeValueAsString(jsonValue);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (defaultValue != null) {
				valueStr = "\"".concat(defaultValue).concat("\"");
			} else {
				valueStr = documentContext.read(jsonPath);
				if (valueStr != null)
					valueStr = "\"".concat(valueStr.toString().concat("\""));
			}

			log.info("valueStr:" + valueStr);

			if (valueStr != null)
				jsonBody = jsonBody.replace(regEx, valueStr.toString());
			else
				jsonBody = jsonBody.replace(regEx, "null");
			log.info("prepareReqBody jsonBody:" + i + jsonBody);
		}
		return jsonBody;
	}

	public void setResponse(String wfResponse, DocumentContext documentContext, TopicMap topicMap, String curObj) {
		DocumentContext wfResponseDC = JsonPath.parse(wfResponse);
		List<ReplaceableParameter> replaceableParameters = null;
		if(topicMap.getResponse() != null)
			replaceableParameters = topicMap.getResponse().getReplaceableParameter();
		
		if(replaceableParameters != null) {
		for (ReplaceableParameter replaceableParameter : replaceableParameters) {
			String replPath = replaceableParameter.getJsonPath();
			String[] replPaths = replPath.split("=");
			String destPath = replPaths[0];
			if (curObj != null)
				destPath = destPath.replace("*", curObj);

			log.info("destPath:" + destPath);
			log.info("documentContext:" + documentContext.jsonString());
			log.info("replPaths[1]:" + replPaths[1]);
			String sourcePathvalue = wfResponseDC.read(replPaths[1]);
			log.info("sourcePathvalue:" + sourcePathvalue);
			documentContext.put(destPath, replaceableParameter.getName(), sourcePathvalue);
		}
	}
	}
	private void sendToQueue(DocumentContext documentContext, String toTopic) {
		kafkaTemplate.send(toTopic, documentContext.json());
	}
}

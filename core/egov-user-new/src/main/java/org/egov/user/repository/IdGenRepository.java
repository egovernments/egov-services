package org.egov.user.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.egov.user.domain.model.IdGenRequestInfo;
import org.egov.user.domain.model.IdRequest;
import org.egov.user.domain.model.IdRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;

@Repository
public class IdGenRepository {

	@Autowired
	public RestTemplate restTemplate;

	@Value("${egov.idgen.hostname}")
	private String IdGenHostName;

	@Value("${egov.idgen.uri}")
	private String IdGenBaseUrl;

	public static final Logger LOGGER = LoggerFactory.getLogger(IdGenRepository.class);

	public String generateUserOtpNumber(RequestInfo requestInfo, String tenantId) {
		LOGGER.info("Generating user otp .");

		StringBuilder builder = new StringBuilder();
		builder.append(IdGenHostName).append(IdGenBaseUrl);

		LOGGER.info("URI being hit: " + builder.toString());

		IdRequestWrapper idRequestWrapper = new IdRequestWrapper();
		IdGenRequestInfo idGenReq = new IdGenRequestInfo();

		// Because idGen Svc uses a slightly different form of requestInfo

		idGenReq.setAction(requestInfo.getAction());
		idGenReq.setApiId(requestInfo.getApiId());
		idGenReq.setAuthToken(requestInfo.getAuthToken());
		idGenReq.setCorrelationId(requestInfo.getCorrelationId());
		idGenReq.setDid(requestInfo.getDid());
		idGenReq.setKey(requestInfo.getKey());
		idGenReq.setMsgId(requestInfo.getMsgId());
		// idGenReq.setRequesterId(requestInfo.getRequesterId());
		if(requestInfo.getTs()!=null){
			idGenReq.setTs(requestInfo.getTs().getTime());
		}
		idGenReq.setUserInfo(requestInfo.getUserInfo());
		idGenReq.setVer(requestInfo.getVer());

		IdRequest idRequest = new IdRequest();
		idRequest.setIdName("userotp");
		idRequest.setTenantId(tenantId);
		idRequest.setFormat("[d{8}]");

		List<IdRequest> idRequests = new ArrayList<>();
		idRequests.add(idRequest);

		idRequestWrapper.setIdGenRequestInfo(idGenReq);
		idRequestWrapper.setIdRequests(idRequests);
		Object response = null;

		LOGGER.info("Request for idgen otp: " + idRequestWrapper.toString());

		try {
			response = restTemplate.postForObject(builder.toString(), idRequestWrapper, Object.class);
		} catch (Exception e) {
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "User Otp Couldn't be Generated");

		}
		LOGGER.info("Response from id gen service: " + response.toString());

		return JsonPath.read(response, "$.idResponses[0].id");
	}
}

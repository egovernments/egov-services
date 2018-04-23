package org.egov.idgen.sevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.idgen.model.IdGenerationRequest;
import org.egov.idgen.model.IdGenerationResponse;
import org.egov.idgen.model.IdRequest;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IdGenService {
	
	private RestTemplate restTemplate;

	@Value("${egov.otp.host:http://localhost:8080/}")
	private String idGenHost;

	@Value("${egov.otp.host:egov-idgen/id/_generate}")
	private String IdgenEndPoint;

	/**
	 * 
	 * get List of Id from IdGen service based on the count and format of the id
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param count
	 */
	public IdGenerationResponse getId(RequestInfo requestInfo, String tenantId, Integer count, String name,
			String format) {

		List<IdRequest> reqList = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			reqList.add(IdRequest.builder().idName(name).format(format).tenantId(tenantId).build());
		}
		IdGenerationRequest req = IdGenerationRequest.builder().idRequests(reqList).requestInfo(requestInfo).build();
		IdGenerationResponse response = null;
		try {
			response = restTemplate.postForObject(idGenHost.concat(IdgenEndPoint), req, IdGenerationResponse.class);
		} catch (HttpClientErrorException e) {
			log.info("HttpClientErrorException:" + e);
			throw new ServiceCallException(e.getResponseBodyAsString());
		} catch (Exception e) {
			log.info("Exception:" + e);
			Map<String, String> map = new HashMap<>();
			map.put(e.getCause().getClass().getName(),e.getMessage());
			throw new CustomException(map);
		}
		return response;
	}

}

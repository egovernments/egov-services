package org.egov.pgr.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.contract.IdGenerationRequest;
import org.egov.pgr.contract.IdGenerationResponse;
import org.egov.pgr.contract.IdRequest;
import org.egov.pgr.utils.PGRConstants;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class IdGenRepo {

	@Value("${egov.idgen.host}")
	private String idGenHost;

	@Value("${egov.idgen.path}")
	private String idGenPath;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 
	 * get List of Id from IdGen service based on the count and format of the id
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param count
	 */
	public IdGenerationResponse getId(RequestInfo requestInfo, String tenantId, Long count) {

		List<IdRequest> reqList = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			reqList.add(IdRequest.builder().idName(PGRConstants.ID_NAME).format(PGRConstants.ID_FORMAT)
					.tenantId(tenantId).build());
		}
		IdGenerationRequest req = IdGenerationRequest.builder().idRequests(reqList).requestInfo(requestInfo).build();
		IdGenerationResponse response = null;
		try {
			response = restTemplate.postForObject(idGenHost + idGenPath, req, IdGenerationResponse.class);
		} catch (HttpClientErrorException e) {
			throw new ServiceCallException(e.getResponseBodyAsString());
		}
		return response;
	}
}

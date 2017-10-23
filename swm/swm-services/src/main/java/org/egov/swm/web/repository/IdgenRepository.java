package org.egov.swm.web.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.web.contract.IdGenerationRequest;
import org.egov.swm.web.contract.IdRequest;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class IdgenRepository {

	private static final Logger logger = LoggerFactory.getLogger(IdgenRepository.class);

	@Value("${egov.services.egov_idgen.hostname}")
	private String host;

	@Value("${egov.services.egov_idgen.createpath}")
	private String createPath;

	@Value("${egov.swm.vehiclefuellingdetails.transaction.num.idgen.name}")
	private String idGenNameForTrnNumPath;

	@Autowired
	private LogAwareRestTemplate restTemplate;

	public String getIdGeneration(String tenantId, RequestInfo requestInfo) {

		StringBuffer idGenerationUrl = new StringBuffer();
		idGenerationUrl.append(host);
		idGenerationUrl.append(createPath);

		List<IdRequest> idRequests = new ArrayList<>();
		IdRequest idrequest = new IdRequest();
		idrequest.setFormat(null);
		idrequest.setIdName(idGenNameForTrnNumPath);
		idrequest.setTenantId(tenantId);
		IdGenerationRequest idGeneration = new IdGenerationRequest();
		idRequests.add(idrequest);
		idGeneration.setIdRequests(idRequests);
		idGeneration.setRequestInfo(requestInfo);
		String response = null;
		try {
			logger.info("UpicNoGeneration calling id generation service url :" + idGenerationUrl.toString()
					+ " request is " + requestInfo);
			response = restTemplate.postForObject(idGenerationUrl.toString(), idGeneration, String.class);
		} catch (Exception ex) {
			throw new CustomException("InValidUrl", idGenerationUrl.toString());
		}
		return response;
	}

}

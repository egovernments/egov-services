package org.egov.workflow.domain.service;

import org.egov.workflow.domain.model.ComplaintTypeResponse;
import org.egov.workflow.domain.model.ComplaintTypeServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ComplaintTypeServiceImpl implements ComplaintTypeService {

	@Value("${egov.services.pgrrest.host}")
	private String complaintTypeServiceHost;

	@Override
	public ComplaintTypeResponse fetchComplaintTypeByCode(String code) {
		String url = complaintTypeServiceHost + "pgr/complaintType?complaintType.code={code}";
		return getComplaintTypeServiceResponse(url, code).getComplaintType().get(0);
	}

	private ComplaintTypeServiceResponse getComplaintTypeServiceResponse(final String url, String code) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, ComplaintTypeServiceResponse.class, code);
	}

}

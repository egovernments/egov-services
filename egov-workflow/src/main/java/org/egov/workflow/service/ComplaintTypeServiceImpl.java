package org.egov.workflow.service;

import org.egov.workflow.model.ComplaintTypeResponse;
import org.egov.workflow.model.ComplaintTypeServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ComplaintTypeServiceImpl implements ComplaintTypeService {

	@Value("${egov.services.complainttype_service.host}")
	private String complaintTypeServiceHost;

	@Override
	public ComplaintTypeResponse fetchComplaintTypeByCode(String code) {
	        //this api belongs to pgr not in workflow
		String url = complaintTypeServiceHost + "workflow/complaintType?complaintType.code={code}";
		return getComplaintTypeServiceResponse(url, code).getComplaintType().get(0);
	}

	private ComplaintTypeServiceResponse getComplaintTypeServiceResponse(final String url, String code) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, ComplaintTypeServiceResponse.class, code);
	}

}

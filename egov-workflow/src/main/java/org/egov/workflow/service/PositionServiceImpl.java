package org.egov.workflow.service;

import java.util.List;

import org.egov.workflow.model.PositionResponse;
import org.egov.workflow.model.PositionServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionServiceImpl implements PositionService {

	@Value("${egov.services.position_service.host}")
	private String positionServiceHost;

	@Override
	public PositionResponse getById(Long id) {
		String url = positionServiceHost + "eis/position?id={id}";
		return getPositionServiceResponseById(url, id).getPosition().get(0);
	}

	@Override
	public List<PositionResponse> getByEmployeeCode(String code) {
		String url = positionServiceHost + "eis/employee/{code}/positions";
		return getPositionServiceResponseByCode(url, code).getPosition();
	}

	private PositionServiceResponse getPositionServiceResponseById(final String url, Long id) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, PositionServiceResponse.class, id);
	}

	private PositionServiceResponse getPositionServiceResponseByCode(final String url, String code) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, PositionServiceResponse.class, code);
	}

}

package org.egov.workflow.persistence.repository;

import java.util.List;

import org.egov.workflow.domain.model.AuthenticatedUser;
import org.egov.workflow.web.contract.Position;
import org.egov.workflow.web.contract.PositionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionRepository {

	private final RestTemplate restTemplate;
	private final String positionsByIdUrl;
	private final String positionsForEmployeeCodeUrl;

	public PositionRepository(final RestTemplate restTemplate,
			@Value("${egov.services.eis.host}") final String eisServiceHostname,
			@Value("${egov.services.eis.position_by_id}") final String positionsByIdUrl,
			@Value("${egov.services.eis.position_by_employee_code}") final String positionsForEmployeeCodeUrl) {
		this.restTemplate = restTemplate;
		this.positionsByIdUrl = eisServiceHostname + positionsByIdUrl;
		this.positionsForEmployeeCodeUrl = eisServiceHostname + positionsForEmployeeCodeUrl;

	}

	public Position getById(final Long id) {
		/*
		 * final PositionRequest positionRequest =
		 * restTemplate.getForObject(positionsByIdUrl, PositionRequest.class,
		 * id);
		 */
		final PositionRequest positionRequest = restTemplate.postForObject(positionsByIdUrl, null,
				PositionRequest.class);
		return positionRequest.getPosition().get(0);
	}

	public List<Position> getByEmployeeCode(final String code) {
		final PositionRequest positionRequest = restTemplate.getForObject(positionsForEmployeeCodeUrl,
				PositionRequest.class, code);
		return positionRequest.getPositions();
	}

}

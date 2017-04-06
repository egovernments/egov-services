package org.egov.workflow.persistence.repository;

import java.util.List;

import org.egov.workflow.web.contract.Position;
import org.egov.workflow.web.contract.PositionResponse;
import org.egov.workflow.web.contract.RequestInfo;
import org.egov.workflow.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionRepository {

	private final RestTemplate restTemplate;
	private final String positionsByIdUrl;
	private final String positionsForEmployeeCodeUrl;

	public PositionRepository(final RestTemplate restTemplate,
			@Value("${egov.services.hr.host}") final String hrServiceHostname,
			@Value("${egov.services.eis.position_by_id}") final String positionsByIdUrl,
			@Value("${egov.services.eis.position_by_employee_code}") final String positionsForEmployeeCodeUrl) {
		this.restTemplate = restTemplate;
		this.positionsByIdUrl = hrServiceHostname + positionsByIdUrl;
		this.positionsForEmployeeCodeUrl = hrServiceHostname + positionsForEmployeeCodeUrl;

	}

	public Position getById(final Long id, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		String tenantId = "";
		if (requestInfo != null) {
			requestInfoWrapper.setRequestInfo(requestInfo);
			tenantId = requestInfo.getUserInfo().getTenantId();
		} else
			requestInfoWrapper.setRequestInfo(new RequestInfo());

		PositionResponse positionResponse = restTemplate.postForObject(positionsByIdUrl, requestInfoWrapper,
				PositionResponse.class, id, tenantId);
		System.out.println(positionResponse);
		Position position = positionResponse != null
				? ((positionResponse.getPosition() != null && !positionResponse.getPosition().isEmpty())
						? positionResponse.getPosition().get(0) : null)
				: null;
			System.out.println(position);			
		return position;
	}

	public List<Position> getByEmployeeCode(final String code, RequestInfo requestInfo) {
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		String tenantId = "";
		if (requestInfo != null) {
			requestInfoWrapper.setRequestInfo(requestInfo);
			tenantId = requestInfo.getTenantId();
		} else
			requestInfoWrapper.setRequestInfo(new RequestInfo());

		PositionResponse positionResponse = restTemplate.postForObject(positionsForEmployeeCodeUrl, requestInfoWrapper,
				PositionResponse.class, code, tenantId);

		return positionResponse.getPosition();
	}

}

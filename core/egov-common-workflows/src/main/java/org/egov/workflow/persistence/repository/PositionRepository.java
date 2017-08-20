package org.egov.workflow.persistence.repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.workflow.web.contract.Position;
import org.egov.workflow.web.contract.PositionResponse;
import org.egov.workflow.web.contract.RequestInfo;
import org.egov.workflow.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionRepository {

	private final RestTemplate restTemplate;
	private final String positionsByIdUrl;
	private final String positionsForEmployeeCodeUrl;
	private final String primaryPositionsForEmployeeIdUrl;

	private static final Logger LOGGER = Logger.getLogger(PositionRepository.class);

	public PositionRepository(final RestTemplate restTemplate,
			@Value("${egov.services.hr-employee.host}") final String hrEmployeeServiceHostname,
			@Value("${egov.services.hr-masters.host}") final String hrServiceHostname,
			@Value("${position_by_id}") final String positionsByIdUrl,
			@Value("${position_by_employee_code}") final String positionsForEmployeeCodeUrl,
			@Value("${position_by_employee}") final String primaryPositionsForEmployeeIdUrl,
			@Value("${position_by_employee_id}") final String positionsForEmployeeIdUrl) {
		this.restTemplate = restTemplate;
		this.positionsByIdUrl = hrServiceHostname + positionsByIdUrl;
		this.positionsForEmployeeCodeUrl = hrEmployeeServiceHostname + positionsForEmployeeIdUrl;
		this.primaryPositionsForEmployeeIdUrl = hrEmployeeServiceHostname + primaryPositionsForEmployeeIdUrl;
		// positionsForEmployeeCodeUrl=hrEmployeeServiceHostname +
		// primaryPositionsForEmployeeIdUrl;

	}

	public Position getById(final Long id,String tenantId, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		if (requestInfo != null) {
			requestInfoWrapper.setRequestInfo(requestInfo);
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

	public Position getPrimaryPositionByEmployeeId(final Long employeeId,final String tenantId , RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		if (requestInfo != null) {
			requestInfoWrapper.setRequestInfo(requestInfo);
		} else
			requestInfoWrapper.setRequestInfo(new RequestInfo());

		PositionResponse positionResponse = restTemplate.postForObject(primaryPositionsForEmployeeIdUrl,
				requestInfoWrapper, PositionResponse.class, employeeId, tenantId,
				new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		System.out.println(positionResponse);
		Position position = positionResponse != null
				? ((positionResponse.getPosition() != null && !positionResponse.getPosition().isEmpty())
						? positionResponse.getPosition().get(0) : null)
				: null;
		System.out.println(position);
		return position;
	}

	public List<Position> getByEmployeeId(final String id, RequestInfo requestInfo) {
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		String tenantId = "";
		if (requestInfo != null) {
			requestInfoWrapper.setRequestInfo(requestInfo);
			tenantId = requestInfo.getUserInfo().getTenantId();
		} else
			requestInfoWrapper.setRequestInfo(new RequestInfo());
		PositionResponse positionResponse = null;
		try {

			positionResponse = restTemplate.postForObject(positionsForEmployeeCodeUrl + "?tenantId={tenantId}",
					requestInfoWrapper, PositionResponse.class, id, tenantId);
		} catch (HttpClientErrorException e) {
			LOGGER.info("Following exception occurred: " + e.getResponseBodyAsString());
			e.printStackTrace();

		} catch (Exception e) {
			LOGGER.error("Exception Occurred While Calling Position service Service : " + e.getMessage());
			throw e;
		}

		return positionResponse.getPosition();
	}

}

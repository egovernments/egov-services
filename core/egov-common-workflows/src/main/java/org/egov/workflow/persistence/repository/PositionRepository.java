package org.egov.workflow.persistence.repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.egov.workflow.web.contract.Position;
import org.egov.workflow.web.contract.PositionResponse;
import org.egov.workflow.web.contract.RequestInfo;
import org.egov.workflow.web.contract.RequestInfoWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionRepository {
	
	public static final Logger logger = LoggerFactory.getLogger(PositionRepository.class);

	private final RestTemplate restTemplate;
	private final String positionsByIdUrl;
	private final String positionsForEmployeeCodeUrl;
	private final String primaryPositionsForEmployeeIdUrl;

	public PositionRepository(final RestTemplate restTemplate,
			@Value("${egov.services.hr-employee.host}") final String hrEmployeeServiceHostname,
			@Value("${egov.services.hr-masters.host}") final String hrServiceHostname,
			@Value("${position_by_id}") final String positionsByIdUrl,
			@Value("${position_by_employee_code}") final String positionsForEmployeeCodeUrl,
			@Value("${position_by_employee}") final String primaryPositionsForEmployeeIdUrl,
			@Value("${position_by_employee_id}") final String positionsForEmployeeIdUrl )
			{
		this.restTemplate = restTemplate;
		this.positionsByIdUrl = hrServiceHostname + positionsByIdUrl;
		this.positionsForEmployeeCodeUrl = hrServiceHostname + positionsForEmployeeIdUrl;
		this.primaryPositionsForEmployeeIdUrl = hrEmployeeServiceHostname + primaryPositionsForEmployeeIdUrl;
		//positionsForEmployeeCodeUrl=hrEmployeeServiceHostname + primaryPositionsForEmployeeIdUrl;

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

	public Position getPrimaryPositionByEmployeeId(final Long employeeId, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		String tenantId = "";
		if (requestInfo != null) {
			requestInfoWrapper.setRequestInfo(requestInfo);
			tenantId = requestInfo.getUserInfo().getTenantId();
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
			tenantId = requestInfo.getTenantId();
		} else
			requestInfoWrapper.setRequestInfo(new RequestInfo());

		String hrEmployeeUrl = positionsForEmployeeCodeUrl.replace("{id}",id);
		logger.info("the url for hr-employee position search call ::: "+hrEmployeeUrl);
		PositionResponse positionResponse = restTemplate.postForObject(hrEmployeeUrl+"?tenantId="+tenantId, requestInfoWrapper,
				PositionResponse.class);

		return positionResponse.getPosition();
	}

	
	
	
}

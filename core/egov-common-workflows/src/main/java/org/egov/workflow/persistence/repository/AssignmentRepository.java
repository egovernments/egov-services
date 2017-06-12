package org.egov.workflow.persistence.repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.workflow.web.contract.Assignment;
import org.egov.workflow.web.contract.AssignmentResponse;
import org.egov.workflow.web.contract.RequestInfo;
import org.egov.workflow.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AssignmentRepository {

	private final RestTemplate restTemplate;
	private final String assignmentsForEmployeeIdUrl;

	private static final Logger LOGGER = Logger.getLogger(PositionRepository.class);

	public AssignmentRepository(final RestTemplate restTemplate,
			@Value("${egov.services.hr-employee.host}") final String hrEmployeeServiceHostname,
			@Value("${assignment_by_employee_id}") final String assignmentsForEmployeeIdUrl) {
		this.restTemplate = restTemplate;
		this.assignmentsForEmployeeIdUrl = hrEmployeeServiceHostname + assignmentsForEmployeeIdUrl;

	}

	public List<Assignment> getByEmployeeId(final String id, RequestInfo requestInfo, Date asOnDate) {
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		String tenantId = "";
		if (requestInfo != null) {
			requestInfoWrapper.setRequestInfo(requestInfo);
			tenantId = requestInfo.getUserInfo().getTenantId();
		} else
			requestInfoWrapper.setRequestInfo(new RequestInfo());
		AssignmentResponse assignmentResponse = null;
		try {

			assignmentResponse = restTemplate.postForObject(assignmentsForEmployeeIdUrl + "&tenantId={tenantId}",
					requestInfoWrapper, AssignmentResponse.class, id,
					new SimpleDateFormat("dd/MM/yyyy").format(asOnDate), tenantId);
		} catch (HttpClientErrorException e) {
			LOGGER.info("Following exception occurred: " + e.getResponseBodyAsString());
			e.printStackTrace();

		} catch (Exception e) {
			LOGGER.error("Exception Occurred While Calling Position service Service : " + e.getMessage());
			throw e;
		}

		return assignmentResponse.getAssignment();
	}

}

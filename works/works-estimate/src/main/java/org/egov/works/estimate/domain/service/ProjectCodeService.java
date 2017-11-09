package org.egov.works.estimate.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.exception.ErrorCode;
import org.egov.works.commons.exception.InvalidDataException;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.domain.repository.ProjectCodeRepository;
import org.egov.works.estimate.persistence.repository.IdGenerationRepository;
import org.egov.works.estimate.web.contract.AuditDetails;
import org.egov.works.estimate.web.contract.ProjectCode;
import org.egov.works.estimate.web.contract.ProjectCodeRequest;
import org.egov.works.estimate.web.contract.ProjectCodeSearchContract;
import org.egov.works.estimate.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProjectCodeService {

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private ProjectCodeRepository projectCodeRepository;

	@Autowired
	private IdGenerationRepository idGenerationRepository;

	public List<ProjectCode> create(ProjectCodeRequest projectCodeRequest) {

		if (projectCodeRequest.getProjectCodes().get(0).getCode() != null
				&& projectCodeRequest.getProjectCodes().get(0).getCode() != "")
			validateProjectCode(projectCodeRequest);

		RequestInfo requestInfo = projectCodeRequest.getRequestInfo();
		AuditDetails auditDetails = new AuditDetails();
		String workIdentificationNumber;
		for (ProjectCode projectCode : projectCodeRequest.getProjectCodes()) {
			projectCode.setId(UUID.randomUUID().toString().replace("-", ""));
			auditDetails.setCreatedBy(requestInfo.getUserInfo().getUserName());
			auditDetails.setCreatedTime(new Date().getTime());

			if (projectCode.getCode() == null || projectCode.getCode().isEmpty()) {
				workIdentificationNumber = idGenerationRepository.generateWorkIdentificationNumber(
						projectCode.getTenantId(), projectCodeRequest.getRequestInfo());
				projectCode.setCode(workIdentificationNumber);
			}

			projectCode.setAuditDetails(auditDetails);

		}

		kafkaTemplate.send(propertiesManager.getWorksProjectCodeCreateTopic(), projectCodeRequest);
		// TODO: To be enabled when account detail consumer is available
		// kafkaTemplate.send(propertiesManager.getCreateAccountDetailKeyTopic(),
		// projectCodeRequest);
		return projectCodeRequest.getProjectCodes();
	}

	private void validateProjectCode(ProjectCodeRequest projectCodeRequest) {
		ProjectCode projectCode = projectCodeRequest.getProjectCodes().get(0);
		ProjectCodeSearchContract projectCodeSearchContract = new ProjectCodeSearchContract();
		List<String> workIdentificationNumbers = new ArrayList<>();
		workIdentificationNumbers.add(projectCode.getCode());
		projectCodeSearchContract.setWorkIdentificationNumbers(workIdentificationNumbers);

		List<ProjectCode> projectCodes = search(projectCodeSearchContract);
		if (!projectCodes.isEmpty()) {
			throw new InvalidDataException("WorkIdentificationNumber", ErrorCode.NON_UNIQUE_VALUE.getCode(),
					projectCode.getCode());
		}
	}

	public List<ProjectCode> search(ProjectCodeSearchContract projectCodeSearchContract) {
		return projectCodeRepository.search(projectCodeSearchContract);
	}

	public List<ProjectCode> update(ProjectCodeRequest projectCodeRequest) {

		RequestInfo requestInfo = projectCodeRequest.getRequestInfo();
		AuditDetails auditDetails = new AuditDetails();

		for (ProjectCode projectCode : projectCodeRequest.getProjectCodes()) {
			auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getUserName());
			auditDetails.setLastModifiedTime(new Date().getTime());
			projectCode.setAuditDetails(auditDetails);
		}
		kafkaTemplate.send(propertiesManager.getWorksProjectCodeUpdateTopic(), projectCodeRequest);
		return projectCodeRequest.getProjectCodes();
	}

}

package org.egov.works.estimate.domain.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.domain.model.AuditDetails;
import org.egov.works.commons.web.contract.RequestInfo;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.domain.repository.ProjectCodeRepository;
import org.egov.works.estimate.persistence.repository.IdGenerationRepository;
import org.egov.works.estimate.web.contract.ProjectCodeRequest;
import org.egov.works.estimate.web.contract.ProjectCodeSearchContract;
import org.egov.works.estimate.web.model.ProjectCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly= true)
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

//		ProjectCode projectCode = projectCodeRequest.getProjectCodes().get(0);
//		ProjectCodeSearchContract projectCodeSearchContract = new ProjectCodeSearchContract();
//		List<String> workIdentificationNumbers = new ArrayList<>() ;
//		workIdentificationNumbers.add(projectCode.getCode());
//		projectCodeSearchContract.setWorkIdentificationNumbers(workIdentificationNumbers);
//
//		List<ProjectCode> projectCodes = search(projectCodeSearchContract);
//		if(!projectCodes.isEmpty()) {
//			throw new InvalidDataException("code", ErrorCode.NON_UNIQUE_VALUE.getCode(),
//					projectCode.getCode());
//		}

		RequestInfo requestInfo = projectCodeRequest.getRequestInfo();
		AuditDetails auditDetails = new AuditDetails();
		String workIdentificationNumber;
		for(ProjectCode projectCode:projectCodeRequest.getProjectCodes()) {
			projectCode.setId(UUID.randomUUID().toString().replace("-", ""));
			auditDetails.setCreatedBy(requestInfo.getUserInfo().getUsername());
			auditDetails.setCreatedTime(new Date().getTime());
			
			if(projectCode.getCode() == null || projectCode.getCode().isEmpty()) {
				workIdentificationNumber = idGenerationRepository
						.generateWorkIdentificationNumber(projectCode.getTenantId(), projectCodeRequest.getRequestInfo());
				projectCode.setCode(workIdentificationNumber);
			}
				
			
			projectCode.setAuditDetails(auditDetails);

		}
		
		kafkaTemplate.send(propertiesManager.getWorksProjectCodeCreateTopic(), projectCodeRequest);
		return projectCodeRequest.getProjectCodes();
	}

	public List<ProjectCode> search(ProjectCodeSearchContract projectCodeSearchContract) {
		return projectCodeRepository.search(projectCodeSearchContract);
	}
	
	public List<ProjectCode> update(ProjectCodeRequest projectCodeRequest) {

//		ProjectCode projectCode = projectCodeRequest.getProjectCodes().get(0);
//		ProjectCodeSearchContract projectCodeSearchContract = new ProjectCodeSearchContract();
//		List<String> workIdentificationNumbers = new ArrayList<>() ;
//		workIdentificationNumbers.add(projectCode.getCode());
//		projectCodeSearchContract.setWorkIdentificationNumbers(workIdentificationNumbers);
//
//		List<ProjectCode> projectCodes = search(projectCodeSearchContract);
//		if(projectCodes.isEmpty()) {
//			throw new InvalidDataException("code", ErrorCode.INVALID_REF_VALUE.getCode(),
//					projectCode.getCode());
//		}

		RequestInfo requestInfo = projectCodeRequest.getRequestInfo();
		AuditDetails auditDetails = new AuditDetails();

		for(ProjectCode projectCode:projectCodeRequest.getProjectCodes()) {
			auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getUsername());
			auditDetails.setLastModifiedTime(new Date().getTime());
			projectCode.setAuditDetails(auditDetails);
		}
		kafkaTemplate.send(propertiesManager.getWorksProjectCodeUpdateTopic(), projectCodeRequest);
		return projectCodeRequest.getProjectCodes();
	}

}

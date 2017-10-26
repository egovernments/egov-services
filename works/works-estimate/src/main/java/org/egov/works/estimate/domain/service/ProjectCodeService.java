package org.egov.works.estimate.domain.service;

import java.util.List;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.domain.repository.ProjectCodeRepository;
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
	
	public List<ProjectCode> create(ProjectCodeRequest projectCodeRequest) {

		kafkaTemplate.send(propertiesManager.getWorksProjectCodeCreateTopic(), projectCodeRequest);
		return projectCodeRequest.getProjectCodes();
	}

	public List<ProjectCode> search(ProjectCodeSearchContract projectCodeSearchContract) {
		return projectCodeRepository.search(projectCodeSearchContract);
	}

}

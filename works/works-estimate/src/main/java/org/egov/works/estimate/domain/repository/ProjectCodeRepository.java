package org.egov.works.estimate.domain.repository;

import java.util.List;

import org.egov.works.estimate.persistence.repository.ProjectCodeJdbcRepository;
import org.egov.works.estimate.web.contract.ProjectCode;
import org.egov.works.estimate.web.contract.ProjectCodeSearchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectCodeRepository {

	@Autowired
	private ProjectCodeJdbcRepository projectCodeJdbcRepository;
	
	public List<ProjectCode> search(ProjectCodeSearchContract projectCodeSearchContract) {
		return projectCodeJdbcRepository.search(projectCodeSearchContract);
	}
	
}

package org.egov.works.estimate.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.works.commons.web.contract.RequestInfo;
import org.egov.works.estimate.domain.exception.CustomBindException;
import org.egov.works.estimate.domain.service.ProjectCodeService;
import org.egov.works.estimate.web.contract.ProjectCode;
import org.egov.works.estimate.web.contract.ProjectCodeRequest;
import org.egov.works.estimate.web.contract.ProjectCodeResponse;
import org.egov.works.estimate.web.contract.ProjectCodeSearchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projectcodes")
public class ProjectCodeController {

	@Autowired
	private ProjectCodeService projectCodeService;

	@PostMapping("/_create")
	public ProjectCodeResponse create(@Valid @RequestBody ProjectCodeRequest projectCodeRequest, BindingResult errors,
			@RequestParam String tenantId) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		List<ProjectCode> projectCodes = projectCodeService.create(projectCodeRequest);

		ProjectCodeResponse projectCodeResponse = new ProjectCodeResponse();
		projectCodeResponse.setProjectCodes(projectCodes);
		return projectCodeResponse;
	}
	
	@PostMapping("/_update")
	public ProjectCodeResponse update(@Valid @RequestBody ProjectCodeRequest projectCodeRequest, BindingResult errors,
			@RequestParam String tenantId) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		List<ProjectCode> projectCodes = projectCodeService.update(projectCodeRequest);

		ProjectCodeResponse projectCodeResponse = new ProjectCodeResponse();
		projectCodeResponse.setProjectCodes(projectCodes);
		return projectCodeResponse;
	}
	

	@PostMapping("/_search")
	public ProjectCodeResponse search(@ModelAttribute ProjectCodeSearchContract projectCodeSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors, @RequestParam String tenantId) {
		List<ProjectCode> projectCodes = projectCodeService.search(projectCodeSearchContract);
		ProjectCodeResponse projectCodeResponse = new ProjectCodeResponse();
		projectCodeResponse.setProjectCodes(projectCodes);
		return projectCodeResponse;
	}

}

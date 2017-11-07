package org.egov.works.estimate.web.controller;

import java.util.List;

import org.egov.works.estimate.domain.service.ProjectCodeService;
import org.egov.works.estimate.web.contract.ProjectCode;
import org.egov.works.estimate.web.contract.ProjectCodeRequest;
import org.egov.works.estimate.web.contract.ProjectCodeResponse;
import org.egov.works.estimate.web.contract.ProjectCodeSearchContract;
import org.egov.works.estimate.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projectcodes")
public class ProjectCodeController {

	@Autowired
	private ProjectCodeService projectCodeService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.OK)
	public ProjectCodeResponse create(@RequestBody ProjectCodeRequest projectCodeRequest, BindingResult errors) {
		List<ProjectCode> projectCodes = projectCodeService.create(projectCodeRequest);

		ProjectCodeResponse projectCodeResponse = new ProjectCodeResponse();
		projectCodeResponse.setProjectCodes(projectCodes);
		return projectCodeResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.OK)
	public ProjectCodeResponse update(@RequestBody ProjectCodeRequest projectCodeRequest, BindingResult errors) {
		List<ProjectCode> projectCodes = projectCodeService.update(projectCodeRequest);

		ProjectCodeResponse projectCodeResponse = new ProjectCodeResponse();
		projectCodeResponse.setProjectCodes(projectCodes);
		return projectCodeResponse;
	}

	@PostMapping("/_search")
	@ResponseStatus(HttpStatus.OK)
	public ProjectCodeResponse search(@ModelAttribute ProjectCodeSearchContract projectCodeSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors,
			@RequestParam(required = true) String tenantId) {
		List<ProjectCode> projectCodes = projectCodeService.search(projectCodeSearchContract);
		ProjectCodeResponse projectCodeResponse = new ProjectCodeResponse();
		projectCodeResponse.setProjectCodes(projectCodes);
		return projectCodeResponse;
	}

}

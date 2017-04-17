package org.egov.access.web.controller;

import org.egov.access.domain.model.Action;
import org.egov.access.domain.service.ActionService;
import org.egov.access.web.contract.ActionContract;
import org.egov.access.web.contract.ActionRequest;
import org.egov.access.web.contract.ActionResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/v1/actions")
public class ActionController {

	private ActionService actionService;

	public ActionController(ActionService actionService) {
		this.actionService = actionService;
	}

	@PostMapping(value = "_search")
	public ActionResponse getActionsBasedOnRoles(@RequestBody final ActionRequest actionRequest) {
		List<Action> actionsList = actionService.getActions(actionRequest.toDomain());
		return getSuccessResponse(actionsList);
	}

	private ActionResponse getSuccessResponse(final List<Action> actionList) {
		final ResponseInfo responseInfo = ResponseInfo.builder()
				.status(HttpStatus.OK.toString())
				.build();
		List<ActionContract> actionContracts = new ActionContract().getActions(actionList);
		return new ActionResponse(responseInfo, actionContracts);
	}
}

package org.egov.access.web.controller;

import org.egov.access.domain.model.Action;
import org.egov.access.domain.model.ActionValidation;
import org.egov.access.domain.service.ActionService;
import org.egov.access.web.contract.action.ActionContract;
import org.egov.access.web.contract.action.ActionRequest;
import org.egov.access.web.contract.action.ActionResponse;
import org.egov.access.web.contract.validateaction.ActionValidationContract;
import org.egov.access.web.contract.validateaction.ValidateActionRequest;
import org.egov.access.web.contract.validateaction.ValidateActionResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/v1/actions")
public class ActionController {

    @Autowired
    private ActionService actionService;

    @PostMapping(value = "_search")
    public ActionResponse getActionsBasedOnRoles(@RequestBody final ActionRequest actionRequest) {
        List<Action> actionsList = actionService.getActions(actionRequest.toDomain());
        return getSuccessResponse(actionsList);
    }

    @PostMapping(value = "_validate")
    public ValidateActionResponse validateAction(@RequestBody ValidateActionRequest validateActionRequest) {
        ActionValidation actionValidation = actionService.validate(validateActionRequest.toDomain());
        return getValidateActionResponse(actionValidation);
    }

    private ActionResponse getSuccessResponse(final List<Action> actionList) {
        final ResponseInfo responseInfo = ResponseInfo.builder()
                .status(HttpStatus.OK.toString())
                .build();
        List<ActionContract> actionContracts = new ActionContract().getActions(actionList);
        return new ActionResponse(responseInfo, actionContracts);
    }

    private ValidateActionResponse getValidateActionResponse(ActionValidation actionValidation) {
        final ResponseInfo responseInfo = ResponseInfo.builder()
                .status(HttpStatus.OK.toString())
                .build();
        return new ValidateActionResponse(responseInfo,
                ActionValidationContract.builder().allowed(actionValidation.isAllowed() ? "TRUE" : "FALSE").build());
    }
}

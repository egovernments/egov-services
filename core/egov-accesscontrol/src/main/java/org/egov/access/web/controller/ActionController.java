package org.egov.access.web.controller;

import org.egov.access.domain.model.Action;
import org.egov.access.domain.service.ActionService;
import org.egov.access.web.contract.ActionContract;
import org.egov.access.web.contract.ActionRequest;
import org.egov.access.web.contract.ActionResponse;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;


@RestController
@RequestMapping("/v1/actions")
public class ActionController {

    @Autowired
    private ActionService actionService;

    @PostMapping(value="_search")
    public ActionResponse getActionsBasedOnRoles(@RequestBody final ActionRequest actionRequest) throws ParseException {

        RequestInfo requestInfo = actionRequest.getRequestInfo();
        List<Action> actionsList = actionService.getActions(actionRequest.toDomain());
        return getSuccessResponse(actionsList, requestInfo);
    }

    private ActionResponse getSuccessResponse(final List<Action> actionList, final RequestInfo requestInfo) {
        final ResponseInfo responseInfo = ResponseInfo.builder().apiId(requestInfo.getApiId())
                .ver(requestInfo.getVer()).msgId(requestInfo.getMsgId()).status(HttpStatus.OK.toString()).build();
        List<ActionContract> actionContracts =  new ActionContract().getActions(actionList);
        return new ActionResponse(responseInfo,actionContracts);
    }
}

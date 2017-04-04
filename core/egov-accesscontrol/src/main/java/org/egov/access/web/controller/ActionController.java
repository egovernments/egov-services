package org.egov.access.web.controller;

import org.egov.access.domain.model.Action;
import org.egov.access.domain.service.ActionService;
import org.egov.access.web.contact.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<?> getActionsBasedOnRoles(@RequestBody final ActionRequest actionRequest, final BindingResult bindingResult) throws ParseException {

        RequestInfo requestInfo = actionRequest.getRequestInfo();
        List<Action> actionsList = actionService.getActions(actionRequest.toDomain());
        return getSuccessResponse(actionsList, requestInfo);
    }

    private ResponseEntity<?> getSuccessResponse(final List<Action> actionList, final RequestInfo requestInfo) {
        final ResponseInfo responseInfo = ResponseInfo.builder().apiId(requestInfo.getApiId())
                .ver(requestInfo.getVer()).msgId(requestInfo.getMsgId()).status(HttpStatus.OK.toString()).build();
        List<ActionContract> actionContracts =  new ActionContract().getActions(actionList);
        return new ResponseEntity<>(new ActionResponse(responseInfo,actionContracts), HttpStatus.OK);
    }
}

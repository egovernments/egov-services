package org.egov.workflow.controller;

import org.egov.workflow.contract.ProcessInstance;
import org.egov.workflow.model.AssigneeFilterInfo;
import org.egov.workflow.model.AssigneeRequestInfo;
import org.egov.workflow.model.PositionResponse;
import org.egov.workflow.model.ResponseInfo;
import org.egov.workflow.producer.WorkflowProducer;
import org.egov.workflow.producer.contract.WorkflowRequest;
import org.egov.workflow.service.WorkflowInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class WorkFlowController {

    @Autowired
    private WorkflowInterface pgrWorkflowImpl;

    @Autowired
    private WorkflowProducer workflowProducer;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new ProcessInstanceValidator());
    }

    @PostMapping(value = "/assignee", produces = MediaType.APPLICATION_JSON_VALUE)
    public PositionResponse getComplaintAssignee(@RequestBody final AssigneeRequestInfo request) {
        final AssigneeFilterInfo assigneeFilterInfo = request.getAssigneeFilterInfo();

        final PositionResponse positionResponse = (PositionResponse) pgrWorkflowImpl.getAssignee(assigneeFilterInfo.getBoundaryId(),
                assigneeFilterInfo.getComplaintTypeCode(), assigneeFilterInfo.getCurrentAssigneeId());
        if (positionResponse != null)
            positionResponse.setResponseInfo(
                    new ResponseInfo("", "", new Date().toString(), "", "", "Successful response", ""));
        pgrWorkflowImpl.getAssignee(null, null, null);
        return positionResponse;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public String startWorkflow(@Valid @RequestBody final ProcessInstance processInstance) {
        WorkflowRequest workflowRequest = new WorkflowRequest().fromProcessInstance(processInstance);
        workflowProducer.sendMessage("egov-workflow-create", workflowRequest);
        return "yay!";
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handle(MethodArgumentNotValidException exception) {
        ValidationErrorDTO dto = new ValidationErrorDTO();
        dto.addFieldErrors(exception.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList()));

        return new ResponseEntity(dto, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity processFieldErrors(List<FieldError> fieldErrors) {
        ValidationErrorDTO dto = new ValidationErrorDTO();

        for (FieldError fieldError : fieldErrors) {
            //TODO - Consider resolving localized version of the message
            String localizedErrorMessage = fieldError.getDefaultMessage();
            dto.addFieldError(fieldError.getField(), localizedErrorMessage);
        }

        return new ResponseEntity(dto, HttpStatus.BAD_REQUEST);
    }


}

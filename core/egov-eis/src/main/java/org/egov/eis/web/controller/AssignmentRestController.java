package org.egov.eis.web.controller;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.domain.service.AssignmentService;
import org.egov.eis.web.contract.Assignment;
import org.egov.eis.web.contract.AssignmentRes;
import org.egov.eis.web.contract.EmployeeRes;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssignmentRestController {

    private AssignmentService assignmentService;

    @Autowired
    public AssignmentRestController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @RequestMapping(value = "/employee/{code}/assignments", method = RequestMethod.GET)
    @ResponseBody
    public EmployeeRes getAssignments(@PathVariable("code") String code,
            @RequestParam(value = "tenantId", required = true) String tenantId,
            @RequestParam(value = "assignmentId", required = false) Long assignmentId,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "sort", required = false, defaultValue = "[-fromDate, -primary]") List<String> sort,
            @RequestParam(value = "asOnDate", required = false) LocalDate asOnDate,
            @RequestParam(value = "isPrimary", required = false) Boolean isPrimary) throws Exception {

        EmployeeRes response = new EmployeeRes();
        response.setResponseInfo(new ResponseInfo("", "", new Date().toString(), "", "", "Successful response"));
        if (tenantId != null && !tenantId.isEmpty() && code != null && !code.isEmpty() && asOnDate != null) {
            response.getAssignments().addAll(assignmentService.getAllActiveEmployeeAssignmentsByEmpCode(code,
                    asOnDate.toDateTimeAtStartOfDay().toDate(), tenantId));
        } else {
            throw new Exception();
        }

        return response;
    }

    @GetMapping(value = "/assignments")
    public AssignmentRes getAssignments(@RequestParam(value = "tenantId", required = true) String tenantId,
            @RequestParam(value = "id", required = false) Long id) {
        AssignmentRes response = new AssignmentRes();
        if (tenantId != null && !tenantId.isEmpty()) {
            if (id != null) {
                org.egov.eis.persistence.entity.Assignment entityAssignment = assignmentService.getAssignmentById(id,
                        tenantId);
                if (entityAssignment != null) {
                    Assignment assignment = new Assignment(entityAssignment);
                    response.setAssignment(Collections.singletonList(assignment));
                }
            } else {
                List<org.egov.eis.persistence.entity.Assignment> entityAssignments = assignmentService.getAll(tenantId);
                if (!entityAssignments.isEmpty()) {
                    response.setAssignment(
                            entityAssignments.stream().map(Assignment::new).collect(Collectors.toList()));
                }
            }
        }
        return response;
    }

    @PostMapping(value = "/assignmentsByDeptOrDesignId")
    @ResponseBody
    public ResponseEntity<?> getAssignmentsByDeptOrDesgnId(
            @RequestParam(value = "tenantId", required = true) String tenantId,
            @RequestParam(value = "deptId", required = false) Long deptId,
            @RequestParam(value = "desgnId", required = false) Long desgnId) {
        AssignmentRes response = new AssignmentRes();
        if (tenantId != null && !tenantId.isEmpty() && (deptId != null || desgnId != null)) {
            List<org.egov.eis.persistence.entity.Assignment> entityAssignments = assignmentService
                    .getPositionsByDepartmentAndDesignationForGivenRange(deptId, desgnId, new Date(), tenantId);
            if (!entityAssignments.isEmpty()) {
                response.setAssignment(entityAssignments.stream().map(Assignment::new).collect(Collectors.toList()));
                return new ResponseEntity<AssignmentRes>(response, HttpStatus.OK);
            }
        }
        return new ResponseEntity<AssignmentRes>(response, HttpStatus.BAD_REQUEST);
    }

    // TODO SHOULD BE CHANGED TO URL FROM HRM MODULE
    @PostMapping(value = "/_assignmentByEmployeeId")
    @ResponseBody
    public ResponseEntity<AssignmentRes> getPrimaryAssignmentByEmployeeId(
            @RequestParam(value = "tenantId", required = true) String tenantId,
            @RequestParam(value = "employeeId", required = true) Long employeeId) {
        AssignmentRes response = AssignmentRes.builder().build();
        if (tenantId != null && !tenantId.isEmpty() && Objects.nonNull(employeeId)) {
            org.egov.eis.persistence.entity.Assignment assignment = assignmentService
                    .getPrimaryAssignmentForEmployee(employeeId, tenantId);
            if (Objects.nonNull(assignment))
                response.setAssignment(Collections.singletonList(new Assignment(assignment)));
            response.setResponseInfo(new ResponseInfo());
            return new ResponseEntity<AssignmentRes>(response, HttpStatus.OK);
        }
        return new ResponseEntity<AssignmentRes>(response, HttpStatus.BAD_REQUEST);
    }

}
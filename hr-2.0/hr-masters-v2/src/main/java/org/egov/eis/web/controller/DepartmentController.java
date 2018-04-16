package org.egov.eis.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.model.Department;
import org.egov.eis.service.DepartmentService;
import org.egov.eis.web.contract.DepartmentGetRequest;
import org.egov.eis.web.contract.DepartmentResponse;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandlers.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid DepartmentGetRequest departmentRequest,
                                    BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
                                    BindingResult requestBodyBindingResult) {
        RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        // validate input params
        if (modelAttributeBindingResult.hasErrors()) {
            return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);
        }

        // validate input params
        if (requestBodyBindingResult.hasErrors()) {
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);
        }

        // Call service
        List<Department> departmentList = null;
        try {
            departmentRequest.setTenantId(departmentRequest.getDestinationTenant());
            departmentList = departmentService.getDepartmentList(departmentRequest.getTenantId(), requestInfoWrapper);

        } catch (Exception exception) {
            logger.error("Error while processing request " + departmentRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(departmentList, requestInfo);
    }


    /**
     * Populate Response object and returndesignationsList
     *
     * @param departmentsList
     * @return
     */
    private ResponseEntity<?> getSuccessResponse(List<Department> departmentsList, RequestInfo requestInfo) {
        DepartmentResponse departmentRes = new DepartmentResponse();
        departmentRes.setDepartment(departmentsList);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        departmentRes.setResponseInfo(responseInfo);
        return new ResponseEntity<DepartmentResponse>(departmentRes, HttpStatus.OK);

    }

}

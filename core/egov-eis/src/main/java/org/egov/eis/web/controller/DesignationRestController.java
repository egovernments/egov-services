package org.egov.eis.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.domain.service.DesignationService;
import org.egov.eis.web.contract.Designation;
import org.egov.eis.web.contract.DesignationRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DesignationRestController {

    @Autowired
    private DesignationService designationService;

    @RequestMapping(value = "/designations", method = RequestMethod.GET)
    @ResponseBody
    public DesignationRes getDesignations(@RequestParam(value = "tenantId", required = true) String tenantId,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "sort", required = false, defaultValue = "[-name]") List<String> sort)
            throws Exception {

        DesignationRes response = new DesignationRes();
        if (tenantId != null && !tenantId.isEmpty()) {
            response.setResponseInfo(new ResponseInfo());
            if (name != null && !name.isEmpty()) {
                response.setDesignation(
                        mapToContractDesignationList(designationService.getDesignationsByName(name, tenantId)));
            } else if (name == null || name.isEmpty()) {
                response.setDesignation(mapToContractDesignationList(designationService.getAllDesignations(tenantId)));
            } else {
                throw new Exception();
            }
        }
        return response;
    }

    @RequestMapping(value = "/designation", method = RequestMethod.GET)
    @ResponseBody
    public DesignationRes getDesignation(@RequestParam(value = "tenantId", required = true) String tenantId,
            @RequestParam(value = "id") String id) throws Exception {

        DesignationRes response = new DesignationRes();
        if (tenantId != null && !tenantId.isEmpty()) {
            List<Designation> designationList = new ArrayList<>();
            response.setResponseInfo(new ResponseInfo("", "", new Date().toString(), "", "", "Successful response"));
            if (id != null && !id.isEmpty()) {
                Designation designation = mapToContractDesignation(
                        designationService.getDesignationById(Long.valueOf(id),tenantId));
                designationList.add(designation);
                response.setDesignation(designationList);
            } else {
                throw new Exception();
            }
        }
        return response;
    }

    @PostMapping(value = "/designationByDepartmentId")
    @ResponseBody
    public ResponseEntity<?> getDesignationByDepartmentId(
            @RequestParam(value = "tenantId", required = true) String tenantId, @RequestParam(value = "id") String id) {
        DesignationRes desigResponse = new DesignationRes();
        if (tenantId != null && !tenantId.isEmpty() && id != null && !id.isEmpty()) {
            ResponseInfo responseInfo = new ResponseInfo();
            responseInfo.setStatus(HttpStatus.OK.toString());
            desigResponse.setResponseInfo(responseInfo);
            List<Designation> designations = getDesignationByDepartment(id, tenantId);
            desigResponse.setDesignation(designations);
            return new ResponseEntity<DesignationRes>(desigResponse, HttpStatus.OK);
        } else
            return new ResponseEntity<DesignationRes>(desigResponse, HttpStatus.BAD_REQUEST);

    }

    private List<Designation> getDesignationByDepartment(String departmentId, String tenantId) {
        return mapToContractDesignationList(
                designationService.getAllDesignationByDepartment(Long.valueOf(departmentId), new Date(), tenantId));
    }

    private Designation mapToContractDesignation(org.egov.eis.persistence.entity.Designation designationEntity) {
        Designation designation = new Designation();
        if (designationEntity != null) {
            designation = new Designation(designationEntity);
        }
        return designation;
    }

    private List<Designation> mapToContractDesignationList(
            List<org.egov.eis.persistence.entity.Designation> designationEntity) {
        return designationEntity.stream().map(Designation::new).collect(Collectors.toList());
    }
}
package org.egov.pgr.controller;

import org.egov.pgr.contract.ComplaintType;
import org.egov.pgr.model.ResponseInfo;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.wrapper.ComplaintTypeRequest;
import org.egov.pgr.wrapper.ComplaintTypeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/complaintType")
public class ComplaintTypeController {

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @GetMapping(value = "/complaintTypeByCategory")
    public List<ComplaintType> getAllComplaintTypeByCategory(@RequestParam Long categoryId,
                                                             @RequestParam String tenantId) {
        return complaintTypeService.findActiveComplaintTypesByCategory(categoryId)
                .stream()
                .map(ComplaintType::new)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/getFrequentComplaints")
    public List<ComplaintType> getFrequentComplaintsFiled(@RequestParam Integer count, @RequestParam String tenantId) {
        return complaintTypeService.getFrequentlyFiledComplaints(count)
                .stream()
                .map(ComplaintType::new)
                .collect(Collectors.toList());
    }

    @GetMapping
    public ResponseEntity<?> search(@ModelAttribute ComplaintTypeRequest complaintTypeRequest) {

        ComplaintTypeResponse complaintTypeResponse = new ComplaintTypeResponse();
        List<org.egov.pgr.entity.ComplaintType> complaintTypes = complaintTypeService.getComplaintType
                (complaintTypeRequest);
        complaintTypeResponse.getComplaintTypes().addAll(complaintTypes);
        ResponseInfo responseInfo = new ResponseInfo("", "", new Date().toString(), "", "", "");
        responseInfo.setStatus(HttpStatus.CREATED.toString());
        complaintTypeResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<ComplaintTypeResponse>(complaintTypeResponse, HttpStatus.OK);
    }


}

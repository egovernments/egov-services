package org.egov.pgr.controller;

import org.egov.pgr.contract.ComplaintType;
import org.egov.pgr.contract.ComplaintTypeResponse;
import org.egov.pgr.model.ComplaintTypeSearchCriteria;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/complaintTypes")
public class ComplaintTypeController {

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @GetMapping
    public List<ComplaintType> getComplaintTypes(@RequestParam String type,
                                                 @RequestParam(required = false) Long categoryId,
                                                 @RequestParam(required = false) Long count,
                                                 @RequestParam String tenantId) {
        final ComplaintTypeSearchCriteria searchCriteria = ComplaintTypeSearchCriteria.builder()
                .categoryId(categoryId)
                .count(count)
                .complaintTypeSearch(type).build();
        return complaintTypeService.findByCategories(searchCriteria)
                .stream()
                .map(ComplaintType::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{complaintTypeCode}")
    public ComplaintTypeResponse getComplaintTypes(@PathVariable(name = "complaintTypeCode") String complaintTypeCode,
                                                   @RequestParam String tenantId) {
        org.egov.pgr.entity.ComplaintType complaintType = complaintTypeService
                .getComplaintType(complaintTypeCode);
        return new ComplaintTypeResponse(complaintType);
    }

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

}

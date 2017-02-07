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
                                                 @RequestParam(required = false) Integer count,
                                                 @RequestParam String tenantId) {
        final ComplaintTypeSearchCriteria searchCriteria = ComplaintTypeSearchCriteria.builder()
                .categoryId(categoryId)
                .count(count)
                .complaintTypeSearch(type).build();
        return complaintTypeService.findByCriteria(searchCriteria)
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

}

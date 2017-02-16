package org.egov.pgr.web.controller;

import org.egov.pgr.domain.model.ComplaintTypeSearchCriteria;
import org.egov.pgr.domain.service.ComplaintTypeService;
import org.egov.pgr.web.contract.ComplaintType;
import org.egov.pgr.web.contract.ComplaintTypeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/services")
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

    @GetMapping("/{serviceCode}")
    public ComplaintTypeResponse getComplaintTypes(@PathVariable(name = "complaintTypeCode") String complaintTypeCode,
                                                   @RequestParam String tenantId) {
        org.egov.pgr.persistence.entity.ComplaintType complaintType = complaintTypeService
                .getComplaintType(complaintTypeCode);
        return new ComplaintTypeResponse(complaintType);
    }

}

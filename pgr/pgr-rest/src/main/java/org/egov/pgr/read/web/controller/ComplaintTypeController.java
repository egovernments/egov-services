package org.egov.pgr.read.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.pgr.read.domain.model.ComplaintTypeSearchCriteria;
import org.egov.pgr.read.domain.service.ComplaintTypeService;
import org.egov.pgr.read.web.contract.ComplaintType;
import org.egov.pgr.read.web.contract.ComplaintTypeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services")
public class ComplaintTypeController {

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @GetMapping
    public List<ComplaintType> getComplaintTypes(@RequestParam String type,
            @RequestParam(required = false) Long categoryId, @RequestParam(required = false) Integer count,
            @RequestParam(value = "tenantId", required = true) final String tenantId) {
        if (tenantId != null && !tenantId.isEmpty()) {
            final ComplaintTypeSearchCriteria searchCriteria = ComplaintTypeSearchCriteria.builder()
                    .categoryId(categoryId).count(count).tenantId(tenantId).complaintTypeSearch(type).build();
            return complaintTypeService.findByCriteria(searchCriteria).stream().map(ComplaintType::new)
                    .collect(Collectors.toList());
        } else
            return new ArrayList<ComplaintType>();
    }

    @GetMapping("/{serviceCode}")
    public ComplaintTypeResponse getComplaintTypes(@PathVariable(name = "serviceCode") String complaintTypeCode,
            @RequestParam(value = "tenantId", required = true) String tenantId) {
        if (tenantId != null && !tenantId.isEmpty()) {
            org.egov.pgr.common.entity.ComplaintType complaintType = complaintTypeService
                    .getComplaintType(complaintTypeCode, tenantId);
            return new ComplaintTypeResponse(complaintType);
        }
        return new ComplaintTypeResponse(new  org.egov.pgr.common.entity.ComplaintType());
    }

}

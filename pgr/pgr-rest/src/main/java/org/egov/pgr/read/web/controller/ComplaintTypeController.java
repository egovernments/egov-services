package org.egov.pgr.read.web.controller;

import org.egov.pgr.read.domain.model.ComplaintTypeSearchCriteria;
import org.egov.pgr.read.domain.service.ComplaintTypeService;
import org.egov.pgr.read.web.contract.ComplaintType;
import org.egov.pgr.read.web.contract.ComplaintTypeResponse;
import org.egov.pgr.read.web.contract.RequestInfoBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/services")
public class ComplaintTypeController {

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @PostMapping
    public ComplaintTypeResponse getComplaintTypes(@RequestParam String type,
                                                   @RequestParam(required = false) Long categoryId, @RequestParam(required = false) Integer count,
                                                   @RequestParam(value = "tenantId", defaultValue = "default") final String tenantId,
                                                   @RequestBody RequestInfoBody requestInfo) {
        final ComplaintTypeSearchCriteria searchCriteria = ComplaintTypeSearchCriteria.builder()
            .categoryId(categoryId).count(count).tenantId(tenantId).complaintTypeSearch(type).build();
        List<ComplaintType> complaintTypes = complaintTypeService.findByCriteria(searchCriteria).stream().map(ComplaintType::new)
            .collect(Collectors.toList());
        return new ComplaintTypeResponse(null, complaintTypes);
    }

    @PostMapping("/{serviceCode}")
    public ComplaintTypeResponse getComplaintTypes(@PathVariable(name = "serviceCode") String complaintTypeCode,
                                                   @RequestParam(value = "tenantId", defaultValue = "default") String tenantId) {
        org.egov.pgr.common.entity.ComplaintType complaintType = complaintTypeService
            .getComplaintType(complaintTypeCode, tenantId);
        return new ComplaintTypeResponse(null, getComplaintType(complaintType));
    }


    private List<ComplaintType> getComplaintType(org.egov.pgr.common.entity.ComplaintType complaintType) {
        if (complaintType == null)
            return new ArrayList<ComplaintType>();
        else
            return Collections.singletonList(complaintType).stream().map(ComplaintType::new)
                .collect(Collectors.toList());
    }
}

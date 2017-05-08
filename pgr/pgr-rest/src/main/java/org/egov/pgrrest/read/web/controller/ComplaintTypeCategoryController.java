package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.read.domain.service.ComplaintTypeCategoryService;
import org.egov.pgrrest.read.web.contract.ComplaintTypeCategory;
import org.egov.pgrrest.read.web.contract.ComplaintTypeCategoryResponse;
import org.egov.pgrrest.read.web.contract.RequestInfoBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/complaintTypeCategories")
public class ComplaintTypeCategoryController {

    @Autowired
    private ComplaintTypeCategoryService complaintTypeCategoryService;

    @PostMapping
    public ComplaintTypeCategoryResponse getAllCompaintTypeCategory(
        @RequestParam(value = "tenantId", defaultValue = "default") final String tenantId,
        @RequestBody RequestInfoBody requestInfo) {
        List<ComplaintTypeCategory> complaintTypeCategoryList = complaintTypeCategoryService.getAll(tenantId).stream().map(ComplaintTypeCategory::new)
            .collect(Collectors.toList());
        return new ComplaintTypeCategoryResponse(null, complaintTypeCategoryList);
    }
}

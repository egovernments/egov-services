package org.egov.pgr.web.controller;

import org.egov.pgr.domain.service.ComplaintTypeCategoryService;
import org.egov.pgr.web.contract.ComplaintTypeCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/complaintTypeCategories")
public class ComplaintTypeCategoryController {

    @Autowired
    private ComplaintTypeCategoryService complaintTypeCategoryService;

    @GetMapping
    public List<ComplaintTypeCategory> getAllCompaintTypeCategory(@RequestParam String tenantId) {
        return complaintTypeCategoryService.getAll()
                .stream()
                .map(ComplaintTypeCategory::new)
                .collect(Collectors.toList());
    }
}

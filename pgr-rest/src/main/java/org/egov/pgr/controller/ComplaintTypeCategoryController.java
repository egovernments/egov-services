package org.egov.pgr.controller;

import org.egov.pgr.entity.ComplaintTypeCategory;
import org.egov.pgr.service.ComplaintTypeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/complaintTypeCategory")
public class ComplaintTypeCategoryController {

    @Autowired
    private ComplaintTypeCategoryService complaintTypeCategoryService;

    @GetMapping
    public List<ComplaintTypeCategory> getAllCompaintTypeCategory(@RequestParam String tenantId) {
        return complaintTypeCategoryService.getAll();
    }
}

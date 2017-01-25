package org.egov.pgr.controller;

import java.util.List;

import org.egov.pgr.entity.ComplaintTypeCategory;
import org.egov.pgr.service.ComplaintTypeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/complaintTypeCategory")
public class ComplaintTypeCategoryController {

	@Autowired
	private ComplaintTypeCategoryService complaintTypeCategoryService;

	@RequestMapping(value = "/getAllComplaintTypeCategory", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ComplaintTypeCategory> getAllCompaintTypeCategory(@RequestParam String tenantId) {
		return complaintTypeCategoryService.findAll();
	}
}

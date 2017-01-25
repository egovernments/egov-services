package org.egov.pgr.controller;

import java.util.List;

import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/complaintType")
public class ComplaintTypeController {

	@Autowired
	private ComplaintTypeService complaintTypeService;

	@RequestMapping(value = "/complaintTypeByCategory", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ComplaintType> getAllComplaintTypeByCategory(@RequestParam Long categoryId,
			@RequestParam String tenantId) {
		return complaintTypeService.findActiveComplaintTypesByCategory(categoryId);
	}

	@RequestMapping(value = "/getFrequentComplaints", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ComplaintType> getFrequentComplaintsFiled(@RequestParam Integer count,@RequestParam String tenantId) {
		return complaintTypeService.getFrequentlyFiledComplaints(count);
	}

}

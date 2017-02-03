package org.egov.pgr.controller;

import java.util.Date;
import java.util.List;

import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.model.ResponseInfo;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.wrapper.ComplaintTypeRequest;
import org.egov.pgr.wrapper.ComplaintTypeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	public List<ComplaintType> getFrequentComplaintsFiled(@RequestParam Integer count, @RequestParam String tenantId) {
		return complaintTypeService.getFrequentlyFiledComplaints(count);
	}

	@GetMapping
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute ComplaintTypeRequest complaintTypeRequest) {

		ComplaintTypeResponse complaintTypeResponse = new ComplaintTypeResponse();
		List<ComplaintType> complaintTypes = complaintTypeService.getComplaintType(complaintTypeRequest);
		complaintTypeResponse.getComplaintTypes().addAll(complaintTypes);
		ResponseInfo responseInfo = new ResponseInfo("","",new Date().toString(),"","","");
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		complaintTypeResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<ComplaintTypeResponse>(complaintTypeResponse, HttpStatus.OK);
	}


}

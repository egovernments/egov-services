package org.egov.pa.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.pa.web.contract.KPITargetRequest;
import org.egov.pa.web.contract.RequestInfoWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/kpitarget")
public interface KpiTarget {

	@PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final KPITargetRequest kpiTargetRequest,
            final BindingResult errors);
	
	@PostMapping(value = "/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final KPITargetRequest kpiTargetRequest,
            final BindingResult errors); 
	
	@PostMapping(value = "/_delete")
    @ResponseBody
    public ResponseEntity<?> delete(@RequestBody @Valid final KPITargetRequest kpiTargetRequest,
            final BindingResult errors); 
	
	@PostMapping(value = "/_search")
    @ResponseBody
    public ResponseEntity<?> search(@RequestParam(value="kpiCodes", required = false) List<String> kpiCodes,
			 @RequestParam(value="finYear", required = false) List<String> finYearList,
			 @RequestParam(value="departmentId", required = false) List<Long> departmentId,
			 @RequestParam(value="categoryId", required = false) List<String> categoryId,
			 @RequestParam(value="tenantId", required = false) String tenantId,
			 @RequestBody RequestInfoWrapper requestInfo);
	
}

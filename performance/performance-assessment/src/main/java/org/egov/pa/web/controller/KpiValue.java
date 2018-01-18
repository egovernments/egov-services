package org.egov.pa.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.pa.web.contract.KPIValueRequest;
import org.egov.pa.web.contract.RequestInfoWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/kpivalue")
public interface KpiValue {
	
	@PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final KPIValueRequest kpiValueRequest,
            final BindingResult errors); 
	
	@PostMapping(value = "/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final KPIValueRequest kpiValueRequest,
            final BindingResult errors); 
	
	@PostMapping(value = "/_comparesearch")
    @ResponseBody
    public ResponseEntity<?> compareAndSearch(@RequestParam("tenantId") List<String> tenantIdList,
			 @RequestParam(value="departmentId", required = false) Long departmentId,
			 @RequestParam(value="kpiCodes", required = false) List<String> kpiCode,
			 @RequestParam(value="finYear", required = false) List<String> finYearList,
			 @RequestParam(value="ulbs", required = false) List<String> ulbList,
			 @RequestParam(value="categoryId", required = false) Long categoryId,
			 @RequestParam(value="needDocs", required = false) Boolean needDocs,
			 @RequestBody RequestInfoWrapper requestInfo);
	
	@PostMapping(value = "/_search")
    @ResponseBody
    public ResponseEntity<?> search(@RequestParam(value = "tenantId", required=false) List<String> tenantIdList,
			 @RequestParam(value="departmentId", required = false) Long departmentId,
			 @RequestParam(value="kpiCodes", required = false) List<String> kpiCode,
			 @RequestParam(value="finYear", required = false) List<String> finYearList,
			 @RequestParam(value="ulbs", required = false) List<String> ulbList,
			 @RequestParam(value="categoryId", required = false) Long categoryId,
			 @RequestBody RequestInfoWrapper requestInfo);

}

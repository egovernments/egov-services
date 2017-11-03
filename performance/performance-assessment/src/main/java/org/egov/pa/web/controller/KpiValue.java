package org.egov.pa.web.controller;

import javax.validation.Valid;

import org.egov.pa.web.contract.KPIValueRequest;
import org.egov.pa.web.contract.KPIValueSearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<?> compareAndSearch(@RequestBody @Valid final KPIValueSearchRequest kpiValueSearchReq,
            final BindingResult errors);
	
	@PostMapping(value = "/_search")
    @ResponseBody
    public ResponseEntity<?> search(@RequestBody @Valid final KPIValueSearchRequest kpiValueSearchReq,
            final BindingResult errors);

}

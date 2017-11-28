package org.egov.pa.web.controller;

import javax.validation.Valid;

import org.egov.pa.web.contract.KPIGetRequest;
import org.egov.pa.web.contract.KPITargetRequest;
import org.egov.pa.web.contract.RequestInfoWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<?> search(@ModelAttribute @Valid final KPIGetRequest kpiGetRequest,
            final BindingResult errors, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult);
	
}

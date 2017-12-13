package org.egov.pa.web.controller;

import javax.validation.Valid;

import org.egov.pa.web.contract.KPIGetRequest;
import org.egov.pa.web.contract.KPIRequest;
import org.egov.pa.web.contract.RequestInfoWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/kpimaster")
public interface KpiMaster {
	
	@PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final KPIRequest kpiRequest,
            final BindingResult errors);
	
	@PostMapping(value = "/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final KPIRequest kpiRequest,
            final BindingResult errors); 
	
	@PostMapping(value = "/_delete")
    @ResponseBody
    public ResponseEntity<?> delete(@RequestBody @Valid final KPIRequest kpiRequest,
            final BindingResult errors); 
	
	@PostMapping(value = "/_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final KPIGetRequest kpiGetRequest,
            final BindingResult errors, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult);
	
	@PostMapping(value = "/_getkpitype")
    @ResponseBody
	public Boolean getKpiType(@RequestParam(value = "kpiCode") String kpiCode);
	
	@PostMapping(value = "/_getDocumentForKpi")
	@ResponseBody
	public ResponseEntity<?> getDocumentForKpi(@RequestParam("kpiCode") String kpiCode,
			 @RequestBody RequestInfoWrapper requestInfo); 
	

}

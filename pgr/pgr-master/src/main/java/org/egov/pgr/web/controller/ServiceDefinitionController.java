package org.egov.pgr.web.controller;

import org.egov.pgr.domain.model.ServiceDefinitionSearchCriteria;
import org.egov.pgr.domain.service.ServiceDefinitionService;
import org.egov.pgr.web.contract.RequestInfoBody;
import org.egov.pgr.web.contract.ServiceDefinition;
import org.egov.pgr.web.contract.ServiceDefinitionRequest;
import org.egov.pgr.web.contract.ServiceDefinitionResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/servicedefinition")
public class ServiceDefinitionController {

	private ServiceDefinitionService serviceDefinitionService;

	public ServiceDefinitionController(ServiceDefinitionService serviceDefinitionService) {
		this.serviceDefinitionService = serviceDefinitionService;
	}

	@PostMapping("/v1/_create")
	public ServiceDefinitionResponse create(@RequestBody ServiceDefinitionRequest request) {
		serviceDefinitionService.create(request.toDomain(), request);
		return new ServiceDefinitionResponse(null, request.getServiceDefinition());
	}

	@PostMapping("/v1/_update")
	public ServiceDefinitionResponse update(@RequestBody ServiceDefinitionRequest request){
		serviceDefinitionService.update(request.toDomain(), request);
		return new ServiceDefinitionResponse(null, request.getServiceDefinition());
	}

	@PostMapping("/v1/_search")
	public List<ServiceDefinition> search(@RequestParam(value = "tenantId", defaultValue = "default") String tenantId,
			@RequestParam(value = "serviceCode", required = false) String serviceCode,
			@RequestBody RequestInfoBody requestInfoBody) {

		ServiceDefinitionSearchCriteria searchCriteria = ServiceDefinitionSearchCriteria.builder().tenantId(tenantId)
				.serviceCode(serviceCode).build();

		List<org.egov.pgr.domain.model.ServiceDefinition> serviceDefinitions = serviceDefinitionService
				.search(searchCriteria);

		return serviceDefinitions.stream()
				.map(serviceDefinition -> new ServiceDefinition(serviceDefinition, serviceDefinition.getAttributes()))
				.collect(Collectors.toList());
	}

}

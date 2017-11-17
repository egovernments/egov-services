package org.egov.tenant.domain.service;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tenant.domain.exception.DuplicateTenantCodeException;
import org.egov.tenant.domain.exception.TenantInvalidCodeException;
import org.egov.tenant.domain.model.Tenant;
import org.egov.tenant.persistence.repository.MdmsRepository;
import org.egov.tenant.persistence.repository.TenantRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class TenantService {

	private TenantRepository tenantRepository;

	@Autowired
	private MdmsRepository mdmsRepository;

	public TenantService(TenantRepository tenantRepository) {
		this.tenantRepository = tenantRepository;
	}

	public List<Tenant> getTenants(List<String> code, RequestInfo requestInfo) {

		JSONArray responseJSONArray;
		ObjectMapper mapper = new ObjectMapper();

		responseJSONArray = mdmsRepository.getByCriteria("default", "tenant", "tenants", "code", code, requestInfo);

		if (responseJSONArray != null && responseJSONArray.size() > 0)
			return mapper.convertValue(responseJSONArray, new TypeReference<List<Tenant>>() {
			});
		else
			throw new CustomException("Tenants", "Given Tenant is invalid " + code);
	}

	public Tenant createTenant(Tenant tenant) {
		tenant.validate();
		validateDuplicateTenant(tenant);
		return tenantRepository.save(tenant);
	}

	public Tenant updateTenant(Tenant tenant) {
		tenant.validate();
		checkTenantExist(tenant);
		return tenantRepository.update(tenant);
	}

	private void validateDuplicateTenant(Tenant tenant) {
		if (tenantRepository.isTenantPresent(tenant.getCode()) > 0) {
			throw new DuplicateTenantCodeException(tenant);
		}
	}

	private void checkTenantExist(Tenant tenant) {

		if (!(tenantRepository.isTenantPresent(tenant.getCode()) > 0)) {
			throw new TenantInvalidCodeException(tenant);
		}
	}
}

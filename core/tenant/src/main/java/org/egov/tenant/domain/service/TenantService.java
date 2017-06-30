package org.egov.tenant.domain.service;

import java.util.List;

import org.egov.tenant.domain.exception.DuplicateTenantCodeException;
import org.egov.tenant.domain.exception.TenantInvalidCodeException;
import org.egov.tenant.domain.model.Tenant;
import org.egov.tenant.domain.model.TenantSearchCriteria;
import org.egov.tenant.persistence.repository.TenantRepository;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

    private TenantRepository tenantRepository;

    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public List<Tenant> find(final TenantSearchCriteria tenantSearchCriteria) {
        return tenantRepository.find(tenantSearchCriteria);
    }

    public Tenant createTenant(Tenant tenant) {
        tenant.validate();
        validateDuplicateTenant(tenant);
        return tenantRepository.save(tenant);
    }

    public Tenant updateTenant(Tenant tenant){
       	tenant.validate();
       	checkTenantExist(tenant);
    	return tenantRepository.update(tenant);
    }
    
    private void validateDuplicateTenant(Tenant tenant) {
        if (tenantRepository.isTenantPresent(tenant.getCode()) > 0) {
            throw new DuplicateTenantCodeException(tenant);
        }
    }
    
    private void checkTenantExist(Tenant tenant){
    	
    	if (!(tenantRepository.isTenantPresent(tenant.getCode()) > 0)) {
            throw new TenantInvalidCodeException(tenant);
        }
    }
}

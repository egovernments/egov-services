package org.egov.pgr.domain.service.validator.servicetypevalidators;


import java.util.HashMap;

import org.egov.pgr.domain.exception.PGRMasterException;
import org.egov.pgr.domain.model.ServiceType;
import org.springframework.stereotype.Service;

@Service
public class TenantIdMandatoryValidator implements ServiceTypeValidator {
    @Override
    public boolean canValidate(ServiceType serviceType) {
        return false;
    }

    @Override
    public void validate(ServiceType serviceType) {
    	
    	if (null == serviceType.getTenantId() || serviceType.isTenantIdAbsent()) {
			HashMap<String, String> error = new HashMap<>();
			error.put("code", "TenantIdValidator.1");
			error.put("field", "serviceType.tenantid");
			error.put("message", "tenantid mandatory");

			throw new PGRMasterException(error);
		}

    }
}

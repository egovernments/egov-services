package org.egov.swm.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.Tenant;
import org.egov.swm.web.repository.MdmsRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class TenantService {

    @Autowired
    private MdmsRepository mdmsRepository;

    public Tenant getTenant(final String tenantId, final String code, final RequestInfo requestInfo) {

        JSONArray responseJSONArray;
        final ObjectMapper mapper = new ObjectMapper();

        responseJSONArray = mdmsRepository.getByCriteria(tenantId, Constants.TENANT_MODULE_CODE,
                Constants.TENANTS_MASTER_NAME, "code", code, requestInfo);

        if (responseJSONArray != null && responseJSONArray.size() > 0)
            return mapper.convertValue(responseJSONArray.get(0), Tenant.class);
        else
            throw new CustomException("Tenant", "Given Tenant is invalid: " + code);

    }

    public List<Tenant> getAll(final String tenantId, final RequestInfo requestInfo) {

        List<Tenant> tenants = new ArrayList<>();

        JSONArray responseJSONArray;
        final ObjectMapper mapper = new ObjectMapper();

        responseJSONArray = mdmsRepository.getByCriteria(tenantId, Constants.TENANT_MODULE_CODE,
                Constants.TENANTS_MASTER_NAME, null, null, requestInfo);

        if (responseJSONArray != null && responseJSONArray.size() > 0)
            for (Object obj : responseJSONArray)
                tenants.add(mapper.convertValue(obj, Tenant.class));

        return tenants;

    }
}
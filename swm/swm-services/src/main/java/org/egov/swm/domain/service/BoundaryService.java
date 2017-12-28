package org.egov.swm.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.TenantBoundary;
import org.egov.swm.web.repository.MdmsRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class BoundaryService {

    @Autowired
    private MdmsRepository mdmsRepository;

    @Autowired
    private ObjectMapper mapper;

    public TenantBoundary getTenantBoundary(final String tenantId, final String code, final RequestInfo requestInfo) {

        JSONArray responseJSONArray;

        responseJSONArray = mdmsRepository.getByCriteria(tenantId, Constants.EGOV_LOCATION_MODULE_CODE,
                Constants.TENANTBOUNDARY_MASTER_NAME, "boundary.code", code, requestInfo);

        if (responseJSONArray != null && responseJSONArray.size() > 0)
            return mapper.convertValue(responseJSONArray.get(0), TenantBoundary.class);
        else
            throw new CustomException("TenantBoundary", "Given TenantBoundary is invalid: " + code);

    }

    public List<TenantBoundary> getAll(final String tenantId, final RequestInfo requestInfo) {

        List<TenantBoundary> tenantBoundarys = new ArrayList<>();

        JSONArray responseJSONArray;

        responseJSONArray = mdmsRepository.getByCriteria(tenantId, Constants.EGOV_LOCATION_MODULE_CODE,
                Constants.TENANTBOUNDARY_MASTER_NAME, null, null, requestInfo);

        if (responseJSONArray != null && responseJSONArray.size() > 0)
            for (Object obj : responseJSONArray)
                tenantBoundarys.add(mapper.convertValue(obj, TenantBoundary.class));

        return tenantBoundarys;

    }
}
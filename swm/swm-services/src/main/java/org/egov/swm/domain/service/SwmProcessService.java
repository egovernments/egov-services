package org.egov.swm.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.SwmProcess;
import org.egov.swm.web.repository.MdmsRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class SwmProcessService {

    @Autowired
    private MdmsRepository mdmsRepository;

    public SwmProcess getSwmProcess(final String tenantId, final String code, final RequestInfo requestInfo) {

        JSONArray responseJSONArray;
        final ObjectMapper mapper = new ObjectMapper();

        responseJSONArray = mdmsRepository.getByCriteria(tenantId, Constants.MODULE_CODE,
                Constants.SWMPROCESS_MASTER_NAME, "code", code, requestInfo);

        if (responseJSONArray != null && responseJSONArray.size() > 0)
            return mapper.convertValue(responseJSONArray.get(0), SwmProcess.class);
        else
            throw new CustomException("SwmProcess", "Given SwmProcess is invalid: " + code);

    }

    public List<SwmProcess> getAll(final String tenantId, final RequestInfo requestInfo) {

        List<SwmProcess> sps = new ArrayList<>();

        JSONArray responseJSONArray;
        final ObjectMapper mapper = new ObjectMapper();

        responseJSONArray = mdmsRepository.getByCriteria(tenantId, Constants.MODULE_CODE,
                Constants.SWMPROCESS_MASTER_NAME, null, null, requestInfo);

        if (responseJSONArray != null && responseJSONArray.size() > 0)
            for (Object obj : responseJSONArray)
                sps.add(mapper.convertValue(obj, SwmProcess.class));

        return sps;

    }

}
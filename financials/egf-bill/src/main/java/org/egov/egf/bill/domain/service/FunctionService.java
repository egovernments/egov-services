package org.egov.egf.bill.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.bill.constants.Constants;
import org.egov.egf.bill.web.contract.Function;
import org.egov.egf.bill.web.repository.MdmsRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class FunctionService {

    @Autowired
    private MdmsRepository mdmsRepository;

    @Autowired
    private ObjectMapper mapper;

    public Function getFunction(final String tenantId, final String code, final RequestInfo requestInfo) {

        JSONArray responseJSONArray;

        responseJSONArray = mdmsRepository.getByCriteria(tenantId, Constants.EGF_MASTER_MODULE_CODE,
                Constants.FUNCTION_MASTER_NAME, "code", code, requestInfo);

        if (responseJSONArray != null && responseJSONArray.size() > 0)
            return mapper.convertValue(responseJSONArray.get(0), Function.class);
        else
            throw new CustomException("Function", "Given Function is invalid: " + code);

    }

    public List<Function> getAll(final String tenantId, final RequestInfo requestInfo) {

        List<Function> functions = new ArrayList<>();

        JSONArray responseJSONArray;

        responseJSONArray = mdmsRepository.getByCriteria(tenantId, Constants.EGF_MASTER_MODULE_CODE,
                Constants.FUNCTION_MASTER_NAME, null, null, requestInfo);

        if (responseJSONArray != null && responseJSONArray.size() > 0)
            for (Object obj : responseJSONArray)
                functions.add(mapper.convertValue(obj, Function.class));

        return functions;

    }
}
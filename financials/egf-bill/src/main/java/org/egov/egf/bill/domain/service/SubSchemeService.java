package org.egov.egf.bill.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.bill.constants.Constants;
import org.egov.egf.bill.web.contract.SubScheme;
import org.egov.egf.bill.web.repository.MdmsRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class SubSchemeService {

    @Autowired
    private MdmsRepository mdmsRepository;

    @Autowired
    private ObjectMapper mapper;

    public SubScheme getSubScheme(final String tenantId, final String code, final RequestInfo requestInfo) {

        JSONArray responseJSONArray;

        responseJSONArray = mdmsRepository.getByCriteria(tenantId, Constants.EGF_MASTER_MODULE_CODE,
                Constants.SUBSCHEME_MASTER_NAME, "code", code, requestInfo);

        if (responseJSONArray != null && responseJSONArray.size() > 0)
            return mapper.convertValue(responseJSONArray.get(0), SubScheme.class);
        else
            throw new CustomException("SubScheme", "Given SubScheme is invalid: " + code);

    }

    public List<SubScheme> getAll(final String tenantId, final RequestInfo requestInfo) {

        List<SubScheme> subSchemes = new ArrayList<>();

        JSONArray responseJSONArray;

        responseJSONArray = mdmsRepository.getByCriteria(tenantId, Constants.EGF_MASTER_MODULE_CODE,
                Constants.SUBSCHEME_MASTER_NAME, null, null, requestInfo);

        if (responseJSONArray != null && responseJSONArray.size() > 0)
            for (Object obj : responseJSONArray)
                subSchemes.add(mapper.convertValue(obj, SubScheme.class));

        return subSchemes;

    }
}
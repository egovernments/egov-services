package org.egov.egf.bill.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.bill.constants.Constants;
import org.egov.egf.bill.web.contract.Fundsource;
import org.egov.egf.bill.web.repository.MdmsRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class FundSourceService {

    @Autowired
    private MdmsRepository mdmsRepository;

    @Autowired
    private ObjectMapper mapper;

    public Fundsource getFundSource(final String tenantId, final String code, final RequestInfo requestInfo) {

        JSONArray responseJSONArray;

        responseJSONArray = mdmsRepository.getByCriteria(tenantId, Constants.EGF_MASTER_MODULE_CODE,
                Constants.FUNDSOURCE_MASTER_NAME, "code", code, requestInfo);

        if (responseJSONArray != null && responseJSONArray.size() > 0)
            return mapper.convertValue(responseJSONArray.get(0), Fundsource.class);
        else
            throw new CustomException("FundSource", "Given FundSource is invalid: " + code);

    }

    public List<Fundsource> getAll(final String tenantId, final RequestInfo requestInfo) {

        List<Fundsource> fundSources = new ArrayList<>();

        JSONArray responseJSONArray;

        responseJSONArray = mdmsRepository.getByCriteria(tenantId, Constants.EGF_MASTER_MODULE_CODE,
                Constants.FUNDSOURCE_MASTER_NAME, null, null, requestInfo);

        if (responseJSONArray != null && responseJSONArray.size() > 0)
            for (Object obj : responseJSONArray)
                fundSources.add(mapper.convertValue(obj, Fundsource.class));

        return fundSources;

    }
}
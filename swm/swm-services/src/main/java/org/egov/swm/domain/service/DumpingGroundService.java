package org.egov.swm.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.DumpingGround;
import org.egov.swm.web.repository.MdmsRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class DumpingGroundService {

    @Autowired
    private MdmsRepository mdmsRepository;

    @Autowired
    private ObjectMapper mapper;

    public DumpingGround getDumpingGround(final String tenantId, final String code, final RequestInfo requestInfo) {

        JSONArray responseJSONArray;

        responseJSONArray = mdmsRepository.getByCriteria(tenantId, Constants.SWM_MODULE_CODE,
                Constants.DUMPINGGROUND_MASTER_NAME, "code", code, requestInfo);

        if (responseJSONArray != null && responseJSONArray.size() > 0)
            return mapper.convertValue(responseJSONArray.get(0), DumpingGround.class);
        else
            throw new CustomException("DumpingGround", "Given DumpingGround is invalid: " + code);

    }

    public List<DumpingGround> getAll(final String tenantId, final RequestInfo requestInfo) {

        final List<DumpingGround> dgs = new ArrayList<>();

        JSONArray responseJSONArray;

        responseJSONArray = mdmsRepository.getByCriteria(tenantId, Constants.SWM_MODULE_CODE,
                Constants.DUMPINGGROUND_MASTER_NAME, null, null, requestInfo);

        if (responseJSONArray != null && responseJSONArray.size() > 0)
            for (final Object obj : responseJSONArray)
                dgs.add(mapper.convertValue(obj, DumpingGround.class));

        return dgs;

    }

}
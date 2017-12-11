package org.egov.inv.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import org.egov.common.MdmsRepository;
import org.egov.inv.model.Department;
import org.egov.inv.model.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MdmsCommonService {

    @Autowired
    private MdmsRepository mdmsRepository;

    public Object getObject(String tenantId, String moduleName, String masterName,
                            String code, Class className) {

        JSONArray responseJSONArray;
        final ObjectMapper mapper = new ObjectMapper();

        responseJSONArray = mdmsRepository.getByCriteria(tenantId, moduleName,
                masterName, "code", code, new RequestInfo());

        return mapper.convertValue(responseJSONArray.get(0), className);

    }

}

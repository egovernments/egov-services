package org.egov.swm.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.CollectionType;
import org.egov.swm.web.repository.MdmsRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class CollectionTypeService {

    @Autowired
    private MdmsRepository mdmsRepository;

    @Autowired
    private ObjectMapper mapper;

    public CollectionType getCollectionType(final String tenantId, final String code, final RequestInfo requestInfo) {

        JSONArray responseJSONArray;

        responseJSONArray = mdmsRepository.getByCriteria(tenantId, Constants.SWM_MODULE_CODE,
                Constants.COLLECTIONTYPE_MASTER_NAME, "code", code, requestInfo);

        if (responseJSONArray != null && responseJSONArray.size() > 0)
            return mapper.convertValue(responseJSONArray.get(0), CollectionType.class);
        else
            throw new CustomException("CollectionType", "Given CollectionType is invalid: " + code);

    }

    public List<CollectionType> getAll(final String tenantId, final RequestInfo requestInfo) {

        final List<CollectionType> collectionTypes = new ArrayList<>();

        JSONArray responseJSONArray;

        responseJSONArray = mdmsRepository.getByCriteria(tenantId, Constants.SWM_MODULE_CODE,
                Constants.COLLECTIONTYPE_MASTER_NAME, null, null, requestInfo);

        if (responseJSONArray != null && responseJSONArray.size() > 0)
            for (final Object obj : responseJSONArray)
                collectionTypes.add(mapper.convertValue(obj, CollectionType.class));

        return collectionTypes;

    }

}
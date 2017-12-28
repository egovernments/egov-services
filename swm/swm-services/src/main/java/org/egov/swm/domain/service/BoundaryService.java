package org.egov.swm.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.MdmsResponse;
import org.egov.mdms.model.ModuleDetail;
import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.TenantBoundary;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class BoundaryService {

    private final RestTemplate restTemplate;

    private final String hierarchyTypeCode;

    private final String mdmsBySearchCriteriaUrl;

    @Autowired
    public BoundaryService(final RestTemplate restTemplate,
            @Value("${egov.swm.hierarchy.type.code}") final String hierarchyTypeCode,
            @Value("${egov.services.egov_mdms.hostname}") final String mdmsServiceHostname,
            @Value("${egov.services.egov_mdms.searchpath}") final String mdmsBySearchCriteriaUrl) {

        this.restTemplate = restTemplate;
        this.hierarchyTypeCode = hierarchyTypeCode;
        this.mdmsBySearchCriteriaUrl = mdmsServiceHostname + mdmsBySearchCriteriaUrl;
    }

    @Autowired
    private ObjectMapper mapper;

    public TenantBoundary getByCode(final String tenantId, final String code, final RequestInfo requestInfo) {

        JSONArray responseJSONArray;

        responseJSONArray = getByCriteria(tenantId, Constants.EGOV_LOCATION_MODULE_CODE,
                Constants.TENANTBOUNDARY_MASTER_NAME, code, requestInfo);

        if (responseJSONArray != null && responseJSONArray.size() > 0)
            return mapper.convertValue(responseJSONArray.get(0), TenantBoundary.class);
        else
            throw new CustomException("TenantBoundary", "Given TenantBoundary is invalid: " + code);

    }

    public JSONArray getByCriteria(final String tenantId, final String moduleName, final String masterName,
            final String filterFieldValue, final RequestInfo requestInfo) {

        List<MasterDetail> masterDetails;
        List<ModuleDetail> moduleDetails;
        MdmsCriteriaReq request = null;
        MdmsResponse response = null;
        masterDetails = new ArrayList<>();
        moduleDetails = new ArrayList<>();

        masterDetails.add(MasterDetail.builder().name(masterName).build());
        if (filterFieldValue != null && !filterFieldValue.isEmpty())
            masterDetails.get(0).setFilter("[?(('" + filterFieldValue + "' in @.boundary.children[*].code || @.boundary.code=='"
                    + filterFieldValue + "') && @.hierarchyType.code=='" + hierarchyTypeCode + "' )]");
        moduleDetails.add(ModuleDetail.builder().moduleName(moduleName).masterDetails(masterDetails).build());

        request = MdmsCriteriaReq.builder()
                .mdmsCriteria(MdmsCriteria.builder().moduleDetails(moduleDetails).tenantId(tenantId).build())
                .requestInfo(requestInfo).build();
        response = restTemplate.postForObject(mdmsBySearchCriteriaUrl, request, MdmsResponse.class);
        if (response == null || response.getMdmsRes() == null || !response.getMdmsRes().containsKey(moduleName)
                || response.getMdmsRes().get(moduleName) == null
                || !response.getMdmsRes().get(moduleName).containsKey(masterName)
                || response.getMdmsRes().get(moduleName).get(masterName) == null)
            return null;
        else
            return response.getMdmsRes().get(moduleName).get(masterName);
    }

}
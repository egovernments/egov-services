package org.egov.inv.domain.service;

import io.swagger.model.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryUtilityService {

    public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public InventoryUtilityService(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Long> getIdList(int size, String sequenceName) {
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("size", size);
        String getIdListQuery = "SELECT NEXTVAL('" + sequenceName + "') FROM GENERATE_SERIES(1,:size)";
        List<Long> idList;
        try {
            idList = namedParameterJdbcTemplate.queryForList(getIdListQuery, paramValues, Long.class);
        } catch (Exception e) {
            throw new RuntimeException("Next id is not generated.");
        }
        return idList;
    }

    public AuditDetails mapAuditDetails(RequestInfo requestInfo, String tenantId) {

        return AuditDetails.builder()
                .createdBy(requestInfo.getUserInfo().getId().toString())
                .lastModifiedBy(requestInfo.getUserInfo().getId().toString())
                .createdTime(requestInfo.getTs())
                .lastModifiedTime(requestInfo.getTs())
                .tenantId(tenantId)
                .build();

    }

    public AuditDetails mapAuditDetailsForUpdate(RequestInfo requestInfo, String tenantId) {

        return AuditDetails.builder()
                .lastModifiedBy(requestInfo.getUserInfo().getId().toString())
                .lastModifiedTime(requestInfo.getTs())
                .tenantId(tenantId)
                .build();
    }

    public ResponseInfo getResponseInfo(RequestInfo requestInfo) {
        return ResponseInfo.builder()
                .apiId(requestInfo.getApiId())
                .ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId())
                .resMsgId("placeholder")
                .status("placeholder")
                .build();
    }
}

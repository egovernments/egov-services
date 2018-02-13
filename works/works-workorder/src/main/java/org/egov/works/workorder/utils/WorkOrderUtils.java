package org.egov.works.workorder.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.web.contract.MasterDetails;
import org.egov.works.commons.web.contract.MdmsCriteria;
import org.egov.works.commons.web.contract.ModuleDetails;
import org.egov.works.commons.web.contract.Remarks;
import org.egov.works.workorder.domain.repository.LetterOfAcceptanceRepository;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
public class WorkOrderUtils {

    private final RestTemplate restTemplate;

    private final String mdmsBySearchCriteriaUrl;

    @Autowired
    private LetterOfAcceptanceRepository letterOfAcceptanceRepository;

    @Autowired
    public WorkOrderUtils(final RestTemplate restTemplate,
                          @Value("${egov.services.egov_mdms.hostname}") final String mdmsServiceHostname,
                          @Value("${egov.services.egov_mdms.searchpath}") final String mdmsBySearchCriteriaUrl) {

        this.restTemplate = restTemplate;
        this.mdmsBySearchCriteriaUrl = mdmsServiceHostname + mdmsBySearchCriteriaUrl;
    }

    public ResponseInfo createResponseInfoFromRequestInfo(RequestInfo requestInfo, Boolean isSuccess) {
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setApiId(requestInfo.getApiId());
        responseInfo.setVer(requestInfo.getVer());
        responseInfo.setTs(requestInfo.getTs()); // Fix me : set response time
        responseInfo.setResMsgId("uief87324"); // Fix me : do we need to
        // generate a new response
        // message id?
        responseInfo.setMsgId(requestInfo.getMsgId());
        responseInfo.setStatus(isSuccess ? ResponseInfo.StatusEnum.SUCCESSFUL : ResponseInfo.StatusEnum.FAILED);
        return responseInfo;
    }

    public AuditDetails setAuditDetails(final RequestInfo requestInfo, final Boolean isUpdate) {
        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy(requestInfo.getUserInfo().getUserName());
        auditDetails.setCreatedTime(new Date().getTime());
        auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getUserName());
        auditDetails.setLastModifiedTime(new Date().getTime());

        return auditDetails;
    }

    /**
     * @param objectName  accepts the name of entity like : ScheduleOfRate,Contractor
     *                    camelcase should be follwed
     * @param tenantId    tenantId for which the data to should be retrived
     * @param requestInfo
     * @param fieldName   name of the field in given object
     * @param fieldvalue  value for given field name
     * @return the json map it to your object.
     */

    public JSONArray getMDMSData(final String objectName, final String fieldName, final String fieldValue, final String tenantId,
                                 final RequestInfo requestInfo, final String moduleName) {
        MasterDetails[] masterDetailsArray;
        ModuleDetails[] moduleDetailsArray;
        MdmsRequest mdmsRequest;
        MdmsResponse mdmsResponse;
        String filter = "";

        if (StringUtils.isNotBlank(fieldName) && StringUtils.isNotBlank(fieldValue))
            filter = "[?(@." + fieldName + " == '" + fieldValue + "')]";

        masterDetailsArray = new MasterDetails[1];
        masterDetailsArray[0] = MasterDetails.builder().name(objectName).filter(filter).build();
        moduleDetailsArray = new ModuleDetails[1];
        moduleDetailsArray[0] = ModuleDetails.builder().moduleName(moduleName).masterDetails(masterDetailsArray)
                .build();

        mdmsRequest = MdmsRequest.builder()
                .mdmsCriteria(MdmsCriteria.builder().moduleDetails(moduleDetailsArray).tenantId(tenantId).build())
                .requestInfo(requestInfo).build();

        mdmsResponse = restTemplate.postForObject(mdmsBySearchCriteriaUrl, mdmsRequest, MdmsResponse.class);

        return mdmsResponse.getMdmsRes().get(moduleName).get(objectName);
    }

    /**
     *
     * @param objectName accepts the name of entity like : ScheduleOfRate,Contractor camelcase should be follwed
     * @param tenantId tenantId for which the data to should be retrived
     * @param requestInfo
     *
     * @param fieldName - List of fields as search
     * @param fieldvalue - List of values
     * @return the json map it to your object.
     */

    public JSONArray getMDMSData(final String objectName, final List<String> fieldNames, final List<String> fieldValues,
                                 final String tenantId, final RequestInfo requestInfo, final String moduleName) {
        MasterDetails[] masterDetailsArray;
        ModuleDetails[] moduleDetailsArray;
        MdmsRequest mdmsRequest;
        MdmsResponse mdmsResponse;
        StringBuilder filter = new StringBuilder();
        filter.append("[?(@." + fieldNames.get(0) + " == '" + fieldValues.get(0) + "'&&@." + fieldNames.get(1) + "=='" + fieldValues.get(1) + "')]");

        masterDetailsArray = new MasterDetails[1];
        masterDetailsArray[0] = MasterDetails.builder().name(objectName).filter(filter.toString()).build();
        moduleDetailsArray = new ModuleDetails[1];
        moduleDetailsArray[0] = ModuleDetails.builder().moduleName(moduleName).masterDetails(masterDetailsArray)
                .build();

        mdmsRequest = MdmsRequest.builder()
                .mdmsCriteria(MdmsCriteria.builder().moduleDetails(moduleDetailsArray).tenantId(tenantId).build())
                .requestInfo(requestInfo).build();

        mdmsResponse = restTemplate.postForObject(mdmsBySearchCriteriaUrl, mdmsRequest, MdmsResponse.class);

        return mdmsResponse.getMdmsRes().get(moduleName).get(objectName);
    }

    public ResponseInfo getResponseInfo(RequestInfo requestInfo) {
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setApiId(requestInfo.getApiId());
        responseInfo.setVer(requestInfo.getVer());
        responseInfo.setResMsgId(requestInfo.getMsgId());
        return responseInfo;
    }

    @SuppressWarnings("rawtypes")
    public String getCityCode(final String tenantId, final RequestInfo requestInfo) {
        String cityCode = "";
        JSONArray responseJSONArray = getMDMSData(CommonConstants.TENANT_OBJECTNAME,
                CommonConstants.CODE,
                tenantId, tenantId, requestInfo,
                CommonConstants.TENANT_MODULENAME);
        if (responseJSONArray != null && !responseJSONArray.isEmpty()) {
            @SuppressWarnings("unchecked")
            Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
            cityCode = ((Map) jsonMap.get("city")).get("code").toString();
        }
        return cityCode;
    }


    public String getRemarks(final String tenantId, final RequestInfo requestInfo,final String remarksCode) {
        String cityCode = "";
        JSONArray responseJSONArray = getMDMSData(CommonConstants.REMARKS_OBJECTNAME,
                CommonConstants.CODE,
                remarksCode, tenantId, requestInfo,
                CommonConstants.MODULENAME_WORKS);
        if (responseJSONArray != null && !responseJSONArray.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
//            List<Remarks> remarks = mapper.readV

            Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
            cityCode = ((Map) jsonMap.get("city")).get("code").toString();
        }
        return cityCode;
    }

    public LetterOfAcceptanceEstimate searchLoaEstimateById(String tenantId, LetterOfAcceptanceEstimate letterOfAcceptanceEstimate, RequestInfo requestInfo) {
        LetterOfAcceptanceSearchContract letterOfAcceptanceSearchContract = LetterOfAcceptanceSearchContract.builder()
                .tenantId(tenantId)
                .loaEstimateId(letterOfAcceptanceEstimate.getId()).build();
        List<LetterOfAcceptance> letterOfAcceptances = letterOfAcceptanceRepository.searchLOAs(letterOfAcceptanceSearchContract, requestInfo);
        return letterOfAcceptances.get(0).getLetterOfAcceptanceEstimates().isEmpty() ? letterOfAcceptances.get(0).getLetterOfAcceptanceEstimates().get(0) : null;
    }

}

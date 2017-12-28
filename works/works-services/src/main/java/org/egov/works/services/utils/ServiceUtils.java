package org.egov.works.services.utils;

import java.util.Date;
import java.util.List;

import net.minidev.json.JSONArray;
import org.egov.works.commons.web.contract.MasterDetails;
import org.egov.works.commons.web.contract.MdmsCriteria;
import org.egov.works.commons.web.contract.ModuleDetails;
import org.egov.works.services.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServiceUtils {

    private final RestTemplate restTemplate;

    private final String mdmsBySearchCriteriaUrl;

    @Autowired
    public ServiceUtils(final RestTemplate restTemplate,
                          @Value("${egov.services.egov_mdms.hostname}") final String mdmsServiceHostname,
                          @Value("${egov.services.egov_mdms.searchpath}") final String mdmsBySearchCriteriaUrl) {

        this.restTemplate = restTemplate;
        this.mdmsBySearchCriteriaUrl = mdmsServiceHostname + mdmsBySearchCriteriaUrl;
    }

    public ResponseInfo createResponseInfoFromRequestInfo(RequestInfo requestInfo, Boolean isSuccess) {
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setApiId(requestInfo.getApiId());
        responseInfo.setVer(requestInfo.getVer());
        responseInfo.setTs(requestInfo.getTs()); //Fix me : set response time
        responseInfo.setResMsgId("uief87324"); //Fix me : do we need to generate a new response message id?
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
}

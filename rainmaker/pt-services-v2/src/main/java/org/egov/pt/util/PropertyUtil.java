package org.egov.pt.util;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.pt.web.models.AuditDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PropertyUtil {




    /**
     * Method to return auditDetails for create/update flows
     *
     * @param by
     * @param isCreate
     * @return AuditDetails
     */
    public AuditDetails getAuditDetails(String by, Boolean isCreate) {
        Long time = new Date().getTime();
        if(isCreate)
            return AuditDetails.builder().createdBy(by).lastModifiedBy(by).createdTime(time).lastModifiedTime(time).build();
        else
            return AuditDetails.builder().lastModifiedBy(by).lastModifiedTime(time).build();
    }

    public MdmsCriteriaReq prepareMdMsRequest(String tenantId, List<String> names, RequestInfo requestInfo) {

        List<MasterDetail> masterDetails = new ArrayList<>();
        MasterDetail masterDetail;

        names.forEach(name -> {
            masterDetails.add(MasterDetail.builder().name(name).filter("$.*.code").build());
        });

        ModuleDetail moduleDetail = ModuleDetail.builder()
                .moduleName(PTConstants.MDMS_PT_MOD_NAME).masterDetails(masterDetails).build();
        List<ModuleDetail> moduleDetails = new ArrayList<>();
        moduleDetails.add(moduleDetail);
        MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
        return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
    }


}

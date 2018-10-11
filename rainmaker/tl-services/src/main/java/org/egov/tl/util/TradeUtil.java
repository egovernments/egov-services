package org.egov.tl.util;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.tl.config.TLConfiguration;
import org.egov.tl.web.models.AuditDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.egov.tl.util.TLConstants.*;
import static org.egov.tl.util.TLConstants.COMMON_MASTERS_MODULE;

@Component
public class TradeUtil {

    @Autowired
    private TLConfiguration config;


    /**
     * Method to return auditDetails for create/update flows
     *
     * @param by
     * @param isCreate
     * @return AuditDetails
     */
    public AuditDetails getAuditDetails(String by, Boolean isCreate) {
        Long time = System.currentTimeMillis();
        if(isCreate)
            return AuditDetails.builder().createdBy(by).lastModifiedBy(by).createdTime(time).lastModifiedTime(time).build();
        else
            return AuditDetails.builder().lastModifiedBy(by).lastModifiedTime(time).build();
    }

    public StringBuilder getCalculationURI(){
        StringBuilder uri = new StringBuilder(config.getCalculatorHost());
        uri.append(config.getCalculateEndpoint());
        return uri;
    }

    public String getPropertySearchURL(){
        StringBuilder url = new StringBuilder(config.getPropertyHost());
        url.append(config.getPropertyContextPath());
        url.append(config.getPropertySearchEndpoint());
        url.append("?");
        url.append("tenantId=");
        url.append("{1}");
        url.append("&");
        url.append("ids=");
        url.append("{2}");
        return url.toString();
    }


    /**
     * Methods provides all the master data needed for TL module
     */
    public MdmsCriteriaReq getTradeModuleRequest(RequestInfo requestInfo, String tenantId) {

        // master details for TL module
        List<MasterDetail> tlMasterDetails = new ArrayList<>();

        // filter to only get code field from master data
        final String filterCode = "$.[?(@.active==true)].code";

        tlMasterDetails.add(MasterDetail.builder().name(TRADE_TYPE).filter(filterCode).build());
        tlMasterDetails.add(MasterDetail.builder().name(ACCESSORIES_CATEGORY).filter(filterCode).build());

        ModuleDetail tlModuleDtls = ModuleDetail.builder().masterDetails(tlMasterDetails)
                .moduleName(TRADE_LICENSE_MODULE).build();

        // master details for common-masters module
        List<MasterDetail> commonMasterDetails = new ArrayList<>();
        commonMasterDetails.add(MasterDetail.builder().name(OWNERSHIP_CATEGORY).filter(filterCode).build());
        commonMasterDetails.add(MasterDetail.builder().name(STRUCTURE_TYPE).filter(filterCode).build());
        ModuleDetail commonMasterMDtl = ModuleDetail.builder().masterDetails(commonMasterDetails)
                .moduleName(COMMON_MASTERS_MODULE).build();

        MdmsCriteria mdmsCriteria = MdmsCriteria.builder().moduleDetails(Arrays.asList(tlModuleDtls,commonMasterMDtl)).tenantId(tenantId)
                .build();

        return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
    }



    public MdmsCriteriaReq getTradeUomRequest(RequestInfo requestInfo, String tenantId) {

        // master details for TL module
        List<MasterDetail> tlMasterDetails = new ArrayList<>();

        // filter to only get code field from master data

        final String filterCodeForUom = "$.[?(@.active==true)].uom";

        tlMasterDetails.add(MasterDetail.builder().name(TRADE_TYPE).filter(filterCodeForUom).build());
        tlMasterDetails.add(MasterDetail.builder().name(ACCESSORIES_CATEGORY).filter(filterCodeForUom).build());

        ModuleDetail tlModuleDtls = ModuleDetail.builder().masterDetails(tlMasterDetails)
                .moduleName(TRADE_LICENSE_MODULE).build();


        MdmsCriteria mdmsCriteria = MdmsCriteria.builder().moduleDetails(Collections.singletonList(tlModuleDtls)).tenantId(tenantId)
                .build();

        return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
    }


    /**
     * Returns the url for mdms search endpoint
     *
     * @return
     */
    public StringBuilder getMdmsSearchUrl() {
        return new StringBuilder().append(config.getMdmsHost()).append(config.getMdmsEndPoint());
    }



}

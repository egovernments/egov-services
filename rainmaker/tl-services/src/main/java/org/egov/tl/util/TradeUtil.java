package org.egov.tl.util;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.tl.config.TLConfiguration;
import org.egov.tl.repository.ServiceRequestRepository;
import org.egov.tl.web.models.AuditDetails;
import org.egov.tl.web.models.TradeLicense;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.egov.tl.util.TLConstants.*;
import static org.egov.tl.util.TLConstants.COMMON_MASTERS_MODULE;

@Component
@Slf4j
public class TradeUtil {

    private TLConfiguration config;

    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    public TradeUtil(TLConfiguration config, ServiceRequestRepository serviceRequestRepository) {
        this.config = config;
        this.serviceRequestRepository = serviceRequestRepository;
    }

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


    /**
     * Creates url for tl-calculator service
     * @return url for tl-calculator service
     */
    public StringBuilder getCalculationURI(){
        StringBuilder uri = new StringBuilder(config.getCalculatorHost());
        uri.append(config.getCalculateEndpoint());
        return uri;
    }


    /**
     * Creates search url for pt-services-v2 service
     * @return url for pt-services-v2 service search
     */
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
     * Creates request to search UOM from MDMS
     * @param requestInfo The requestInfo of the request
     * @param tenantId The tenantId of the tradeLicense
     * @return request to search UOM from MDMS
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


    /**
     * Creates request to search UOM from MDMS
     * @param requestInfo The requestInfo of the request
     * @param tenantId The tenantId of the tradeLicense
     * @return request to search UOM from MDMS
     */
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
     * @return url for mdms search endpoint
     */
    public StringBuilder getMdmsSearchUrl() {
        return new StringBuilder().append(config.getMdmsHost()).append(config.getMdmsEndPoint());
    }


    /**
     * Creates map containing the startTime and endTime of the given tradeLicense
     * @param requestInfo The requestInfo of the request
     * @param license The create or update TradeLicense request
     * @return Map containing startTime and endTime
     */
    public Map<String,Long> getTaxPeriods(RequestInfo requestInfo, TradeLicense license){
        Map<String,Long> taxPeriods = new HashMap<>();

        MdmsCriteriaReq mdmsCriteriaReq = getFinancialYearRequest(requestInfo,license.getTenantId());
        try {
            Object result = serviceRequestRepository.fetchResult(getMdmsSearchUrl(), mdmsCriteriaReq);
            String jsonPath = TLConstants.MDMS_FINACIALYEAR_PATH.replace("{}",license.getFinancialYear());
            List<Map<String,Object>> jsonOutput =  JsonPath.read(result, jsonPath);
            Map<String,Object> financialYearProperties = jsonOutput.get(0);
            Object startDate = financialYearProperties.get(TLConstants.MDMS_STARTDATE);
            Object endDate = financialYearProperties.get(TLConstants.MDMS_ENDDATE);
            taxPeriods.put(TLConstants.MDMS_STARTDATE,(Long) startDate);
            taxPeriods.put(TLConstants.MDMS_ENDDATE,(Long) endDate);

        } catch (Exception e) {
            log.error("Error while fetvhing MDMS data", e);
            throw new CustomException("INVALID FINANCIALYEAR", "No data found for the financialYear: "+license.getFinancialYear());
        }
        return taxPeriods;
    }


    /**
     * Creates request to search financialYear in mdms
     * @param requestInfo The requestInfo of the request
     * @param tenantId The tenantId of the tradeLicense
     * @return MDMS request for financialYear
     */
    private MdmsCriteriaReq getFinancialYearRequest(RequestInfo requestInfo, String tenantId) {

        // master details for TL module
        List<MasterDetail> tlMasterDetails = new ArrayList<>();

        // filter to only get code field from master data

        final String filterCodeForUom = "$.[?(@.active==true)]";

        tlMasterDetails.add(MasterDetail.builder().name(TLConstants.MDMS_FINANCIALYEAR).filter(filterCodeForUom).build());

        ModuleDetail tlModuleDtls = ModuleDetail.builder().masterDetails(tlMasterDetails)
                .moduleName(TLConstants.MDMS_EGF_MASTER).build();


        MdmsCriteria mdmsCriteria = MdmsCriteria.builder().moduleDetails(Collections.singletonList(tlModuleDtls)).tenantId(tenantId)
                .build();

        return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
    }





}

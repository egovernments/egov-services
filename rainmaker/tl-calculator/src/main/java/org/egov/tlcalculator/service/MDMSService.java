package org.egov.tlcalculator.service;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.tlcalculator.config.TLCalculatorConfigs;
import org.egov.tlcalculator.repository.ServiceRequestRepository;
import org.egov.tlcalculator.utils.BillingslabConstants;
import org.egov.tlcalculator.utils.TLCalculatorConstants;
import org.egov.tlcalculator.web.models.tradelicense.TradeLicense;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.jayway.jsonpath.JsonPath.read;

@Slf4j
@Service
public class enrichmentService {


    private TLCalculatorConfigs config;

    private ServiceRequestRepository serviceRequestRepository;


    @Autowired
    public enrichmentService(TLCalculatorConfigs config,ServiceRequestRepository serviceRequestRepository) {
        this.config = config;
        this.serviceRequestRepository = serviceRequestRepository;
    }










    private MdmsCriteriaReq getFinancialYearRequest(RequestInfo requestInfo, String tenantId) {

        // master details for TL module
        List<MasterDetail> tlMasterDetails = new ArrayList<>();

        // filter to only get code field from master data

        final String filterCodeForUom = "$.[?(@.active==true)]";

        tlMasterDetails.add(MasterDetail.builder().name(TLCalculatorConstants.MDMS_FINANCIALYEAR).filter(filterCodeForUom).build());

        ModuleDetail tlModuleDtls = ModuleDetail.builder().masterDetails(tlMasterDetails)
                .moduleName(TLCalculatorConstants.MDMS_EGF_MASTER).build();


        MdmsCriteria mdmsCriteria = MdmsCriteria.builder().moduleDetails(Collections.singletonList(tlModuleDtls)).tenantId(tenantId)
                .build();

        return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
    }


    public Map<String,String> getTaxPeriods(RequestInfo requestInfo,TradeLicense license){
        Map<String,String> taxPeriods = new HashMap<>();

        MdmsCriteriaReq mdmsCriteriaReq = getFinancialYearRequest(requestInfo,license.getTenantId());
        try {
            Object result = serviceRequestRepository.fetchResult(getMdmsSearchUrl(), mdmsCriteriaReq);
            String jsonPath = TLCalculatorConstants.MDMS_FINACIALYEAR_PATH.replace("{}",license.getFinancialYear());
            List<Map<String,String>> jsonOutput =  JsonPath.read(result, jsonPath);
            Map<String,String> financialYearProperties = jsonOutput.get(0);
            taxPeriods.put("taxPeriodFrom",financialYearProperties.get(TLCalculatorConstants.MDMS_STARTDATE));
            taxPeriods.put("taxPeriodTo",financialYearProperties.get(TLCalculatorConstants.MDMS_ENDDATE));

        } catch (Exception e) {
            log.error("Error while fetvhing MDMS data", e);
            throw new CustomException("INVALID FINANCIALYEAR", "No data found for the financialYear: "+license.getFinancialYear());
        }
        return taxPeriods;
    }





    /**
     * Returns the url for mdms search endpoint
     *
     * @return
     */
    private StringBuilder getMdmsSearchUrl() {
        return new StringBuilder().append(config.getMdmsHost()).append(config.getMdmsSearchEndpoint());
    }



}

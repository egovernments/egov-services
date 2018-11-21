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
import org.egov.tlcalculator.utils.TLCalculatorConstants;
import org.egov.tlcalculator.web.models.enums.CalculationType;
import org.egov.tlcalculator.web.models.tradelicense.TradeLicense;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.jayway.jsonpath.JsonPath.read;

@Slf4j
@Service
public class MDMSService {


    private TLCalculatorConfigs config;

    private ServiceRequestRepository serviceRequestRepository;


    @Autowired
    public MDMSService(TLCalculatorConfigs config, ServiceRequestRepository serviceRequestRepository) {
        this.config = config;
        this.serviceRequestRepository = serviceRequestRepository;
    }










    private MdmsCriteriaReq getMDMSRequest(RequestInfo requestInfo, String tenantId) {

        // master details for TL module
        List<MasterDetail> fyMasterDetails = new ArrayList<>();
        // filter to only get code field from master data

        final String filterCodeForUom = "$.[?(@.active==true)]";

        fyMasterDetails.add(MasterDetail.builder().name(TLCalculatorConstants.MDMS_FINANCIALYEAR).filter(filterCodeForUom).build());

        ModuleDetail fyModuleDtls = ModuleDetail.builder().masterDetails(fyMasterDetails)
                .moduleName(TLCalculatorConstants.MDMS_EGF_MASTER).build();

        List<MasterDetail> tlMasterDetails = new ArrayList<>();
        tlMasterDetails.add(MasterDetail.builder().name(TLCalculatorConstants.MDMS_CALCULATIONTYPE)
                .filter(filterCodeForUom).build());
        ModuleDetail tlModuleDtls = ModuleDetail.builder().masterDetails(tlMasterDetails)
                .moduleName(TLCalculatorConstants.MDMS_TRADELICENSE).build();

        List<ModuleDetail> moduleDetails = new ArrayList<>();
        moduleDetails.add(fyModuleDtls);
        moduleDetails.add(tlModuleDtls);

        MdmsCriteria mdmsCriteria = MdmsCriteria.builder().moduleDetails(moduleDetails).tenantId(tenantId)
                .build();

        return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
    }


    public Map<String,Long> getTaxPeriods(RequestInfo requestInfo,TradeLicense license){
        Map<String,Long> taxPeriods = new HashMap<>();

        MdmsCriteriaReq mdmsCriteriaReq = getMDMSRequest(requestInfo,license.getTenantId());
        try {
            Object result = serviceRequestRepository.fetchResult(getMdmsSearchUrl(), mdmsCriteriaReq);
            String jsonPath = TLCalculatorConstants.MDMS_FINACIALYEAR_PATH.replace("{}",license.getFinancialYear());
            List<Map<String,Object>> jsonOutput =  JsonPath.read(result, jsonPath);
            Map<String,Object> financialYearProperties = jsonOutput.get(0);
            Object startDate = financialYearProperties.get(TLCalculatorConstants.MDMS_STARTDATE);
            Object endDate = financialYearProperties.get(TLCalculatorConstants.MDMS_ENDDATE);
            taxPeriods.put(TLCalculatorConstants.MDMS_STARTDATE,(Long) startDate);
            taxPeriods.put(TLCalculatorConstants.MDMS_ENDDATE,(Long) endDate);

        } catch (Exception e) {
            log.error("Error while fetvhing MDMS data", e);
            throw new CustomException("INVALID FINANCIALYEAR", "No data found for the financialYear: "+license.getFinancialYear());
        }
        return taxPeriods;
    }


    public Map getCalculationType(RequestInfo requestInfo,TradeLicense license){

        MdmsCriteriaReq mdmsCriteriaReq = getMDMSRequest(requestInfo,license.getTenantId());
        HashMap<String,Object> calculationType = new HashMap<>();
        try {
            Object result = serviceRequestRepository.fetchResult(getMdmsSearchUrl(), mdmsCriteriaReq);
            LinkedHashMap tradeLicenseData = JsonPath.read(result,TLCalculatorConstants.MDMS_TRADELICENSE_PATH);
            if(tradeLicenseData.size()==0)
                return defaultMap();

            List jsonOutput = JsonPath.read(result, TLCalculatorConstants.MDMS_CALCULATIONTYPE_PATH);
            String financialYear = license.getFinancialYear().split("-")[0];
            String maxClosestYear = "0";
            for(Object entry : jsonOutput) {
                HashMap<String,Object> map = (HashMap<String,Object>)entry;
                String mdmsFinancialYear = ((String)map.get(TLCalculatorConstants.MDMS_CALCULATIONTYPE_FINANCIALYEAR));
                String year = mdmsFinancialYear.split("-")[0];
                if(year.compareTo(financialYear)<0 && year.compareTo(maxClosestYear.split("-")[0])>0){
                    maxClosestYear = mdmsFinancialYear;
                }
                if(year.compareTo(financialYear)==0){
                    maxClosestYear = mdmsFinancialYear;
                    break;
                }
            }
            String jsonPath = TLCalculatorConstants.MDMS_CALCULATIONTYPE_FINANCIALYEAR_PATH.replace("{}",maxClosestYear);
            List<HashMap> output = JsonPath.read(result,jsonPath);
            calculationType = output.get(0);
        }
        catch (Exception e){
            throw new CustomException("MDMS ERROR","Failed to get calculationType");
        }

        return calculationType;
    }

    private Map defaultMap(){
        Map defaultMap = new HashMap();
        defaultMap.put(TLCalculatorConstants.MDMS_CALCULATIONTYPE_TRADETYPE,config.getDefaultTradeUnitCalculationType());
        defaultMap.put(TLCalculatorConstants.MDMS_CALCULATIONTYPE_ACCESSORY,config.getDefaultAccessoryCalculationType());
        return defaultMap;
    }


    public Object mDMSCall(RequestInfo requestInfo,TradeLicense license){
        MdmsCriteriaReq mdmsCriteriaReq = getMDMSRequest(requestInfo,license.getTenantId());
        Object result = serviceRequestRepository.fetchResult(getMdmsSearchUrl(), mdmsCriteriaReq);
        return result;
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

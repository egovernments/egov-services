package org.egov.tlcalculator.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tlcalculator.config.TLCalculatorConfigs;
import org.egov.tlcalculator.repository.ServiceRequestRepository;
import org.egov.tlcalculator.web.models.AuditDetails;
import org.egov.tlcalculator.web.models.RequestInfoWrapper;
import org.egov.tlcalculator.web.models.TL.TradeLicense;
import org.egov.tlcalculator.web.models.TL.TradeLicenseResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class CalculationUtils {


    @Autowired
    private TLCalculatorConfigs config;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ObjectMapper mapper;



  private String getTradeLicenseSearchURL(){
      StringBuilder url = new StringBuilder(config.getTradeLicenseHost());
      url.append(config.getTradeLicenseContextPath());
      url.append(config.getTradeLicenseSearchEndpoint());
      url.append("?");
      url.append("tenantId=");
      url.append("{1}");
      url.append("&");
      url.append("applicationNumber=");
      url.append("{2}");
      return url.toString();
  }

    public String getDemandSearchURL(){
        StringBuilder url = new StringBuilder(config.getBillingHost());
        url.append(config.getDemandSearchEndpoint());
        url.append("?");
        url.append("tenantId=");
        url.append("{1}");
        url.append("&");
        url.append("businessService=");
        url.append("{2}");
        url.append("&");
        url.append("consumerCode=");
        url.append("{3}");
        return url.toString();
    }

    public AuditDetails getAuditDetails(String by, Boolean isCreate) {
        Long time = System.currentTimeMillis();
        if(isCreate)
            return AuditDetails.builder().createdBy(by).lastModifiedBy(by).createdTime(time).lastModifiedTime(time).build();
        else
            return AuditDetails.builder().lastModifiedBy(by).lastModifiedTime(time).build();
    }


    public TradeLicense getTradeLicense(RequestInfo requestInfo, String applicationNumber, String tenantId){
        String url = getTradeLicenseSearchURL();
        url = url.replace("{1}",tenantId).replace("{2}",applicationNumber);

        Object result =serviceRequestRepository.fetchResult(new StringBuilder(url),RequestInfoWrapper.builder().
                requestInfo(requestInfo).build());

        TradeLicenseResponse response =null;
        try {
                response = mapper.convertValue(result,TradeLicenseResponse.class);
        }
        catch (IllegalArgumentException e){
            throw new CustomException("PARSING ERROR","Error while parsing response of TradeLicense Search");
        }

        if(response==null || CollectionUtils.isEmpty(response.getLicenses()))
            return null;

        return response.getLicenses().get(0);
    }

}

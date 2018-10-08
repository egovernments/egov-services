package org.egov.tl.validator;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tl.repository.ServiceRequestRepository;
import org.egov.tl.util.TradeUtil;
import org.egov.tl.web.models.RequestInfoWrapper;
import org.egov.tl.web.models.TradeLicenseRequest;
import org.egov.tracer.model.CustomException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static org.egov.tl.util.TLConstants.PROPERTY_JSONPATH;

@Component
public class PropertyValidator {


    private ServiceRequestRepository serviceRequestRepository;

    private TradeUtil util;


    @Autowired
    public PropertyValidator(ServiceRequestRepository serviceRequestRepository, TradeUtil util) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.util = util;
    }




    public void validateProperty(TradeLicenseRequest request){
        RequestInfo requestInfo = request.getRequestInfo();

        request.getLicenses().forEach(license -> {
            if(license.getPropertyId()!=null) {
                String url = util.getPropertySearchURL();
                url = url.replace("{1}", license.getTenantId());
                url = url.replace("{2}", license.getPropertyId());

                try {
                    Object obj = serviceRequestRepository.fetchResult(new StringBuilder(url),
                            RequestInfoWrapper.builder().requestInfo(requestInfo).build());
                    HashMap<String, Object> result = (HashMap<String, Object>) obj;
                    String jsonString = new JSONObject(result).toString();
                    DocumentContext documentContext = JsonPath.parse(jsonString);
                    String propertyIdFromSearch = documentContext.read(PROPERTY_JSONPATH);
                    if (propertyIdFromSearch == null)
                        throw new CustomException("INVALID PROPERTY", " The propertyId " + license.getPropertyId() + " does not exist");
                } catch (Exception e) {
                    throw new CustomException("INVALID PROPERTY", " Failed to parse the response from property search on id " + license.getPropertyId());
                }
            }
        });
    }


}

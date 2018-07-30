package org.egov.pt.service;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.egov.pt.repository.ServiceRequestRepository;
import org.egov.pt.web.models.Boundary;
import org.egov.pt.web.models.PropertyRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BoundaryService {


    @Value("${egov.location.host}")
    private String locationHost;

    @Value("${egov.location.context.path}")
    private String locationContextPath;

    @Value("${egov.location.endpoint}")
    private String locationEndpoint;


    @Autowired
    private ServiceRequestRepository serviceRequestRepository;


    public void getAreaType(PropertyRequest request,String hierarchyTypeCode){
        String tenantId = request.getProperties().get(0).getTenantId();

        LinkedList<String> localities = new LinkedList<>();
        request.getProperties().forEach(property -> {
            localities.add(property.getAddress().getLocality().getCode());
        });

        StringBuilder uri = new StringBuilder(locationHost);
        uri.append(locationContextPath).append(locationEndpoint);

        uri.append("?").append("tenantId=").append(tenantId);

        if(hierarchyTypeCode!=null)
            uri.append("&").append("hierarchyTypeCode=").append(hierarchyTypeCode);

        uri.append("&").append("boundaryType").append("Locality");

        if(!CollectionUtils.isEmpty(localities)) {
            uri.append("&").append("codes=");
            for (int i = 0; i < localities.size(); i++) {
                uri.append(localities.get(i));
                if(i!=localities.size()-1)
                    uri.append(",");
            }
        }

        LinkedHashMap responseMap = (LinkedHashMap)serviceRequestRepository.fetchResult(uri, request.getRequestInfo());
        String jsonString = new JSONObject(responseMap).toString();

        Map<String,String> propertyIdToJsonPath = getJsonpath(request);

        DocumentContext context = JsonPath.parse(jsonString);

        request.getProperties().forEach(property -> {
            property.getAddress().getLocality().setName(context.read(propertyIdToJsonPath.get(property.getPropertyId())));
        });



        //$..boundary[?(@.code=="JLC476")].name




    }




    private Map<String,String> getJsonpath(PropertyRequest request){
        Map<String,String> propertyIdToJsonPath = new LinkedHashMap<>();
        StringBuilder initialString = new StringBuilder("$..boundary[?(@.code==\"");
        StringBuilder endString = new StringBuilder("\")].name");

        request.getProperties().forEach(property -> {
            StringBuilder jsonpath = new StringBuilder(property.getAddress().getLocality().getCode());
            propertyIdToJsonPath.put(property.getPropertyId(), initialString.append(jsonpath).append(endString).toString());
        });

        return  propertyIdToJsonPath;
    }










}

package org.egov.pt.util;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.pt.config.PropertyConfiguration;
import org.egov.pt.web.models.AuditDetails;
import org.egov.pt.web.models.Property;
import org.egov.pt.web.models.PropertyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.egov.pt.util.PTConstants.NOTIFICATION_LOCALE;
import static org.egov.pt.util.PTConstants.PT_PROPERTYTYPE_VACANT;

@Component
public class PropertyUtil {



    @Autowired
    private PropertyConfiguration config;


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

    public MdmsCriteriaReq prepareMdMsRequest(String tenantId,String moduleName, List<String> names, String filter, RequestInfo requestInfo) {

        List<MasterDetail> masterDetails = new ArrayList<>();
        MasterDetail masterDetail;

        names.forEach(name -> {
            masterDetails.add(MasterDetail.builder().name(name).filter(filter).build());
        });

        ModuleDetail moduleDetail = ModuleDetail.builder()
                .moduleName(moduleName).masterDetails(masterDetails).build();
        List<ModuleDetail> moduleDetails = new ArrayList<>();
        moduleDetails.add(moduleDetail);
        MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
        return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
    }


    public void addAddressIds(List<Property> responseProperties,List<Property> requestProperties){
        Map<String,String > propertyIdToAddressId = new HashMap<>();
        responseProperties.forEach(property -> {
            propertyIdToAddressId.put(property.getPropertyId(),property.getAddress().getId());
        });
        requestProperties.forEach(property -> {
            property.getAddress().setId(propertyIdToAddressId.get(property.getPropertyId()));
        });
    }


    /**
     * Returns the uri for the localization call
     * @param tenantId TenantId of the propertyRequest
     * @return The uri for localization search call
     */
    public StringBuilder getUri(String tenantId, RequestInfo requestInfo){
        if(config.getIsStateLevel())
            tenantId = tenantId.split("\\.")[0];

        String locale = NOTIFICATION_LOCALE;
        if(!StringUtils.isEmpty(requestInfo.getMsgId()) && requestInfo.getMsgId().split("|").length>=2)
            locale = requestInfo.getMsgId().split("\\|")[1];

        StringBuilder uri = new StringBuilder();
        uri.append(config.getLocalizationHost())
                .append(config.getLocalizationContextPath()).append(config.getLocalizationSearchEndpoint());
        uri.append("?").append("locale=").append(locale)
                .append("&tenantId=").append(tenantId)
                .append("&module=").append(PTConstants.MODULE);
        return uri;
    }


    public List<PropertyRequest> aggregatePropertyRequest(PropertyRequest request){
        RequestInfo requestInfo = request.getRequestInfo();
        List<PropertyRequest> requests = new LinkedList<>();
        List<Property> properties = request.getProperties();
        List<Property> vacantProperties = new LinkedList<>();
        List<Property> nonVacantProperties = new LinkedList<>();

        properties.forEach(property -> {
            if(property.getPropertyDetails().get(0).getPropertyType().equalsIgnoreCase(PT_PROPERTYTYPE_VACANT))
                vacantProperties.add(property);
            else nonVacantProperties.add(property);
        });

        if(!CollectionUtils.isEmpty(vacantProperties)){
            PropertyRequest vacantPropertyRequest = PropertyRequest.builder().properties(vacantProperties)
                    .requestInfo(requestInfo).build();
            requests.add(vacantPropertyRequest);
        }

        if(!CollectionUtils.isEmpty(nonVacantProperties)){
            PropertyRequest nonVacantPropertyRequest = PropertyRequest.builder().properties(nonVacantProperties)
                    .requestInfo(requestInfo).build();
            requests.add(nonVacantPropertyRequest);
        }

        return requests;
    }

}

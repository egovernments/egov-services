package org.egov.pt.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pt.config.PropertyConfiguration;
import org.egov.pt.repository.IdGenRepository;
import org.egov.pt.util.PTConstants;
import org.egov.pt.util.PropertyUtil;
import org.egov.pt.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EnrichmentService {


    @Autowired
    PropertyUtil propertyutil;

    @Autowired
    IdGenRepository idGenRepository;

    @Autowired
    private PropertyConfiguration config;


    /**
     * Assigns UUIDs to all id fields and also assigns acknowledgementnumber and assessmentnumber generated from idgen
     * @param request
     */
    public void enrichCreateRequest(PropertyRequest request) {

        RequestInfo requestInfo = request.getRequestInfo();
        AuditDetails auditDetails = propertyutil.getAuditDetails(requestInfo.getUserInfo().getId().toString(), true);
        String tenantId = request.getProperties().get(0).getTenantId();

        for (Property property : request.getProperties()) {
            property.getAddress().setId(UUID.randomUUID().toString());
            property.setAuditDetails(auditDetails);

            property.getPropertyDetails().forEach(propertyDetail -> {
                 propertyDetail.setAssessmentNumber(UUID.randomUUID().toString());
                Set<OwnerInfo> owners = propertyDetail.getOwners();
                owners.forEach(owner -> {
                    owner.setId(UUID.randomUUID().toString());
                    owner.setTenantId(tenantId);
                });
                Set<Unit> units =propertyDetail.getUnits();
                units.forEach(unit -> {
                    unit.setId(UUID.randomUUID().toString());
                    unit.setTenantId(tenantId);
                });
                Set<Document> documents = propertyDetail.getDocuments();
                documents.forEach(document ->{
                    document.setId(UUID.randomUUID().toString());
                });

                Long time = new Date().getTime();
                propertyDetail.setAssessmentDate(time);
            });


        }


        setIdgenIds(request);
    }

    /**
     * Assigns UUID for new fields that are added and sets propertyDetail and address ids from propertyId
     * @param request
     * @param propertiesFromResponse
     */

    public void enrichUpdateRequest(PropertyRequest request,List<Property> propertiesFromResponse) {

        RequestInfo requestInfo = request.getRequestInfo();
        AuditDetails auditDetails = propertyutil.getAuditDetails(requestInfo.getUserInfo().getId().toString(), false);
        String tenantId = request.getProperties().get(0).getTenantId();

        Map<String,Property> idToProperty = new HashMap<>();
        propertiesFromResponse.forEach(propertyFromResponse -> {
            idToProperty.put(propertyFromResponse.getPropertyId(),propertyFromResponse);
        });

        for (Property property : request.getProperties()){
            property.setAuditDetails(auditDetails);
            String id = property.getPropertyId();
            Property responseProperty = idToProperty.get(id);
            property.getAddress().setId(responseProperty.getAddress().getId());


            property.getPropertyDetails().forEach(propertyDetail -> {
                if (propertyDetail.getAssessmentNumber() == null) {
                    propertyDetail.setAssessmentNumber(UUID.randomUUID().toString());
                }
                Set<OwnerInfo> ownerInfos = propertyDetail.getOwners();
                Set<Document> documents = propertyDetail.getDocuments();
                Set<Unit> units=propertyDetail.getUnits();

                if(ownerInfos!=null && !ownerInfos.isEmpty()) {
                    ownerInfos.forEach(owner -> {
                        if (owner.getId() == null) {
                            owner.setId(UUID.randomUUID().toString());
                            owner.setTenantId(tenantId);
                        }
                    });
                }

                if(documents!=null && !documents.isEmpty()){
                    documents.forEach(document ->{
                        if(document.getId()==null){
                            document.setId(UUID.randomUUID().toString());
                        }
                    });
                }

                if(units!=null && !units.isEmpty()){
                    units.forEach(unit ->{
                        if(unit.getId()==null){
                            unit.setId(UUID.randomUUID().toString());
                            unit.setTenantId(tenantId);
                        }
                    });
                }
            });

        }
    }


    /**
     *
     * @param requestInfo
     * @param tenantId
     * @param idKey
     * @param idformat
     * @param count
     * @return
     */

    private List<String> getIdList(RequestInfo requestInfo, String tenantId, String idKey,
                                   String idformat,int count) {
        return idGenRepository.getId(requestInfo, tenantId, idKey, idformat,count).getIdResponses().stream()
                .map(IdResponse::getId).collect(Collectors.toList());
    }

    /**
     * Sets the acknowledgement and assessment Numbers for given PropertyRequest
     * @param request
     */

    public void setIdgenIds(PropertyRequest request)
    {   RequestInfo requestInfo = request.getRequestInfo();
        String tenantId = request.getProperties().get(0).getTenantId();
        List<Property> properties = request.getProperties();

        List<String> acknowledgementNumbers = getIdList(requestInfo,tenantId,config.getAcknowldgementIdGenName(),config.getAcknowldgementIdGenFormat(),request.getProperties().size());

        for(int i=0;i<acknowledgementNumbers.size();i++)
        {
            properties.get(i).setAcknowldgementNumber(acknowledgementNumbers.get(i));
        }

        List<String> propertyIds = getIdList(requestInfo,tenantId,config.getAcknowldgementIdGenName(),config.getAcknowldgementIdGenFormat(),request.getProperties().size());

        for(int i=0;i<propertyIds.size();i++)
        {
            properties.get(i).setPropertyId(propertyIds.get(i));
        }

       /* properties.forEach(property -> {
            List<PropertyDetail> propertyDetails = property.getPropertyDetails();
            List<String> assessmentNumbers = getIdList(requestInfo,tenantId,config.getAssessmentIdGenName(),config.getAssessmentIdGenFormat(),propertyDetails.size());
            for(int i=0;i<assessmentNumbers.size();i++)
            {
                propertyDetails.get(i).setAssessmentNumber(assessmentNumbers.get(i));
            }

        });*/
    }





}

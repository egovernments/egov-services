package org.egov.pt.service;

import org.dozer.DozerBeanMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pt.config.PropertyConfiguration;
import org.egov.pt.repository.IdGenRepository;
import org.egov.pt.util.PTConstants;
import org.egov.pt.util.PropertyUtil;
import org.egov.pt.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.dozer.DozerBeanMapper;

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

        for (Property property : request.getProperties()) {
            property.getAddress().setId(UUID.randomUUID().toString());
            property.setAuditDetails(auditDetails);

            property.getPropertyDetails().forEach(propertyDetail -> {
                propertyDetail.setAssessmentNumber(UUID.randomUUID().toString());
              /*  Set<OwnerInfo> owners = propertyDetail.getOwners();
                owners.forEach(owner -> {
                    owner.setId(UUID.randomUUID().toString());
                });*/
                Set<Unit> units =propertyDetail.getUnits();
                units.forEach(unit -> {
                    unit.setId(UUID.randomUUID().toString());
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

               /* if(ownerInfos!=null && !ownerInfos.isEmpty()) {
                    ownerInfos.forEach(owner -> {
                        if (owner.getId() == null) {
                            owner.setId(UUID.randomUUID().toString());
                        }
                    });
                }*/
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

    /**
     * Populates the owner fields inside of property objects from the response got from calling user api
     * @param userDetailResponse
     * @param properties
     */


    public void enrichOwner(UserDetailResponse userDetailResponse, List<Property> properties){
        List<OwnerInfo> users = userDetailResponse.getUser();
        Map<Long,OwnerInfo> userIdToOwnerMap = new HashMap<>();
        users.forEach(user -> {
            userIdToOwnerMap.put(user.getId(),user);
        });
        properties.forEach(property -> {
            property.getPropertyDetails().forEach(propertyDetail -> {
                propertyDetail.getOwners().forEach(owner -> {
                    addOwnerDetail(owner,userIdToOwnerMap);
                });

            });
        });
    }

   /* private void addOwnerDetail(OwnerInfo owner,Map<Long,OwnerInfo> userIdToOwnerMap){
        OwnerInfo user = userIdToOwnerMap.get(owner.getId());
        owner.setLastModifiedDate(user.getLastModifiedDate());
        owner.setLastModifiedDate(user.getLastModifiedDate());
        owner.setCreatedBy(user.getCreatedBy());
        owner.setCreatedDate(user.getCreatedDate());
        owner.setUserName(user.getUserName());
        owner.setPassword(user.getPassword());
        owner.setSalutation(user.getSalutation());
        owner.setName(user.getName());
        owner.setGender(user.getGender());
        owner.setMobileNumber(user.getMobileNumber());
        owner.setEmailId(user.getEmailId());
        owner.setAltContactNumber(user.getAltContactNumber());
        owner.setPan(user.getPan());
        owner.setAadhaarNumber(user.getAadhaarNumber());
        owner.setPermanentAddress(user.getPermanentAddress());
        owner.setPermanentCity(user.getPermanentCity());
        owner.setPermanentPincode(user.getPermanentPincode());
        owner.setCorrespondenceAddress(user.getCorrespondenceAddress());
        owner.setCorrespondenceCity(user.getCorrespondenceCity());
        owner.setCorrespondencePincode(user.getCorrespondencePincode());
        owner.setActive(user.getActive());
        owner.setDob(user.getDob());
        owner.setPwdExpiryDate(user.getPwdExpiryDate());
        owner.setLocale(user.getLocale());
        owner.setType(user.getType());
        owner.setAccountLocked(user.getAccountLocked());
        owner.setRoles(user.getRoles());
        owner.setFatherOrHusbandName(user.getFatherOrHusbandName());
        owner.setBloodGroup(user.getBloodGroup());
        owner.setIdentificationMark(user.getIdentificationMark());
        owner.setPhoto(user.getPhoto());
    }*/


    /**
     * Copies all fields to owner from the corresponding ownerInfo from userIdToOwnerMap
     * @param owner Owner whose fields are to be populated
     * @param userIdToOwnerMap Map of userId to OwnerInfo
     */
    private void addOwnerDetail(OwnerInfo owner,Map<Long,OwnerInfo> userIdToOwnerMap){
        OwnerInfo user = userIdToOwnerMap.get(owner.getId());
        DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
        dozerBeanMapper.map(user,owner);
    }

    public void enrichPropertyCriteria(PropertyCriteria criteria,UserDetailResponse userDetailResponse){
        if(CollectionUtils.isEmpty(criteria.getOwnerids())){
            List<Long> ownerids = new ArrayList<>();
            userDetailResponse.getUser().forEach(owner -> {
                ownerids.add(owner.getId());
            });
            criteria.setOwnerids(ownerids);
        }
    }


}

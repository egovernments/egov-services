package org.egov.pt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pt.config.PropertyConfiguration;
import org.egov.pt.producer.Producer;
import org.egov.pt.repository.PropertyRepository;
import org.egov.pt.util.PropertyUtil;
import org.egov.pt.util.ResponseInfoFactory;
import org.egov.pt.web.models.AuditDetails;
import org.egov.pt.web.models.Document;
import org.egov.pt.web.models.OwnerInfo;
import org.egov.pt.web.models.Property;
import org.egov.pt.web.models.PropertyCriteria;
import org.egov.pt.web.models.PropertyDetail;
import org.egov.pt.web.models.PropertyRequest;
import org.egov.pt.web.models.Unit;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class PropertyService {

	@Autowired
	private Producer producer;

	@Autowired
	private PropertyConfiguration config;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private PropertyRepository repository;

	@Autowired
	PropertyUtil propertyutil;

	public List<Property> createProperty(PropertyRequest request) {
		enrichCreateRequest(request);
		producer.push(config.getSavePropertyTopic(), request);
		return request.getProperties();
	}

	private void enrichCreateRequest(PropertyRequest request) {

		RequestInfo requestInfo = request.getRequestInfo();
		AuditDetails auditDetails = propertyutil.getAuditDetails(requestInfo.getUserInfo().getId().toString(), true);

		for (Property property : request.getProperties()) {
			property.setId(UUID.randomUUID().toString());
			property.getAddress().setId(UUID.randomUUID().toString());
			property.setAuditDetails(auditDetails);
			property.getPropertyDetail().setId(UUID.randomUUID().toString());
			Set<OwnerInfo> owners = property.getOwners();
			owners.forEach(owner -> {
				owner.setId(UUID.randomUUID().toString());
			});
			Set<Unit> units = property.getPropertyDetail().getUnits();
			units.forEach(unit -> {
				unit.setId(UUID.randomUUID().toString());
			});
			Set<Document> documents = property.getPropertyDetail().getDocuments();
			documents.forEach(document ->{
				document.setId(UUID.randomUUID().toString());
			});

		}
	}

	public List<Property> searchProperty(PropertyCriteria criteria) {

		List<Property> properties = repository.getProperties(criteria);
		return properties;
	}


	public List<Property> updateProperty(PropertyRequest request) {
		List<Property> responseProperties = getResponseProperties(request);
        boolean ifPropertyExists=PropertyExists(request,responseProperties);
        if(ifPropertyExists) {
			enrichUpdateRequest(request,responseProperties);
			producer.push(config.getUpdatePropertyTopic(), request);
			return request.getProperties();
		}
		else
		{    throw new CustomException("usr_002","invalid id");  // Change the error code

		}
	}


	private void enrichUpdateRequest(PropertyRequest request,List<Property> propertiesFromResponse) {

		RequestInfo requestInfo = request.getRequestInfo();
		AuditDetails auditDetails = propertyutil.getAuditDetails(requestInfo.getUserInfo().getId().toString(), false);

		Map<String,Property> idToProperty = new HashMap<>();
        propertiesFromResponse.forEach(propertyFromResponse -> {
			idToProperty.put(propertyFromResponse.getId(),propertyFromResponse);
		});

		for (Property property : request.getProperties()){
			property.setAuditDetails(auditDetails);
			String id = property.getId();
			Property responseProperty = idToProperty.get(id);

			property.getPropertyDetail().setId(responseProperty.getPropertyDetail().getId());
			property.getAddress().setId(responseProperty.getAddress().getId());


			Set<OwnerInfo> ownerInfos = property.getOwners();
			Set<Document> documents = property.getPropertyDetail().getDocuments();
			Set<Unit> units=property.getPropertyDetail().getUnits();

			if(ownerInfos!=null && !ownerInfos.isEmpty()) {
				ownerInfos.forEach(owner -> {
					if (owner.getId() == null) {
						owner.setId(UUID.randomUUID().toString());
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
					}
				});
			}

		 }
	}


	private List<Property> getResponseProperties(PropertyRequest request) {

		RequestInfo requestInfo = request.getRequestInfo();
		List<Property> properties=request.getProperties();
		Set<String> ids = new HashSet<>();
        Set<String> propertyDetailids = new HashSet<>();
        Set<String> unitids = new HashSet<>();
        Set<String> documentids = new HashSet<>();
        Set<String> ownerids = new HashSet<>();
        Set<String> addressids = new HashSet<>();

		PropertyCriteria propertyCriteria = new PropertyCriteria();

		properties.forEach(property -> {
                 ids.add(property.getId());
                 if(!CollectionUtils.isEmpty(ids)) {
                     if(property.getPropertyDetail().getId()!=null)
                      propertyDetailids.add(property.getPropertyDetail().getId());
                     if(property.getAddress().getId()!=null)
                      addressids.add(property.getAddress().getId());
                     if(!CollectionUtils.isEmpty(property.getOwners()))
                        ownerids.addAll(getOwnerids(property));
                     if(!CollectionUtils.isEmpty(property.getPropertyDetail().getDocuments()))
                        documentids.addAll(getDocumentids(property.getPropertyDetail()));
                     if(!CollectionUtils.isEmpty(property.getPropertyDetail().getUnits())) {
                        unitids.addAll(getUnitids(property.getPropertyDetail()));
                     }
                 }
				}
		);

		propertyCriteria.setTenantId(properties.get(0).getTenantId());
		propertyCriteria.setIds(ids);
		propertyCriteria.setPropertyDetailids(propertyDetailids);
        propertyCriteria.setAddressids(addressids);
        propertyCriteria.setOwnerids(ownerids);
        propertyCriteria.setUnitids(unitids);
        propertyCriteria.setDocumentids(documentids);

		List<Property> responseProperties = searchProperty(propertyCriteria);
		return responseProperties;
	}


	private Set<String> getOwnerids(Property property){
        Set<OwnerInfo> owners= property.getOwners();
        Set<String> ownerIds = new HashSet<>();
        owners.forEach(owner -> {
            if(owner.getId()!=null)
             ownerIds.add(owner.getId());
        });
        return ownerIds;
    }

	private Set<String> getUnitids(PropertyDetail propertyDetail){
	    Set<Unit> units= propertyDetail.getUnits();
	    Set<String> unitIds = new HashSet<>();
	    units.forEach(unit -> {
	        if(unit.getId()!=null)
	         unitIds.add(unit.getId());
        });
	    return unitIds;
    }



	private Set<String> getDocumentids(PropertyDetail propertyDetail){
        Set<Document> documents= propertyDetail.getDocuments();
        Set<String> documentIds = new HashSet<>();
        documents.forEach(document -> {
			if(document.getId()!=null)
            documentIds.add(document.getId());
        });
        return documentIds;
    }

    private boolean PropertyExists(PropertyRequest request,List<Property> responseProperties){

		List<String> responseids = new ArrayList<>();
		List<String> requestids = new ArrayList<>();

		request.getProperties().forEach(property -> {
			requestids.add(property.getId());
		});

		responseProperties.forEach(property -> {
			responseids.add(property.getId());
		});
		return listEqualsIgnoreOrder(responseids,requestids);
	}



	private static <T> boolean listEqualsIgnoreOrder(List<T> list1, List<T> list2) {
		return new HashSet<>(list1).equals(new HashSet<>(list2));
	}




	}
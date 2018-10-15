package org.egov.tl.validator;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tl.config.TLConfiguration;
import org.egov.tl.repository.TLRepository;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.web.models.TradeLicense;
import org.egov.tl.web.models.TradeLicenseRequest;
import org.egov.tl.web.models.TradeLicenseSearchCriteria;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Component
public class TLValidator {


    private TLRepository tlRepository;

    private TLConfiguration config;

    private PropertyValidator propertyValidator;

    private MDMSValidator mdmsValidator;



    @Autowired
    public TLValidator(TLRepository tlRepository, TLConfiguration config,
                       PropertyValidator propertyValidator, MDMSValidator mdmsValidator) {
        this.tlRepository = tlRepository;
        this.config = config;
        this.propertyValidator = propertyValidator;
        this.mdmsValidator = mdmsValidator;
    }



    public void validateCreate(TradeLicenseRequest request){
        validateInstitution(request);
        propertyValidator.validateProperty(request);
        mdmsValidator.validateMdmsData(request);
    }


    private void validateInstitution(TradeLicenseRequest request){
        List<TradeLicense> licenses = request.getLicenses();
        licenses.forEach(license -> {
            if(license.getTradeLicenseDetail().getInstitution()!=null &&
                    !license.getTradeLicenseDetail().getSubOwnerShipCategory().contains(config.getInstitutional()))
                throw new CustomException("INVALID REQUEST","The institution object should be null for ownershipCategory "
                        +license.getTradeLicenseDetail().getSubOwnerShipCategory());

            if(license.getTradeLicenseDetail().getInstitution()==null &&
                    license.getTradeLicenseDetail().getSubOwnerShipCategory().contains(config.getInstitutional()))
                throw new CustomException("INVALID REQUEST","The institution object cannot be null for ownershipCategory "
                        +license.getTradeLicenseDetail().getSubOwnerShipCategory());

        });
    }



    public void validateUpdate(TradeLicenseRequest request){
      List<TradeLicense> licenses = request.getLicenses();

      Map<String,List<String>> tenantIdToIds= new HashMap<>();

      // Map of tenantId to tradeLicenses created
      request.getLicenses().forEach(license -> {
          if(tenantIdToIds.containsKey(license.getTenantId()))
              tenantIdToIds.get(license.getTenantId()).add(license.getId());
          else {
              List<String> perTenantIds = new LinkedList<>();
              perTenantIds.add(license.getId());
              tenantIdToIds.put(license.getTenantId(),perTenantIds);
          }
      });

      List<TradeLicense> searchResult = new LinkedList<>();
        tenantIdToIds.keySet().forEach(key -> {
                addTradeLicenseFromSearch(key,tenantIdToIds.get(key),searchResult);
      });

      if(searchResult.size()!=licenses.size())
          throw new CustomException("INVALID UPDATE","The license to be updated is not in database");

      validateAllIds(searchResult,licenses);
      mdmsValidator.validateMdmsData(request);

      setFieldsFromSearch(request,searchResult);
   }


   private void addTradeLicenseFromSearch(String tenantId,List<String> ids,
                                          List<TradeLicense> searchResult){
         TradeLicenseSearchCriteria criteria = new TradeLicenseSearchCriteria();
         criteria.setTenantId(tenantId);
         criteria.setIds(ids);
         searchResult.addAll(tlRepository.getLicenses(criteria));
   }


    private List<String> getOwnerIds(TradeLicense license){
        List<String> ownerIds = new LinkedList<>();
        if(!CollectionUtils.isEmpty(license.getTradeLicenseDetail().getOwners())){
            license.getTradeLicenseDetail().getOwners().forEach(owner -> {
                ownerIds.add(owner.getUuid());
            });
        }
        return ownerIds;
    }

    private List<String> getTradeUnitIds(TradeLicense license){
        List<String> tradeUnitIds = new LinkedList<>();
        if(!CollectionUtils.isEmpty(license.getTradeLicenseDetail().getTradeUnits())){
            license.getTradeLicenseDetail().getTradeUnits().forEach(tradeUnit -> {
                tradeUnitIds.add(tradeUnit.getId());
            });
        }
        return tradeUnitIds;
    }

    private List<String> getAccessoryIds(TradeLicense license){
        List<String> accessoryIds = new LinkedList<>();
        if(!CollectionUtils.isEmpty(license.getTradeLicenseDetail().getAccessories())){
            license.getTradeLicenseDetail().getAccessories().forEach(accessory -> {
                accessoryIds.add(accessory.getId());
            });
        }
        return accessoryIds;
    }

    private List<String> getOwnerDocIds(TradeLicense license){
        List<String> ownerDocIds = new LinkedList<>();
        if(!CollectionUtils.isEmpty(license.getTradeLicenseDetail().getOwners())){
            license.getTradeLicenseDetail().getOwners().forEach(owner -> {
                if(!CollectionUtils.isEmpty(owner.getDocuments())){
                    owner.getDocuments().forEach(document -> {
                        ownerDocIds.add(document.getId());
                    });
                }
            });
        }
        return ownerDocIds;
    }

    private List<String> getApplicationDocIds(TradeLicense license){
        List<String> applicationDocIds = new LinkedList<>();
        if(!CollectionUtils.isEmpty(license.getTradeLicenseDetail().getApplicationDocuments())){
            license.getTradeLicenseDetail().getApplicationDocuments().forEach(document -> {
                applicationDocIds.add(document.getId());
            });
        }
        return applicationDocIds;
    }

    private List<String> getVerficationDocIds(TradeLicense license){
        List<String> verficationDocIds = new LinkedList<>();
        if(!CollectionUtils.isEmpty(license.getTradeLicenseDetail().getVerificationDocuments())) {
            license.getTradeLicenseDetail().getVerificationDocuments().forEach(document -> {
                verficationDocIds.add(document.getId());
            });
        }
        return verficationDocIds;
    }


    private void setFieldsFromSearch(TradeLicenseRequest request,List<TradeLicense> searchResult){
        Map<String,TradeLicense> idToTradeLicenseFromSearch = new HashMap<>();
        searchResult.forEach(tradeLicense -> {
            idToTradeLicenseFromSearch.put(tradeLicense.getId(),tradeLicense);
        });
        request.getLicenses().forEach(license -> {
            license.setStatus(idToTradeLicenseFromSearch.get(license.getId()).getStatus());
            license.setLicenseNumber(idToTradeLicenseFromSearch.get(license.getId()).getLicenseNumber());
        });
    }

    private void validateAllIds(List<TradeLicense> searchResult,List<TradeLicense> licenses){

        Map<String,TradeLicense> idToTradeLicenseFromSearch = new HashMap<>();
        searchResult.forEach(tradeLicense -> {
            idToTradeLicenseFromSearch.put(tradeLicense.getId(),tradeLicense);
        });

        Map<String,String> errorMap = new HashMap<>();
        licenses.forEach(license -> {
            TradeLicense searchedLicense = idToTradeLicenseFromSearch.get(license.getId());
            if(!searchedLicense.getTradeLicenseDetail().getId().
                    equalsIgnoreCase(license.getTradeLicenseDetail().getId()))
                errorMap.put("INVALID UPDATE","The id "+license.getTradeLicenseDetail().getId()+" does not exist");

            if(!searchedLicense.getTradeLicenseDetail().getAddress().getId().
                    equalsIgnoreCase(license.getTradeLicenseDetail().getAddress().getId()))
                errorMap.put("INVALID UPDATE","The id "+license.getTradeLicenseDetail().getAddress().getId()+" does not exist");

            compareIdList(getTradeUnitIds(searchedLicense),getTradeUnitIds(license),errorMap);
            compareIdList(getAccessoryIds(searchedLicense),getAccessoryIds(license),errorMap);
            compareIdList(getOwnerIds(searchedLicense),getOwnerIds(license),errorMap);
            compareIdList(getOwnerDocIds(searchedLicense),getOwnerDocIds(license),errorMap);
            compareIdList(getApplicationDocIds(searchedLicense),getApplicationDocIds(license),errorMap);
            compareIdList(getVerficationDocIds(searchedLicense),getVerficationDocIds(license),errorMap);
        });

        if(!CollectionUtils.isEmpty(errorMap))
            throw new CustomException(errorMap);
    }


    private void compareIdList(List<String> searchIds,List<String> updateIds,Map<String,String> errorMap){
        if(!CollectionUtils.isEmpty(searchIds))
            updateIds.forEach(id -> {
                if(id!=null && !searchIds.contains(id))
                    errorMap.put("INVALID UPDATE","The id "+id+" does not exist in database");
            });
    }


    public void validateSearch(RequestInfo requestInfo,TradeLicenseSearchCriteria criteria){
        if(!requestInfo.getUserInfo().getType().equalsIgnoreCase("CITIZEN" )&& criteria.tenantIdOnly()==true)
            throw new CustomException("INVALID SEARCH","Search based only on tenantId is not allowed");
    }





}



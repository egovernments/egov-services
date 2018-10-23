package org.egov.tl.validator;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.tl.repository.ServiceRequestRepository;
import org.egov.tl.util.TLConstants;
import org.egov.tl.util.TradeUtil;
import org.egov.tl.web.models.TradeLicenseRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Component
@Slf4j
public class MDMSValidator {


    private ServiceRequestRepository requestRepository;

    private TradeUtil util;


    @Autowired
    public MDMSValidator(ServiceRequestRepository requestRepository, TradeUtil util) {
        this.requestRepository = requestRepository;
        this.util = util;
    }





    /**
     * method to validate the mdms data in the request
     *
     * @param licenseRequest
     */
    public void validateMdmsData(TradeLicenseRequest licenseRequest) {

        Map<String, String> errorMap = new HashMap<>();

        String tenantId = licenseRequest.getLicenses().get(0).getTenantId();
        RequestInfo requestInfo = licenseRequest.getRequestInfo();

        Map<String, List<String>> masterData = getAttributeValues(tenantId, requestInfo);

        Map<String, List<String>> uomMasterData = getUomMap(tenantId, requestInfo);

        String[] masterArray = { TLConstants.ACCESSORIES_CATEGORY, TLConstants.TRADE_TYPE,
                                 TLConstants.OWNERSHIP_CATEGORY, TLConstants.STRUCTURE_TYPE};

        validateIfMasterPresent(masterArray, masterData);

        licenseRequest.getLicenses().forEach(license -> {

            if(!masterData.get(TLConstants.OWNERSHIP_CATEGORY)
                    .contains(license.getTradeLicenseDetail().getSubOwnerShipCategory()))
                errorMap.put("INVALID OWNERSHIPCATEGORY", "The SubOwnerShipCategory '"
                        + license.getTradeLicenseDetail().getSubOwnerShipCategory() + "' does not exists");

            if(!masterData.get(TLConstants.STRUCTURE_TYPE).
                    contains(license.getTradeLicenseDetail().getStructureType()))
                errorMap.put("INVALID STRUCTURETYPE", "The structureType '"
                        + license.getTradeLicenseDetail().getStructureType() + "' does not exists");

               license.getTradeLicenseDetail().getTradeUnits().forEach(unit -> {
                   if (!masterData.get(TLConstants.TRADE_TYPE).contains(unit.getTradeType()))
                    errorMap.put("INVALID TRADETYPE", "The Trade type '" + unit.getTradeType() + "' does not exists");

                    if(unit.getUom()!=null){
                       int index = masterData.get(TLConstants.TRADE_TYPE).indexOf(unit.getTradeType());
                       if(!unit.getUom().equalsIgnoreCase(uomMasterData.get(TLConstants.TRADE_TYPE).get(index)))
                           errorMap.put("INVALID UOM","The UOM: "+unit.getUom()+" is not valid for tradeType: "+unit.getTradeType());
                       else if(unit.getUom().equalsIgnoreCase(uomMasterData.get(TLConstants.TRADE_TYPE).get(index))
                               && unit.getUomValue()==null)
                           throw new CustomException("INVALID UOMVALUE","The uomValue cannot be null");
                   }

                   else if(unit.getUom()==null){
                       int index = masterData.get(TLConstants.TRADE_TYPE).indexOf(unit.getTradeType());
                       if(uomMasterData.get(TLConstants.TRADE_TYPE).get(index)!=null)
                           errorMap.put("INVALID UOM","The UOM cannot be null for tradeType: "+unit.getTradeType());
                   }
                });

               if(!CollectionUtils.isEmpty(license.getTradeLicenseDetail().getAccessories())){
                    license.getTradeLicenseDetail().getAccessories().forEach(accessory -> {
                        if (!masterData.get(TLConstants.ACCESSORIES_CATEGORY).contains(accessory.getAccessoryCategory()))
                            errorMap.put("INVALID ACCESORRYCATEGORY",
                                    "The Accessory Category '" + accessory.getAccessoryCategory() + "' does not exists");

                         if(accessory.getUom()!=null){
                            int index = masterData.get(TLConstants.ACCESSORIES_CATEGORY).indexOf(accessory.getAccessoryCategory());
                            if(!accessory.getUom().equalsIgnoreCase(uomMasterData.get(TLConstants.ACCESSORIES_CATEGORY).get(index)))
                                errorMap.put("INVALID UOM","The UOM: "+accessory.getUom()+" is not valid for accessoryCategory: "
                                        +accessory.getAccessoryCategory());
                            else if(accessory.getUom().equalsIgnoreCase(uomMasterData.get(TLConstants.ACCESSORIES_CATEGORY).get(index))
                                    && accessory.getUomValue()==null)
                                throw new CustomException("INVALID UOMVALUE","The uomValue cannot be null");
                        }

                        else if(accessory.getUom()==null){
                            int index = masterData.get(TLConstants.ACCESSORIES_CATEGORY).indexOf(accessory.getAccessoryCategory());
                            if(uomMasterData.get(TLConstants.ACCESSORIES_CATEGORY).get(index)!=null)
                                errorMap.put("INVALID UOM","The UOM cannot be null for tradeType: "+accessory.getAccessoryCategory());
                        }
                    });
               }

        });

        if (!CollectionUtils.isEmpty(errorMap))
            throw new CustomException(errorMap);
    }



    /**
     * Validates if MasterData is properly fetched for the given MasterData names
     * @param masterNames
     * @param codes
     */
    private void validateIfMasterPresent(String[] masterNames,Map<String,List<String>> codes){
        Map<String,String> errorMap = new HashMap<>();
        for(String masterName:masterNames){
            if(CollectionUtils.isEmpty(codes.get(masterName))){
                errorMap.put("MDMS DATA ERROR ","Unable to fetch "+masterName+" codes from MDMS");
            }
        }
        if (!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }




    /**
     * Fetches all the values of particular attribute as map of field name to list
     *
     * takes all the masters from each module and adds them in to a single map
     *
     * note : if two masters from different modules have the same name then it
     *
     *  will lead to overriding of the earlier one by the latest one added to the map
     *
     * @param tenantId    tenantId of properties in PropertyRequest
     * @param requestInfo RequestInfo of the received PropertyRequest
     * @return Map of MasterData name to the list of code in the MasterData
     *
     */
    private Map<String, List<String>> getAttributeValues(String tenantId, RequestInfo requestInfo) {

        List<String> modulepaths = Arrays.asList(TLConstants.TL_JSONPATH_CODE,
                TLConstants.COMMON_MASTER_JSONPATH_CODE);

        StringBuilder uri = util.getMdmsSearchUrl();
        MdmsCriteriaReq criteriaReq = util.getTradeModuleRequest(requestInfo, tenantId);
        final Map<String, List<String>> mdmsResMap = new HashMap<>();


        modulepaths.forEach( modulepath -> {
            try {
                Object result = requestRepository.fetchResult(uri, criteriaReq);
                mdmsResMap.putAll(JsonPath.read(result, modulepath));
            } catch (Exception e) {
                log.error("Error while fetvhing MDMS data", e);
                throw new CustomException(TLConstants.INVALID_TENANT_ID_MDMS_KEY, TLConstants.INVALID_TENANT_ID_MDMS_MSG);
            }
        });

        System.err.println(" the mdms response is : " + mdmsResMap);
        return mdmsResMap;
    }


    /**
     * Fetches map of UOM to UOMValues
     * @param tenantId tenantId of the tradeLicense
     * @param requestInfo The requestInfo of the request
     * @return
     */
    private Map<String, List<String>> getUomMap(String tenantId, RequestInfo requestInfo) {

        List<String> modulepaths = Arrays.asList(TLConstants.TL_JSONPATH_CODE);

        StringBuilder uri = util.getMdmsSearchUrl();
        MdmsCriteriaReq criteriaReq = util.getTradeUomRequest(requestInfo, tenantId);
        final Map<String, List<String>> mdmsResMap = new HashMap<>();

        modulepaths.forEach( modulepath -> {
            try {
                Object result = requestRepository.fetchResult(uri, criteriaReq);
                mdmsResMap.putAll(JsonPath.read(result, modulepath));
            } catch (Exception e) {
                log.error("Error while fetvhing MDMS data", e);
                throw new CustomException(TLConstants.INVALID_TENANT_ID_MDMS_KEY, TLConstants.INVALID_TENANT_ID_MDMS_MSG);
            }
        });

        System.err.println(" the mdms response is : " + mdmsResMap);
        return mdmsResMap;
    }














}

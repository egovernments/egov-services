package org.egov.pt.validator;

import com.jayway.jsonpath.JsonPath;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.pt.repository.ServiceRequestRepository;
import org.egov.pt.util.ErrorConstants;
import  org.egov.pt.util.PTConstants;
import org.egov.pt.util.PropertyUtil;
import org.egov.pt.web.models.*;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class PropertyValidator {


    @Autowired
    private PropertyUtil propertyUtil;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Value("${egov.mdms.host}")
    private String mdmsHost;

    @Value("${egov.mdms.search.endpoint}")
    private String mdmsEndpoint;

    /**
     * Validates if the PropertyRequest contains RequestInfo and the RequestInfo contains UserInfo
     *
     * @param request
     * @param errorMap
     */
     public void validateRequestInfo(PropertyRequest request,Map<String,String> errorMap)
     {

         if(request.getRequestInfo()==null){
             errorMap.put(ErrorConstants.EG_PT_REQUESTINFO_Key,ErrorConstants.EG_PT_REQUESTINFO_MSG);
         }
         else if(request.getRequestInfo().getUserInfo()==null){
             errorMap.put("EG_PT_REQUESTINFO_USERINFO","UserInfo is mandatory for RequestInfo");
         }
         if (!errorMap.isEmpty())
             throw new CustomException(errorMap);

     }

    /**
     * Validates if the fields in PropertyRequest are present in the MDMS master Data
     *
     * @param request
     *
     *
     */
    public void validateMasterData(PropertyRequest request){
      Map<String,String> errorMap = new HashMap<>();
      String tenantId = request.getProperties().get(0).getTenantId();

      String[] masterNames = {PTConstants.MDMS_PT_CONSTRUCTIONSUBTYPE, PTConstants.MDMS_PT_CONSTRUCTIONTYPE, PTConstants.MDMS_PT_OCCUPANCYTYPE,
      PTConstants.MDMS_PT_PROPERTYTYPE,PTConstants.MDMS_PT_PROPERTYSUBTYPE,PTConstants.MDMS_PT_OWNERSHIP,PTConstants.MDMS_PT_SUBOWNERSHIP,
      PTConstants.MDMS_PT_USAGEMAJOR,PTConstants.MDMS_PT_USAGEMINOR,PTConstants.MDMS_PT_USAGESUBMINOR,PTConstants.MDMS_PT_USAGEDETAIL,
      PTConstants.MDMS_PT_OWNERTYPE};
      List<String> names = new ArrayList<>();
      names.addAll(Arrays.asList(masterNames));

    //  validateFinancialYear(request,errorMap);
      validateInstitution(request,errorMap);
      Map<String,List<String>> codes = getAttributeValues(tenantId,PTConstants.MDMS_PT_MOD_NAME,names,"$.*.code",request.getRequestInfo());
      validateMDMSData(masterNames,codes);
      validateCodes(request.getProperties(),codes,errorMap);

        if (!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }

    /**
     *Fetches all the values of particular attribute as map of fieldname to list
     *
     * @param tenantId
     * @param names
     * @param requestInfo
     * @return
     *
     */
    public Map<String,List<String>> getAttributeValues(String tenantId, String moduleName, List<String> names, String filter, RequestInfo requestInfo){
        StringBuilder uri = new StringBuilder(mdmsHost).append(mdmsEndpoint);
        MdmsCriteriaReq criteriaReq = propertyUtil.prepareMdMsRequest(tenantId,moduleName,names,filter,requestInfo);
        try {
            Object result = serviceRequestRepository.fetchResult(uri, criteriaReq);
            return JsonPath.read(result,PTConstants.JSONPATH_CODES);
        } catch (Exception e) {
            throw new CustomException(ErrorConstants.INVALID_TENANT_ID_MDMS_KEY,
                    ErrorConstants.INVALID_TENANT_ID_MDMS_MSG);
        }
    }

    /**
     *Checks if the codes of all fields are in the list of codes obtain from master data
     *
     * @param properties
     * @param codes
     * @param errorMap
     * @return
     *
     */
   public static Map<String,String> validateCodes(List<Property> properties,Map<String,List<String>> codes,Map<String,String> errorMap){
       properties.forEach(property -> {
           property.getPropertyDetails().forEach(propertyDetail -> {

       if(!codes.get(PTConstants.MDMS_PT_PROPERTYTYPE).contains(propertyDetail.getPropertyType()) && propertyDetail.getPropertyType()!=null){
       errorMap.put("Invalid PropertyType","The PropertyType '"+propertyDetail.getPropertyType()+"' does not exists");
       }

       if(!codes.get(PTConstants.MDMS_PT_SUBOWNERSHIP).contains(propertyDetail.getSubOwnershipCategory()) && propertyDetail.getSubOwnershipCategory()!=null){
           errorMap.put("Invalid SubOwnershipCategory","The SubOwnershipCategory '"+propertyDetail.getSubOwnershipCategory()+"' does not exists");
       }

       if(!codes.get(PTConstants.MDMS_PT_OWNERSHIP).contains(propertyDetail.getOwnershipCategory()) && propertyDetail.getOwnershipCategory()!=null){
           errorMap.put("Invalid OwnershipCategory","The OwnershipCategory '"+propertyDetail.getOwnershipCategory()+"' does not exists");
       }

       if(!codes.get(PTConstants.MDMS_PT_PROPERTYSUBTYPE).contains(propertyDetail.getPropertySubType()) && propertyDetail.getPropertySubType()!=null){
           errorMap.put(ErrorConstants.INVALID_PROPERTYSUBTYPE,"The PropertySubType '"+propertyDetail.getPropertySubType()+"' does not exists");
       }

           if(!codes.get(PTConstants.MDMS_PT_USAGEMAJOR).contains(propertyDetail.getUsageCategoryMajor()) && propertyDetail.getUsageCategoryMajor()!=null){
               errorMap.put("Invalid UsageCategoryMajor","The UsageCategoryMajor '"+propertyDetail.getUsageCategoryMajor()+"' at Property level does not exists");
           }

               propertyDetail.getUnits().forEach(unit ->{
               if(!codes.get(PTConstants.MDMS_PT_USAGEMAJOR).contains(unit.getUsageCategoryMajor()) && unit.getUsageCategoryMajor()!=null){
                   errorMap.put("Invalid UsageCategoryMajor","The UsageCategoryMajor '"+unit.getUsageCategoryMajor()+"' at unit level does not exists");
               }

               if(!codes.get(PTConstants.MDMS_PT_USAGEMINOR).contains(unit.getUsageCategoryMinor()) && unit.getUsageCategoryMinor()!=null){
                   errorMap.put("Invalid UsageCategoryMinor","The UsageCategoryMinor '"+unit.getUsageCategoryMinor()+"' does not exists");
               }

               if(!codes.get(PTConstants.MDMS_PT_USAGESUBMINOR).contains(unit.getUsageCategorySubMinor()) && unit.getUsageCategorySubMinor()!=null){
                   errorMap.put("Invalid UsageCategorySubMinor","The UsageCategorySubMinor '"+unit.getUsageCategorySubMinor()+"' does not exists");
               }

               if(!codes.get(PTConstants.MDMS_PT_USAGEDETAIL).contains(unit.getUsageCategoryDetail()) && unit.getUsageCategoryDetail()!=null){
                   errorMap.put("Invalid UsageCategoryDetail","The UsageCategoryDetail "+unit.getUsageCategoryDetail()+" does not exists");
               }

               if(!codes.get(PTConstants.MDMS_PT_CONSTRUCTIONTYPE).contains(unit.getConstructionType()) && unit.getConstructionType()!=null){
                   errorMap.put("Invalid ConstructionType","The ConstructionType '"+unit.getConstructionType()+"' does not exists");
               }

               if(!codes.get(PTConstants.MDMS_PT_CONSTRUCTIONSUBTYPE).contains(unit.getConstructionSubType()) && unit.getConstructionSubType()!=null){
                   errorMap.put("Invalid ConstructionSubType","The ConstructionSubType '"+unit.getConstructionSubType()+"' does not exists");
               }

               if(!codes.get(PTConstants.MDMS_PT_OCCUPANCYTYPE).contains(unit.getOccupancyType()) && unit.getOccupancyType()!=null){
                   errorMap.put("Invalid OccupancyType","The OccupancyType '"+unit.getOccupancyType()+"' does not exists");
               }
           });

	       propertyDetail.getOwners().forEach(owner ->{
               if(!codes.get(PTConstants.MDMS_PT_OWNERTYPE).contains(owner.getOwnerType()) && owner.getOwnerType()!=null){
                   errorMap.put("Invalid OwnerType","The OwnerType '"+owner.getOwnerType()+"' does not exists");
               }
           });
	     });

       });
	   return errorMap;

   }


   private void validateMDMSData(String[] masterNames,Map<String,List<String>> codes){
       Map<String,String> errorMap = new HashMap<>();
       for(String masterName:masterNames){
               if(CollectionUtils.isEmpty(codes.get(masterName))){
               errorMap.put("MDMS data Error ","Unable to fetch "+masterName+" codes from MDMS");
            }
           }
       if (!errorMap.isEmpty())
           throw new CustomException(errorMap);
   }

    /**
     * Returns PropertyCriteria to search for properties in database with ids set from properties in request
     *
     * @param request
     * @return
     */
    public PropertyCriteria getPropertyCriteriaForSearch(PropertyRequest request) {

        RequestInfo requestInfo = request.getRequestInfo();
        List<Property> properties=request.getProperties();
        Set<String> ids = new HashSet<>();
        Set<String> propertyDetailids = new HashSet<>();
        Set<String> unitids = new HashSet<>();
        Set<String> documentids = new HashSet<>();
        Set<String> ownerids = new HashSet<>();
        Set<String> addressids = new HashSet<>();

        PropertyCriteria propertyCriteria = new PropertyCriteria();
      /*
      * Is search on ids other than propertyIds required?
      * */
        properties.forEach(property -> {
                    ids.add(property.getPropertyId());
                    if(!CollectionUtils.isEmpty(ids)) {
                        if (property.getAddress().getId() != null)
                            addressids.add(property.getAddress().getId());
                        property.getPropertyDetails().forEach(propertyDetail -> {
                            if (propertyDetail.getAssessmentNumber() != null)
                                propertyDetailids.add(propertyDetail.getAssessmentNumber());
                            if (!CollectionUtils.isEmpty(propertyDetail.getOwners()))
                                ownerids.addAll(getOwnerids(propertyDetail));
                            if (!CollectionUtils.isEmpty(propertyDetail.getDocuments()))
                                documentids.addAll(getDocumentids(propertyDetail));
                            if (!CollectionUtils.isEmpty(propertyDetail.getUnits())) {
                                unitids.addAll(getUnitids(propertyDetail));
                            }
                        });
                      }
                    });

        propertyCriteria.setTenantId(properties.get(0).getTenantId());
        propertyCriteria.setIds(ids);
        propertyCriteria.setPropertyDetailids(propertyDetailids);
        propertyCriteria.setAddressids(addressids);
        propertyCriteria.setOwnerids(ownerids);
        propertyCriteria.setUnitids(unitids);
        propertyCriteria.setDocumentids(documentids);

        return propertyCriteria;
    }

    /**
     *
     * @param propertyDetail
     * @return
     */
    private Set<String> getOwnerids(PropertyDetail propertyDetail){
        Set<OwnerInfo> owners= propertyDetail.getOwners();
        Set<String> ownerIds = new HashSet<>();
        owners.forEach(owner -> {
            if(owner.getUuid()!=null)
                ownerIds.add(owner.getUuid());
        });
        return ownerIds;
    }


    /**
     * Adds ids of all Units of property to a list
     * @param propertyDetail
     * @return List of all unitids of a propertyDetail
     */
    private Set<String> getUnitids(PropertyDetail propertyDetail){
        Set<Unit> units= propertyDetail.getUnits();
        Set<String> unitIds = new HashSet<>();
        units.forEach(unit -> {
            if(unit.getId()!=null)
                unitIds.add(unit.getId());
        });
        return unitIds;
    }


    /**
     * Adds ids of all Documents of property to a list
     * @param propertyDetail
     * @return List of all documentids of properyDetail
     */
    private Set<String> getDocumentids(PropertyDetail propertyDetail){
        Set<Document> documents= propertyDetail.getDocuments();
        Set<String> documentIds = new HashSet<>();
        documents.forEach(document -> {
            if(document.getId()!=null)
                documentIds.add(document.getId());
        });
        return documentIds;
    }

    /**
     * Checks if the property ids in search response are same as in request
     * @param request
     * @param responseProperties
     * @return
     */
    public boolean PropertyExists(PropertyRequest request,List<Property> responseProperties){

        List<String> responseids = new ArrayList<>();
        List<String> requestids = new ArrayList<>();

        request.getProperties().forEach(property -> {
            requestids.add(property.getPropertyId());
        });

        responseProperties.forEach(property -> {
            responseids.add(property.getPropertyId());
        });
        return listEqualsIgnoreOrder(responseids,requestids);
    }


    /**
     * Compares if two list contains same elements
     * @param list1
     * @param list2
     * @param <T>
     * @return
     */
    private static <T> boolean listEqualsIgnoreOrder(List<T> list1, List<T> list2) {
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }


    public void validateFinancialYear(PropertyRequest request,Map<String,String> errorMap){
      String tenantId = request.getProperties().get(0).getTenantId();
      RequestInfo requestInfo = request.getRequestInfo();
      request.getProperties().forEach(property ->
              property.getPropertyDetails().forEach(propertyDetail ->
              { // String filter = "$.FinancialYear[?(@.finYearRange == '"+propertyDetail.getFinancialYear()+"')].id";
                  String filter = "$.*.finYearRange";
                  Map<String,List<String>> years = getAttributeValues(tenantId,PTConstants.MDMS_PT_MOD_NAME,Arrays.asList("FinancialYear"),filter,requestInfo);
                  if(!years.get(PTConstants.MDMS_PT_FINANCIALYEAR).contains(propertyDetail.getFinancialYear()))
                  {
                      errorMap.put("Invalid FinancialYear","The finacialYearRange '"+propertyDetail.getFinancialYear()+"' is not valid");
                  }
              }
      ));
    }

    /**
     * Validates if institution Object has null InstitutionType
     * @param request PropertyRequest which is to be validated
     * @param errorMap ErrorMap to catch and to throw error using CustomException
     */
    private void validateInstitution(PropertyRequest request,Map<String,String> errorMap){
        List<Property> properties = request.getProperties();
        properties.forEach(property -> {
            property.getPropertyDetails().forEach(propertyDetail -> {
               if(propertyDetail.getInstitution()!=null){
                   if(propertyDetail.getInstitution().getType()==null)
                       errorMap.put(" INVALID INSTITUTION OBJECT ","The institutionType cannot be null ");
               }
               else if(!propertyDetail.getSubOwnershipCategory().equals("INSTITUTIONAL") && propertyDetail.getInstitution()!=null){
                   errorMap.put(" INVALID INSTITUTION OBJECT ","The institution Object should be null. Type of OwnerShip is not Institution ");
               }
            });
        });
    }



    public void validateAssessees(PropertyRequest request){
       String uuid = request.getRequestInfo().getUserInfo().getUuid();
        Map<String,String> errorMap = new HashMap<>();
        request.getProperties().forEach(property -> {
            property.getPropertyDetails().forEach(propertyDetail -> {
                if(!propertyDetail.getCitizenInfo().getUuid().equals(uuid)){
                    errorMap.put("UPDATE AUTHORIZATION FAILURE","Not Authorized to assess property with propertyId "+property.getPropertyId());
                }
            });
        });
        if(!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }


}

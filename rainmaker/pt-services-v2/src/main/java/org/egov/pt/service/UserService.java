package org.egov.pt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pt.repository.ServiceRequestRepository;
import org.egov.pt.web.models.*;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class UserService {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Value("${egov.user.host}")
    private String userHost;

    @Value("${egov.user.context.path}")
    private String userContextPath;

    @Value("${egov.user.create.path}")
    private String userCreateEndpoint;

    @Value("${egov.user.search.path}")
    private String userSearchEndpoint;

    @Value("${egov.user.update.path}")
    private String userUpdateEndpoint;

    /**
     * Creates user of the owners of property if it is not created already
     * @param request PropertyRequest received for creating properties
     */
    public void createUser(PropertyRequest request){
        StringBuilder uri = new StringBuilder(userHost).append(userContextPath).append(userCreateEndpoint);
        List<Property> properties = request.getProperties();
        RequestInfo requestInfo = request.getRequestInfo();
        properties.forEach(property -> {
            property.getPropertyDetails().forEach(propertyDetail -> {
                // Fetches the unique mobileNumbers from all the owners in the PropertyDetail
                Set<String> listOfMobileNumbers = getMobileNumbers(propertyDetail);
                propertyDetail.getOwners().forEach(owner -> {
                    if(owner.getUuid()==null){
                        // Checks if the user is already present based on name of the owner and mobileNumber
                        UserDetailResponse userDetailResponse = userExists(owner,requestInfo);
                        // If user not present new user is created
                        if(CollectionUtils.isEmpty(userDetailResponse.getUser()))
                        {   /* Sets userName equal to mobileNumber if mobileNumber already assigned as username
                          random number is assigned as username */
                            setUserName(owner,listOfMobileNumbers);
                            userDetailResponse = userCall(new CreateUserRequest(requestInfo,owner),uri);
                            log.info("owner created --> "+userDetailResponse.getUser().get(0).getUuid());
                        }
                        // Assigns value of fields from user got from userDetailResponse to owner object
                        setOwnerFields(owner,userDetailResponse);
                    }
                });
            });
        });
    }

    /**
     * Searches if the owner is already created. Search is based on name of owner, uuid and mobileNumber
     * @param owner Owner which is to be searched
     * @param requestInfo RequestInfo from the propertyRequest
     * @return UserDetailResponse containing the user if present and the responseInfo
     */
    private UserDetailResponse userExists(OwnerInfo owner,RequestInfo requestInfo){
        UserSearchRequest userSearchRequest =new UserSearchRequest();
        userSearchRequest.setTenantId(owner.getTenantId());
        userSearchRequest.setMobileNumber(owner.getMobileNumber());
        userSearchRequest.setName(owner.getName());
        userSearchRequest.setRequestInfo(requestInfo);
        userSearchRequest.setActive(false);
        if(owner.getUuid()!=null)
         userSearchRequest.setUuid(Arrays.asList(owner.getUuid()));
        StringBuilder uri = new StringBuilder(userHost).append(userSearchEndpoint);
        return userCall(userSearchRequest,uri);
    }

    /**
     * Sets userName for the owner as mobileNumber if mobileNumber already assigned last 10 digits of currentTime is assigned as userName
     * @param owner owner whose username has to be assigned
     * @param listOfMobileNumber list of unique mobileNumbers in the propertyRequest
     */
    private void setUserName(OwnerInfo owner,Set<String> listOfMobileNumber){
        if(listOfMobileNumber.contains(owner.getMobileNumber())){
            owner.setUserName(owner.getMobileNumber());
            // Once mobileNumber is set as userName it is removed from the list
            listOfMobileNumber.remove(owner.getMobileNumber());
        }
        else {
            String username = (Long.toString(new Date().getTime())).substring(3);
            owner.setUserName(username);
        }
    }

    /**
     * Fetches all the unique mobileNumbers from a propertyDetail
     * @param propertyDetail whose unique mobileNumbers are needed to be fetched
     * @return list of all unique mobileNumbers in the given propertyDetail
     */
    private Set<String> getMobileNumbers(PropertyDetail propertyDetail){
        Set<String> listOfMobileNumbers = new HashSet<>();
        propertyDetail.getOwners().forEach(owner -> {listOfMobileNumbers.add(owner.getMobileNumber());});
        return listOfMobileNumbers;
    }

    /**
     * Returns user using user search based on propertyCriteria(owner name,mobileNumber,userName)
     * @param criteria
     * @param requestInfo
     * @return serDetailResponse containing the user if present and the responseInfo
     */
    public UserDetailResponse getUser(PropertyCriteria criteria,RequestInfo requestInfo){
        UserSearchRequest userSearchRequest = getUserSearchRequest(criteria,requestInfo);
        StringBuilder uri = new StringBuilder(userHost).append(userSearchEndpoint);
        UserDetailResponse userDetailResponse = userCall(userSearchRequest,uri);
        return userDetailResponse;
    }

    /**
     * Returns UserDetailResponse by calling user service with given uri and object
     * @param userRequest Request object for user service
     * @param uri The address of the endpoint
     * @return Response from user service as parsed as userDetailResponse
     */
    private UserDetailResponse userCall(Object userRequest, StringBuilder uri) {
        String dobFormat = null;
        if(uri.toString().contains(userSearchEndpoint) || uri.toString().contains(userUpdateEndpoint))
            dobFormat="dd-MM-yyyy";
        else if(uri.toString().contains(userCreateEndpoint))
            dobFormat = "dd/MM/yyyy";
        try{
            LinkedHashMap responseMap = (LinkedHashMap)serviceRequestRepository.fetchResult(uri, userRequest);
            parseResponse(responseMap,dobFormat);
            ObjectMapper mapper = new ObjectMapper();
            UserDetailResponse userDetailResponse = mapper.convertValue(responseMap,UserDetailResponse.class);
            return userDetailResponse;
          }
          // Which Exception to throw?
        catch(Exception e)
        {
            log.error("uri does not contain appropriate endpoint");
            throw e;
        }
    }


    /**
     * Parses date formats to long for all users in responseMap
     * @param responeMap LinkedHashMap got from user api response
     * @param dobFormat dob format (required because dob is returned in different format's in search and create response in user service)
     */
    private void parseResponse(LinkedHashMap responeMap,String dobFormat){
        List<LinkedHashMap> users = (List<LinkedHashMap>)responeMap.get("user");
        String format1 = "dd-MM-yyyy HH:mm:ss";
        users.forEach( map -> {
            map.put("createdDate",dateTolong((String)map.get("createdDate"),format1));
            map.put("lastModifiedDate",dateTolong((String)map.get("lastModifiedDate"),format1));
            map.put("dob",dateTolong((String)map.get("dob"),dobFormat));
            map.put("pwdExpiryDate",dateTolong((String)map.get("pwdExpiryDate"),format1));
         }
        );
    }

    /**
     * Converts date to long
     * @param date date to be parsed
     * @param format Format of the date
     * @return Long value of date
     */
    private Long dateTolong(String date,String format){
        SimpleDateFormat f = new SimpleDateFormat(format);
        Date d = null;
        try {
            d = f.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  d.getTime();
    }

    /**
     * Sets owner fields (so that the owner table can be linked to user table)
     * @param owner Owner in the propertyDetail whose user is created
     * @param userDetailResponse userDetailResponse from the user Service corresponding to the given owner
     */
    private void setOwnerFields(OwnerInfo owner, UserDetailResponse userDetailResponse){
        owner.setUuid(userDetailResponse.getUser().get(0).getUuid());
        owner.setCreatedBy(userDetailResponse.getUser().get(0).getCreatedBy());
        owner.setCreatedDate(userDetailResponse.getUser().get(0).getCreatedDate());
        owner.setLastModifiedBy(userDetailResponse.getUser().get(0).getLastModifiedBy());
        owner.setLastModifiedDate(userDetailResponse.getUser().get(0).getLastModifiedDate());
        owner.setActive(userDetailResponse.getUser().get(0).getActive());
    }

    /**
     * Creates and Returns UserSearchRequest from the propertyCriteria(Creates UserSearchRequest from values related to owner(i.e mobileNumber and name) from propertyCriteria )
     * @param criteria PropertyCriteria from which UserSearchRequest is to be created
     * @param requestInfo RequestInfo of the propertyRequest
     * @return UserSearchRequest created from propertyCriteria
     */
    private UserSearchRequest getUserSearchRequest(PropertyCriteria criteria,RequestInfo requestInfo){
        UserSearchRequest userSearchRequest = new UserSearchRequest();
        Set<String> userIds = criteria.getOwnerids();
        if(!CollectionUtils.isEmpty(userIds))
         userSearchRequest.setUuid( new ArrayList(userIds));
        userSearchRequest.setRequestInfo(requestInfo);
        userSearchRequest.setTenantId(criteria.getTenantId());
        userSearchRequest.setMobileNumber(criteria.getMobileNumber());
     //   userSearchRequest.setUserName(criteria.getUserName());
        userSearchRequest.setName(criteria.getName());
        userSearchRequest.setActive(false);
        return userSearchRequest;
    }


    /**
     * Updates user if present else creates new user
     * @param request PropertyRequest received from update
     */
    public void updateUser(PropertyRequest request){
        List<Property> properties = request.getProperties();
        RequestInfo requestInfo = request.getRequestInfo();
        properties.forEach(property -> {
            property.getPropertyDetails().forEach(propertyDetail -> {
                propertyDetail.getOwners().forEach(owner -> {
                    UserDetailResponse userDetailResponse = userExists(owner,requestInfo);
                    StringBuilder uri  = new StringBuilder(userHost);
                    if(CollectionUtils.isEmpty(userDetailResponse.getUser())) {
                        uri = uri.append(userContextPath).append(userCreateEndpoint);
                    }
                    else
                    { owner.setId(userDetailResponse.getUser().get(0).getId());
                      uri=uri.append(userContextPath).append(owner.getId()).append(userUpdateEndpoint);
                    }
                    userDetailResponse = userCall( new CreateUserRequest(requestInfo,owner),uri);
                    setOwnerFields(owner,userDetailResponse);
                });
            });
        });
    }




}

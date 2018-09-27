package org.egov.tl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.tl.config.TLConfiguration;
import org.egov.tl.repository.ServiceRequestRepository;
import org.egov.tl.web.models.*;
import org.egov.tl.web.models.user.CreateUserRequest;
import org.egov.tl.web.models.user.UserDetailResponse;
import org.egov.tl.web.models.user.UserSearchRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@Service
public class UserService{


    private ObjectMapper mapper;

    private ServiceRequestRepository serviceRequestRepository;

    private TLConfiguration config;


    @Autowired
    public UserService(ObjectMapper mapper, ServiceRequestRepository serviceRequestRepository, TLConfiguration config) {
        this.mapper = mapper;
        this.serviceRequestRepository = serviceRequestRepository;
        this.config = config;
    }







    public void createUser(TradeLicenseRequest request){
        List<TradeLicense> licenses = request.getLicenses();
        RequestInfo requestInfo = request.getRequestInfo();
        Role role = getCitizenRole();
        licenses.forEach(tradeLicense -> {

           /* Set<String> listOfMobileNumbers = getMobileNumbers(tradeLicense.getTradeLicenseDetail().getOwners()
                    ,requestInfo,tradeLicense.getTenantId());*/

            tradeLicense.getTradeLicenseDetail().getOwners().forEach(owner ->
            {
                if(owner.getUuid()==null)
                    {
                        addUserDefaultFields(tradeLicense.getTenantId(),role,owner);
                      //  UserDetailResponse userDetailResponse = userExists(owner,requestInfo);
                         StringBuilder uri = new StringBuilder(config.getUserHost())
                                    .append(config.getUserContextPath())
                                    .append(config.getUserCreateEndpoint());
                            setUserName(owner);

                           UserDetailResponse userDetailResponse = userCall(new CreateUserRequest(requestInfo,owner),uri);
                            if(userDetailResponse.getUser().get(0).getUuid()==null){
                                throw new CustomException("INVALID USER RESPONSE","The user created has uuid as null");
                            }
                            log.info("owner created --> "+userDetailResponse.getUser().get(0).getUuid());
                        setOwnerFields(owner,userDetailResponse,requestInfo);
                    }
                 else {
                    UserDetailResponse userDetailResponse = userExists(owner,requestInfo);
                    if(userDetailResponse.getUser().isEmpty())
                        throw new CustomException("INVALID USER","The uuid "+owner.getUuid()+" does not exists");
                    StringBuilder uri =new StringBuilder(config.getUserHost());
                    uri=uri.append(config.getUserContextPath()).append(config.getUserUpdateEndpoint());
                    OwnerInfo user = new OwnerInfo();
                    user.addUserWithoutAuditDetail(owner);
                    addNonUpdatableFields(user,userDetailResponse.getUser().get(0));
                    userDetailResponse = userCall( new CreateUserRequest(requestInfo,user),uri);
                    setOwnerFields(owner,userDetailResponse,requestInfo);
                }
            });
        });
    }


    private void addNonUpdatableFields(User user,User userFromSearchResult){
        user.setUserName(userFromSearchResult.getUserName());
        user.setId(userFromSearchResult.getId());
        user.setActive(userFromSearchResult.getActive());
        user.setPassword(userFromSearchResult.getPassword());
    }


    private UserDetailResponse userExists(OwnerInfo owner,RequestInfo requestInfo){
        UserSearchRequest userSearchRequest =new UserSearchRequest();
        userSearchRequest.setTenantId(owner.getTenantId());
     //   userSearchRequest.setMobileNumber(owner.getMobileNumber());
     //   userSearchRequest.setName(owner.getName());
        userSearchRequest.setRequestInfo(requestInfo);
        userSearchRequest.setActive(true);
        userSearchRequest.setUserType(owner.getType());
     //   if(owner.getUuid()!=null)
            userSearchRequest.setUuid(Arrays.asList(owner.getUuid()));
        StringBuilder uri = new StringBuilder(config.getUserHost()).append(config.getUserSearchEndpoint());
        return userCall(userSearchRequest,uri);
    }



    private void setUserName(OwnerInfo owner){
            String username = UUID.randomUUID().toString();
            owner.setUserName(username);
    }



    private Set<String> getMobileNumbers(List<OwnerInfo> owners,RequestInfo requestInfo,String tenantId){
        Set<String> listOfMobileNumbers = new HashSet<>();
        owners.forEach(owner -> {listOfMobileNumbers.add(owner.getMobileNumber());});
        StringBuilder uri = new StringBuilder(config.getUserHost()).append(config.getUserSearchEndpoint());
        UserSearchRequest userSearchRequest = new UserSearchRequest();
        userSearchRequest.setRequestInfo(requestInfo);
        userSearchRequest.setTenantId(tenantId);
        userSearchRequest.setUserType("CITIZEN");
        Set<String> availableMobileNumbers = new HashSet<>();

        listOfMobileNumbers.forEach(mobilenumber -> {
            userSearchRequest.setMobileNumber(mobilenumber);
            UserDetailResponse userDetailResponse =  userCall(userSearchRequest,uri);
            if(CollectionUtils.isEmpty(userDetailResponse.getUser()))
                availableMobileNumbers.add(mobilenumber);
        });
        return availableMobileNumbers;
    }


    private void setOwnerFields(OwnerInfo owner, UserDetailResponse userDetailResponse,RequestInfo requestInfo){
        owner.setUuid(userDetailResponse.getUser().get(0).getUuid());
        owner.setId(userDetailResponse.getUser().get(0).getId());
        owner.setUserName((userDetailResponse.getUser().get(0).getUserName()));
        owner.setCreatedBy(requestInfo.getUserInfo().getUuid());
        owner.setCreatedDate(System.currentTimeMillis());
        owner.setLastModifiedBy(requestInfo.getUserInfo().getUuid());
        owner.setLastModifiedDate(System.currentTimeMillis());
        owner.setActive(userDetailResponse.getUser().get(0).getActive());
    }


    /**
     * Sets the role,type,active and tenantId for a Citizen
     * @param tenantId TenantId of the property
     * @param role The role of the user set in this case to CITIZEN
     * @param owner The user whose fields are to be set
     */
    private void addUserDefaultFields(String tenantId, Role role, OwnerInfo owner){
        owner.setActive(true);
        owner.setTenantId(tenantId);
        owner.setRoles(Collections.singletonList(role));
        owner.setType("CITIZEN");
    }

    private Role getCitizenRole(){
        Role role = new Role();
        role.setCode("CITIZEN");
        role.setName("Citizen");
        return role;
    }



    /**
     * Returns UserDetailResponse by calling user service with given uri and object
     * @param userRequest Request object for user service
     * @param uri The address of the endpoint
     * @return Response from user service as parsed as userDetailResponse
     */
    private UserDetailResponse userCall(Object userRequest, StringBuilder uri) {
        try{
            LinkedHashMap responseMap = (LinkedHashMap)serviceRequestRepository.fetchResult(uri, userRequest);
            parseResponse(responseMap);
            UserDetailResponse userDetailResponse = mapper.convertValue(responseMap,UserDetailResponse.class);
            return userDetailResponse;
        }
        catch(IllegalArgumentException  e)
        {
            throw new CustomException("IllegalArgumentException","ObjectMapper not able to convertValue in userCall");
        }
    }



    /**
     * Parses date formats to long for all users in responseMap
     * @param responeMap LinkedHashMap got from user api response
     */
    private void parseResponse(LinkedHashMap responeMap){
        List<LinkedHashMap> users = (List<LinkedHashMap>)responeMap.get("user");
        String format = "dd-MM-yyyy HH:mm:ss";
        if(users!=null){
            users.forEach( map -> {
                        map.put("createdDate",dateTolong((String)map.get("createdDate"),format));
                        if((String)map.get("lastModifiedDate")!=null)
                            map.put("lastModifiedDate",dateTolong((String)map.get("lastModifiedDate"),format));
                        if((String)map.get("dob")!=null)
                            map.put("dob",dateTolong((String)map.get("dob"),format));
                        if((String)map.get("pwdExpiryDate")!=null)
                            map.put("pwdExpiryDate",dateTolong((String)map.get("pwdExpiryDate"),format));
                    }
            );
        }
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



    public UserDetailResponse getUser(TradeLicenseSearchCriteria criteria,RequestInfo requestInfo){
        UserSearchRequest userSearchRequest = getUserSearchRequest(criteria,requestInfo);
        StringBuilder uri = new StringBuilder(config.getUserHost()).append(config.getUserSearchEndpoint());
        UserDetailResponse userDetailResponse = userCall(userSearchRequest,uri);
        return userDetailResponse;
    }


    private UserSearchRequest getUserSearchRequest(TradeLicenseSearchCriteria criteria, RequestInfo requestInfo){
        UserSearchRequest userSearchRequest = new UserSearchRequest();
        userSearchRequest.setRequestInfo(requestInfo);
        userSearchRequest.setTenantId(criteria.getTenantId());
        userSearchRequest.setMobileNumber(criteria.getMobileNumber());
        userSearchRequest.setActive(true);
        userSearchRequest.setUserType("CITIZEN");
        if(!CollectionUtils.isEmpty(criteria.getOwnerids()))
            userSearchRequest.setUuid(criteria.getOwnerids());
        return userSearchRequest;
    }



    private UserDetailResponse searchByUserName(String userName,String tenantId){
        UserSearchRequest userSearchRequest = new UserSearchRequest();
        userSearchRequest.setUserType("CITIZEN");
        userSearchRequest.setUserName(userName);
        userSearchRequest.setTenantId(tenantId);
        StringBuilder uri = new StringBuilder(config.getUserHost()).append(config.getUserSearchEndpoint());
        return userCall(userSearchRequest,uri);

    }


    /**
     * Updates user if present else creates new user
     * @param request TradeLicenseRequest received from update
     */
    public void updateUser(TradeLicenseRequest request){
        List<TradeLicense> licenses = request.getLicenses();
        RequestInfo requestInfo = request.getRequestInfo();
        licenses.forEach(license -> {
                license.getTradeLicenseDetail().getOwners().forEach(owner -> {
                    UserDetailResponse userDetailResponse = isUserUpdatable(owner,requestInfo);
                    OwnerInfo user = new OwnerInfo();
                    StringBuilder uri  = new StringBuilder(config.getUserHost());
                    if(CollectionUtils.isEmpty(userDetailResponse.getUser())) {
                        uri = uri.append(config.getUserContextPath()).append(config.getUserCreateEndpoint());
                        user.addUserWithoutAuditDetail(owner);
                        user.setUserName(owner.getMobileNumber());
                    }
                    else
                    {   owner.setUuid(userDetailResponse.getUser().get(0).getUuid());
                        uri=uri.append(config.getUserContextPath()).append(config.getUserUpdateEndpoint());
                        user.addUserWithoutAuditDetail(owner);
                    }
                    userDetailResponse = userCall( new CreateUserRequest(requestInfo,user),uri);
                    setOwnerFields(owner,userDetailResponse,requestInfo);
                });
            });
    }


    private UserDetailResponse isUserUpdatable(OwnerInfo owner,RequestInfo requestInfo){
        UserSearchRequest userSearchRequest =new UserSearchRequest();
        userSearchRequest.setTenantId(owner.getTenantId());
        userSearchRequest.setMobileNumber(owner.getMobileNumber());
        userSearchRequest.setUuid(Collections.singletonList(owner.getUuid()));
        userSearchRequest.setRequestInfo(requestInfo);
        userSearchRequest.setActive(true);
        userSearchRequest.setUserType(owner.getType());
        StringBuilder uri = new StringBuilder(config.getUserHost()).append(config.getUserSearchEndpoint());
        return userCall(userSearchRequest,uri);
    }





}

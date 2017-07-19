package org.egov.propertyUser.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.models.RequestInfo;
import org.egov.models.User;
import org.egov.models.UserResponseInfo;
import org.egov.propertyUser.model.UserRequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserUtil {


    @Autowired
    Environment environment;
    
    @Autowired
    RestTemplate restTemplate;
    
    public User getUserId(User user, RequestInfo requestInfo) throws Exception {
        StringBuffer createUrl = new StringBuffer();
        createUrl.append(environment.getProperty("egov.services.egov_user.hostname"));
        createUrl.append(environment.getProperty("egov.services.egov_user.basepath"));
        createUrl.append(environment.getProperty("egov.services.egov_user.createpath"));

        StringBuffer searchUrl = new StringBuffer();
        searchUrl.append(environment.getProperty("egov.services.egov_user.hostname"));
        searchUrl.append(environment.getProperty("egov.services.egov_user.basepath"));
        searchUrl.append(environment.getProperty("egov.services.egov_user.searchpath"));

        Map<String, Object> userSearchRequestInfo = new HashMap<String, Object>();
        userSearchRequestInfo.put("name", user.getName());
        userSearchRequestInfo.put("mobileNumber", user.getMobileNumber());
        userSearchRequestInfo.put("type", user.getType());
        userSearchRequestInfo.put("active", user.getActive());
        userSearchRequestInfo.put("tenantId", user.getTenantId());
        userSearchRequestInfo.put("RequestInfo", requestInfo);

        if (user.getAadhaarNumber() != null && !user.getAadhaarNumber().isEmpty()) {
                userSearchRequestInfo.put("aadharNumber", user.getAadhaarNumber());
        }

        if (user.getEmailId() != null && !user.getEmailId().isEmpty()) {
                userSearchRequestInfo.put("emailId", user.getEmailId());
        }

        // search user
        UserResponseInfo userResponse = restTemplate.postForObject(searchUrl.toString(), userSearchRequestInfo,
                        UserResponseInfo.class);

        if (userResponse.getUser().size() == 0) {
                UserRequestInfo userRequestInfo = new UserRequestInfo();
                userRequestInfo.setRequestInfo(requestInfo);
                user.setPassword(environment.getProperty("default.password"));
                userRequestInfo.setUser(user);
                UserResponseInfo userCreateResponse = restTemplate.postForObject(createUrl.toString(), userRequestInfo,
                                UserResponseInfo.class);
                user.setId(userCreateResponse.getUser().get(0).getId());
        } else {
                if (userResponse.getUser().size() > 1) {

                        List<User> userFromReponse = userResponse.getUser();

                        List<User> result = userFromReponse.stream()
                                        .filter(value -> value.getGender().equalsIgnoreCase(user.getGender())
                                                        && value.getAltContactNumber().equalsIgnoreCase(user.getAltContactNumber())
                                                        && value.getPan().equalsIgnoreCase(user.getPan())
                                                        && value.getPermanentAddress().equalsIgnoreCase(user.getPermanentAddress())
                                                        && value.getPermanentCity().equalsIgnoreCase(user.getPermanentCity())
                                                        && value.getPermanentPincode().equalsIgnoreCase(user.getPermanentPincode())
                                                        && value.getCorrespondenceAddress().equalsIgnoreCase(user.getCorrespondenceAddress())
                                                        && value.getCorrespondenceCity().equalsIgnoreCase(user.getCorrespondenceCity())
                                                        && value.getCorrespondencePincode().equalsIgnoreCase(user.getCorrespondencePincode())
                                                        && value.getLocale().equalsIgnoreCase(user.getLocale())
                                                        && value.getFatherOrHusbandName().equalsIgnoreCase(user.getFatherOrHusbandName())
                                                        && value.getBloodGroup().equalsIgnoreCase(user.getBloodGroup())
                                                        && value.getIdentificationMark().equalsIgnoreCase(user.getIdentificationMark()))
                                        .collect(Collectors.toList());

                        user.setId(result.get(0).getId());
                } else {
                        user.setId(userResponse.getUser().get(0).getId());
                }
        }

        return user;

}
}

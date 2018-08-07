package org.egov.pt.service;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.pt.producer.Producer;
import org.egov.pt.repository.ServiceRequestRepository;
import org.egov.pt.util.PTConstants;
import org.egov.pt.web.models.Property;
import org.egov.pt.web.models.PropertyRequest;
import org.egov.pt.web.models.SMSRequest;
import org.egov.tracer.model.CustomException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
@Service
@Slf4j
public class NotificationService {

    @Autowired
    private Producer producer;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Value("${egov.localization.host}")
    private String localizationHost;

    @Value("${egov.localization.context.path}")
    private String localizationContextPath;

    @Value("${egov.localization.search.endpoint}")
    private String localizationSearchEndpoint;

    @Value("${kafka.topics.notification.sms}")
    private String smsNotifTopic;

    @Value("${kafka.topics.notification.email}")
    private String emailNotifTopic;

    @Value("${notification.sms.enabled}")
    private Boolean isSMSNotificationEnabled;

    @Value("${notification.email.enabled}")
    private Boolean isEmailNotificationEnabled;

    @Value("${egov.localization.statelevel}")
    private Boolean isStateLevel;

    /**
     * Processes the json and send the SMSRequest
     * @param request The propertyRequest for which notification has to be send
     */
    public void process(PropertyRequest request){
        String tenantId = request.getProperties().get(0).getTenantId();
        StringBuilder uri = getUri(tenantId);
        try{
        LinkedHashMap responseMap = (LinkedHashMap)serviceRequestRepository.fetchResult(uri, request.getRequestInfo());
        String jsonString = new JSONObject(responseMap).toString();
        String message = JsonPath.parse(jsonString).read("$.messages[0].message");
        request.getProperties().forEach(property -> {
            String customMessage = getCustomizedMessage(property,message);
            List<SMSRequest> smsRequests = getSMSRequests(getMobileNumbers(property),customMessage);
            sendSMS(smsRequests);
         });
        }
        catch(Exception e)
        {throw new CustomException("LOCALIZATION ERROR","Unable to get message from localization");}
    }


    /**
     * Returns the uri for the localization call
     * @param tenantId TenantId of the propertyRequest
     * @return The uri for localization search call
     */
    private StringBuilder getUri(String tenantId){
        if(isStateLevel)
           tenantId = tenantId.split("\\.")[0];
        StringBuilder uri = new StringBuilder();
        uri.append(localizationHost).append(localizationContextPath).append(localizationSearchEndpoint);
        uri.append("?").append("locale=").append(PTConstants.NOTIFICATION_LOCALE)
        .append("&tenantId=").append(tenantId)
        .append("&module=").append(PTConstants.MODULE)
        .append("&code=").append(PTConstants.NOTIFICATION_CREATE_CODE);
        return uri;
    }

    /**
     * Returns the message for each specific property
     * @param property The property for which the notification has to be sent
     * @param message The standard message format
     * @return The customized message for the given property
     */
    private String getCustomizedMessage(Property property, String message){
        message = message.replace("<insert ID>",property.getPropertyId());
        if(property.getAddress().getDoorNo()!=null)
            message = message.replace("<House No.>",property.getAddress().getDoorNo());
        else
            message = message.replace("<House No.>,","");

        if(property.getAddress().getBuildingName()!=null)
            message = message.replace("<Colony Name>",property.getAddress().getBuildingName());
        else
            message = message.replace("<Colony Name>,","");

        if(property.getAddress().getStreet()!=null)
            message = message.replace("<Street No.>",property.getAddress().getStreet());
        else
            message = message.replace("<Street No.>,","");

        if(property.getAddress().getLocality().getCode()!=null)
            message = message.replace("<Mohalla>",property.getAddress().getLocality().getCode());
        else
            message = message.replace("<Mohalla>,","");

        if(property.getAddress().getCity()!=null)
            message = message.replace("<City>",property.getAddress().getCity());
        else
            message = message.replace("<City>.","");

        if(property.getAddress().getPincode()!=null)
            message = message.replace("<Pincode>",property.getAddress().getPincode());
        else
            message = message.replace("<Pincode>","");

        return message;
    }

    /**
     * Get all the unique mobileNumbers of the owners of the property
     * @param property The property whose unique mobileNumber are to be returned
     * @return Unique mobileNumber of the given property
     */
    private Set<String> getMobileNumbers(Property property){
        Set<String> mobileNumbers = new HashSet<>();
        property.getPropertyDetails().forEach(propertyDetail -> {
            propertyDetail.getOwners().forEach(owner -> {
                mobileNumbers.add(owner.getMobileNumber());
            });
        });
        return mobileNumbers;
    }


    /**
     * Creates SMSRequest for the given mobileNumber with the given message
     * @param mobileNumbers The set of mobileNumber for which SMSRequest has to be created
     * @param customizedMessage The message to sent
     * @return List of SMSRequest
     */
    private List<SMSRequest> getSMSRequests(Set<String> mobileNumbers,String customizedMessage){
        List<SMSRequest> smsRequests = new ArrayList<>();
             mobileNumbers.forEach(mobileNumber-> {
                 SMSRequest smsRequest = new SMSRequest(mobileNumber,customizedMessage);
                 smsRequests.add(smsRequest);
             });
         return smsRequests;
    }

    /**
     * Send the SMSRequest on the SMSNotification kafka topic
     * @param smsRequestList The list of SMSRequest to be sent
     */
    private void sendSMS(List<SMSRequest> smsRequestList){
        if (isSMSNotificationEnabled) {
            if (CollectionUtils.isEmpty(smsRequestList))
                log.info("Messages from localization couldn't be fetched!");
            for(SMSRequest smsRequest: smsRequestList) {
                producer.push(smsNotifTopic, smsRequest);
            }
        }
    }




}

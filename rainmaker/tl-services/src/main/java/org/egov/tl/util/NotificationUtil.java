package org.egov.tl.util;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.tl.config.TLConfiguration;
import org.egov.tl.producer.Producer;
import org.egov.tl.repository.ServiceRequestRepository;
import org.egov.tl.web.models.SMSRequest;
import org.egov.tl.web.models.TradeLicense;
import org.egov.tl.web.models.TradeLicenseRequest;
import org.egov.tracer.model.CustomException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;


@Component
@Slf4j
public class NotificationUtil {



    private TLConfiguration config;

    private ServiceRequestRepository serviceRequestRepository;

    private Producer producer;


    @Autowired
    public NotificationUtil(TLConfiguration config,ServiceRequestRepository serviceRequestRepository,Producer producer) {
        this.config = config;
        this.serviceRequestRepository = serviceRequestRepository;
        this.producer = producer;
    }


    final String receiptNumberKey = "receiptNumber";

    final String amountPaidKey = "amountPaid";



    public String getCustomizedMsg(TradeLicense license, String localizationMessage){
        String message = null,messageTemplate;
        switch (license.getStatus()){

            case INITIATED :
                messageTemplate = getMessageTemplate(TLConstants.NOTIFICATION_INITIATED,localizationMessage);
                message = getInitiatedMsg(license,messageTemplate);
                break;

            case APPLIED :
                messageTemplate = getMessageTemplate(TLConstants.NOTIFICATION_APPLIED,localizationMessage);
                message = getAppliedMsg(license,messageTemplate);
                break;

            case PAID :
                messageTemplate = getMessageTemplate(TLConstants.NOTIFICATION_PAID,localizationMessage);
                message = getApprovalPendingMsg(license,messageTemplate);
                break;

            case APPROVED :
                messageTemplate = getMessageTemplate(TLConstants.NOTIFICATION_APPROVED,localizationMessage);
                message = getApprovedMsg(license,messageTemplate);
                break;

            case REJECTED :
                messageTemplate = getMessageTemplate(TLConstants.NOTIFICATION_REJECTED,localizationMessage);
                message = getRejectedMsg(license,messageTemplate);
                break;

            case CANCELLED :
                messageTemplate = getMessageTemplate(TLConstants.NOTIFICATION_CANCELLED,localizationMessage);
                message = getCancelledMsg(license,messageTemplate);
                break;
        }

        return message;
    }




    private String getMessageTemplate(String notificationCode,String localizationMessage){
        String path = "$..messages[?(@.code==\"{}\")].message";
        path = path.replace("{}",notificationCode);
        String message = null;
        try {
            Object messageObj = JsonPath.parse(localizationMessage).read(path);
            message = ((ArrayList<String>)messageObj).get(0);
        }
        catch (Exception e){
            throw new CustomException("PARSING ERROR","Failed to parse the response using jsonPath: "+path);
        }
        return message;
    }


    /**
     * Returns the uri for the localization call
     * @param tenantId TenantId of the propertyRequest
     * @return The uri for localization search call
     */
    public StringBuilder getUri(String tenantId){

        if(config.getIsLocalizationStateLevel())
            tenantId = tenantId.split("\\.")[0];

        StringBuilder uri = new StringBuilder();
        uri.append(config.getLocalizationHost()).append(config.getLocalizationContextPath())
                .append(config.getLocalizationSearchEndpoint()).append("?")
                .append("locale=").append(TLConstants.NOTIFICATION_LOCALE)
                .append("&tenantId=").append(tenantId)
                .append("&module=").append(TLConstants.MODULE);

        return uri;
    }



    public String getLocalizationMessages(String tenantId, RequestInfo requestInfo){
        LinkedHashMap responseMap = (LinkedHashMap)serviceRequestRepository.fetchResult(getUri(tenantId),requestInfo);
        String jsonString = new JSONObject(responseMap).toString();
        return jsonString;
    }



    private String getInitiatedMsg(TradeLicense license,String message){
      //  message = message.replace("<1>",license.);
        message = message.replace("<2>",license.getTradeName());
        message = message.replace("<3>",license.getApplicationNumber());

       return message;
    }

    private String getAppliedMsg(TradeLicense license,String message){
      //  message = message.replace("<1>",);
        message = message.replace("<2>",license.getTradeName());
        message = message.replace("<3>",license.getApplicationNumber());

        return message;
    }

    private String getApprovalPendingMsg(TradeLicense license,String message){
     //   message = message.replace("<1>",);
        message = message.replace("<2>",license.getTradeName());

        return message;
    }

    private String getApprovedMsg(TradeLicense license,String message){
      //  message = message.replace("<1>",);

        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String expiryDate = simpleDateFormat.format(license.getValidTo());
        message = message.replace("<2>",license.getTradeName());
        message = message.replace("<3>",license.getLicenseNumber());
        message = message.replace("<4>",expiryDate);

        return message;
    }

    private String getRejectedMsg(TradeLicense license,String message){
      //  message = message.replace("<1>",);
        message = message.replace("<2>",license.getTradeName());

        return message;
    }



    private String getCancelledMsg(TradeLicense license,String message){
     //   message = message.replace("<1>",);
        message = message.replace("<2>",license.getTradeName());
        message = message.replace("<3>",license.getLicenseNumber());

        return message;
    }







    public String getOwnerPaymentMsg(Map<String,String> valMap, String localizationMessages){
        String messageTemplate = getMessageTemplate(TLConstants.NOTIFICATION_PAYMENT_OWNER,localizationMessages);
        messageTemplate = messageTemplate.replace("<2>",valMap.get(amountPaidKey));
        messageTemplate = messageTemplate.replace("<3>",valMap.get(receiptNumberKey));
        return messageTemplate;
    }


    public String getPayerPaymentMsg(Map<String,String> valMap, String localizationMessages){
        String messageTemplate = getMessageTemplate(TLConstants.NOTIFICATION_PAYMENT_PAYER,localizationMessages);
        messageTemplate = messageTemplate.replace("<2>",valMap.get(amountPaidKey));
        messageTemplate = messageTemplate.replace("<3>",valMap.get(receiptNumberKey));
        return messageTemplate;
    }




    /**
     * Send the SMSRequest on the SMSNotification kafka topic
     * @param smsRequestList The list of SMSRequest to be sent
     */
    public void sendSMS(List<SMSRequest> smsRequestList){
        if (config.getIsSMSEnabled()) {
            if (CollectionUtils.isEmpty(smsRequestList))
                log.info("Messages from localization couldn't be fetched!");
            for(SMSRequest smsRequest: smsRequestList) {
                producer.push(config.getSmsNotifTopic(), smsRequest);
                log.info("MobileNumber: "+smsRequest.getMobileNumber()+" Messages: "+smsRequest.getMessage());
            }
        }
    }










}

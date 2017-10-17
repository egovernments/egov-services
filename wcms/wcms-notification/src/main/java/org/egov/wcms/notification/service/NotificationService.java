/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wcms.notification.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.notification.config.PropertiesManager;
import org.egov.wcms.notification.domain.model.EmailMessage;
import org.egov.wcms.notification.domain.model.SmsMessage;
import org.egov.wcms.notification.model.enums.NewConnectionStatus;
import org.egov.wcms.notification.repository.TenantRepository;
import org.egov.wcms.notification.utils.NotificationUtil;
import org.egov.wcms.notification.web.contract.Connection;
import org.egov.wcms.notification.web.contract.ConnectionOwner;
import org.egov.wcms.notification.web.contract.ConnectionRequest;
import org.egov.wcms.notification.web.contract.EmailMessageContext;
import org.egov.wcms.notification.web.contract.EmailRequest;
import org.egov.wcms.notification.web.contract.PropertyOwnerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
    
    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private NotificationUtil notificationUtil;

    /**
     * This method is to send email and sms water new connection with acknowledgement
     *
     */

    public void waterNewCreationAcknowledgement(final ConnectionRequest connectionRequest) {

        String applicantName = "";
        String mobileNumber = "";
        String emailId = "";
        String applicationNumber = "";
        final Connection connection = connectionRequest.getConnection();
        final String ulbName = tenantRepository.getULBName(connection.getTenantId(), connectionRequest.getRequestInfo());
        applicationNumber = connection.getAcknowledgementNumber();
        if (!connection.getIsLegacy())
            if (connection.getWithProperty() && connection.getProperty() != null &&
                    connection.getProperty().getPropertyOwner() != null)
                for (final PropertyOwnerInfo propertyOwner : connection.getProperty().getPropertyOwner()) {
                    applicantName = propertyOwner.getName();
                    mobileNumber = propertyOwner.getMobileNumber();
                    emailId = propertyOwner.getEmailId();

                    createConnectionForNotification(applicantName, mobileNumber, emailId, applicationNumber, ulbName);
                }
            else if (connection.getConnectionOwners() != null)
                for (final ConnectionOwner connectionOwner : connection.getConnectionOwners()) {
                    applicantName = connectionOwner.getName();
                    mobileNumber = connectionOwner.getMobileNumber();
                    emailId = connectionOwner.getEmailId();
                    createConnectionForNotification(applicantName, mobileNumber, emailId, applicationNumber, ulbName);

                }
    }

    public void createConnectionForNotification(final String applicantName, final String mobileNumber, final String emailId,
            final String applicationNumber,
            final String ulbName) {
        final Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
        if (ulbName != null)
            propertyMessage.put("ULB Name", ulbName);
        propertyMessage.put("Owner", applicantName);
        propertyMessage.put("Application Number", applicationNumber);

        final String message = notificationUtil.buildSmsMessage(propertiesManager.getWaterNewConnectionCreateSms(),
                propertyMessage);
        final SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
        final EmailMessageContext emailMessageContext = new EmailMessageContext();
        emailMessageContext.setBodyTemplateName(propertiesManager.getWaterNewConnectionCreateEmailBody());
        emailMessageContext.setBodyTemplateValues(propertyMessage);
        emailMessageContext.setSubjectTemplateName(propertiesManager.getWaterNewConnectionCreateEmailSubject());
        emailMessageContext.setSubjectTemplateValues(propertyMessage);

        final EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
        final EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailId);

        kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
        kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
    }
    
    
    /**
     * This method is to send email and sms water connection to based on workflow status
     *
     */
    public void waterUpdateConnection(final ConnectionRequest connectionRequest) {

        String applicantName = "";
        String mobileNumber = "";
        String emailId = "";
        String applicationNumber = "";
        String comments ="";
        Long executionDate = 0l;
        final Connection connection = connectionRequest.getConnection();
        final String ulbName = tenantRepository.getULBName(connection.getTenantId(), connectionRequest.getRequestInfo());
        applicationNumber = connection.getAcknowledgementNumber();
       String status=connection.getStatus();
       Double lOICharges=connection.getDonationCharge();
      String consumerNumber=connection.getConsumerNumber();
      executionDate =connection.getExecutionDate();
    
     
        
        if (!connection.getIsLegacy())
            if (connection.getWithProperty() && connection.getProperty() != null &&
                    connection.getProperty().getPropertyOwner() != null){
                for (final PropertyOwnerInfo propertyOwner : connection.getProperty().getPropertyOwner()) {
                    applicantName = propertyOwner.getName();
                    mobileNumber = propertyOwner.getMobileNumber();
                    emailId = propertyOwner.getEmailId();
                    if (status != null
                            && (status.equalsIgnoreCase(NewConnectionStatus.APPROVED.name()) && connection.getEstimationNumber()!=null)
                                   ){
                    updateConnectionForApproverAndEstinationNotification(applicantName, mobileNumber, emailId, applicationNumber, ulbName,lOICharges);
                }else if (status != null
                        && 
                        (status.equalsIgnoreCase(NewConnectionStatus.APPLICATIONFEESPAID.name()))){
                    updateConnectionForPaymentEstinationDoneNotification(applicantName, mobileNumber, emailId, applicationNumber, ulbName,lOICharges);
                }else if (status != null
                        && 
                        (status.equalsIgnoreCase(NewConnectionStatus.WORKORDERGENERATED.name()))){
                    updateConnectionForWorkOrderGeneratedNotification(applicantName, mobileNumber, emailId, applicationNumber, ulbName,consumerNumber);
                }
                else if (status != null
                        && 
                        (status.equalsIgnoreCase(NewConnectionStatus.SANCTIONED.name()))){
                    updateConnectionForSanctionedNotification(applicantName, mobileNumber, emailId, ulbName,consumerNumber,lOICharges,executionDate);
                }
                else if (status != null
                        && 
                        (status.equalsIgnoreCase(NewConnectionStatus.REJECTED.name()))){
                    if(connection.getWorkflowDetails()!=null)
                         comments=connection.getWorkflowDetails().getComments();
                        
                    updateConnectionForRejectedNotification(applicantName, mobileNumber, emailId, ulbName,applicationNumber,comments);
                }
                }
                    
            } else if (!connection.getWithProperty() && connection.getConnectionOwners() != null)
                for (final ConnectionOwner connectionOwner : connection.getConnectionOwners()) {
                    applicantName = connectionOwner.getName();
                    mobileNumber = connectionOwner.getMobileNumber();
                    emailId = connectionOwner.getEmailId();
                    if (status != null
                            && (status.equalsIgnoreCase(NewConnectionStatus.APPROVED.name()))&&
                            connection.getEstimationNumber()!=null){
                    updateConnectionForApproverAndEstinationNotification(applicantName, mobileNumber, emailId, applicationNumber, ulbName,lOICharges);
                    }
                    else if (status != null
                            && 
                            (status.equalsIgnoreCase(NewConnectionStatus.APPLICATIONFEESPAID.name()))){
                        updateConnectionForPaymentEstinationDoneNotification(applicantName, mobileNumber, emailId, applicationNumber, ulbName,lOICharges);

                }
                    else if (status != null
                            && 
                            (status.equalsIgnoreCase(NewConnectionStatus.WORKORDERGENERATED.name()))){
                        updateConnectionForWorkOrderGeneratedNotification(applicantName, mobileNumber, emailId, applicationNumber, ulbName,consumerNumber);
                    }
                    else if (status != null
                            && 
                            (status.equalsIgnoreCase(NewConnectionStatus.SANCTIONED.name()))){
                        updateConnectionForSanctionedNotification(applicantName, mobileNumber, emailId, ulbName,consumerNumber,lOICharges,executionDate);
                    }
                    else if (status != null
                            && 
                            (status.equalsIgnoreCase(NewConnectionStatus.REJECTED.name()))){
                        if(connection.getWorkflowDetails()!=null)
                            comments=connection.getWorkflowDetails().getComments();
                        updateConnectionForRejectedNotification(applicantName, mobileNumber, emailId, ulbName,applicationNumber,comments);
                    }
    }
        
    }
    
    
    public void updateConnectionForApproverAndEstinationNotification(final String applicantName, final String mobileNumber, final String emailId,
            final String applicationNumber,
            final String ulbName,final Double lOICharges) {
        final Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
        if (ulbName != null)
            propertyMessage.put("ULB Name", ulbName);
        propertyMessage.put("Owner", applicantName);
        propertyMessage.put("Application Number", applicationNumber);
        propertyMessage.put("LOI charges", lOICharges);

        final String message = notificationUtil.buildSmsMessage(propertiesManager.getWaterConnectionApprovalAndEstimationSms(),
                propertyMessage);
        final SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
        final EmailMessageContext emailMessageContext = new EmailMessageContext();
        emailMessageContext.setBodyTemplateName(propertiesManager.getWaterConnectionApprovalAndEstimationEmailBody());
        emailMessageContext.setBodyTemplateValues(propertyMessage);
        emailMessageContext.setSubjectTemplateName(propertiesManager.getWaterConnectionApprovalAndEstiamtionEmailSubject());
        emailMessageContext.setSubjectTemplateValues(propertyMessage);

        final EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
        final EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailId);

        kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
        kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
    }
    
    
    public void updateConnectionForPaymentEstinationDoneNotification(final String applicantName, final String mobileNumber, final String emailId,
            final String applicationNumber,
            final String ulbName,final Double lOICharges) {
        final Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
        if (ulbName != null)
            propertyMessage.put("ULB Name", ulbName);
        propertyMessage.put("Owner", applicantName);
        propertyMessage.put("Application Number", applicationNumber);
        propertyMessage.put("LOI charges", lOICharges);

        final String message = notificationUtil.buildSmsMessage(propertiesManager.getWaterConnectionPaymentEstimationDoneSms(),
                propertyMessage);
        final SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
        final EmailMessageContext emailMessageContext = new EmailMessageContext();
        emailMessageContext.setBodyTemplateName(propertiesManager.getWaterConnectionPaymentEstimationDoneEmailBody());
        emailMessageContext.setBodyTemplateValues(propertyMessage);
        emailMessageContext.setSubjectTemplateName(propertiesManager.getWaterConnectionPaymentEstiamtionDoneEmailSubject());
        emailMessageContext.setSubjectTemplateValues(propertyMessage);

        final EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
        final EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailId);

        kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
        kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
    }
    
    public void updateConnectionForWorkOrderGeneratedNotification(final String applicantName, final String mobileNumber, final String emailId,
            final String applicationNumber,
            final String ulbName,final String consumerNumber) {
        final Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
        if (ulbName != null)
            propertyMessage.put("ULB Name", ulbName);
        propertyMessage.put("Owner", applicantName);
        propertyMessage.put("Application Number", applicationNumber);
        propertyMessage.put("Consumer Number", consumerNumber);

        final String message = notificationUtil.buildSmsMessage(propertiesManager.getWaterConnectionWorkOrderGeneratedSms(),
                propertyMessage);
        final SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
        final EmailMessageContext emailMessageContext = new EmailMessageContext();
        emailMessageContext.setBodyTemplateName(propertiesManager.getWaterConnectionWorkOrderGeneratedEmailBody());
        emailMessageContext.setBodyTemplateValues(propertyMessage);
        emailMessageContext.setSubjectTemplateName(propertiesManager.getWaterConnectionWorkOrderGeneratedEmailSubject());
        emailMessageContext.setSubjectTemplateValues(propertyMessage);

        final EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
        final EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailId);

        kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
        kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
    }
    
    public void updateConnectionForSanctionedNotification(final String applicantName, final String mobileNumber, final String emailId,
            final String ulbName,final String consumerNumber,final Double lOICharges,final Long executionDate) {
        final Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
        if (ulbName != null)
            propertyMessage.put("ULB Name", ulbName);
        propertyMessage.put("Owner", applicantName);
        propertyMessage.put("Consumer Number", consumerNumber);
        propertyMessage.put("LOI charges", lOICharges);
        if(executionDate>0){
            Date connectionExecutionDate = new Date(executionDate);
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        propertyMessage.put("Execution Date", format.format(connectionExecutionDate));
        }
       


        final String message = notificationUtil.buildSmsMessage(propertiesManager.getWaterConnectionSanctionedSms(),
                propertyMessage);
        final SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
        final EmailMessageContext emailMessageContext = new EmailMessageContext();
        emailMessageContext.setBodyTemplateName(propertiesManager.getWaterConnectionSanctionedEmailBody());
        emailMessageContext.setBodyTemplateValues(propertyMessage);
        emailMessageContext.setSubjectTemplateName(propertiesManager.getWaterConnectionSanctionedEmailSubject());
        emailMessageContext.setSubjectTemplateValues(propertyMessage);

        final EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
        final EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailId);

        kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
        kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
    }

    public void updateConnectionForRejectedNotification(final String applicantName, final String mobileNumber, final String emailId,
            final String ulbName,final String applicationNumber,final String comments) {
        final Map<Object, Object> propertyMessage = new HashMap<Object, Object>();
        if (ulbName != null)
            propertyMessage.put("ULB Name", ulbName);
        propertyMessage.put("Owner", applicantName);
        propertyMessage.put("Application Number", applicationNumber);
        propertyMessage.put("Rejection comments of the approver", comments);


        final String message = notificationUtil.buildSmsMessage(propertiesManager.getWaterConnectionRejectedSms(),
                propertyMessage);
        final SmsMessage smsMessage = new SmsMessage(message, mobileNumber);
        final EmailMessageContext emailMessageContext = new EmailMessageContext();
        emailMessageContext.setBodyTemplateName(propertiesManager.getWaterConnectionRejectedEmailBody());
        emailMessageContext.setBodyTemplateValues(propertyMessage);
        emailMessageContext.setSubjectTemplateName(propertiesManager.getWaterConnectionRejectedEmailSubject());
        emailMessageContext.setSubjectTemplateValues(propertyMessage);

        final EmailRequest emailRequest = notificationUtil.getEmailRequest(emailMessageContext);
        final EmailMessage emailMessage = notificationUtil.buildEmailTemplate(emailRequest, emailId);

        kafkaTemplate.send(propertiesManager.getSmsNotification(), smsMessage);
        kafkaTemplate.send(propertiesManager.getEmailNotification(), emailMessage);
    }

}

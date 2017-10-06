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
package org.egov.wcms.notification.utils;

import java.util.Map;

import org.egov.wcms.notification.domain.model.EmailMessage;
import org.egov.wcms.notification.web.contract.EmailMessageContext;
import org.egov.wcms.notification.web.contract.EmailRequest;
import org.egov.wcms.notification.web.contract.SMSMessageContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationUtil {

    @Autowired
    TemplateUtil templateService;

    /**
     * 
     * @param messageType
     * @param messageParameters
     * @return {@link String}
     */
    public String buildSmsMessage(String messageType, Map<Object, Object> messageParameters) {

        String smsMessage = getSMSMessage(messageType, messageParameters);

        return smsMessage;
    }

    /**
     * 
     * @param messageType
     * @param messageParameters
     * @return {@link String}
     */
    private String getSMSMessage(String messageType, Map<Object, Object> messageParameters) {

        SMSMessageContext messageContext = new SMSMessageContext();

        messageContext.setTemplateName(messageType);
        messageContext.setTemplateValues(messageParameters);

        return templateService.loadByName(messageContext.getTemplateName(), messageContext.getTemplateValues());
    }

    /**
     * 
     * @param emailRequest
     * @param emailAddress
     * @return {@link EmailMessage}
     */
    public EmailMessage buildEmailTemplate(EmailRequest emailRequest, String emailAddress) {

        EmailMessage emailMessage = EmailMessage.builder().body(emailRequest.getBody())
                .subject(emailRequest.getSubject()).sender("").email(emailAddress).build();

        return emailMessage;
    }

    /**
     * 
     * @param messageContext
     * @return {@link EmailRequest}
     */
    public EmailRequest getEmailRequest(EmailMessageContext messageContext) {

        return EmailRequest.builder().subject(getEmailSubject(messageContext)).body(getMailBody(messageContext))
                .build();
    }

    public EmailMessage buildRejectionEmailTemplate(EmailMessageContext emailMessageContext, String emailAddress) {

        return EmailMessage.builder()
                .subject(getEmailSubject(emailMessageContext))
                .body(getMailBody(emailMessageContext))
                .email(emailAddress)
                .isHTML(true)
                .build();
    }

    /**
     * 
     * @param messageContext
     * @return {@link String}
     */
    private String getEmailSubject(EmailMessageContext messageContext) {

        return templateService.loadByName(messageContext.getSubjectTemplateName(),
                messageContext.getSubjectTemplateValues());
    }

    /**
     * 
     * @param messageContext
     * @return {@link String}
     */

    private String getMailBody(EmailMessageContext messageContext) {

        return templateService.loadByName(messageContext.getBodyTemplateName(), messageContext.getBodyTemplateValues());
    }
}
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
package org.egov.wcms.notification.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Setter
@Getter
public class PropertiesManager {

    @Autowired
    private Environment environment;

    @Value("${kafka.topics.notification.connection.create.name}")
    private String createConnection;

    @Value("${kafka.topics.notification.connection.update.name}")
    private String updateConnection;

    @Value("${kafka.topics.wcms-notification.sms}")
    private String smsNotification;

    @Value("${kafka.topics.wcms-notification.email}")
    private String emailNotification;

    @Value("${egov.services.tenant.hostname}")
    private String tenantServiceHostName;

    @Value("${egov.services.tenant.service.searchpath}")
    private String tenantServiceSearchPath;

    @Value("${wcms-notification.template.priority}")
    private String templatePriority;

    @Value("${wcms-notification.template.folder}")
    private String templateFolder;

    @Value("${wcms-notification.template.type}")
    private String templateType;

    @Value("${wcms.sms.create.msg}")
    private String waterNewConnectionCreateSms;

    @Value("${wcms.email.body.create}")
    private String waterNewConnectionCreateEmailBody;

    @Value("${wcms.email.subject.create}")
    private String waterNewConnectionCreateEmailSubject;

    @Value("${wcms.sms.approval.estimation.generated.msg}")
    private String waterConnectionApprovalAndEstimationSms;

    @Value("${wcms.email.body.approval.estimation.generated}")
    private String waterConnectionApprovalAndEstimationEmailBody;

    @Value("${wcms.email.subject.approval.estimation.generated}")
    private String waterConnectionApprovalAndEstiamtionEmailSubject;
    
    @Value("${wcms.sms.payment.estimation.done.msg}")
    private String waterConnectionPaymentEstimationDoneSms;

    @Value("${wcms.email.body.payment.estimation.done}")
    private String waterConnectionPaymentEstimationDoneEmailBody;

    @Value("${wcms.email.subject.payment.estimation.done}")
    private String waterConnectionPaymentEstiamtionDoneEmailSubject;
    
    @Value("${wcms.sms.workorder.generated.msg}")
    private String waterConnectionWorkOrderGeneratedSms;

    @Value("${wcms.email.body.workorder.generated.done}")
    private String waterConnectionWorkOrderGeneratedEmailBody;

    @Value("${wcms.email.subject.workorder.generated.done}")
    private String waterConnectionWorkOrderGeneratedEmailSubject;
    
    @Value("${wcms.sms.connection.sanctioned.msg}")
    private String waterConnectionSanctionedSms;

    @Value("${wcms.email.body.connection.sanctioned.done}")
    private String waterConnectionSanctionedEmailBody;

    @Value("${wcms.email.subject.connection.sanctioned.done}")
    private String waterConnectionSanctionedEmailSubject;
    
    @Value("${wcms.sms.connection.rejected.msg}")
    private String waterConnectionRejectedSms;

    @Value("${wcms.email.body.connection.rejected.done}")
    private String waterConnectionRejectedEmailBody;

    @Value("${wcms.email.subject.connection.rejected.done}")
    private String waterConnectionRejectedEmailSubject;

}

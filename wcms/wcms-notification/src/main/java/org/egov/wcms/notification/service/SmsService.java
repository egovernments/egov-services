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


import java.text.MessageFormat;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.notification.config.PropertiesManager;
import org.egov.wcms.notification.domain.model.City;
import org.egov.wcms.notification.model.enums.NewConnectionStatus;
import org.egov.wcms.notification.repository.TenantRepository;
import org.egov.wcms.notification.web.contract.Connection;
import org.egov.wcms.notification.web.contract.ConnectionRequest;
import org.egov.wcms.notification.web.contract.SmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SmsService {

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    public void send(final ConnectionRequest connectionRequest) {

        String applicantName = "";
        String mobileNumber = "";
        final Connection connection = connectionRequest.getConnection();
        final City city = tenantRepository.fetchTenantByCode(connection.getTenantId());
        if (!connection.getIsLegacy()) {
            if (connection.getWithProperty() && connection.getProperty().getPropertyidentifier() != null) {
                applicantName = connection.getProperty().getNameOfApplicant();
                mobileNumber = connection.getProperty().getMobileNumber();
            } else if (!connection.getWithProperty() && connection.getConnectionOwner() != null) {
                applicantName = connection.getConnectionOwner().getName();
                mobileNumber = connection.getConnectionOwner().getMobileNumber();

            }
                sendSMSNotification(connection, applicantName, mobileNumber, city);
        }

    }

    public void sendSMSNotification(final Connection connection, final String applicantName, final String mobileNumber,
            final City city) {
        final SmsRequest smsRequest = new SmsRequest();
        smsRequest.setMessage(getSmsMessage(connection, applicantName, city));
        smsRequest.setMobileNumber(mobileNumber);

        log.info("NewConnectionSMS------------" + smsRequest);
        try {
            kafkaTemplate.send(propertiesManager.getSmsNotificationTopic(), propertiesManager.getSmsNotificationTopicKey(),
                    smsRequest);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getSmsMessage(final Connection connection, final String applicantName, final City city) {
        String message = null;
        if (connection.getStatus() != null
                && connection.getStatus().equalsIgnoreCase(NewConnectionStatus.CREATED.name()))
            message = MessageFormat.format(propertiesManager.getCreateNotificationMessage(),
                    applicantName, connection.getAcknowledgementNumber(),
                    city.getName());
         /*   else if (connection.getStatus() != null &&
        connection.getStatus().equalsIgnoreCase(NewConnectionStatus.ESTIMATIONNOTICEGENERATED.name())) 
              estimationCharge =connection.getEstimationCharge().get(0); message =
         MessageFormat.format(propertiesManager.getApprovalnotificationMessage(), connection.getProperty().getNameOfApplicant(),
        connection.getAcknowledgementNumber(), connection.getDonationCharge(), estimationCharge.getEstimationCharges(),
         connection.getDonationCharge() + estimationCharge.getEstimationCharges(), city.getName());*/
         
        return message;

    }

}

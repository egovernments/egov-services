/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.web.notification.sms.services;

import org.egov.web.notification.sms.config.SmsProperties;
import org.egov.web.notification.sms.models.Sms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@ConditionalOnProperty(value = "sms.enabled", havingValue = "true")
public class ExternalSMSService implements SMSService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalSMSService.class);

    private static final String SMS_RESPONSE_NOT_SUCCESSFUL = "Sms response not successful";

    private SmsProperties smsProperties;
    private RestTemplate restTemplate;

    @Autowired
    public ExternalSMSService(SmsProperties smsProperties, RestTemplate restTemplate) {
        this.smsProperties = smsProperties;
        this.restTemplate = restTemplate;
    }

    @Override
    public void sendSMS(Sms sms) {
        if (!sms.isValid()) {
            LOGGER.error(String.format("Sms %s is not valid", sms));
            return;
        }
        submitToExternalSmsService(sms);
    }

    private void submitToExternalSmsService(Sms sms) {
        try {
            HttpEntity<MultiValueMap<String, String>> request = getRequest(sms);
            String url = smsProperties.getSmsProviderURL();
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (isResponseCodeInKnownErrorCodeList(response)) {
                throw new RuntimeException(SMS_RESPONSE_NOT_SUCCESSFUL);
            }
        } catch (RestClientException e) {
            LOGGER.error("Error occurred while sending SMS to " + sms.getMobileNumber(), e);
            throw e;
        }
    }

    private boolean isResponseCodeInKnownErrorCodeList(ResponseEntity<?> response) {
        final String responseCode = Integer.toString(response.getStatusCodeValue());
        return smsProperties.getSmsErrorCodes().stream().anyMatch(errorCode -> errorCode.equals(responseCode));
    }

    private HttpEntity<MultiValueMap<String, String>> getRequest(Sms sms) {
        final MultiValueMap<String, String> requestBody = smsProperties.getSmsRequestBody(sms);
        return new HttpEntity<>(requestBody, getHttpHeaders());
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

}

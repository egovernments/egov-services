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

import org.apache.commons.lang3.StringUtils;
import org.egov.web.notification.sms.config.properties.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class SMSService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SMSService.class);

    private static final String SMS_PRIORITY_PARAM_VALUE = "sms.%s.priority.param.value";
    private static final String SENDERID_PARAM_NAME = "sms.sender.req.param.name";
    private static final String USERNAME_PARAM_NAME = "sms.sender.username.req.param.name";
    private static final String PASWRD_PARAM_NAME = "sms.sender.password.req.param.name";
    private static final String DEST_MOBILENUM_PARAM_NAME = "sms.destination.mobile.req.param.name";
    private static final String DEST_MESSAGE_PARAM_NAME = "sms.message.req.param.name";
    private static final String SMS_EXTRA_REQ_PARAMS = "sms.extra.req.params";
    private static final String MOBILE_NUMBER_PREFIX = "mobile.number.prefix";

    private ApplicationProperties applicationProperties;
    private RestTemplate restTemplate;

    @Autowired
    public SMSService(ApplicationProperties applicationProperties, RestTemplate restTemplate) {
        this.applicationProperties = applicationProperties;
        this.restTemplate = restTemplate;
    }

    public boolean sendSMS(String mobileNumber, String message, MessagePriority priority) {
        if (applicationProperties.smsEnabled()) {
            try {
                final MultiValueMap<String, String> requestBody = getSmsRequestBody(mobileNumber, message, priority);
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, getHttpHeaders());
                final String url = applicationProperties.smsProviderURL();
                ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
                return isResponseCodeNotInKnownErrorCodeList(response);
            } catch (RestClientException e) {
                LOGGER.error("Error occurred while sending SMS to " + mobileNumber, e);
                return false;
            }
        } else {
            LOGGER.info("SMS is disabled");
            return false;
        }
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private MultiValueMap<String, String> getSmsRequestBody(String mobileNumber, String message, MessagePriority
            priority) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(applicationProperties.getProperty(USERNAME_PARAM_NAME), applicationProperties.smsSenderUsername());
        map.add(applicationProperties.getProperty(PASWRD_PARAM_NAME), applicationProperties.smsSenderPassword());
        map.add(applicationProperties.getProperty(SENDERID_PARAM_NAME), applicationProperties.smsSender());
        map.add(applicationProperties.getProperty(DEST_MOBILENUM_PARAM_NAME),
                applicationProperties.getProperty(MOBILE_NUMBER_PREFIX) + mobileNumber);
        map.add(applicationProperties.getProperty(DEST_MESSAGE_PARAM_NAME), message);
        populateSmsPriority(priority, map);
        populateAdditionalSmsParameters(map);

        return map;
    }

    private void populateSmsPriority(MessagePriority priority, MultiValueMap<String, String> map) {
        if (applicationProperties.getProperty("sms.priority.enabled", Boolean.class)) {
            map.add(applicationProperties.getProperty("sms.priority.param.name"),
                    applicationProperties.getProperty(String.format(SMS_PRIORITY_PARAM_VALUE, priority.toString())));
        }
    }

    private void populateAdditionalSmsParameters(MultiValueMap<String, String> map) {
        if (StringUtils.isNotBlank(applicationProperties.getProperty(SMS_EXTRA_REQ_PARAMS))) {
            String[] extraParameters = applicationProperties.getProperty(SMS_EXTRA_REQ_PARAMS).split("&");
            if (extraParameters.length > 0)
                for (String extraParm : extraParameters) {
                    String[] paramNameValue = extraParm.split("=");
                    map.add(paramNameValue[0], paramNameValue[1]);
                }
        }
    }

    private boolean isResponseCodeNotInKnownErrorCodeList(ResponseEntity<?> response) {
        final String responseCode = Integer.toString(response.getStatusCodeValue());
        return applicationProperties.smsErrorCodes().stream().noneMatch(responseCode::startsWith);
    }

}

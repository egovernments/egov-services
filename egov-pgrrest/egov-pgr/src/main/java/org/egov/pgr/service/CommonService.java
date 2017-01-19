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

package org.egov.pgr.service;

import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.egov.infra.security.token.service.TokenService;
import org.egov.pgr.model.RequestInfo;
import org.egov.pgr.model.SMSRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional(readOnly = true)
public class CommonService {

	private static final Logger LOG = LoggerFactory.getLogger(CommonService.class);

	private static final String CITIZEN_REG_SERVICE = "Citizen Registration";

	@Autowired
	private TokenService tokenService;

	@Transactional
	public boolean sendOTPMessage(String mobileNumber) {
		String otp = randomNumeric(5);
		tokenService.generate(otp, mobileNumber, CITIZEN_REG_SERVICE);
		try {
			sendMessage(mobileNumber, " Use OTP " + otp + " for portal registration");
		} catch (JsonProcessingException e) {
			LOG.error("Error while sending otp for mobile number: " + mobileNumber);
			e.printStackTrace();
		}
		return TRUE;
	}

	private void sendMessage(String mobileNumber, String message) throws JsonProcessingException {
		Properties props = new Properties();
		props.put("bootstrap.servers", "kafka:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 1);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		Producer producer = new KafkaProducer<>(props);

		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setAction("POST");
		requestInfo.setApi_id("org.egov.pgr");
		requestInfo.setAuth_token("aggaidgqiwgudiqwud");
		requestInfo.setDid("eewfwefwefwef");

		ObjectMapper mapper = new ObjectMapper();

		SMSRequest smsRequest = new SMSRequest();
		smsRequest.setRequestInfo(requestInfo);
		smsRequest.setMobile_no(mobileNumber);
		smsRequest.setMessage(message);

		producer.send(new ProducerRecord<String, String>("egov-notification-sms", "pgrrest",
				mapper.writeValueAsString(smsRequest)));
		producer.flush();
		producer.close();
	}
}

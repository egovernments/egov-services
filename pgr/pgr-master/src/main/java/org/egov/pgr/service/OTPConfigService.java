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

import java.util.ArrayList;
import java.util.List;

import org.egov.pgr.domain.model.OTPConfig;
import org.egov.pgr.producers.PGRProducer;
import org.egov.pgr.repository.OTPConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OTPConfigService {
	
	public static final Logger logger = LoggerFactory.getLogger(OTPConfigService.class);
	
	@Autowired
	private PGRProducer pgrProducer;
	
	@Autowired
	private OTPConfigRepository otpConfigRepository;
	
	public OTPConfig createOTPConfig(final String topic, final String key,
			final OTPConfig otpConfig) {
		final ObjectMapper mapper = new ObjectMapper();
		String otpConfigValue = null;
		try {
			logger.info("OTP Config::" + otpConfig);
			otpConfigValue = mapper.writeValueAsString(otpConfig);
			logger.info("Value to the Queue : OTP Config Value::" + otpConfigValue);
		} catch (final JsonProcessingException e) {
			logger.error("Exception Encountered : " + e);
		}
		try {
			pgrProducer.sendMessage(topic, key, otpConfigValue);
		} catch (final Exception e) {
			logger.error("Exception while posting to kafka Queue : " + e);
			return otpConfig;
		}
		logger.info("Producer successfully posted the request to Queue");
		return otpConfig;
	}
	
	public OTPConfig updateOTPConfig(final String topic, final String key,
			final OTPConfig otpConfig) {
		final ObjectMapper mapper = new ObjectMapper();
		String otpConfigValue = null;
		try {
			logger.info("OTP Config::" + otpConfig);
			otpConfigValue = mapper.writeValueAsString(otpConfig);
			logger.info("Value to the Queue : OTP Config Value::" + otpConfigValue);
		} catch (final JsonProcessingException e) {
			logger.error("Exception Encountered : " + e);
		}
		try {
			pgrProducer.sendMessage(topic, key, otpConfigValue);
		} catch (final Exception e) {
			logger.error("Exception while posting to kafka Queue : " + e);
			return otpConfig;
		}
		logger.info("Producer successfully posted the request to Queue");
		return otpConfig;
	}
	
	public OTPConfig create(OTPConfig otpConfig) {
		logger.info("Persisting OTP Config Data");
		return otpConfigRepository.persistOTPConfig(otpConfig);
	}
	
	public boolean update(OTPConfig otpConfig) {
		logger.info("Updating OTP Config Data");
		return otpConfigRepository.updateOTPConfig(otpConfig);
	}
	
	public List<org.egov.pgr.web.contract.OTPConfig> getAllOtpConfig(List<String> tenantList) {
		List<OTPConfig> configList = otpConfigRepository.getAllOtpConfig(tenantList);
		for(int i=0;i<configList.size();i++){
			tenantList.remove(configList.get(i).getTenantId());
		}
		for (int i = 0; i < tenantList.size(); i++) {
			OTPConfig config = new OTPConfig();
			config.setTenantId(tenantList.get(i));
			config.setOtpConfigEnabled(false);
			configList.add(config);
		}
		return modelToContract(configList);

	}
	
	private List<org.egov.pgr.web.contract.OTPConfig> modelToContract(List<OTPConfig> configList){
		List<org.egov.pgr.web.contract.OTPConfig> contractConfigList = new ArrayList<>();
		for(int i=0; i<configList.size(); i++) {
			org.egov.pgr.web.contract.OTPConfig con = new org.egov.pgr.web.contract.OTPConfig();
			con.setTenantId(configList.get(i).getTenantId());
			con.setOtpEnabledForAnonymousComplaint(configList.get(i).isOtpConfigEnabled());
			contractConfigList.add(con);
		}
		return contractConfigList;
	}

}

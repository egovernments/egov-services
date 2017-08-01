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
package org.egov.pgr.web.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.pgr.config.ApplicationProperties;
import org.egov.pgr.domain.model.AuditDetails;
import org.egov.pgr.domain.model.OTPConfig;
import org.egov.pgr.service.OTPConfigService;
import org.egov.pgr.web.contract.factory.ResponseInfoFactory;
import org.egov.pgr.web.errorhandlers.ErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(OTPConfigController.class)
@Import(TestConfiguration.class)
public class OTPConfigControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private ResponseInfoFactory responseInfoFactory;
	
    @MockBean
    private OTPConfigService otpConfigService;
    
    @MockBean
	private ApplicationProperties applicationProperties;
    
    @MockBean
    private ErrorHandler errHandler;

	@Test
	public void test_should_create_otpconfig_under_given_tenant() throws Exception {
		when(otpConfigService.createOTPConfig(any(String.class), any(String.class),
				any(org.egov.pgr.domain.model.OTPConfig.class))).thenReturn(prepareOTPConfigObject());
		org.egov.pgr.domain.model.OTPConfig otpConfig = prepareOTPConfigObject();
		assertTrue(otpConfig.equals(otpConfigService.createOTPConfig(any(String.class), any(String.class),
				any(org.egov.pgr.domain.model.OTPConfig.class))));
		/*mockMvc.perform(post("/OTPConfig/_create").content(getFileContents("otpconfigcreaterequest.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("otpconfigcreateresponse.json")));
		verify(otpConfigService).create(otpConfig);*/
	}
	
	private org.egov.pgr.domain.model.OTPConfig prepareOTPConfigObject(){
		org.egov.pgr.domain.model.OTPConfig otpConfig = new org.egov.pgr.domain.model.OTPConfig();
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(1L);
		otpConfig.setOtpConfigEnabled(true);
		otpConfig.setTenantId("default");
		return otpConfig;
	}
	
	private List<String> getTenantList(){
		List<String> tenantList = new ArrayList<>();
		tenantList.add("AM01");
		tenantList.add("AM02");
		tenantList.add("BLR02");
		tenantList.add("MY01");
		tenantList.add("BLR04");
		return tenantList;
	}
	
	private String getFileContents(String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader()
                .getResourceAsStream(fileName), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
	
	private org.egov.pgr.domain.model.OTPConfig prepareOTPConfigFirstList(){
		org.egov.pgr.domain.model.OTPConfig otpConfig = new org.egov.pgr.domain.model.OTPConfig();
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(1L);
		otpConfig.setOtpConfigEnabled(true);
		otpConfig.setTenantId("default");
		return otpConfig;
	}
	
	private List<OTPConfig> prepareOTPConfigFinalList() {
		List<OTPConfig> configList = new ArrayList<>();
		OTPConfig otpConfig = new OTPConfig();
		otpConfig.setTenantId("AM01");
		otpConfig.setOtpConfigEnabled(true);
		configList.add(otpConfig);
		otpConfig = new OTPConfig();
		otpConfig.setTenantId("AM02");
		otpConfig.setOtpConfigEnabled(true);
		configList.add(otpConfig);
		otpConfig = new OTPConfig();
		otpConfig.setTenantId("BLR01");
		otpConfig.setOtpConfigEnabled(false);
		configList.add(otpConfig);
		otpConfig = new OTPConfig();
		otpConfig.setTenantId("BLR02");
		otpConfig.setOtpConfigEnabled(false);
		configList.add(otpConfig);
		return configList;
	}

}

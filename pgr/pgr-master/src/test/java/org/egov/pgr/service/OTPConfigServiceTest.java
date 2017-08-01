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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.pgr.domain.model.AuditDetails;
import org.egov.pgr.domain.model.OTPConfig;
import org.egov.pgr.producers.PGRProducer;
import org.egov.pgr.repository.OTPConfigRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OTPConfigServiceTest {
	
	@Mock
    private OTPConfigRepository otpConfigRepository;

    @Mock
    private PGRProducer pgrProducer;

    @InjectMocks
    private OTPConfigService otpConfigService;

	@Test
	public void test_should_create_otpconfig_entry() {
		OTPConfig otpConfig = prepareOTPConfigFirstObject();
		when(otpConfigRepository.persistOTPConfig(any(OTPConfig.class))).thenReturn(otpConfig);
		assertTrue(otpConfig.equals(otpConfigService.create(otpConfig)));
	}
	
	@Test
	public void test_should_update_otpconfig_entry() {
		OTPConfig otpConfig = prepareOTPConfigFirstObject();
		when(otpConfigRepository.updateOTPConfig(any(OTPConfig.class))).thenReturn(true);
		assertTrue(otpConfigService.update(otpConfig));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_should_get_otpconfig_for_all_the_tenants(){
		List<String> tenantList = prepareTenantList();
		when(otpConfigRepository.getAllOtpConfig(any(List.class))).thenReturn(prepareOTPConfigList());
		List<org.egov.pgr.web.contract.OTPConfig> returnList = modelToContract(prepareOTPConfigFinalList());
		assertTrue(otpConfigService.getAllOtpConfig(tenantList).equals(returnList));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_should_get_otpconfig_for_all_the_tenants_even_if_records_unavailable_in_db(){
		List<String> tenantList = prepareTenantList();
		when(otpConfigRepository.getAllOtpConfig(any(List.class))).thenReturn(prepareOTPConfigList());
		List<org.egov.pgr.web.contract.OTPConfig> returnList = modelToContract(prepareOTPConfigList());
		assertTrue(!otpConfigService.getAllOtpConfig(tenantList).equals(returnList));
	}
	
	private List<String> prepareTenantList(){
		List<String> tenantList = new ArrayList<>();
		tenantList.add("AM01");
		tenantList.add("AM02");
		tenantList.add("BLR01");
		tenantList.add("BLR02");
		return tenantList;
	}
	
	private OTPConfig prepareOTPConfigFirstObject(){
		OTPConfig otpConfig = new OTPConfig();
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
	
	private List<OTPConfig> prepareOTPConfigList() {
		List<OTPConfig> configList = new ArrayList<>();
		OTPConfig otpConfig = new OTPConfig();
		otpConfig.setTenantId("AM01");
		otpConfig.setOtpConfigEnabled(true);
		configList.add(otpConfig);
		otpConfig = new OTPConfig();
		otpConfig.setTenantId("AM02");
		otpConfig.setOtpConfigEnabled(true);
		configList.add(otpConfig);
		return configList;
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

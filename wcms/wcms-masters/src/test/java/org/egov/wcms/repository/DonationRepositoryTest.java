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

package org.egov.wcms.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.wcms.model.AuditDetails;
import org.egov.wcms.model.Donation;
import org.egov.wcms.repository.rowmapper.DonationRowMapper;
import org.egov.wcms.web.contract.DonationRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class DonationRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private DonationRowMapper donationRowMapper;
    
    @InjectMocks
    private DonationRepository donationRepository;
    
    @Test
    public void test_Should_Create_Donation_Valid() {
    	DonationRequest donationRequest = getDonationRequest();
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(donationRequest.equals(donationRepository.persistDonationDetails(donationRequest)));
    }
    
    @Test
    public void test_Should_Create_Donation_Invalid() {
    	DonationRequest donationRequest = getDonationRequest();
    	Donation donation = donationRequest.getDonation();
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(!donation.equals(donationRepository.persistDonationDetails(donationRequest)));
    }
    
    @Test
	public void test_Should_Find_Donation_Valid() {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		DonationRequest donationRequest = getDonationRequest();
    	Donation donation = donationRequest.getDonation();
		String queryString = "MyQuery" ;
		List<Donation> donations = new ArrayList<>();
		when(jdbcTemplate.query(queryString, preparedStatementValues.toArray(), donationRowMapper))
				.thenReturn(donations);

		assertTrue(
				donations.equals(donationRepository.getDonationList(donation)));
	}

    private DonationRequest getDonationRequest(){
    	DonationRequest donationReq = new DonationRequest();
    	Donation donation = new Donation();
    	donation.setCategoryTypeId(1);
    	donation.setPropertyTypeId(2);
    	donation.setUsageTypeId(3);
    	donation.setMaxHSCPipeSizeId(123);
    	donation.setMinHSCPipeSizeId(12);
    	donation.setFromDate(new Date());
    	donation.setToDate(new Date());
    	
    	donation.setActive(true);
    	RequestInfo requestInfo = new RequestInfo();
    	User newUser = new User();
    	newUser.setId(2L);
    	requestInfo.setUserInfo(newUser);
    	donationReq.setRequestInfo(requestInfo);
    	donationReq.setDonation(donation);
    	return donationReq;
    }
	
	@Test
	public void test_Should_Modify_Donation() throws Exception{
        DonationRequest donationRequest = new DonationRequest();
        
        RequestInfo requestInfo = new RequestInfo();
        User user = new User();
        user.setId(1L);
        requestInfo.setUserInfo(user);
        Donation donation = new  Donation();
        AuditDetails auditDetails = new AuditDetails();
        donation.setAuditDetails(auditDetails);
        donation.setActive(true);
        donation.setCategoryTypeId(12);
        donation.setPropertyTypeId(23);
        donation.setUsageTypeId(13);
        donation.setFromDate(new Date());
        donation.setToDate(new Date());
        donation.setMaxHSCPipeSizeId(123);
        donation.setMinHSCPipeSizeId(234);
        donation.getAuditDetails().setCreatedBy(1L);
        donationRequest.setRequestInfo(requestInfo);
        donationRequest.setDonation(donation);
        
        assertNotNull(donationRepository.persistModifyDonationDetails(donationRequest));
        
	}
	
	@Test(expected = Exception.class)
	public void test_throwException_Modify_Donation() throws Exception{
        DonationRequest donationRequest = new DonationRequest();
        RequestInfo requestInfo = new RequestInfo();
        Donation donation = new Donation();
        donation.setActive(true);
        donation.getAuditDetails().setCreatedBy(1L);
        donation.setCategoryTypeId(12);
        donation.setPropertyTypeId(23);
        donation.setUsageTypeId(13);
        donation.setFromDate(new Date());
        donation.setToDate(new Date());
        donation.setMaxHSCPipeSizeId(123);
        donation.setMinHSCPipeSizeId(234);
        
        donationRequest.setRequestInfo(requestInfo);
        donationRequest.setDonation(donation);
        
        assertNotNull(donationRepository.persistModifyDonationDetails(donationRequest));
        
	}
    
}
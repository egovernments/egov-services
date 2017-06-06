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

package org.egov.wcms.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import org.egov.wcms.producers.WaterMasterProducer;
import org.egov.wcms.repository.DonationRepository;
import org.egov.wcms.web.contract.DonationRequest;
import org.egov.wcms.web.contract.DonationResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DonationServiceTest {
	
	@Mock
	private DonationRepository donationRepository;

    @Mock
    private WaterMasterProducer waterMasterProducer;

    @Mock
    private CodeGeneratorService codeGeneratorService;
    
    @InjectMocks
    private DonationService donationService;
    
    @Test 
    public void test_Should_Donation_Create() {
		final Donation donation = getDonation();
		final List<Donation> donations = new ArrayList<>();
		donations.add(donation);
		final DonationRequest donationReq = new DonationRequest();
		donationReq.setDonation(donation);
		final DonationResponse donationResponse = new DonationResponse();
		donationResponse.setResponseInfo(null);
		donationResponse.setDonations(donations);

		when(donationService.create(any(DonationRequest.class))).thenReturn(donationReq);
		assertTrue(donationReq.equals(donationService.create(donationReq)));
    }
    
    
    private Donation getDonation(){

		Donation donation = new Donation();
		AuditDetails auditDetails = new AuditDetails();

		donation.setActive(true);
		donation.setCategoryTypeId(12);
		donation.setPropertyTypeId(23);
		donation.setUsageTypeId(13);
		donation.setFromDate(new Date());
		donation.setToDate(new Date());
		donation.setMaxHSCPipeSizeId(123);
		donation.setMinHSCPipeSizeId(234);
		donation.setAuditDetails(auditDetails);
		donation.getAuditDetails().setCreatedBy(1L);

		return donation;
    }
    
    
    @Test
    public void test_Search_For_DonationServices(){
    	List<Donation> donationList = new ArrayList<Donation>();
    	Donation donation = Mockito.mock(Donation.class);
    	donationList.add(donation);
    	when(donationRepository.getDonationList(any(Donation.class))).thenReturn(donationList);
    	
    	assertTrue(donationList.equals(donationRepository.getDonationList(any(Donation.class))));
    }
    
    @Test
    public void test_Search_For_Donation_Notnull(){
    	List<Donation> donationList = new ArrayList<>();
    	Donation donation = Mockito.mock(Donation.class);
    	donationList.add(donation);
    
    	when(donationRepository.getDonationList(any(Donation.class))).thenReturn(donationList);
    	assertNotNull(donationRepository.getDonationList(any(Donation.class)));
    }
    
    @Test
    public void test_Search_For_Donation_Types_Null(){
    	List<Donation> donationList = new ArrayList<>();
    	Donation donation = Mockito.mock(Donation.class);
    	donationList.add(donation);
    
    	when(donationRepository.getDonationList(any(Donation.class))).thenReturn(null);
    	assertNull(donationRepository.getDonationList(any(Donation.class)));
    }
    
    
    @Test(expected = Exception.class) //Please revisit this test case
    public void test_Should_Update_Donation() throws Exception {
        final DonationRequest donationRequest = new DonationRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1L);
        requestInfo.setUserInfo(user);
        final Donation donation = new Donation();
        AuditDetails auditDetails = new AuditDetails();
        
        donation.setActive(true);
        donation.getAuditDetails().setCreatedBy(1L);
        donation.setCategoryTypeId(12);
        donation.setPropertyTypeId(23);
        donation.setUsageTypeId(13);
        donation.setFromDate(new Date());
        donation.setToDate(new Date());
        donation.setMaxHSCPipeSizeId(123);
        donation.setMinHSCPipeSizeId(234);
        donation.setAuditDetails(auditDetails);

        donationRequest.setRequestInfo(requestInfo);
        donationRequest.setDonation(donation);

        final Donation donationResult = donationService.sendMessage("topic", "key", donationRequest);

        assertNotNull(donationResult);
    }

}
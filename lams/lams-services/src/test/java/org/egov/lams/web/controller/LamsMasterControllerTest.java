/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.lams.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.lams.TestConfiguration;
import org.egov.lams.model.RentIncrementType;
import org.egov.lams.model.ReservationCategory;
import org.egov.lams.repository.RentIncrementRepository;
import org.egov.lams.service.AgreementMasterService;
import org.egov.lams.util.FileUtils;
import org.egov.lams.web.contract.factory.ResponseInfoFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(LamsMasterController.class)
@Import(TestConfiguration.class)
public class LamsMasterControllerTest {
	
	@Autowired 
	private MockMvc mockMvc;
	
	@MockBean
	private ResponseInfoFactory responseInfoFactory;
	
	@MockBean
	private RentIncrementRepository rentIncrementService;
	
	@MockBean
	private AgreementMasterService agreementMasterService;

	@Test
	public void test_Should_Return_Status() throws Exception{
	       
	        mockMvc.perform(get("/getstatus"))
	                .andExpect(status().isOk())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	                .andExpect(content().json(getFileContents("status.json")));
	}
	
	@Test
	public void test_Should_Return_PaymentCycle() throws Exception{
	       
	        mockMvc.perform(get("/getpaymentcycle"))
	                .andExpect(status().isOk())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	                .andExpect(content().json(getFileContents("paymentcycle.json")));
	}
	
	@Test
	public void test_Should_Return_NatureOfAllotment() throws Exception{
	       
	        mockMvc.perform(get("/getnatureofallotment"))
	                .andExpect(status().isOk())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	                .andExpect(content().json(getFileContents("natureofallotment.json")));
	}

	@Test
	public void test_Should_Return_Source() throws Exception {
		mockMvc.perform(get("/getsource"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("source.json")));
	}

	@Test
	public void test_Should_Return_RentIncrementType() throws Exception {
		List<RentIncrementType> rentIncrementTypes = new ArrayList<>();
		RentIncrementType rentIncrementType = new RentIncrementType();
		rentIncrementType.setTenantId("1");
		rentIncrementType.setType("type1");
		rentIncrementTypes.add(rentIncrementType);
		when(rentIncrementService.getRentIncrements(rentIncrementType.getTenantId(), rentIncrementType.getType()))
				.thenReturn(rentIncrementTypes);

		mockMvc.perform(get("/getrentincrements?tenantId=" + rentIncrementType.getTenantId() + "&basisOfAllotment="
				+ rentIncrementType.getType())).andExpect(status().isOk())
				.andExpect(content().json(getFileContents("rentincrementtype.json")));
	}

	private String getFileContents(String fileName) throws IOException {
		return new FileUtils().getFileContents(fileName);
	}
	
	@Test
	public void test_Should_return_Reservation_Categories() throws Exception {
		List<ReservationCategory> reservationCategories = new ArrayList<>();
		ReservationCategory reservationCategory = new ReservationCategory();
		reservationCategory.setCode("SC");
		reservationCategory.setName("Scheduled Caste");
		reservationCategory.setIsActive(true);
		reservationCategory.setTenantId("default");
		reservationCategories.add(reservationCategory);
		
		when(agreementMasterService.getReservationCategories(reservationCategory.getTenantId())).thenReturn(reservationCategories);

		mockMvc.perform(get("/getreservations?tenantId=" + reservationCategory.getTenantId()))
				.andExpect(status().isOk()).andExpect(content().json(getFileContents("reservations.json")));
	}
}

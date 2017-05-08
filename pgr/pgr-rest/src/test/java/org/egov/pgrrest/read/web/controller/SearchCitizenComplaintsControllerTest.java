/*
package org.egov.pgr.read.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.pgr.TestConfiguration;
import org.egov.pgr.common.model.AuthenticatedUser;
import org.egov.pgr.common.model.Complainant;
import org.egov.pgr.read.domain.model.Complaint;
import org.egov.pgr.read.domain.model.ComplaintLocation;
import org.egov.pgr.read.domain.model.ComplaintType;
import org.egov.pgr.read.domain.model.Coordinates;
import org.egov.pgr.common.model.Role;
import org.egov.pgr.common.model.UserType;
import org.egov.pgr.read.domain.service.ComplaintService;
import org.egov.pgr.common.repository.UserRepository;
import org.egov.pgr.common.contract.GetUserByIdResponse;
import org.egov.pgr.read.web.contract.User;
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
@WebMvcTest(SearchCitizenComplaintsController.class)
@Import(TestConfiguration.class)
public class SearchCitizenComplaintsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private ComplaintService complaintService;

	@Test
	public void test_should_return_bad_request_when_user_id_is_empty() throws Exception {
		mockMvc.perform(post("/searchcitizencomplaints")
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isBadRequest());
	}

	@Test
	public void test_should_fetch_citizen_complaints_for_given_user_id() throws Exception {
		List<Role> roles = new ArrayList<>();
		roles.add(new Role(1L, "Citizen"));
		roles.add(new Role(2L, "Employee"));
		AuthenticatedUser user = AuthenticatedUser.builder().mobileNumber("1234567890").emailId("abc@xyz.com")
				.name("ram").id(1).anonymousUser(false).roles(roles).type(UserType.CITIZEN).build();
		final Complaint complaint = Complaint.builder().crn("crn124").authenticatedUser(user)
				.complaintLocation(new ComplaintLocation(new Coordinates(0.0, 0.0), null, "34"))
				.complainant(new Complainant("kumar", null, null, "mico layout", "user"))
				.complaintType(new ComplaintType("abc", "complaintCode")).description("no road lights").build();
		when(userRepository.findUserById(any(Long.class))).thenReturn(getCitizen());
		when(complaintService.getAllModifiedCitizenComplaints(any(Long.class)))
				.thenReturn(Collections.singletonList(complaint));
		mockMvc.perform(post("/searchcitizencomplaints").param("userId", "1").header("X-CORRELATION-ID", "someId"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("citizenComplaintsServiceResponse.json")));
	}

	private String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public GetUserByIdResponse getCitizen() {
		User user = User.builder().id(1L).type(UserType.CITIZEN.toString()).build();
		return GetUserByIdResponse.builder().user(Collections.singletonList(user)).build();
	}
}
*/

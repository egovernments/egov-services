package org.egov.user.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.user.TestConfiguration;
import org.egov.user.model.User;
import org.egov.user.model.UserReq;
import org.egov.user.model.UserRes;
import org.egov.user.model.UserSearchCriteria;
import org.egov.user.model.enums.Gender;
import org.egov.user.model.enums.Type;
import org.egov.user.service.NewUserService;
import org.egov.user.utils.FileUtil;
import org.egov.user.web.contract.factory.ResponseFactory;
import org.egov.user.web.validator.NewUserValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(NewUserController.class)
@Import(TestConfiguration.class)
public class NewUserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private NewUserService newUserService;
	
	@MockBean
	private NewUserValidator userValidator;
	
	@MockBean
	private ResponseFactory responseFactory;
	
	@Test
	@WithMockUser
	public void test_Should_Search_User() throws IOException, Exception{
		List<User> users=new ArrayList<User>();
		users.add(getUser());
		UserRes userRes =new UserRes();
		userRes.setResponseInfo(new ResponseInfo());
		userRes.setUsers(users);
		
		when(newUserService.search(any(UserSearchCriteria.class))).thenReturn(userRes);
		
		mockMvc.perform(post("/v110/_search").param("tenantId", "default")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("requestinfowrapper.json"))).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("newUserResponse.json")));
	}
	
	@Test
	@WithMockUser
	public void test_Should_Update_User() throws IOException, Exception {
		List<User> users=new ArrayList<User>();
		users.add(getUser());
		UserRes userRes =new UserRes();
		userRes.setResponseInfo(new ResponseInfo());
		userRes.setUsers(users);
		
		when(newUserService.updateAsync(any(UserReq.class))).thenReturn(userRes);
		
		mockMvc.perform(post("/v110/_update").contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("newUserRequest.json"))).andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("newUserResponse.json")));
	
	}
	
	@Test
	@WithMockUser
	public void test_Should_Create_User() throws IOException, Exception {
		List<User> users=new ArrayList<User>();
		users.add(getUser());
		UserRes userRes =new UserRes();
		userRes.setResponseInfo(new ResponseInfo());
		userRes.setUsers(users);
		
		when(newUserService.createAsync(any(UserReq.class))).thenReturn(userRes);
		
		mockMvc.perform(post("/v110/_create").contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("newUserRequest.json"))).andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("newUserResponse.json")));
	
	}
	
	@Test
	@WithMockUser
	public void test_Should_Create_User_Exception() throws IOException, Exception {
		List<User> users=new ArrayList<User>();
		users.add(getUser());
		UserRes userRes =new UserRes();
		userRes.setResponseInfo(new ResponseInfo());
		userRes.setUsers(users);
		
		when(newUserService.createAsync(any(UserReq.class))).thenReturn(userRes);
		
		mockMvc.perform(post("/v110/_create").contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("newUserRequestForExeption.json"))).andExpect(status().isBadRequest());
	
	}

	private String getFileContents(String fileName) throws IOException {
		return new FileUtil().getFileContents(fileName);
	}
	
	private User getUser(){
		
		User user=new User();
		
		user.setId(12l);
		user.setTenantId("default");
		user.setUsername("manas");
		user.setMobile("9865433212");
		user.setPassword("demo");
		user.setEmail("abc@xyz.com");
		user.setSalutation("MR.");
		user.setName("manas");
		user.setGender(Gender.fromValue("MALE"));
		user.setAadhaarNumber("123456788765");
		user.setActive(true);
		user.setPwdExpiryDate(4070908800000l);
		user.setLocale("en_IN");
		user.setType(Type.valueOf("EMPLOYEE"));
		user.setAccountLocked(false);
		
		return user;
	}
}

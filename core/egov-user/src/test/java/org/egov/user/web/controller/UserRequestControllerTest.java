package org.egov.user.web.controller;

import org.apache.commons.io.IOUtils;
import org.egov.user.domain.exception.InvalidUserException;
import org.egov.user.domain.exception.OtpValidationPendingException;
import org.egov.user.domain.service.UserService;
import org.egov.user.persistence.entity.Address;
import org.egov.user.persistence.entity.Role;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.entity.enums.*;
import org.egov.user.security.SecurityConfig;
import org.egov.user.security.oauth2.custom.CustomAuthenticationProvider;
import org.egov.user.security.oauth2.custom.CustomUserDetailService;
import org.egov.user.web.contract.RequestInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.*;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserRequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    UserService userService;

    @MockBean
    CustomAuthenticationProvider customAuthenticationProvider;

    @MockBean
    CustomUserDetailService customUserDetailService;

    @Test
    public void testShouldGetUserById() throws Exception {
        when(userService.getUsersById(asList(1L, 2L))).thenReturn(getUserEntities());

        mockMvc.perform(post("/_search/").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(getFileContents("getUserByIdRequest.json"))).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("getUserByIdResponse.json")));
    }

    @Test
    public void testShouldGetUserByUserName() throws Exception {
        when(userService.getUserByUsername("userName")).thenReturn(populateUser());

        mockMvc.perform(post("/_search/").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(getFileContents("getUserByUserNameRequest.json"))).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("getUserByUserNameResponse.json")));
    }

    @Test
    public void testShouldReturnErrorForBadRequest() throws Exception {
        mockMvc.perform(post("/_search/").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(getFileContents("getUserByIdBadRequest.json"))).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("getUserByIdErrorResponse.json")));
    }

    @Test
    public void testShouldRegisterACitizen() throws Exception {
        when(userService.save(any(RequestInfo.class), any(org.egov.user.domain.model.User.class), eq(Boolean.TRUE))).thenReturn(buildUser());

        String fileContents = getFileContents("createValidatedCitizenSuccessRequest.json");
        mockMvc.perform(post("/_create/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(fileContents)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("createValidatedCitizenSuccessResponse.json")));
    }

    @Test
    public void testShouldThrowErrorWhileRegisteringWithInvalidCitizen() throws Exception {
        InvalidUserException exception = new InvalidUserException(org.egov.user.domain.model.User.builder().build());
        when(userService.save(any(RequestInfo.class), any(org.egov.user.domain.model.User.class), eq(Boolean.TRUE))).thenThrow(exception);

        String fileContents = getFileContents("createCitizenUnsuccessfulRequest.json");
        mockMvc.perform(post("/_create/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(fileContents)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("createCitizenUnsuccessfulResponse.json")));
    }

    @Test
    public void testShouldThrowErrorWhileRegisteringWithPendingOtpValidation() throws Exception {
        OtpValidationPendingException exception = new OtpValidationPendingException(org.egov.user.domain.model.User.builder().build());
        when(userService.save(any(RequestInfo.class), any(org.egov.user.domain.model.User.class), eq(Boolean.TRUE))).thenThrow(exception);

        String fileContents = getFileContents("createValidatedCitizenSuccessRequest.json");
        mockMvc.perform(post("/_create/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(fileContents)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("createCitizenOtpFailureResponse.json")));
    }

    private User buildUser() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.set(1917, Calendar.MARCH, 8, 11, 15, 36);
        Date dateOfBirth = c.getTime();
        c.set(2017, Calendar.FEBRUARY, 8, 11, 15, 36);
        Date createdDate = c.getTime();
        c.set(2018, Calendar.FEBRUARY, 8, 11, 15, 36);
        Date pwdExpiryDate = c.getTime();
        Role role = new Role(12L, "CITIZEN", "Citizen role");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User createdBy = new User();
        createdBy.setId(22L);
        User user = new User();
        user.setId(12L);
        user.setName("Jamaal Bhai");
        user.setUsername("jamaalbhai");
        user.setSalutation("dawakhana@charminar");
        user.setGender(Gender.MALE);
        user.setMobileNumber("9988776655");
        user.setEmailId("jamalbhai@maildrop.cc");
        user.setPan("AITGC5624P");
        user.setAadhaarNumber("96a70a9a-03bd-11e7-93ae-92361f002671");
        user.setActive(Boolean.TRUE);
        user.setDob(dateOfBirth);
        user.setPwdExpiryDate(pwdExpiryDate);
        user.setLocale("en_IN");
        user.setType(UserType.CITIZEN);
        user.setAccountLocked(Boolean.FALSE);
        user.setBloodGroup(BloodGroup.O_POSITIVE);
        user.setIdentificationMark("Head is missing on the body");
        user.setCreatedDate(createdDate);
        user.setLastModifiedDate(createdDate);
        user.setCreatedBy(createdBy);
        user.setLastModifiedBy(createdBy);
        user.setRoles(roles);
        return user;
    }

    private List<User> getUserEntities() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(1990, Calendar.JULY, 1, 11, 11, 11);
        Date date = calendar.getTime();

        User user = User.builder().id(1L).username("userName").salutation("salutation").name("name")
                .gender(Gender.FEMALE).mobileNumber("mobileNumber1").emailId("email").altContactNumber("mobileNumber2")
                .pan("pan").aadhaarNumber("aadhaarNumber").address(getAddressList()).active(true).dob(date)
                .pwdExpiryDate(date).locale("en_IN").type(UserType.CITIZEN).accountLocked(false).roles(getListOfRoles())
                .guardian("name of relative").guardianRelation(GuardianRelation.Father)
                .signature("7a9d7f12-bdcb-4487-9d43-709838a0ad39").bloodGroup(BloodGroup.A_POSITIVE)
                .photo("3b26fb49-e43d-401b-899a-f8f0a1572de0").identificationMark("identification mark").build();

        user.setCreatedBy(user);
        user.setCreatedDate(date);
        user.setLastModifiedBy(user);
        user.setLastModifiedDate(date);

        User user2 = User.builder().id(2L).username("userName").salutation("salutation").name("name")
                .gender(Gender.FEMALE).mobileNumber("mobileNumber1").emailId("email").altContactNumber("mobileNumber2")
                .pan("pan").aadhaarNumber("aadhaarNumber").address(getAddressList()).active(true).dob(date)
                .pwdExpiryDate(date).locale("en_IN").type(UserType.CITIZEN).accountLocked(false).roles(getListOfRoles())
                .guardian("name of relative").guardianRelation(GuardianRelation.Father)
                .signature("7a9d7f12-bdcb-4487-9d43-709838a0ad39").bloodGroup(BloodGroup.AB_POSITIVE)
                .photo("3b26fb49-e43d-401b-899a-f8f0a1572de0").identificationMark("identification mark").build();

        user2.setCreatedBy(user2);
        user2.setCreatedDate(date);
        user2.setLastModifiedBy(user2);
        user2.setLastModifiedDate(date);

        return asList(user, user2);
    }

    private User populateUser() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(1990, Calendar.JULY, 1, 11, 11, 11);
        Date date = calendar.getTime();

        User user = User.builder().id(1L).username("userName").salutation("salutation").name("name")
                .gender(Gender.FEMALE).mobileNumber("mobileNumber1").emailId("email").altContactNumber("mobileNumber2")
                .pan("pan").aadhaarNumber("aadhaarNumber").address(getAddressList()).active(true).dob(date)
                .pwdExpiryDate(date).locale("en_IN").type(UserType.CITIZEN).accountLocked(false).roles(getListOfRoles())
                .guardian("name of relative").guardianRelation(GuardianRelation.Father)
                .signature("7a9d7f12-bdcb-4487-9d43-709838a0ad39").bloodGroup(BloodGroup.A_POSITIVE)
                .photo("3b26fb49-e43d-401b-899a-f8f0a1572de0").identificationMark("identification mark").build();

        user.setCreatedBy(user);
        user.setCreatedDate(date);
        user.setLastModifiedBy(user);
        user.setLastModifiedDate(date);

        return user;
    }

    private List<Address> getAddressList() {
        return asList(Address.builder().id(1L).type(AddressType.PERMANENT).houseNoBldgApt("house number 1")
                        .areaLocalitySector("area/locality/sector").streetRoadLine("street/road/line").landmark("landmark")
                        .cityTownVillage("city/town/village 1").postOffice("post office").subDistrict("sub district")
                        .district("district").state("state").country("country").pinCode("pincode 1").build(),

                Address.builder().id(1L).type(AddressType.CORRESPONDENCE).houseNoBldgApt("house number 2")
                        .areaLocalitySector("area/locality/sector").streetRoadLine("street/road/line")
                        .landmark("landmark").cityTownVillage("city/town/village 2").postOffice("post office")
                        .subDistrict("sub district").district("district").state("state").country("country")
                        .pinCode("pincode 2").build());
    }

    private Set<Role> getListOfRoles() {
        User user = User.builder().id(0L).build();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(1990, Calendar.JULY, 1);

        Role role1 = Role.builder().id(1L).name("name of the role 1").description("description").build();
        role1.setCreatedBy(user);
        role1.setCreatedDate(calendar.getTime());
        role1.setLastModifiedBy(user);
        role1.setLastModifiedDate(calendar.getTime());

        Role role2 = Role.builder().id(2L).name("name of the role 2").description("description").build();
        role2.setCreatedBy(user);
        role2.setCreatedDate(calendar.getTime());
        role2.setLastModifiedBy(user);
        role2.setLastModifiedDate(calendar.getTime());

        return new HashSet<Role>() {
            {
                add(role1);
                add(role2);
            }
        };
    }

    private String getFileContents(String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
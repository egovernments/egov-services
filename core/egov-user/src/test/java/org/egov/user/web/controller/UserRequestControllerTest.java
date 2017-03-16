package org.egov.user.web.controller;

import org.apache.commons.io.IOUtils;
import org.egov.user.domain.exception.InvalidUserException;
import org.egov.user.domain.exception.OtpValidationPendingException;
import org.egov.user.domain.service.UserService;
import org.egov.user.persistence.entity.Role;
import org.egov.user.persistence.entity.User;
import org.egov.user.domain.model.enums.BloodGroup;
import org.egov.user.domain.model.enums.Gender;
import org.egov.user.domain.model.enums.UserType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserRequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    UserService userService;

    @Test
    @WithMockUser
    public void testShouldRegisterACitizen() throws Exception {
        when(userService.save(any(org.egov.user.domain.model.User.class), eq(Boolean.TRUE))).thenReturn(buildUser());

        String fileContents = getFileContents("createValidatedCitizenSuccessRequest.json");
        mockMvc.perform(post("/users/_create/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(fileContents)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("createValidatedCitizenSuccessResponse.json")));
    }

    @Test
    @WithMockUser
    public void testShouldThrowErrorWhileRegisteringWithInvalidCitizen() throws Exception {
        InvalidUserException exception = new InvalidUserException(org.egov.user.domain.model.User.builder().build());
        when(userService.save(any(org.egov.user.domain.model.User.class), eq(Boolean.TRUE))).thenThrow(exception);

        String fileContents = getFileContents("createCitizenUnsuccessfulRequest.json");
        mockMvc.perform(post("/users/_create/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(fileContents)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("createCitizenUnsuccessfulResponse.json")));
    }

    @Test
    @WithMockUser
    public void testShouldThrowErrorWhileRegisteringWithPendingOtpValidation() throws Exception {
        OtpValidationPendingException exception = new OtpValidationPendingException(org.egov.user.domain.model.User.builder().build());
        when(userService.save(any(org.egov.user.domain.model.User.class), eq(Boolean.TRUE))).thenThrow(exception);

        String fileContents = getFileContents("createValidatedCitizenSuccessRequest.json");
        mockMvc.perform(post("/users/_create/")
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

    private String getFileContents(String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
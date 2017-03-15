package org.egov.user.web.controller;

import org.apache.commons.io.IOUtils;
import org.egov.user.domain.model.UserSearch;
import org.egov.user.domain.service.UserService;
import org.egov.user.persistence.entity.Address;
import org.egov.user.persistence.entity.Role;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.entity.enums.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    @WithMockUser
    public void testUserSearch() throws Exception {
        when(userService.searchUsers(argThat(new UserSearchMatcher(getUserSearch())))).thenReturn(getUserEntities());

        mockMvc.perform(post("/_search/").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(getFileContents("getUserByIdRequest.json"))).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("multiUserResponse.json")));
    }

    private UserSearch getUserSearch() {
        return UserSearch.builder()
                .id(asList(1L, 2L))
                .userName("userName")
                .name("name")
                .mobileNumber("mobileNumber")
                .aadhaarNumber("aadhaarNumber")
                .pan("pan")
                .emailId("emailId")
                .fuzzyLogic(true)
                .active(true)
                .pageSize(20)
                .pageNumber(0)
                .sort(singletonList("name"))
                .type("CITIZEN")
                .build();
    }

    private List<User> getUserEntities() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("IST"));
        calendar.set(1990, Calendar.JULY, 1, 16, 41, 11);
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
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("IST"));
        calendar.set(1990, Calendar.JULY, 1, 16, 41, 11);

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

    class UserSearchMatcher extends ArgumentMatcher<UserSearch> {

        private UserSearch expectedUserSearch;

        public UserSearchMatcher(UserSearch expectedUserSearch) {
            this.expectedUserSearch = expectedUserSearch;
        }

        @Override
        public boolean matches(Object o) {
            UserSearch userSearch = (UserSearch) o;
            return userSearch.getId().equals(expectedUserSearch.getId()) &&
                    userSearch.getUserName().equals(expectedUserSearch.getUserName()) &&
                    userSearch.getName().equals(expectedUserSearch.getName()) &&
                    userSearch.getMobileNumber().equals(expectedUserSearch.getMobileNumber()) &&
                    userSearch.getAadhaarNumber().equals(expectedUserSearch.getAadhaarNumber()) &&
                    userSearch.getPan().equals(expectedUserSearch.getPan()) &&
                    userSearch.getEmailId().equals(expectedUserSearch.getEmailId()) &&
                    userSearch.isFuzzyLogic() == expectedUserSearch.isFuzzyLogic() &&
                    userSearch.isActive() == expectedUserSearch.isActive() &&
                    userSearch.getPageSize() == expectedUserSearch.getPageSize() &&
                    userSearch.getPageNumber() == expectedUserSearch.getPageNumber() &&
                    userSearch.getSort().equals(expectedUserSearch.getSort()) &&
                    userSearch.getType().equals(expectedUserSearch.getType());
        }
    }
}
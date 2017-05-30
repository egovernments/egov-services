package org.egov.access.web.controller;


import org.egov.access.Resources;
import org.egov.access.TestConfiguration;
import org.egov.access.domain.model.Role;
import org.egov.access.domain.criteria.RoleSearchCriteria;
import org.egov.access.domain.service.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RoleController.class)
@Import(TestConfiguration.class)
public class RoleControllerTest {

    @MockBean
    private RoleService roleService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testShouldGetRolesForCodes() throws Exception {
        List<Role> roles = getRoles();
        RoleSearchCriteria criteria = RoleSearchCriteria.builder().codes(Arrays.asList("CITIZEN", "EMPLOYEE")).build();
        when(roleService.getRoles(criteria)).thenReturn(roles);

        mockMvc.perform(post("/v1/roles/_search")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Resources().getFileContents("roleRequest.json"))
                .param("code", "CITIZEN,EMPLOYEE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(new Resources().getFileContents("roleResponse.json")));

    }

    @Test
    public void testShouldGetRolesShouldTrimWhiteSpacesInCodes() throws Exception {
        List<Role> roles = getRoles();
        RoleSearchCriteria criteria = RoleSearchCriteria.builder().codes(Arrays.asList("CITIZEN", "EMPLOYEE")).build();
        when(roleService.getRoles(criteria)).thenReturn(roles);

        mockMvc.perform(post("/v1/roles/_search")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Resources().getFileContents("roleRequest.json"))
                .param("code", "  CITIZEN,   EMPLOYEE   "))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(new Resources().getFileContents("roleResponse.json")));

    }

    private List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();
        Role role1 = Role.builder().id(1L).name("Citizen").code("CITIZEN")
                .description("Citizen of a demography").build();
        Role role2 = Role.builder().id(2L).name("Employee").code("EMPLOYEE")
                .description("Employee of an org").build();
        roles.add(role1);
        roles.add(role2);
        return roles;
    }
}
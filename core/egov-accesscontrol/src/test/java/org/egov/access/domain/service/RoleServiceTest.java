package org.egov.access.domain.service;

import org.egov.access.domain.model.Action;
import org.egov.access.domain.model.Role;
import org.egov.access.domain.model.RoleSearchCriteria;
import org.egov.access.persistence.repository.RoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    public void testShouldReturnRolesForCodes() throws Exception {

        final RoleSearchCriteria roleSearchCriteria = RoleSearchCriteria.builder().build();
        final List<Role> rolesExpected = getRoles();
        when(roleRepository.findForCriteria(roleSearchCriteria)).thenReturn(rolesExpected);

        List<Role> actualActions = roleService.getRoles(roleSearchCriteria);
        assertEquals(rolesExpected, actualActions);
    }

    private List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();
        Role role1 = Role.builder().id(1L).name("Employee").code("EMP")
                .description("Employee of an org").build();
        Role role2 = Role.builder().id(1L).name("Another Employee").code("EMP")
                .description("Employee of an org").build();
        roles.add(role1);
        roles.add(role2);
        return roles;
    }
}

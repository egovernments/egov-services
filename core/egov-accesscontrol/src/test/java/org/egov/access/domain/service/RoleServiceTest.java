package org.egov.access.domain.service;

import org.egov.access.domain.model.Role;
import org.egov.access.domain.criteria.RoleSearchCriteria;
import org.egov.access.persistence.repository.BaseRepository;
import org.egov.access.persistence.repository.querybuilder.RoleFinderQueryBuilder;
import org.egov.access.persistence.repository.rowmapper.RoleRowMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    @Mock
    private BaseRepository repository;

    @InjectMocks
    private RoleService roleService;

    @Test
    public void testShouldReturnRolesForCodes() throws Exception {

        RoleSearchCriteria roleSearchCriteria = RoleSearchCriteria.builder().build();
        List<Object> expectedRoles = getRoles();
        when(repository.run(Mockito.any(RoleFinderQueryBuilder.class), Mockito.any(RoleRowMapper.class))).
                thenReturn(expectedRoles);

        List<Role> actualActions = roleService.getRoles(roleSearchCriteria);
        assertEquals(expectedRoles, actualActions);
    }

    private List<Object> getRoles() {
        List<Object> roles = new ArrayList<>();
        Role role1 = Role.builder().id(1L).name("Employee").code("EMP")
                .description("Employee of an org").build();
        Role role2 = Role.builder().id(1L).name("Another Employee").code("EMP")
                .description("Employee of an org").build();
        roles.add(role1);
        roles.add(role2);
        return roles;
    }
}

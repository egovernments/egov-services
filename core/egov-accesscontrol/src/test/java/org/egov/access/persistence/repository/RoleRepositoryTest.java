package org.egov.access.persistence.repository;

import org.egov.access.domain.model.Role;
import org.egov.access.domain.model.RoleSearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @Sql(scripts = {"/sql/clearRole.sql", "/sql/insertRoleData.sql"})
    public void testShouldReturnRolesForGivenCodes() throws Exception {
        List<String> codes = new ArrayList<String>();
        codes.add("CITIZEN");
        codes.add("EMPLOYEE");
        RoleSearchCriteria roleSearchCriteria = RoleSearchCriteria.builder().codes("CITIZEN,EMPLOYEE").build();
        List<Role> roles = roleRepository.findForCriteria(roleSearchCriteria);
        assertEquals(2, roles.size());
        assertEquals("Citizen", roles.get(0).getName());
        assertEquals("Citizen who can raise complaint", roles.get(0).getDescription());
    }

    @Test
    @Sql(scripts = {"/sql/clearRole.sql", "/sql/insertRoleData.sql"})
    public void testShouldReturnAllRolesWhenNoCodesAreGiven() throws Exception {
        List<String> codes = new ArrayList<String>();
        RoleSearchCriteria roleSearchCriteria = RoleSearchCriteria.builder().build();
        List<Role> roles = roleRepository.findForCriteria(roleSearchCriteria);
        assertEquals(3, roles.size());
        assertEquals("Citizen", roles.get(0).getName());
        assertEquals("Employee", roles.get(1).getName());
        assertEquals("Super User", roles.get(2).getName());
    }

}

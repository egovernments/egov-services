package org.egov.user.persistence.repository;

import org.egov.user.TestConfiguration;
import org.egov.user.persistence.entity.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(TestConfiguration.class)
public class RoleJpaRepositoryTest {

    @Autowired
    private RoleJpaRepository roleJpaRepository;

    @Test
    @Sql(scripts = {
            "/sql/clearUserRoles.sql",
            "/sql/clearRoles.sql",
            "/sql/createRoles.sql"
    })
    public void test_should_return_role_by_name_ignoring_case() {
        final Role actualRole = roleJpaRepository
				.findByRoleKeyTenantIdAndCodeIgnoreCase("ap.public","emp");

        assertNotNull(actualRole);
        assertEquals(Long.valueOf(1), actualRole.getId().getId());
        assertEquals("Employee", actualRole.getName());
    }

    @Test
    @Sql(scripts = {
            "/sql/clearUserRoles.sql",
            "/sql/clearRoles.sql",
            "/sql/createRoles.sql"
    })
    public void test_should_return_null_when_role_with_given_name_does_not_exist() {
        final Role actualRole = roleJpaRepository
				.findByRoleKeyTenantIdAndCodeIgnoreCase("tenantId","unknown");

        assertNull(actualRole);
    }
}
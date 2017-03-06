package org.egov.user.web.contract;

import org.egov.user.persistence.entity.User;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleTest {

    @Test
    public void test_entity_to_contract_conversion() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1990, Calendar.JULY, 1);
        Date date = calendar.getTime();

        org.egov.user.persistence.entity.User user = new User();
        user.setId(1L);

        org.egov.user.persistence.entity.Role roleEntity = org.egov.user.persistence.entity.Role.builder()
                .id(1L)
                .name("name of the role 1")
                .description("description")
                .build();
        roleEntity.setCreatedBy(user);
        roleEntity.setCreatedDate(date);
        roleEntity.setLastModifiedBy(user);
        roleEntity.setLastModifiedDate(date);

        Role role = new Role(roleEntity);

        assertThat(role.getId()).isEqualTo(1L);
        assertThat(role.getName()).isEqualTo("name of the role 1");
        assertThat(role.getDescription()).isEqualTo("description");
        assertThat(role.getCreatedBy()).isEqualTo(1L);
        assertThat(role.getCreatedDate()).isEqualTo(date);
        assertThat(role.getLastModifiedBy()).isEqualTo(1L);
        assertThat(role.getLastModifiedDate()).isEqualTo(date);
    }
}
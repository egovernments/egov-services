package org.egov.user.persistence.entity;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RoleTest {

    @Test
    public void test_entity_role_should_build_itself_from_domain_role() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("IST"));
        calendar.set(1990, Calendar.JULY, 23);
        Date date = calendar.getTime();

        org.egov.user.domain.model.Role domainRole = org.egov.user.domain.model.Role.builder()
                .id(1L)
                .name("USER")
                .description("description")
                .lastModifiedDate(date)
                .createdDate(date)
                .build();

        Role entityRole = new Role(domainRole);

        assertThat(entityRole.getId().getId()).isEqualTo(domainRole.getId());
        assertThat(entityRole.getName()).isEqualTo(domainRole.getName());
        assertThat(entityRole.getDescription()).isEqualTo(domainRole.getDescription());
        assertThat(entityRole.getLastModifiedDate()).isEqualTo(domainRole.getLastModifiedDate());
        assertThat(entityRole.getCreatedDate()).isEqualTo(domainRole.getCreatedDate());
    }

    @Test
    public void test_entity_should_convert_itself_to_domain() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("IST"));
        calendar.set(1990, Calendar.JULY, 23);
        Date date = calendar.getTime();

        User user = mock(User.class);
        when(user.getId()).thenReturn(new UserKey(1L, "tenant"));

        Role entityRole = Role.builder()
                .roleKey(new RoleKey(1L, "tenant"))
                .name("ADMIN")
                .description("description").build();
        entityRole.setLastModifiedDate(date);
        entityRole.setCreatedDate(date);
        entityRole.setCreatedBy(1L);
        entityRole.setLastModifiedBy(1L);

        org.egov.user.domain.model.Role domainRole = entityRole.toDomain();

        assertThat(domainRole.getId()).isEqualTo(entityRole.getId().getId());
        assertThat(domainRole.getName()).isEqualTo(entityRole.getName());
        assertThat(domainRole.getDescription()).isEqualTo(entityRole.getDescription());
        assertThat(domainRole.getLastModifiedDate()).isEqualTo(entityRole.getLastModifiedDate());
        assertThat(domainRole.getCreatedDate()).isEqualTo(entityRole.getCreatedDate());
        assertThat(domainRole.getCreatedBy()).isEqualTo(entityRole.getCreatedBy());
        assertThat(domainRole.getLastModifiedBy()).isEqualTo(entityRole.getLastModifiedBy());
    }
}

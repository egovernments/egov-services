package org.egov.user.web.contract;

import org.egov.user.persistence.entity.User;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleRequestTest {

    @Test
    public void test_entity_to_contract_conversion() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1990, Calendar.JULY, 1);
        Date date = calendar.getTime();

        org.egov.user.persistence.entity.User user = new User();
        user.setId(1L);

        org.egov.user.persistence.entity.Role roleEntity = org.egov.user.persistence.entity.Role.builder()
                .id(1L)
                .name("name of the roleRequest 1")
                .description("description")
                .build();
        roleEntity.setCreatedBy(1L);
        roleEntity.setCreatedDate(date);
        roleEntity.setLastModifiedBy(1L);
        roleEntity.setLastModifiedDate(date);

        RoleRequest roleRequest = new RoleRequest(roleEntity);

        assertThat(roleRequest.getId()).isEqualTo(1L);
        assertThat(roleRequest.getName()).isEqualTo("name of the roleRequest 1");
        assertThat(roleRequest.getDescription()).isEqualTo("description");
        assertThat(roleRequest.getCreatedBy()).isEqualTo(1L);
        assertThat(roleRequest.getCreatedDate()).isEqualTo(date);
        assertThat(roleRequest.getLastModifiedBy()).isEqualTo(1L);
        assertThat(roleRequest.getLastModifiedDate()).isEqualTo(date);
    }

    @Test
    public void test_model_to_contract_conversion() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1990, Calendar.JULY, 1);
        Date date = calendar.getTime();

        org.egov.user.domain.model.Role domainRole = org.egov.user.domain.model.Role.builder()
                .id(1L)
                .name("name of the roleRequest 1")
                .description("description")
                .createdBy(1L)
                .lastModifiedBy(1L)
                .createdDate(date)
                .lastModifiedDate(date)
                .build();

        RoleRequest roleRequest = new RoleRequest(domainRole);

        assertThat(roleRequest.getId()).isEqualTo(1L);
        assertThat(roleRequest.getName()).isEqualTo("name of the roleRequest 1");
        assertThat(roleRequest.getDescription()).isEqualTo("description");
        assertThat(roleRequest.getCreatedBy()).isEqualTo(1L);
        assertThat(roleRequest.getCreatedDate()).isEqualTo(date);
        assertThat(roleRequest.getLastModifiedBy()).isEqualTo(1L);
        assertThat(roleRequest.getLastModifiedDate()).isEqualTo(date);
    }
}
package org.egov.tenant.persistence.rowmapper;

import org.egov.tenant.persistence.entity.Tenant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.ResultSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TenantRowMapperTest {

    @Mock
    ResultSet resultSet;

    @Test
    public void test_should_map_result_set_to_model() throws Exception {
        when(resultSet.getLong(Tenant.Fields.ID.getValue())).thenReturn(1L);
        when(resultSet.getString(Tenant.Fields.CODE.getValue())).thenReturn("AP.KURNOOL");
        when(resultSet.getString(Tenant.Fields.DESCRIPTION.getValue())).thenReturn("description");
        when(resultSet.getString(Tenant.Fields.DOMAIN_URL.getValue())).thenReturn("domainurl");
        when(resultSet.getString(Tenant.Fields.LOGO_ID.getValue())).thenReturn("logoid");
        when(resultSet.getString(Tenant.Fields.IMAGE_ID.getValue())).thenReturn("imageid");
        TenantRowMapper tenantRowMapper = new TenantRowMapper();

        Tenant tenant = tenantRowMapper.mapRow(resultSet, 1);

        assertThat(tenant.getId()).isEqualTo(1L);
        assertThat(tenant.getCode()).isEqualTo("AP.KURNOOL");
        assertThat(tenant.getDescription()).isEqualTo("description");
        assertThat(tenant.getDomainUrl()).isEqualTo("domainurl");
        assertThat(tenant.getLogoId()).isEqualTo("logoid");
        assertThat(tenant.getImageId()).isEqualTo("imageid");
    }
}
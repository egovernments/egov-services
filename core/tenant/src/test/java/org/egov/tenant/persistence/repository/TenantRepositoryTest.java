package org.egov.tenant.persistence.repository;

import org.egov.tenant.domain.model.TenantSearchCriteria;
import org.egov.tenant.persistence.entity.Tenant;
import org.egov.tenant.persistence.repository.builder.TenantQueryBuilder;
import org.egov.tenant.persistence.rowmapper.TenantRowMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TenantRepositoryTest {

    private TenantRepository tenantRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private TenantQueryBuilder tenantQueryBuilder;

    @Before
    public void setUp() throws Exception {
        tenantRepository = new TenantRepository(jdbcTemplate, tenantQueryBuilder);
    }

    @Test
    public void test_should_retrieve_tenant() {
        List<Tenant> listOfEntities = getListOfEntities();
        TenantSearchCriteria tenantSearchCriteria = mock(TenantSearchCriteria.class);
        when(tenantQueryBuilder.getQuery(tenantSearchCriteria)).thenReturn("query");
        when(jdbcTemplate.query(eq("query"), any(TenantRowMapper.class))).thenReturn(listOfEntities);

        List<org.egov.tenant.domain.model.Tenant> result = tenantRepository.findForCriteria(tenantSearchCriteria);

        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(1).getId()).isEqualTo(2);
    }

    private List<Tenant> getListOfEntities() {
        return Arrays.asList(
                Tenant.builder().id(1L).build(),
                Tenant.builder().id(2L).build()
        );
    }
}
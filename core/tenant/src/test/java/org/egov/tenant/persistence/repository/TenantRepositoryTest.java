package org.egov.tenant.persistence.repository;

import org.egov.tenant.domain.model.City;
import org.egov.tenant.domain.model.TenantSearchCriteria;
import org.egov.tenant.domain.model.TenantType;
import org.egov.tenant.persistence.entity.Tenant;
import org.egov.tenant.persistence.repository.builder.TenantQueryBuilder;
import org.egov.tenant.persistence.rowmapper.TenantRowMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TenantRepositoryTest {

    private TenantRepository tenantRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private TenantQueryBuilder tenantQueryBuilder;

    @Mock
    private CityRepository cityRepository;

    @Before
    public void setUp() throws Exception {
        tenantRepository = new TenantRepository(jdbcTemplate, tenantQueryBuilder, cityRepository);
    }

    @Test
    public void test_should_retrieve_tenant() {
        List<Tenant> listOfEntities = getListOfEntities();
        TenantSearchCriteria tenantSearchCriteria = mock(TenantSearchCriteria.class);
        when(tenantQueryBuilder.getSearchQuery(tenantSearchCriteria)).thenReturn("query");
        when(jdbcTemplate.query(eq("query"), any(TenantRowMapper.class))).thenReturn(listOfEntities);

        City city = new City();

        when(cityRepository.find("AP.KURNOOL")).thenReturn(city);
        when(cityRepository.find("AP.GUNTOOR")).thenReturn(city);

        List<org.egov.tenant.domain.model.Tenant> result = tenantRepository.find(tenantSearchCriteria);

        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(0).getCity()).isEqualTo(city);
        assertThat(result.get(1).getId()).isEqualTo(2);
        assertThat(result.get(1).getCity()).isEqualTo(city);
    }

    @Test
    public void test_should_save_tenant() {
        when(tenantQueryBuilder.getInsertQuery()).thenReturn("insert query");
        ArrayList<Object> fields = new ArrayList<Object>() {{
            add("AP.KURNOOL");
            add("description");
            add("domainUrl");
            add("logoid");
            add("imageid");
            add(1L);
            add(1L);
        }};
        when(jdbcTemplate.update(
                eq("insert query"),
                eq("AP.KURNOOL"),
                eq("description"),
                eq("domainUrl"),
                eq("logoid"),
                eq("imageid"),
                eq("CITY"),
                eq(1L),
                any(Date.class),
                eq(1L),
                any(Date.class))
        ).thenReturn(1);

        City city = new City();

        org.egov.tenant.domain.model.Tenant tenant = org.egov.tenant.domain.model.Tenant.builder()
                .code("AP.KURNOOL")
                .description("description")
                .domainUrl("domainUrl")
                .logoId("logoid")
                .imageId("imageid")
                .type(TenantType.CITY)
                .city(city)
                .build();

        tenantRepository.save(tenant);

        verify(cityRepository).save(city, "AP.KURNOOL");
        verify(jdbcTemplate).update(
                eq("insert query"),
                eq("AP.KURNOOL"),
                eq("description"),
                eq("domainUrl"),
                eq("logoid"),
                eq("imageid"),
                eq("CITY"),
                eq(1L),
                any(Date.class),
                eq(1L),
                any(Date.class)
        );
    }

    private List<Tenant> getListOfEntities() {
        return asList(
                Tenant.builder().id(1L).code("AP.KURNOOL").build(),
                Tenant.builder().id(2L).code("AP.GUNTOOR").build()
        );
    }

}
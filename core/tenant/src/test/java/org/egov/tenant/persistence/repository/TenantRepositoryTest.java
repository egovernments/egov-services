package org.egov.tenant.persistence.repository;

import org.egov.tenant.domain.model.City;
import org.egov.tenant.domain.model.TenantSearchCriteria;
import org.egov.tenant.persistence.entity.Tenant;
import org.egov.tenant.persistence.repository.builder.TenantQueryBuilder;
import org.egov.tenant.persistence.rowmapper.TenantRowMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
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

        List<org.egov.tenant.domain.model.Tenant> result = tenantRepository.find(tenantSearchCriteria);

        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(1).getId()).isEqualTo(2);
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
        when(jdbcTemplate.update(eq("insert query"), argThat(new ListArgumentMatcher(fields)))).thenReturn(1);

        City city = new City();

        org.egov.tenant.domain.model.Tenant tenant = org.egov.tenant.domain.model.Tenant.builder()
                .code("AP.KURNOOL")
                .description("description")
                .domainUrl("domainUrl")
                .logoId("logoid")
                .imageId("imageid")
                .city(city)
                .build();

        tenantRepository.save(tenant);

        verify(cityRepository).save(city, "AP.KURNOOL");
        verify(jdbcTemplate).update(eq("insert query"), argThat(new ListArgumentMatcher(fields)));
    }

    private List<Tenant> getListOfEntities() {
        return asList(
                Tenant.builder().id(1L).build(),
                Tenant.builder().id(2L).build()
        );
    }

    private class ListArgumentMatcher extends ArgumentMatcher<List<Object>> {

        private List<Object> expectedValue;

        public ListArgumentMatcher(List<Object> expectedValue) {
            this.expectedValue = expectedValue;
        }

        @Override
        public boolean matches(Object o) {
            ArrayList<Object> actualValue = (ArrayList<Object>) o;

            //Check if the dates are not null
            if(actualValue.get(6) == null && actualValue.get(8) == null) {
                return false;
            }

            return expectedValue.get(0).equals(actualValue.get(0)) &&
                    expectedValue.get(1).equals(actualValue.get(1)) &&
                    expectedValue.get(2).equals(actualValue.get(2)) &&
                    expectedValue.get(3).equals(actualValue.get(3)) &&
                    expectedValue.get(4).equals(actualValue.get(4)) &&
                    expectedValue.get(5).equals(actualValue.get(5)) &&
                    expectedValue.get(6).equals(actualValue.get(7));

        }
    }
}
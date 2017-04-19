package org.egov.tenant.persistence.repository;

import org.egov.tenant.domain.model.City;
import org.egov.tenant.domain.model.Tenant;
import org.egov.tenant.domain.model.TenantSearchCriteria;
import org.egov.tenant.domain.model.TenantType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.egov.tenant.persistence.entity.Tenant.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TenantRepositoryTest {
    private static final List<String> TENANT_CODES = asList("AP.KURNOOL", "AP.GUNTOOR");

    @MockBean
    private CityRepository cityRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private TenantRepository tenantRepository;

    @Before
    public void setUp() throws Exception {
        tenantRepository = new TenantRepository(cityRepository, namedParameterJdbcTemplate);
    }

    @Test
    @Sql(scripts = {"/sql/clearCity.sql", "/sql/clearTenant.sql", "/sql/insertTenantData.sql"})
    public void test_should_retrieve_tenant() throws Exception {
        TenantSearchCriteria tenantSearchCriteria = TenantSearchCriteria.builder()
            .tenantCodes(TENANT_CODES)
            .build();

        City guntoorCity = City.builder().id(1L).build();
        City kurnoolCity = City.builder().id(2L).build();

        when(cityRepository.find("AP.KURNOOL")).thenReturn(kurnoolCity);
        when(cityRepository.find("AP.GUNTOOR")).thenReturn(guntoorCity);

        List<Tenant> tenants = tenantRepository.find(tenantSearchCriteria);

        assertThat(tenants.size()).isEqualTo(2);
        Tenant tenant = tenants.get(0);
        assertThat(tenant.getId()).isEqualTo(1L);
        assertThat(tenant.getCode()).isEqualTo("AP.KURNOOL");
        assertThat(tenant.getDescription()).isEqualTo("description");
        assertThat(tenant.getDomainUrl()).isEqualTo("http://egov.ap.gov.in/kurnool");
        assertThat(tenant.getLogoId()).isEqualTo("d45d7118-2013-11e7-93ae-92361f002671");
        assertThat(tenant.getImageId()).isEqualTo("8716872c-cd50-4fbb-a0d6-722e6bc9c143");
        assertThat(tenant.getType()).isEqualTo(TenantType.CITY);
        assertThat(tenant.getCity()).isEqualTo(kurnoolCity);
        assertThat(tenants.get(1).getCode()).isEqualTo("AP.GUNTOOR");
        assertThat(tenants.get(1).getCity()).isEqualTo(guntoorCity);
    }

    @Test
    @Sql(scripts = {"/sql/clearCity.sql", "/sql/clearTenant.sql"})
    public void test_should_save_tenant() throws Exception {
        City city = City.builder().id(1L).build();

        Tenant tenant = Tenant.builder()
            .code("AP.KURNOOL")
            .description("description")
            .domainUrl("http://egov.ap.gov.in/kurnool")
            .logoId("d45d7118-2013-11e7-93ae-92361f002671")
            .imageId("8716872c-cd50-4fbb-a0d6-722e6bc9c143")
            .type(TenantType.CITY)
            .city(city)
            .build();

        tenantRepository.save(tenant);

        List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM tenant", new TenantResultExtractor());
        Map<String, Object> row = result.get(0);
        assertThat(row.get(ID)).isEqualTo(1L);
        assertThat(row.get(CODE)).isEqualTo("AP.KURNOOL");
        assertThat(row.get(DESCRIPTION)).isEqualTo("description");
        assertThat(row.get(DOMAIN_URL)).isEqualTo("http://egov.ap.gov.in/kurnool");
        assertThat(row.get(LOGO_ID)).isEqualTo("d45d7118-2013-11e7-93ae-92361f002671");
        assertThat(row.get(IMAGE_ID)).isEqualTo("8716872c-cd50-4fbb-a0d6-722e6bc9c143");
        assertThat(row.get(TYPE)).isEqualTo("CITY");
        assertThat(row.get(CREATED_BY)).isEqualTo(1L);
        assertThat(row.get(CREATED_DATE)).isNotNull();
        assertThat(row.get(LAST_MODIFIED_BY)).isEqualTo(1L);
        assertThat(row.get(LAST_MODIFIED_DATE)).isNotNull();

        verify(cityRepository).save(city, "AP.KURNOOL");
    }

    class TenantResultExtractor implements ResultSetExtractor<List<Map<String, Object>>> {
        @Override
        public List<Map<String, Object>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            List<Map<String, Object>> rows = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<String, Object>() {{
                    put(ID, resultSet.getLong(ID));
                    put(CODE, resultSet.getString(CODE));
                    put(DESCRIPTION, resultSet.getString(DESCRIPTION));
                    put(DOMAIN_URL, resultSet.getString(DOMAIN_URL));
                    put(LOGO_ID, resultSet.getString(LOGO_ID));
                    put(IMAGE_ID, resultSet.getString(IMAGE_ID));
                    put(TYPE, resultSet.getString(TYPE));
                    put(CREATED_BY, resultSet.getLong(CREATED_BY));
                    put(CREATED_DATE, resultSet.getString(CREATED_DATE));
                    put(LAST_MODIFIED_BY, resultSet.getLong(LAST_MODIFIED_BY));
                    put(LAST_MODIFIED_DATE, resultSet.getString(LAST_MODIFIED_DATE));
                }};

                rows.add(row);
            }
            return rows;
        }
    }
}
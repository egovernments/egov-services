package org.egov.tenant.persistence.repository.builder;

import org.egov.tenant.domain.model.TenantSearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TenantQueryBuilderTest {

    private static final List<String> TENANT_CODES = asList("AP.KURNOOL", "AP.GUNTOOR");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Sql(scripts = {"/sql/clearTenant.sql", "/sql/insertTenantData.sql"})
    public void test_should_retrieve_tenant() throws Exception {
        TenantSearchCriteria tenantSearchCriteria = TenantSearchCriteria.builder()
                .tenantCodes(TENANT_CODES)
                .build();
        TenantQueryBuilder tenantQueryBuilder = new TenantQueryBuilder();
        String query = tenantQueryBuilder.getSearchQuery(tenantSearchCriteria);

        List<Map<String, Object>> result = jdbcTemplate.query(query, new TenantResultExtractor());

        assertThat(result.size()).isEqualTo(2);
        Map<String, Object> row = result.get(0);
        assertThat(row.get("id")).isEqualTo(1L);
        assertThat(row.get("code")).isEqualTo("AP.KURNOOL");
        assertThat(row.get("description")).isEqualTo("description");
        assertThat(row.get("domainurl")).isEqualTo("http://egov.ap.gov.in/kurnool");
        assertThat(row.get("logoid")).isEqualTo("d45d7118-2013-11e7-93ae-92361f002671");
        assertThat(row.get("imageid")).isEqualTo("8716872c-cd50-4fbb-a0d6-722e6bc9c143");
        assertThat(row.get("type")).isEqualTo("CITY");
        assertThat(row.get("createdby")).isEqualTo(1L);
        assertThat(row.get("createddate")).isEqualTo("1990-07-23 00:00:00.0");
        assertThat(row.get("lastmodifiedby")).isEqualTo(0L);
        assertThat(row.get("lastmodifieddate")).isEqualTo("1990-07-23 00:00:00.0");
        assertThat(result.get(1).get("code")).isEqualTo("AP.GUNTOOR");
    }

    @Test
    @Sql(scripts = {"/sql/clearTenant.sql"})
    public void test_should_save_tenant() throws Exception {
        TenantQueryBuilder tenantQueryBuilder = new TenantQueryBuilder();
        String query = tenantQueryBuilder.getInsertQuery();

        jdbcTemplate.update(query, "AP.KURNOOL", "description", "http://egov.ap.gov.in/kurnool", "d45d7118-2013-11e7-93ae-92361f002671", "8716872c-cd50-4fbb-a0d6-722e6bc9c143", "CITY", 1L, new Date(), 1L, new Date());

        List<Map<String, Object>> result = jdbcTemplate.query("SELECT * FROM tenant", new TenantResultExtractor());
        Map<String, Object> row = result.get(0);
        assertThat(row.get("id")).isEqualTo(1L);
        assertThat(row.get("code")).isEqualTo("AP.KURNOOL");
        assertThat(row.get("description")).isEqualTo("description");
        assertThat(row.get("domainurl")).isEqualTo("http://egov.ap.gov.in/kurnool");
        assertThat(row.get("logoid")).isEqualTo("d45d7118-2013-11e7-93ae-92361f002671");
        assertThat(row.get("imageid")).isEqualTo("8716872c-cd50-4fbb-a0d6-722e6bc9c143");
        assertThat(row.get("type")).isEqualTo("CITY");
        assertThat(row.get("createdby")).isEqualTo(1L);
        assertThat(row.get("createddate")).isNotNull();
        assertThat(row.get("lastmodifiedby")).isEqualTo(1L);
        assertThat(row.get("lastmodifieddate")).isNotNull();
    }

    class TenantResultExtractor implements ResultSetExtractor<List<Map<String, Object>>> {
        @Override
        public List<Map<String, Object>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            List<Map<String, Object>> rows = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<String, Object>() {{
                    put("id", resultSet.getLong("id"));
                    put("code", resultSet.getString("code"));
                    put("description", resultSet.getString("description"));
                    put("domainurl", resultSet.getString("domainurl"));
                    put("logoid", resultSet.getString("logoid"));
                    put("imageid", resultSet.getString("imageid"));
                    put("type", resultSet.getString("type"));
                    put("createdby", resultSet.getLong("createdby"));
                    put("createddate", resultSet.getString("createddate"));
                    put("lastmodifiedby", resultSet.getLong("lastmodifiedby"));
                    put("lastmodifieddate", resultSet.getString("lastmodifieddate"));
                }};

                rows.add(row);
            }
            return rows;
        }
    }
}
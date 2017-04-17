package org.egov.tenant.persistence.repository.builder;

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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CityQueryBuilderTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Sql(scripts = {"/sql/clearCity.sql", "/sql/clearTenant.sql", "/sql/insertTenantData.sql"})
    public void test_should_retrieve_tenant() throws Exception {
        CityQueryBuilder cityQueryBuilder = new CityQueryBuilder();

        String insertQuery = cityQueryBuilder.getInsertQuery();

        jdbcTemplate.update(insertQuery, "name", "localname", "dcode", "districtname", "regionname", 35.345, 75.234, "AP.KURNOOL", 1, new Date(), 1, new Date());

        List<Map<String, Object>> result = jdbcTemplate.query("SELECT * FROM city", new CityResultExtractor());

        Map<String, Object> row = result.get(0);
        assertThat(row.get("id")).isEqualTo(1L);
    }

    class CityResultExtractor implements ResultSetExtractor<List<Map<String, Object>>> {
        @Override
        public List<Map<String, Object>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            List<Map<String, Object>> rows = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<String, Object>() {{
                    put("id", resultSet.getLong("id"));
                    put("name", resultSet.getString("name"));
                    put("localname", resultSet.getString("localname"));
                    put("districtcode", resultSet.getString("districtcode"));
                    put("districtname", resultSet.getString("districtname"));
                    put("regionname", resultSet.getString("regionname"));
                    put("longitude", resultSet.getDouble("longitude"));
                    put("latitude", resultSet.getDouble("latitude"));
                    put("tenantcode", resultSet.getString("tenantcode"));
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
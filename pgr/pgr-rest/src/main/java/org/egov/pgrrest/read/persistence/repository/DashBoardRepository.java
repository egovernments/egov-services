package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.read.domain.model.DashboardResponse;
import org.egov.pgrrest.read.persistence.rowmapper.DashboardRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class DashBoardRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private DashboardRowMapper dashboardRowMapper;

    public DashBoardRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                               DashboardRowMapper dashboardRowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.dashboardRowMapper = dashboardRowMapper;
    }

    public List<DashboardResponse> getCountByComplaintType(String tenantId){

        String query = "select count(*) as count, to_char(date_trunc('month',createddate), 'MON') as month, to_char(date_trunc('year',createddate), 'YYYY') as year from submission " +
            "where servicecode in (select servicecode from servicetype_keyword where tenantid = :tenantId and keyword = 'complaint')" +
            "group by date_trunc('month',createddate), date_trunc('year',createddate)" +
            "order by date_trunc('year',createddate), date_trunc('month',createddate) DESC LIMIT 7";

        return namedParameterJdbcTemplate.query(query,getSearchMap(tenantId),dashboardRowMapper);

    }

    private HashMap getSearchMap(String tenantId) {
        HashMap<String, Object> parametersMap = new HashMap<>();

        parametersMap.put("tenantId", tenantId);

        return parametersMap;
    }
}

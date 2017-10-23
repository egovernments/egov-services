package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.read.domain.model.DashboardResponse;
import org.egov.pgrrest.read.domain.model.TopComplaintTypesResponse;
import org.egov.pgrrest.read.persistence.rowmapper.DailyCountRowMapper;
import org.egov.pgrrest.read.persistence.rowmapper.DashboardRowMapper;
import org.egov.pgrrest.read.persistence.rowmapper.TopComplaintsRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class DashBoardRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private DashboardRowMapper dashboardRowMapper;

    private DailyCountRowMapper dailyCountRowMapper;

    public DashBoardRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                               DashboardRowMapper dashboardRowMapper,
                               DailyCountRowMapper dailyCountRowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.dashboardRowMapper = dashboardRowMapper;
        this.dailyCountRowMapper = dailyCountRowMapper;
    }

    public List<DashboardResponse> getCountByComplaintType(String tenantId){

        String query = "select count(*) as count, to_char(date_trunc('month',createddate), 'MON') as month, to_char(date_trunc('year',createddate), 'YYYY') as year from submission " +
            "where servicecode in (select servicecode from servicetype_keyword where tenantid = :tenantId and keyword = 'complaint')" +
            "group by date_trunc('month',createddate), date_trunc('year',createddate)" +
            "order by date_trunc('year',createddate), date_trunc('month',createddate) DESC LIMIT 7";

        return namedParameterJdbcTemplate.query(query,getSearchMap(tenantId),dashboardRowMapper);

    }

    public List<DashboardResponse> getWeeklyRegisteredAndClosedComplaintsCount(String tenantId){
        String query = "select count(*) as count, status, to_char(date_trunc('day',createddate), 'DAY') as day, to_char(date_trunc('day',createddate), 'dd') as date from submission " +
            "where servicecode in (select servicecode from servicetype_keyword where tenantid = 'default' and keyword = 'complaint')" +
            "and status in ('REGISTERED', 'COMPLETED') and createddate > current_date - interval '7' day and tenantid = 'default'" +
            "group by date_trunc('day',createddate), status " +
            "order by date_trunc('day',createddate), status";

        return namedParameterJdbcTemplate.query(query,getSearchMap(tenantId),dailyCountRowMapper);
    }

    public List<TopComplaintTypesResponse> getTopComplaintTypeWithCount(String tenantId, int size){

        String query = "select count(*) as count, ctype.name as complainttypename" +
            " from submission cs, egpgr_complainttype ctype, servicetype_keyword sk" +
            " where cs.servicecode = ctype.code and ctype.code = sk.servicecode and sk.keyword = 'complaint'" +
            " and cs.tenantid = :tenantId and sk.tenantid = :tenantId and ctype.tenantid = :tenantId " +
            "group by cs.servicecode, complainttypename order by count DESC LIMIT :size";

        return namedParameterJdbcTemplate.query(query, getSearchMapForTopComplaints(tenantId, size),
            new TopComplaintsRowMapper());
    }

    private HashMap getSearchMap(String tenantId) {
        HashMap<String, Object> parametersMap = new HashMap<>();

        parametersMap.put("tenantId", tenantId);

        return parametersMap;
    }

    private HashMap getSearchMapForTopComplaints(String tenantId, int size) {
        HashMap<String, Object> parametersMap = new HashMap<>();

        parametersMap.put("tenantId", tenantId);
        parametersMap.put("size", size);

        return parametersMap;
    }


}

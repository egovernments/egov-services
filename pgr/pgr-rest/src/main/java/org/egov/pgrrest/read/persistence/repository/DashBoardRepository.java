package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.read.domain.model.AgeingResponse;
import org.egov.pgrrest.read.domain.model.DashboardResponse;
import org.egov.pgrrest.read.domain.model.TopComplaintTypesResponse;
import org.egov.pgrrest.read.persistence.rowmapper.*;
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

        String query = "select count(*) as count, ctype.name as complainttypename, cs.servicecode as code" +
            " from submission cs, egpgr_complainttype ctype, servicetype_keyword sk" +
            " where cs.servicecode = ctype.code and ctype.code = sk.servicecode and sk.keyword = 'complaint'" +
            " and cs.tenantid = :tenantId and sk.tenantid = :tenantId and ctype.tenantid = :tenantId " +
            "group by cs.servicecode, complainttypename order by count DESC LIMIT :size";

        return namedParameterJdbcTemplate.query(query, getSearchMapForTopComplaints(tenantId, size),
            new TopComplaintsRowMapper());
    }

    public List<TopComplaintTypesResponse> getTopFiveComplaintTypesMonthly(String tenantId){

        String query = "select count(*) as count, cs.servicecode as code, ctype.name as complainttypename, extract(month from date_trunc('month',cs.createddate)) as month" +
            " from submission cs, egpgr_complainttype ctype" +
            " where cs.servicecode = ctype.code" +
            " and servicecode in (select servicecode from servicetype_keyword where tenantid = :tenantId and keyword = 'complaint')" +
            " and cs.createddate > current_date - interval '7' month" +
            " and cs.tenantid = :tenantId and ctype.tenantid = :tenantId" +
            " and servicecode in (select result.code from (select count(*) as count, ctype.name as complainttypename, cs.servicecode as code" +
            " from submission cs, egpgr_complainttype ctype, servicetype_keyword sk" +
            " where cs.servicecode = ctype.code and ctype.code = sk.servicecode and sk.keyword = 'complaint'" +
            " and cs.createddate > current_date - interval '7' month" +
            " and cs.tenantid = :tenantId and sk.tenantid = :tenantId and ctype.tenantid = :tenantId" +
            " group by cs.servicecode, complainttypename" +
            " order by count DESC LIMIT 5 ) as result)" +
            " group by date_trunc('month',cs.createddate), cs.servicecode, ctype.name" +
            " order by month";

        return namedParameterJdbcTemplate.query(query,getSearchMap(tenantId), new TopComplaintTypesRowMapper());

    }

    public List<TopComplaintTypesResponse> getWardWiseCountForComplainttype(String tenantId, String serviceCode){

        String query = "select (select boundarynum from eg_boundary where id = csa.code::bigint and tenantid = :tenantId) as boundary , count(*) as count" +
            " from submission cs, submission_attribute csa, servicetype_keyword sk" +
            " where cs.crn = csa.crn and csa.key = 'systemLocationId' and cs.servicecode = :servicecode" +
            " and cs.servicecode = sk.servicecode and sk.keyword = 'complaint'" +
            " and cs.tenantid = :tenantId and csa.tenantid = :tenantId and sk.tenantid = :tenantId" +
            " group by csa.code, csa.key";

        return namedParameterJdbcTemplate.query(query, getWardWiseSearchMap(tenantId, serviceCode),
            new WardWiseRowMapper());

    }

    public List<AgeingResponse> getAgeingOfComplaints(String tenantId){

        String query = "select " +
            " count(CASE WHEN (date_part('epoch'::text, now() - (cs.createddate + (interval '1 hour' * ctype.slahours)))/ 86400::double precision) < 15::double precision THEN 1 ELSE" +
            " NULL::integer END) AS less15," +
            " count(CASE WHEN (date_part('epoch'::text, now() - (cs.createddate + (interval '1 hour' * ctype.slahours)))/ 86400::double precision) >= 15::double precision" +
            " AND (date_part('epoch'::text, now() - (cs.createddate + (interval '1 hour' * ctype.slahours)))/ 86400::double precision) <= 45::double" +
            " precision THEN 1 ELSE NULL::integer END) AS btw15to45," +
            " count(CASE WHEN (date_part('epoch'::text, now() - (cs.createddate + (interval '1 hour' * ctype.slahours)))/ 86400::double precision) >= 45::double precision" +
            " AND (date_part('epoch'::text, now() - (cs.createddate + (interval '1 hour' * ctype.slahours)))/ 86400::double precision) <= 90::double" +
            " precision THEN 1 ELSE NULL::integer END) AS btw45to90," +
            " count(CASE WHEN (date_part('epoch'::text, now() - (cs.createddate + (interval '1 hour' * ctype.slahours)))/ 86400::double precision) > 90::double precision THEN 1 ELSE" +
            " NULL::integer END) AS greaterthan90" +
            " FROM egpgr_complainttype ctype, submission cs, servicetype_keyword sk" +
            " WHERE cs.servicecode = ctype.code and cs.createddate <= now() and cs.status IN ('REGISTERED', 'FORWARDED', 'PROCESSING', 'REOPENED', 'ONHOLD')" +
            " and ctype.code = sk.servicecode and sk.keyword = 'complaint' and cs.tenantid = :tenantId and sk.tenantid = :tenantId and ctype.tenantid = :tenantId";

        return namedParameterJdbcTemplate.query(query, getSearchMap(tenantId), new AgeingRowMapper());

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

    private HashMap getWardWiseSearchMap(String tenantId, String serviceCode){
        HashMap<String, Object> parametersMap = new HashMap<>();

        parametersMap.put("tenantId", tenantId);
        parametersMap.put("servicecode", serviceCode);

        return parametersMap;
    }


}

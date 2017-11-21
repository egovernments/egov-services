package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.read.domain.model.AgeingResponse;
import org.egov.pgrrest.read.domain.model.ComplaintTypeLegend;
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
            " where servicecode in (select servicecode from servicetype_keyword where tenantid = :tenantId and upper(keyword) = upper('complaint'))" +
            " and tenantid = :tenantId"+
            " group by date_trunc('month',createddate), date_trunc('year',createddate)" +
            " order by date_trunc('year',createddate), date_trunc('month',createddate) DESC LIMIT 7";

        return namedParameterJdbcTemplate.query(query,getSearchMap(tenantId),dashboardRowMapper);

    }

    public List<DashboardResponse> getWeeklyRegisteredAndClosedComplaintsCount(String tenantId){

        String registeredCountQuery = "select count(*) as count from submission where servicecode in (select servicecode from servicetype_keyword where tenantid = :tenantId" +
            " and upper(keyword) = upper('complaint'))" +
            " and createddate > current_date - interval '6' day and tenantid = :tenantId";

        String closedCountQuery = "select count(*) as count from submission where servicecode in (select servicecode from servicetype_keyword where tenantid = :tenantId" +
            " and upper(keyword) = upper('complaint')) and createddate > current_date - interval '6' day and tenantid = :tenantId" +
            " and status in ('COMPLETED', 'REJECTED', 'WITHDRAWN')";

        String query = "select a.regcount, b.closedcount, a.day, a.date from" +
            " (select count(*) as regcount, to_char(date_trunc('day',createddate), 'DAY') as day, to_char(date_trunc('day',createddate), 'dd') as date from submission" +
            " where servicecode in (select servicecode from servicetype_keyword where tenantid = :tenantId and upper(keyword) = upper('complaint'))" +
            " and createddate > current_date - interval '6' day and tenantid = :tenantId" +
            " group by date_trunc('day',createddate)" +
            " order by date_trunc('day',createddate) ASC ) as a," +
            " (select count(*) as closedcount, to_char(date_trunc('day',createddate), 'DAY') as day, to_char(date_trunc('day',createddate), 'dd') as date from submission" +
            " where servicecode in (select servicecode from servicetype_keyword where tenantid = :tenantId and upper(keyword) = upper('complaint'))" +
            " and createddate > current_date - interval '6' day and tenantid = :tenantId" +
            " and status in ('COMPLETED', 'REJECTED', 'WITHDRAWN')" +
            " group by date_trunc('day',createddate)" +
            " order by date_trunc('day',createddate) ASC) as b" +
            " where a.day = b.day";

        Long registeredCount = getCount(registeredCountQuery, tenantId);
        Long closedCount = getCount(closedCountQuery, tenantId);

        if(registeredCount != 0 && closedCount == 0){
            query = "select count(*) as regcount, 0 as closedcount, to_char(date_trunc('day',createddate), 'DAY') as day, to_char(date_trunc('day',createddate),'dd') as date from submission" +
                " where servicecode in (select servicecode from servicetype_keyword where tenantid = :tenantId" +
                " and upper(keyword) = upper('complaint')) and createddate > current_date - interval '6' day and tenantid = :tenantId" +
                " group by date_trunc('day',createddate) order by date_trunc('day',createddate) ASC";
        }
        else if(registeredCount == 0 && closedCount != 0){
            query = "select 0 as regcount, count(*) as closedcount, to_char(date_trunc('day',createddate), 'DAY') as day, to_char(date_trunc('day',createddate), 'dd') as date from submission" +
                " where servicecode in (select servicecode from servicetype_keyword where tenantid = :tenantId and upper(keyword) = upper('complaint'))" +
                " and createddate > current_date - interval '6' day and tenantid = :tenantId and status in ('COMPLETED', 'REJECTED', 'WITHDRAWN')" +
                " group by date_trunc('day',createddate) order by date_trunc('day',createddate) ASC";
        }

        return namedParameterJdbcTemplate.query(query,getSearchMap(tenantId),dailyCountRowMapper);
    }

    public List<TopComplaintTypesResponse> getTopComplaintTypeWithCount(String tenantId, int size){

        String query = "select count(*) as count, ctype.name as complainttypename, cs.servicecode as code" +
            " from submission cs, egpgr_complainttype ctype, servicetype_keyword sk" +
            " where cs.servicecode = ctype.code and ctype.code = sk.servicecode and upper(sk.keyword) = upper('complaint')" +
            " and cs.tenantid = :tenantId and sk.tenantid = :tenantId and ctype.tenantid = :tenantId " +
            "group by cs.servicecode, complainttypename order by count DESC LIMIT :size";

        return namedParameterJdbcTemplate.query(query, getSearchMapForTopComplaints(tenantId, size),
            new TopComplaintsRowMapper());
    }

    public List<ComplaintTypeLegend> getTopFiveComplaintTypesLegendData(String tenantId){

        String query = "select count(*) as count, ctype.name as complainttypename, cs.servicecode as code" +
            " from submission cs, egpgr_complainttype ctype, servicetype_keyword sk" +
            " where cs.servicecode = ctype.code and ctype.code = sk.servicecode and upper(sk.keyword) = upper('complaint')" +
            " and cs.createddate > current_date - interval '7' month" +
            " and cs.tenantid = :tenantId and sk.tenantid = :tenantId and ctype.tenantid = :tenantId" +
            " group by cs.servicecode, complainttypename" +
            " order by count DESC LIMIT 5";

        List<ComplaintTypeLegend> complaintTypeLegendList = namedParameterJdbcTemplate.query(query, getSearchMap(tenantId), new TopComplaintTypesLegendRowMapper());
        return updateRank(complaintTypeLegendList);
    }

    private Long getCount(String query, String tenantId){

        return namedParameterJdbcTemplate.queryForObject(query,getSearchMap(tenantId), Long.class);
    }

    private List<ComplaintTypeLegend> updateRank(List<ComplaintTypeLegend> complaintTypeLegendList){

        for(int i=0 ; i<complaintTypeLegendList.size() ;i++){
            complaintTypeLegendList.get(i).setRank(i+1);
        }

        return complaintTypeLegendList;
    }

    public List<TopComplaintTypesResponse> getTopFiveComplaintTypesMonthly(String tenantId){

        String query = "select count(*) as count, cs.servicecode as code, ctype.name as complainttypename, extract(month from date_trunc('month',cs.createddate)) as month" +
            " from submission cs, egpgr_complainttype ctype" +
            " where cs.servicecode = ctype.code" +
            " and servicecode in (select servicecode from servicetype_keyword where tenantid = :tenantId and upper(keyword) = upper('complaint'))" +
            " and cs.createddate > current_date - interval '7' month" +
            " and cs.tenantid = :tenantId and ctype.tenantid = :tenantId" +
            " and servicecode in (select result.code from (select count(*) as count, ctype.name as complainttypename, cs.servicecode as code" +
            " from submission cs, egpgr_complainttype ctype, servicetype_keyword sk" +
            " where cs.servicecode = ctype.code and ctype.code = sk.servicecode and upper(sk.keyword) = upper('complaint')" +
            " and cs.createddate > current_date - interval '7' month" +
            " and cs.tenantid = :tenantId and sk.tenantid = :tenantId and ctype.tenantid = :tenantId" +
            " group by cs.servicecode, complainttypename" +
            " order by count DESC LIMIT 5 ) as result)" +
            " group by date_trunc('month',cs.createddate), cs.servicecode, ctype.name" +
            " order by month";

        return namedParameterJdbcTemplate.query(query,getSearchMap(tenantId), new TopComplaintTypesRowMapper());

    }

    public List<TopComplaintTypesResponse> getWardWiseCountForComplainttype(String tenantId, String serviceCode, String type){

        StringBuilder query = new StringBuilder("select (select boundarynum from eg_boundary where id = csa.code::bigint and tenantid = :tenantId) as boundary ," +
            "(select name from eg_boundary where id = csa.code::bigint and tenantid = :tenantId) as boundaryname," +
            " count(*) as count" +
            " from submission cs, submission_attribute csa, servicetype_keyword sk" +
            " where cs.crn = csa.crn and csa.key = 'systemLocationId'" +
            " and cs.servicecode = sk.servicecode and upper(sk.keyword) = upper('complaint')" +
            " and cs.tenantid = :tenantId and csa.tenantid = :tenantId and sk.tenantid = :tenantId");

        if(type.trim().equalsIgnoreCase("wardwise"))
            query.append(" and cs.servicecode = :servicecode");

        if(type.trim().equalsIgnoreCase("wardwiseregistered"))
            query.append(" and cs.status in ('REGISTERED', 'FORWARDED', 'PROCESSING', 'REOPENED', 'ONHOLD')");

        if(type.trim().equalsIgnoreCase("wardwiseresolved"))
            query.append(" and cs.status not in ('REGISTERED', 'FORWARDED', 'PROCESSING', 'REOPENED', 'ONHOLD')");

        String groupByQuery = " group by csa.code, csa.key";

        return namedParameterJdbcTemplate.query(query.append(groupByQuery).toString(), getWardWiseSearchMap(tenantId, serviceCode),
            new WardWiseRowMapper());

    }

    public List<AgeingResponse> getAgeingOfComplaints(String tenantId, List<Integer> range){

        String query = "select " +
            " count(CASE WHEN (date_part('epoch'::text, now() - (cs.createddate + (interval '1 hour' * ctype.slahours)))/ 86400::double precision) < "+range.get(0)+"::double precision THEN 1 ELSE" +
            " NULL::integer END) AS interval1," +
            " count(CASE WHEN (date_part('epoch'::text, now() - (cs.createddate + (interval '1 hour' * ctype.slahours)))/ 86400::double precision) >= "+range.get(0)+"::double precision" +
            " AND (date_part('epoch'::text, now() - (cs.createddate + (interval '1 hour' * ctype.slahours)))/ 86400::double precision) <= "+range.get(1)+"::double" +
            " precision THEN 1 ELSE NULL::integer END) AS interval2," +
            " count(CASE WHEN (date_part('epoch'::text, now() - (cs.createddate + (interval '1 hour' * ctype.slahours)))/ 86400::double precision) >= "+range.get(1)+"::double precision" +
            " AND (date_part('epoch'::text, now() - (cs.createddate + (interval '1 hour' * ctype.slahours)))/ 86400::double precision) <= "+range.get(2)+"::double" +
            " precision THEN 1 ELSE NULL::integer END) AS interval3," +
            " count(CASE WHEN (date_part('epoch'::text, now() - (cs.createddate + (interval '1 hour' * ctype.slahours)))/ 86400::double precision) >= "+range.get(2)+"::double precision" +
            " AND (date_part('epoch'::text, now() - (cs.createddate + (interval '1 hour' * ctype.slahours)))/ 86400::double precision) <= "+range.get(3)+"::double" +
            " precision THEN 1 ELSE NULL::integer END) AS interval4," +
            " count(CASE WHEN (date_part('epoch'::text, now() - (cs.createddate + (interval '1 hour' * ctype.slahours)))/ 86400::double precision) > "+range.get(3)+"::double precision THEN 1 ELSE" +
            " NULL::integer END) AS interval5" +
            " FROM egpgr_complainttype ctype, submission cs, servicetype_keyword sk" +
            " WHERE cs.servicecode = ctype.code and cs.createddate <= now() and cs.status IN ('REGISTERED', 'FORWARDED', 'PROCESSING', 'REOPENED', 'ONHOLD')" +
            " and ctype.code = sk.servicecode and upper(sk.keyword) = upper('complaint') and cs.tenantid = :tenantId and sk.tenantid = :tenantId and ctype.tenantid = :tenantId";

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

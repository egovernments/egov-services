package org.egov.works.workorder.domain.repository;

import org.egov.works.common.persistence.repository.JdbcRepository;
import org.egov.works.workorder.persistence.helper.MilestoneActivityHelper;
import org.egov.works.workorder.persistence.helper.TrackMilestoneActivityHelper;
import org.egov.works.workorder.persistence.helper.TrackMilestoneHelper;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ramki on 26/12/17.
 */
@Repository
public class TrackMilestoneJdbcRepository extends JdbcRepository {
    public static final String TABLE_NAME = "egw_trackmilestone milestone";
    public static final String MILESTONE_ESTIMATESEARCH_EXTENTION = " , egw_letterofacceptanceestimate loaestimate";
    public static final String MILESTONE_WORKORDER_EXTENTION = " , egw_workorder wo";
    public static final String MILESTONE_LOA_EXTENTION = " , egw_letterofacceptance loa";
    public static final String MILESTONE_CONTRACTOR_EXTENTION = " , egw_contractor contractor";
    public static final String GET_MILESTONE_ACTIVTIES_BY_MILESTONE = "select * from egw_trackmilestoneactivity where tenantid = :tenantId and deleted=false and trackmilestone=:milestone;";

    @Autowired
    private EstimateRepository estimateRepository;

    public List<TrackMilestone> search(final TrackMilestoneSearchContract trackMilestoneSearchContract, final RequestInfo requestInfo) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();
        List<String> detailedEstimateNumbers = new ArrayList<>();

        if (trackMilestoneSearchContract.getSortBy() != null && !trackMilestoneSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(trackMilestoneSearchContract.getSortBy());
            validateEntityFieldName(trackMilestoneSearchContract.getSortBy(), Milestone.class);
        }

        if (trackMilestoneSearchContract.getWorkOrderNumbers() != null && !trackMilestoneSearchContract.getWorkOrderNumbers().isEmpty()) {
            tableName += MILESTONE_WORKORDER_EXTENTION;
        }

        if ((trackMilestoneSearchContract.getLoaNumbers() != null && !trackMilestoneSearchContract.getLoaNumbers().isEmpty())) {
            tableName += MILESTONE_LOA_EXTENTION;
        }

        if ((trackMilestoneSearchContract.getContractorNames() != null && !trackMilestoneSearchContract.getContractorNames().isEmpty())
                || (trackMilestoneSearchContract.getContractorCodes() != null && !trackMilestoneSearchContract.getContractorCodes().isEmpty())) {
            tableName += MILESTONE_LOA_EXTENTION;
            tableName += MILESTONE_CONTRACTOR_EXTENTION;
        }

        if ((trackMilestoneSearchContract.getDetailedEstimateNumbers() != null && !trackMilestoneSearchContract.getDetailedEstimateNumbers().isEmpty())
                || (trackMilestoneSearchContract.getDepartments() != null && !trackMilestoneSearchContract.getDepartments().isEmpty())
                || (trackMilestoneSearchContract.getWorkOrderNumbers() != null && !trackMilestoneSearchContract.getWorkOrderNumbers().isEmpty())
                || (trackMilestoneSearchContract.getLoaNumbers() != null && !trackMilestoneSearchContract.getLoaNumbers().isEmpty())
                || (trackMilestoneSearchContract.getContractorNames() != null && !trackMilestoneSearchContract.getContractorNames().isEmpty())
                || (trackMilestoneSearchContract.getContractorCodes() != null && !trackMilestoneSearchContract.getContractorCodes().isEmpty())) {
            tableName += MILESTONE_ESTIMATESEARCH_EXTENTION;
            addAnd(params);
            params.append(" loaestimate.id = milestone.letterofacceptanceestimate");
        }

        StringBuilder orderBy = new StringBuilder("order by milestone.createdtime");
        if (trackMilestoneSearchContract.getSortBy() != null
                && !trackMilestoneSearchContract.getSortBy().isEmpty()) {
            orderBy.delete(0,orderBy.length()).append(" order by milestone.").append(trackMilestoneSearchContract.getSortBy());
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " milestone.* ");

        if (trackMilestoneSearchContract.getTenantId() != null) {
            addAnd(params);
            params.append("milestone.tenantId =:tenantId");
            paramValues.put("tenantId", trackMilestoneSearchContract.getTenantId());
        }

        if (trackMilestoneSearchContract.getIds() != null) {
            addAnd(params);
            params.append("milestone.id in (:ids) ");
            paramValues.put("ids", trackMilestoneSearchContract.getIds());
        }

        if (trackMilestoneSearchContract.getDetailedEstimateNumbers() != null && !trackMilestoneSearchContract.getDetailedEstimateNumbers().isEmpty())
            detailedEstimateNumbers.addAll(trackMilestoneSearchContract.getDetailedEstimateNumbers());

        if (trackMilestoneSearchContract.getLoaNumbers() != null && !trackMilestoneSearchContract.getLoaNumbers().isEmpty()) {
            addAnd(params);
            params.append("loa.loaNumber in (:loaNumbers)");
            paramValues.put("loaNumbers", trackMilestoneSearchContract.getLoaNumbers());
        }

        if (trackMilestoneSearchContract.getStatuses() != null) {
            addAnd(params);
            params.append("milestone.status in (:status)");
            paramValues.put("status", trackMilestoneSearchContract.getStatuses());
        }

        if (trackMilestoneSearchContract.getWorkOrderNumbers() != null && !trackMilestoneSearchContract.getWorkOrderNumbers().isEmpty()) {
            addAnd(params);
            params.append("loaestimate.letterofacceptance = wo.letterofacceptance and wo.workordernumber in (:workordernumbers) and wo.tenantId =:tenantId and wo.deleted=false");
            paramValues.put("tenantId", trackMilestoneSearchContract.getTenantId());
            paramValues.put("workordernumber", trackMilestoneSearchContract.getWorkOrderNumbers());
        }

        if (trackMilestoneSearchContract.getWorkIdentificationNumbers() != null && !trackMilestoneSearchContract.getWorkIdentificationNumbers().isEmpty()) {
            List<DetailedEstimate> detailedEstimates = estimateRepository.searchDetailedEstimatesByProjectCode(trackMilestoneSearchContract.getWorkIdentificationNumbers(), trackMilestoneSearchContract.getTenantId(), requestInfo);
            for (DetailedEstimate detailedEstimate : detailedEstimates)
                detailedEstimateNumbers.add(detailedEstimate.getEstimateNumber());
        }

        if (trackMilestoneSearchContract.getDepartments() != null && !trackMilestoneSearchContract.getDepartments().isEmpty()) {
            List<DetailedEstimate> detailedEstimates = estimateRepository.searchDetailedEstimatesByDepartment(trackMilestoneSearchContract.getDepartments(), trackMilestoneSearchContract.getTenantId(), requestInfo);
            for (DetailedEstimate detailedEstimate : detailedEstimates)
                detailedEstimateNumbers.add(detailedEstimate.getEstimateNumber());
        }

        if (detailedEstimateNumbers != null && !detailedEstimateNumbers.isEmpty()) {
            addAnd(params);
            params.append("loaestimate.detailedestimate in (:detailedestimatenumber) and loaestimate.tenantid=:tenantid and loaestimate.deleted=false");
            paramValues.put("detailedestimatenumber", detailedEstimateNumbers);
            paramValues.put("tenantid", trackMilestoneSearchContract.getTenantId());
        }

        if (trackMilestoneSearchContract.getContractorNames() != null && !trackMilestoneSearchContract.getContractorNames().isEmpty()) {
            addAnd(params);
            params.append("loa.contractor = contractor.code and contractor.name in (:contractorName) and contractor.tenantid=:tenantid and contractor.deleted=false");
            paramValues.put("contractorName", trackMilestoneSearchContract.getContractorNames());
            paramValues.put("tenantid", trackMilestoneSearchContract.getTenantId());
        }

        if (trackMilestoneSearchContract.getContractorCodes() != null && !trackMilestoneSearchContract.getContractorCodes().isEmpty()) {
            addAnd(params);
            params.append("loa.contractor = contractor.code and contractor.code in (:contractorCode) and contractor.tenantid=:tenantid and contractor.deleted=false");
            paramValues.put("contractorCode", trackMilestoneSearchContract.getContractorCodes());
            paramValues.put("tenantid", trackMilestoneSearchContract.getTenantId());
        }

        params.append(" and milestone.deleted = false");

        if (params.length() > 0) {
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        } else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        List<TrackMilestoneHelper> trackMilestoneHelpers = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, new BeanPropertyRowMapper(TrackMilestoneHelper.class));
        List<TrackMilestone> trackMilestones = new ArrayList<>();
        for (TrackMilestoneHelper trackMilestoneHelper : trackMilestoneHelpers) {
            TrackMilestone trackMilestone = trackMilestoneHelper.toDomain();
            trackMilestone.setTrackMilestoneActivities(prepareActivities(trackMilestone.getId(), trackMilestone.getTenantId()));
            trackMilestones.add(trackMilestone);
        }
        return trackMilestones;
    }

    private List<TrackMilestoneActivity> prepareActivities(String trackMilestone, String tenantId) {
        List<TrackMilestoneActivity> trackMilestoneActivities = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("tenantId", tenantId);
        params.put("milestone", trackMilestone);
        List<TrackMilestoneActivityHelper> trackMilestoneActivityHelpers = namedParameterJdbcTemplate.query(GET_MILESTONE_ACTIVTIES_BY_MILESTONE, params, new BeanPropertyRowMapper(TrackMilestoneActivityHelper.class));
        for (TrackMilestoneActivityHelper trackMilestoneActivityHelper : trackMilestoneActivityHelpers) {
            trackMilestoneActivities.add(trackMilestoneActivityHelper.toDomain());
        }
        return trackMilestoneActivities;
    }
}
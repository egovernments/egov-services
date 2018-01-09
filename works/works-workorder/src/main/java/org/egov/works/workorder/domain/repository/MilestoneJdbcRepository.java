package org.egov.works.workorder.domain.repository;

import org.egov.works.common.persistence.repository.JdbcRepository;
import org.egov.works.workorder.persistence.helper.MilestoneActivityHelper;
import org.egov.works.workorder.persistence.helper.MilestoneHelper;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ramki on 20/12/17.
 */
@Repository
public class MilestoneJdbcRepository extends JdbcRepository {
    public static final String TABLE_NAME = "egw_milestone milestone";
    public static final String MILESTONE_ESTIMATESEARCH_EXTENTION = " , egw_letterofacceptanceestimate loaestimate";
    public static final String MILESTONE_WORKORDER_EXTENTION = " , egw_workorder wo";
    public static final String MILESTONE_LOA_EXTENTION = " , egw_letterofacceptance loa";
    public static final String MILESTONE_CONTRACTOR_EXTENTION = " , egw_contractor contractor";
    public static final String GET_MILESTONE_ACTIVTIES_BY_MILESTONE = "select * from egw_milestoneactivity where " +
            "tenantid = :tenantId and deleted=false and milestone=:milestone;";
    public static final String GET_MILESTONE_ACTIVTIES_BY_ID = "select * from egw_milestoneactivity where " +
            "tenantid = :tenantId and deleted=false and id=:id;";

    @Autowired
    private EstimateRepository estimateRepository;

    public List<Milestone> search(final MilestoneSearchContract milestoneSearchContract, final RequestInfo requestInfo) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();
        List<String> detailedEstimateNumbers = new ArrayList<>();

        if (milestoneSearchContract.getSortBy() != null && !milestoneSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(milestoneSearchContract.getSortBy());
            validateEntityFieldName(milestoneSearchContract.getSortBy(), Milestone.class);
        }

        if (milestoneSearchContract.getWorkOrderNumbers() != null && !milestoneSearchContract.getWorkOrderNumbers().isEmpty()) {
            tableName += MILESTONE_WORKORDER_EXTENTION;
        }

        if ((milestoneSearchContract.getLoaNumbers() != null && !milestoneSearchContract.getLoaNumbers().isEmpty())) {
            tableName += MILESTONE_LOA_EXTENTION;
        }

        if ((milestoneSearchContract.getContractorNames() != null && !milestoneSearchContract.getContractorNames().isEmpty())
                || (milestoneSearchContract.getContractorCodes() != null && !milestoneSearchContract.getContractorCodes().isEmpty())) {
            tableName += MILESTONE_LOA_EXTENTION;
            tableName += MILESTONE_CONTRACTOR_EXTENTION;
        }

        if ((milestoneSearchContract.getDetailedEstimateNumbers() != null && !milestoneSearchContract.getDetailedEstimateNumbers().isEmpty())
                || (milestoneSearchContract.getDepartments() != null && !milestoneSearchContract.getDepartments().isEmpty())
                || (milestoneSearchContract.getWorkOrderNumbers() != null && !milestoneSearchContract.getWorkOrderNumbers().isEmpty())
                || (milestoneSearchContract.getLoaNumbers() != null && !milestoneSearchContract.getLoaNumbers().isEmpty())
                || (milestoneSearchContract.getContractorNames() != null && !milestoneSearchContract.getContractorNames().isEmpty())
                || (milestoneSearchContract.getContractorCodes() != null && !milestoneSearchContract.getContractorCodes().isEmpty())) {
            tableName += MILESTONE_ESTIMATESEARCH_EXTENTION;
            addAnd(params);
            params.append(" loaestimate.id = milestone.letterofacceptanceestimate");
        }

        StringBuilder orderBy = new StringBuilder("order by milestone.createdtime");
        if (milestoneSearchContract.getSortBy() != null
                && !milestoneSearchContract.getSortBy().isEmpty()) {
            orderBy.delete(0,orderBy.length()).append("order by milestone.").append(milestoneSearchContract.getSortBy());
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " milestone.* ");

        if (milestoneSearchContract.getTenantId() != null) {
            addAnd(params);
            params.append("milestone.tenantId =:tenantId");
            paramValues.put("tenantId", milestoneSearchContract.getTenantId());
        }

        if (milestoneSearchContract.getIds() != null) {
            addAnd(params);
            params.append("milestone.id in (:ids) ");
            paramValues.put("ids", milestoneSearchContract.getIds());
        }

        if (milestoneSearchContract.getDetailedEstimateNumbers() != null && !milestoneSearchContract.getDetailedEstimateNumbers().isEmpty())
            detailedEstimateNumbers.addAll(milestoneSearchContract.getDetailedEstimateNumbers());

        if (milestoneSearchContract.getLoaNumbers() != null && !milestoneSearchContract.getLoaNumbers().isEmpty()) {
            addAnd(params);
            params.append("loa.loaNumber in (:loaNumbers)");
            paramValues.put("loaNumbers", milestoneSearchContract.getLoaNumbers());
        }

        if (milestoneSearchContract.getStatuses() != null) {
            addAnd(params);
            params.append("milestone.status in (:status)");
            paramValues.put("status", milestoneSearchContract.getStatuses());
        }

        if (milestoneSearchContract.getWorkOrderNumbers() != null && !milestoneSearchContract.getWorkOrderNumbers().isEmpty()) {
            addAnd(params);
            params.append("loaestimate.letterofacceptance = wo.letterofacceptance and wo.workordernumber in (:workordernumbers) and wo.tenantId =:tenantId and wo.deleted=false");
            paramValues.put("tenantId", milestoneSearchContract.getTenantId());
            paramValues.put("workordernumber", milestoneSearchContract.getWorkOrderNumbers());
        }

        if (milestoneSearchContract.getWorkIdentificationNumbers() != null && !milestoneSearchContract.getWorkIdentificationNumbers().isEmpty()) {
            List<DetailedEstimate> detailedEstimates = estimateRepository.searchDetailedEstimatesByProjectCode(milestoneSearchContract.getWorkIdentificationNumbers(), milestoneSearchContract.getTenantId(), requestInfo);
            for (DetailedEstimate detailedEstimate : detailedEstimates)
                detailedEstimateNumbers.add(detailedEstimate.getEstimateNumber());
        }

        if (milestoneSearchContract.getDepartments() != null && !milestoneSearchContract.getDepartments().isEmpty()) {
            List<DetailedEstimate> detailedEstimates = estimateRepository.searchDetailedEstimatesByDepartment(milestoneSearchContract.getDepartments(), milestoneSearchContract.getTenantId(), requestInfo);
            for (DetailedEstimate detailedEstimate : detailedEstimates)
                detailedEstimateNumbers.add(detailedEstimate.getEstimateNumber());
        }

        if (detailedEstimateNumbers != null && !detailedEstimateNumbers.isEmpty()) {
            addAnd(params);
            params.append("loaestimate.detailedestimate in (:detailedestimatenumber) and loaestimate.tenantid=:tenantid and loaestimate.deleted=false");
            paramValues.put("detailedestimatenumber", detailedEstimateNumbers);
            paramValues.put("tenantid", milestoneSearchContract.getTenantId());
        }

        if (milestoneSearchContract.getContractorNames() != null && !milestoneSearchContract.getContractorNames().isEmpty()) {
            addAnd(params);
            params.append("loa.contractor = contractor.code and contractor.name in (:contractorName) and contractor.tenantid=:tenantid and contractor.deleted=false");
            paramValues.put("contractorName", milestoneSearchContract.getContractorNames());
            paramValues.put("tenantid", milestoneSearchContract.getTenantId());
        }

        if (milestoneSearchContract.getContractorCodes() != null && !milestoneSearchContract.getContractorCodes().isEmpty()) {
            addAnd(params);
            params.append("loa.contractor = contractor.code and contractor.code in (:contractorCode) and contractor.tenantid=:tenantid and contractor.deleted=false");
            paramValues.put("contractorCode", milestoneSearchContract.getContractorCodes());
            paramValues.put("tenantid", milestoneSearchContract.getTenantId());
        }

        params.append(" and milestone.deleted = false");

        if (params.length() > 0) {
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        } else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        List<MilestoneHelper> milestoneHelpers = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, new BeanPropertyRowMapper(MilestoneHelper.class));
        List<Milestone> milestones = new ArrayList<>();
        for (MilestoneHelper milestoneHelper : milestoneHelpers) {
            Milestone milestone = milestoneHelper.toDomain();
            milestone.setMilestoneActivities(prepareActivities(milestone.getId(), milestone.getTenantId()));
            milestones.add(milestone);
        }
        return milestones;
    }

    private List<MilestoneActivity> prepareActivities(String milestone, String tenantId) {
        List<MilestoneActivity> milestoneActivities = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("tenantId", tenantId);
        params.put("milestone", milestone);
        List<MilestoneActivityHelper> milestoneActivityHelpers = namedParameterJdbcTemplate.query(GET_MILESTONE_ACTIVTIES_BY_MILESTONE, params, new BeanPropertyRowMapper(MilestoneActivityHelper.class));
        for (MilestoneActivityHelper milestoneActivityHelper : milestoneActivityHelpers) {
            milestoneActivities.add(milestoneActivityHelper.toDomain());
        }
        return milestoneActivities;
    }

    public MilestoneActivity getActivityById(String milestoneActivityId, String tenantId) {
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("tenantId", tenantId);
        paramValues.put("id", milestoneActivityId);
        List<MilestoneActivityHelper> milestoneActivityHelpers = namedParameterJdbcTemplate.query(GET_MILESTONE_ACTIVTIES_BY_ID, paramValues, new BeanPropertyRowMapper(MilestoneActivityHelper.class));
        return milestoneActivityHelpers.get(0).toDomain();
    }
}
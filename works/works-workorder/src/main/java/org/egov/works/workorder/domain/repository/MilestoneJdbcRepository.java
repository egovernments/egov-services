package org.egov.works.workorder.domain.repository;

import org.egov.works.common.persistence.repository.JdbcRepository;
import org.egov.works.workorder.persistence.helper.MilestoneHelper;
import org.egov.works.workorder.web.contract.DetailedEstimate;
import org.egov.works.workorder.web.contract.Milestone;
import org.egov.works.workorder.web.contract.MilestoneSearchContract;
import org.egov.works.workorder.web.contract.RequestInfo;
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

    @Autowired
    private EstimateRepository estimateRepository;

    public List<Milestone> search(final MilestoneSearchContract milestoneSearchContract, final RequestInfo requestInfo) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (milestoneSearchContract.getSortBy() != null
                && !milestoneSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(milestoneSearchContract.getSortBy());
            validateEntityFieldName(milestoneSearchContract.getSortBy(), Milestone.class);
        }

        if ((milestoneSearchContract.getDetailedEstimateNumbers() != null && !milestoneSearchContract.getDetailedEstimateNumbers().isEmpty())
                || (milestoneSearchContract.getDepartments() != null && !milestoneSearchContract.getDepartments().isEmpty())
                || (milestoneSearchContract.getWorkOrderNumbers() != null && !milestoneSearchContract.getWorkOrderNumbers().isEmpty()))
            tableName += MILESTONE_ESTIMATESEARCH_EXTENTION;

        if (milestoneSearchContract.getWorkOrderNumbers() != null && !milestoneSearchContract.getWorkOrderNumbers().isEmpty()) {
            tableName += MILESTONE_WORKORDER_EXTENTION;
        }

        String orderBy = "order by milestone.id";
        if (milestoneSearchContract.getSortBy() != null
                && !milestoneSearchContract.getSortBy().isEmpty()) {
            orderBy = "order by milestone." + milestoneSearchContract.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (milestoneSearchContract.getTenantId() != null) {
            addAnd(params);
            params.append("milestone.tenantId =:tenantId");
            paramValues.put("tenantId", milestoneSearchContract.getTenantId());
        }
        if (milestoneSearchContract.getIds() != null) {
            addAnd(params);
            params.append("milestone.id in(:ids) ");
            paramValues.put("ids", milestoneSearchContract.getIds());
        }
        if (milestoneSearchContract.getLoaNumbers() != null && !milestoneSearchContract.getLoaNumbers().isEmpty()) {
            addAnd(params);
            params.append("loa.loaNumber in(:loaNumbers)");
            paramValues.put("loaNumbers", milestoneSearchContract.getLoaNumbers());
        }

        if (milestoneSearchContract.getStatuses() != null) {
            addAnd(params);
            params.append("milestone.status in(:status)");
            paramValues.put("status", milestoneSearchContract.getStatuses());
        }

        if (milestoneSearchContract.getDetailedEstimateNumbers() != null && !milestoneSearchContract.getDetailedEstimateNumbers().isEmpty()) {
            addAnd(params);
            params.append("loaestimate.id = milestone.letterofacceptanceestimate and loaestimate.detailedestimate in :detailedestimatenumber and loaestimate.tenantId =:tenantId");
            paramValues.put("tenantId", milestoneSearchContract.getTenantId());
            paramValues.put("detailedestimatenumber", milestoneSearchContract.getDetailedEstimateNumbers());
        }

        if (milestoneSearchContract.getWorkOrderNumbers() != null && !milestoneSearchContract.getWorkOrderNumbers().isEmpty()) {
            addAnd(params);
            params.append("loaestimate.id = milestone.letterofacceptanceestimate and loaestimate.letterofacceptance = wo.letterofacceptance and wo.workordernumber in :workordernumbers and wo.tenantId =:tenantId");
            paramValues.put("tenantId", milestoneSearchContract.getTenantId());
            paramValues.put("workordernumber", milestoneSearchContract.getWorkOrderNumbers());
        }

        List<String> detailedEstimateNumbers = new ArrayList<>();
        if (milestoneSearchContract.getWorkIdentificationNumbers() != null && !milestoneSearchContract.getWorkIdentificationNumbers().isEmpty()) {
            List<DetailedEstimate> detailedEstimates = estimateRepository.searchDetailedEstimatesByProjectCode(milestoneSearchContract.getWorkIdentificationNumbers(), milestoneSearchContract.getTenantId(), requestInfo);
            for (DetailedEstimate detailedEstimate : detailedEstimates)
                detailedEstimateNumbers.add(detailedEstimate.getEstimateNumber());

            addAnd(params);
            params.append("loaestimate.id = milestone.letterofacceptanceestimate and loaestimate.letterofacceptance = wo.letterofacceptance and loaestimate.detailedestimate in :detailedestimatenumber and loaestimate.tenantid=:tenantid");
            paramValues.put("detailedestimatenumber", detailedEstimateNumbers);
            paramValues.put("tenantid", milestoneSearchContract.getTenantId());
        }

        List<String> estimateNumbers = new ArrayList<>();
        if (milestoneSearchContract.getDepartments() != null && !milestoneSearchContract.getDepartments().isEmpty()) {
            List<DetailedEstimate> detailedEstimates = estimateRepository.searchDetailedEstimatesByDepartment(milestoneSearchContract.getDepartments(), milestoneSearchContract.getTenantId(), requestInfo);
            for (DetailedEstimate detailedEstimate : detailedEstimates)
                estimateNumbers.add(detailedEstimate.getEstimateNumber());

            addAnd(params);
            params.append("loaestimate.letterofacceptance = wo.letterofacceptance and loaestimate.detailedestimate in :detailedestimatenumber and loaestimate.tenantid=:tenantid");
            paramValues.put("detailedestimatenumber", milestoneSearchContract.getDetailedEstimateNumbers());
            paramValues.put("tenantid",milestoneSearchContract.getTenantId());
        }

        params.append(" and milestone.deleted = false");

        if (params.length() > 0) {
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        } else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(MilestoneHelper.class);

        List<MilestoneHelper> milestoneList = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
        List<Milestone> milestones = new ArrayList<>();
        for (MilestoneHelper milestoneHelper : milestoneList) {
            Milestone milestone = milestoneHelper.toDomain();
            milestones.add(milestone);
        }
        return milestones;
    }
}

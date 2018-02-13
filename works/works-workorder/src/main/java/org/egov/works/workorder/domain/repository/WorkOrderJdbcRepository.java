package org.egov.works.workorder.domain.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.works.common.persistence.repository.JdbcRepository;
import org.egov.works.workorder.persistence.helper.WorkOrderHelper;
import org.egov.works.workorder.web.contract.Contractor;
import org.egov.works.workorder.web.contract.DetailedEstimate;
import org.egov.works.workorder.web.contract.LetterOfAcceptance;
import org.egov.works.workorder.web.contract.LetterOfAcceptanceSearchContract;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.egov.works.workorder.web.contract.WorkOrder;
import org.egov.works.workorder.web.contract.WorkOrderDetailSearchContract;
import org.egov.works.workorder.web.contract.WorkOrderSearchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * Created by ritesh on 27/11/17.
 */
@Repository
public class WorkOrderJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egw_workorder wo";
    public static final String LOA_ESTIMATESEARCH_EXTENTION = "egw_letterofacceptanceestimate loaestimate";
    @Autowired
    private WorksMastersRepository worksMastersRepository;

    @Autowired
    private EstimateRepository estimateRepository;

    @Autowired
    private WorkOrderDetailRepository workOrderDetailRepository;

    @Autowired
    private LetterOfAcceptanceRepository letterOfAcceptanceRepository;

    public List<WorkOrder> searchWorkOrders(final WorkOrderSearchContract workOrderSearchContract,
            final RequestInfo requestInfo) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (workOrderSearchContract.getSortBy() != null
                && !workOrderSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(workOrderSearchContract.getSortBy());
            validateEntityFieldName(workOrderSearchContract.getSortBy(), WorkOrder.class);
        }

        if ((workOrderSearchContract.getDetailedEstimateNumbers() != null
                && !workOrderSearchContract.getDetailedEstimateNumbers().isEmpty())
                || (workOrderSearchContract.getDepartment() != null && !workOrderSearchContract.getDepartment().isEmpty()))
            tableName += LOA_ESTIMATESEARCH_EXTENTION;

        StringBuilder orderBy = new StringBuilder("order by wo.createdtime");
        if (workOrderSearchContract.getSortBy() != null
                && !workOrderSearchContract.getSortBy().isEmpty()) {
            orderBy.delete(0,orderBy.length()).append("order by wo.").append(workOrderSearchContract.getSortBy());
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (workOrderSearchContract.getTenantId() != null) {
            addAnd(params);
            params.append("wo.tenantId =:tenantId");
            paramValues.put("tenantId", workOrderSearchContract.getTenantId());
        }
        if (workOrderSearchContract.getIds() != null) {
            addAnd(params);
            params.append("wo.id in(:ids) ");
            paramValues.put("ids", workOrderSearchContract.getIds());
        }

        if (workOrderSearchContract.getStatuses() != null) {
            addAnd(params);
            params.append("wo.status in(:status)");
            paramValues.put("status", workOrderSearchContract.getStatuses());
        }

        if (workOrderSearchContract.getLetterOfAcceptances() != null) {
            addAnd(params);
            params.append("wo.letterofacceptance in(:letterofacceptance)");
            paramValues.put("letterofacceptance", workOrderSearchContract.getLetterOfAcceptances());
        }

        if (workOrderSearchContract.getFromDate() != null) {
            addAnd(params);
            params.append("wo.workorderdate >= (:createddate)");
            paramValues.put("createddate", workOrderSearchContract.getFromDate());
        }

        if (workOrderSearchContract.getToDate() != null) {
            addAnd(params);
            params.append("wo.workorderdate <= (:todate)");
            paramValues.put("todate", workOrderSearchContract.getToDate());
        }

        if (StringUtils.isNotBlank(workOrderSearchContract.getWorkOrderNumberLike())) {
            addAnd(params);
            params.append("lower(wo.workordernumber) like :workordernumberslike");
            paramValues.put("workordernumberslike", "%" + workOrderSearchContract.getWorkOrderNumberLike().toLowerCase() + "%");
        }
        if (workOrderSearchContract.getWorkOrderNumbers() != null && !workOrderSearchContract.getWorkOrderNumbers().isEmpty()) {
            addAnd(params);
            params.append("wo.workordernumber in (:workordernumbers)");
            paramValues.put("workordernumbers", workOrderSearchContract.getWorkOrderNumbers());
        }

        if (StringUtils.isNotBlank(workOrderSearchContract.getLoaNumberLike())) {
            addAnd(params);
            params.append(
                    "wo.letterofacceptance in (select loa.id from egw_letterofacceptance loa where lower(loa.loanumber) like :loaNumber and loa.tenantId=:tenantId)");
            paramValues.put("loaNumber", "%" + workOrderSearchContract.getLoaNumberLike().toLowerCase() + "%");
        }
        if (workOrderSearchContract.getLoaNumbers() != null && !workOrderSearchContract.getLoaNumbers().isEmpty()) {
            addAnd(params);
            params.append(
                    "wo.letterofacceptance in (select loa.id from egw_letterofacceptance loa where loa.loanumber in (:loaNumbers) and loa.tenantId=:tenantId)");
            paramValues.put("loaNumbers", workOrderSearchContract.getLoaNumbers());
        }

        if (StringUtils.isNotBlank(workOrderSearchContract.getContractorCodeLike())) {
            addAnd(params);
            params.append(
                    "wo.letterofacceptance in (select loa.id from egw_letterofacceptance loa where lower(loa.contractor) like (:contractorcode))");
            paramValues.put("contractorcode", "%" + workOrderSearchContract.getContractorCodeLike().toLowerCase() + "%");
        }
        if (workOrderSearchContract.getContractorCodes() != null && !workOrderSearchContract.getContractorCodes().isEmpty()) {
            addAnd(params);
            params.append(
                    "wo.letterofacceptance in (select loa.id from egw_letterofacceptance loa where loa.contractor in (:contractorcodes))");
            paramValues.put("contractorcodes", workOrderSearchContract.getContractorCodes());
        }

        List<String> contractorCodes = new ArrayList<>();
        if (workOrderSearchContract.getContractorNames() != null && !workOrderSearchContract.getContractorNames().isEmpty()) {
            List<Contractor> contractors = worksMastersRepository.searchContractors(workOrderSearchContract.getTenantId(),
                    workOrderSearchContract.getContractorNames(), requestInfo);
            for (Contractor contractor : contractors)
                contractorCodes.add(contractor.getCode());
            if (!contractorCodes.isEmpty()) {
                if (!contractorCodes.isEmpty() && contractorCodes.size() == 1) {
                    addAnd(params);
                    params.append(
                            "wo.letterofacceptance in (select loa.id from egw_letterofacceptance loa where lower(loa.contractor) like (:contractorcode))");
                    paramValues.put("contractorcode", "%" + contractorCodes.get(0).toLowerCase() + "%");
                } else if (contractorCodes != null) {
                    addAnd(params);
                    params.append(
                            "wo.letterofacceptance in (select loa.id from egw_letterofacceptance loa where loa.contractor in (:contractorcodes))");
                    paramValues.put("contractorcodes", contractorCodes);
                }
            }
        }

        if (StringUtils.isNotBlank(workOrderSearchContract.getDetailedEstimateNumberLike())) {
            addAnd(params);
            params.append(
                    "loaestimate.letterofacceptance = wo.letterofacceptance and upper(loaestimate.detailedestimate) like :detailedestimatenumberlike and loaestimate.tenantid=:tenantid ");
            paramValues.put("detailedestimatenumberlike",
                    '%' + workOrderSearchContract.getDetailedEstimateNumberLike().toUpperCase() + '%');
            paramValues.put("tenantid", workOrderSearchContract.getTenantId());
        }
        if (workOrderSearchContract.getDetailedEstimateNumbers() != null
                && workOrderSearchContract.getDetailedEstimateNumbers().size() > 1) {
            addAnd(params);
            params.append(
                    "loaestimate.letterofacceptance = wo.letterofacceptance and loaestimate.detailedestimate in :detailedestimatenumber and loaestimate.tenantid=:tenantid ");
            paramValues.put("detailedestimatenumber", workOrderSearchContract.getDetailedEstimateNumbers());
            paramValues.put("tenantid", workOrderSearchContract.getTenantId());
        }

        List<String> estimateNumbers = new ArrayList<>();
        if (workOrderSearchContract.getDepartment() != null && !workOrderSearchContract.getDepartment().isEmpty()) {
            List<DetailedEstimate> detailedEstimates = estimateRepository.searchDetailedEstimatesByDepartment(
                    workOrderSearchContract.getDepartment(), workOrderSearchContract.getTenantId(), requestInfo);
            for (DetailedEstimate detailedEstimate : detailedEstimates)
                estimateNumbers.add(detailedEstimate.getEstimateNumber());

            addAnd(params);
            params.append(
                    "loaestimate.letterofacceptance = wo.letterofacceptance and loaestimate.detailedestimate in :detailedestimatenumber and loaestimate.tenantid=:tenantid");
            paramValues.put("detailedestimatenumber", workOrderSearchContract.getDetailedEstimateNumbers());
            paramValues.put("tenantid", workOrderSearchContract.getTenantId());
        }

        List<String> winEstimateNumbers = new ArrayList<>();
        if (workOrderSearchContract.getWorkIdentificationNumbers() != null
                && !workOrderSearchContract.getWorkIdentificationNumbers().isEmpty()) {
            List<DetailedEstimate> detailedEstimates = estimateRepository.searchDetailedEstimatesByProjectCode(
                    workOrderSearchContract.getWorkIdentificationNumbers(), workOrderSearchContract.getTenantId(), requestInfo);
            for (DetailedEstimate detailedEstimate : detailedEstimates)
                winEstimateNumbers.add(detailedEstimate.getEstimateNumber());

            addAnd(params);
            params.append(
                    "loaestimate.letterofacceptance = wo.letterofacceptance and loaestimate.detailedestimate in :detailedestimatenumber and loaestimate.tenantid=:tenantid");
            paramValues.put("detailedestimatenumber", winEstimateNumbers);
            paramValues.put("tenantid", workOrderSearchContract.getTenantId());
        }

        List<String> winEstimateNumberLike = new ArrayList<>();
        if (StringUtils.isNotBlank(workOrderSearchContract.getWorkIdentificationNumberLike())) {
            List<DetailedEstimate> detailedEstimates = estimateRepository.searchDetailedEstimatesByProjectCode(
                    Arrays.asList(workOrderSearchContract.getWorkIdentificationNumberLike()),
                    workOrderSearchContract.getTenantId(), requestInfo);
            for (DetailedEstimate detailedEstimate : detailedEstimates)
                winEstimateNumberLike.add(detailedEstimate.getEstimateNumber());

            addAnd(params);
            params.append(
                    "loaestimate.letterofacceptance = wo.letterofacceptance and lower(loaestimate.detailedestimate) like :detailedestimatenumberlike and loaestimate.tenantid=:tenantid");
            paramValues.put("detailedestimatenumberlike", "%" + winEstimateNumberLike.get(0).toLowerCase() + "%");
            paramValues.put("tenantid", workOrderSearchContract.getTenantId());
        }

        params.append(" and wo.deleted = false");
        if(workOrderSearchContract.getWithAllOfflineStatusAndMBNotCreated() != null)
          params.append(" and wo.withAllOfflineStatusAndWONotCreated = ").append(workOrderSearchContract.getWithAllOfflineStatusAndMBNotCreated());

        if(workOrderSearchContract.getMilestoneExists() != null)
          params.append(" and wo.milestoneExists = ").append(workOrderSearchContract.getMilestoneExists());

        if(workOrderSearchContract.getBillExists() != null)
          params.append(" and wo.billExists = ").append(workOrderSearchContract.getBillExists());

        if(workOrderSearchContract.getContractorAdvanceExists() != null)
          params.append(" and wo.contractorAdvanceExists = ").append(workOrderSearchContract.getContractorAdvanceExists());

        if(workOrderSearchContract.getMbExistsAndBillNotCreated() != null)
          params.append(" and wo.mbExistsAndBillNotCreated = ").append(workOrderSearchContract.getMbExistsAndBillNotCreated());

        if(workOrderSearchContract.getWithoutOfflineStatus() != null)
          params.append(" and wo.withoutOfflineStatus = ").append(workOrderSearchContract.getWithoutOfflineStatus());

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(WorkOrderHelper.class);

        List<WorkOrderHelper> workOrderHelpers = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

        List<WorkOrder> workOrders = new ArrayList<>();
        WorkOrderDetailSearchContract workOrderDetailSearchContract = new WorkOrderDetailSearchContract();
        for (WorkOrderHelper workOrderHelper : workOrderHelpers) {

            WorkOrder workOrder = workOrderHelper.toDomain();
            workOrderDetailSearchContract.setTenantId(workOrderSearchContract.getTenantId());
            workOrderDetailSearchContract.setWorkOrders(Arrays.asList(workOrderHelper.getId()));
            workOrder.setWorkOrderDetails(workOrderDetailRepository.searchWorkOrderDetails(workOrderDetailSearchContract));

            LetterOfAcceptanceSearchContract letterOfAcceptanceSearchContract = new LetterOfAcceptanceSearchContract();
            letterOfAcceptanceSearchContract.setTenantId(workOrderSearchContract.getTenantId());
            letterOfAcceptanceSearchContract.setIds(Arrays.asList(workOrderHelper.getLetterOfAcceptance()));

            List<LetterOfAcceptance> letterOfAcceptanceList = letterOfAcceptanceRepository
                    .searchLOAs(letterOfAcceptanceSearchContract, requestInfo);
            LetterOfAcceptance letterOfAcceptance = new LetterOfAcceptance();
            if (letterOfAcceptanceList != null && !letterOfAcceptanceList.isEmpty())
                letterOfAcceptance = letterOfAcceptanceList.get(0);
            workOrder.setLetterOfAcceptance(letterOfAcceptance);
            workOrders.add(workOrder);
        }

        return workOrders;
    }

}

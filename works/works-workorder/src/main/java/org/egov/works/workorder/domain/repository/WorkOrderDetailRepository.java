package org.egov.works.workorder.domain.repository;

import org.egov.works.common.persistence.repository.JdbcRepository;
import org.egov.works.workorder.persistence.helper.WorkOrderDetailHelper;
import org.egov.works.workorder.web.contract.WorkOrderDetail;
import org.egov.works.workorder.web.contract.WorkOrderDetailSearchContract;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WorkOrderDetailRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egw_workorder_details wod";


    public List<WorkOrderDetail> searchWorkOrderDetails(final WorkOrderDetailSearchContract workOrderDetailSearchContract) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (workOrderDetailSearchContract.getSortBy() != null
                && !workOrderDetailSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(workOrderDetailSearchContract.getSortBy());
            validateEntityFieldName(workOrderDetailSearchContract.getSortBy(), WorkOrderDetailHelper.class);
        }

        String orderBy = "order by wod.workorder";
        if (workOrderDetailSearchContract.getSortBy() != null
                && !workOrderDetailSearchContract.getSortBy().isEmpty()) {
            orderBy = "order by wod." + workOrderDetailSearchContract.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (workOrderDetailSearchContract.getTenantId() != null) {
            addAnd(params);
            params.append("wod.tenantId =:tenantId");
            paramValues.put("tenantId", workOrderDetailSearchContract.getTenantId());
        }
        if (workOrderDetailSearchContract.getIds() != null) {
            addAnd(params);
            params.append("wod.id in(:ids) ");
            paramValues.put("ids", workOrderDetailSearchContract.getIds());
        }


        if (workOrderDetailSearchContract.getWorkOrders() != null && !workOrderDetailSearchContract.getWorkOrders().isEmpty()) {
            addAnd(params);
            params.append("wod.workorder in(:workOrders)");
            paramValues.put("workOrders", workOrderDetailSearchContract.getWorkOrders());
        }

        params.append(" and wod.deleted = false");

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(WorkOrderDetailHelper.class);

        List<WorkOrderDetailHelper> workOrderDetailHelpers = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
        List<WorkOrderDetail> workOrderDetails = new ArrayList<>();

        for (WorkOrderDetailHelper workOrderDetailHelper : workOrderDetailHelpers) {
            workOrderDetails.add(workOrderDetailHelper.toDomain());
        }
        return workOrderDetails;
    }
}

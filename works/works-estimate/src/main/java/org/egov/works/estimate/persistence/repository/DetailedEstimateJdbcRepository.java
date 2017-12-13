package org.egov.works.estimate.persistence.repository;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.estimate.persistence.helper.DetailedEstimateHelper;
import org.egov.works.estimate.web.contract.DetailedEstimate;
import org.egov.works.estimate.web.contract.DetailedEstimateSearchContract;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DetailedEstimateJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egw_detailedestimate de";
    public static final String TABLE_NAME_TECHNICAL = ",egw_estimate_technicalsanction ts";
    public static final String TABLE_NAME_ACTIVITY = ", egw_estimate_activity activity";

    public List<DetailedEstimateHelper> search(DetailedEstimateSearchContract detailedEstimateSearchContract) {
        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        Map<String, Object> paramValues = new HashMap<>();
        StringBuffer params = new StringBuffer();

        String tableName = TABLE_NAME;
        if (detailedEstimateSearchContract.getTechnicalSanctionNumbers() != null
                && !detailedEstimateSearchContract.getTechnicalSanctionNumbers().isEmpty())
            tableName += TABLE_NAME_TECHNICAL;

        if (detailedEstimateSearchContract.getScheduleOfRate() != null
                && !detailedEstimateSearchContract.getScheduleOfRate().isEmpty())
            tableName += TABLE_NAME_ACTIVITY;

        if (detailedEstimateSearchContract.getSortBy() != null
                && !detailedEstimateSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(detailedEstimateSearchContract.getSortBy());
            validateEntityFieldName(detailedEstimateSearchContract.getSortBy(), DetailedEstimate.class);
        }

        String orderBy = "order by de.id";
        if (detailedEstimateSearchContract.getSortBy() != null
                && !detailedEstimateSearchContract.getSortBy().isEmpty()) {
            orderBy = "order by de." + detailedEstimateSearchContract.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " de.* ");

        if (detailedEstimateSearchContract.getTenantId() != null) {
            addAnd(params);
            params.append("de.tenantId =:tenantId");
            paramValues.put("tenantId", detailedEstimateSearchContract.getTenantId());
        }
        if (detailedEstimateSearchContract.getIds() != null) {
            addAnd(params);
            params.append("de.id in(:ids) ");
            paramValues.put("ids", detailedEstimateSearchContract.getIds());
        }
        if (detailedEstimateSearchContract.getDetailedEstimateNumbers() != null && !detailedEstimateSearchContract.getDetailedEstimateNumbers().isEmpty() && detailedEstimateSearchContract.getDetailedEstimateNumbers().size() == 1) {
            addAnd(params);
            params.append("lower(de.estimateNumber) like :estimateNumbers ");
            paramValues.put("estimateNumbers", '%' + detailedEstimateSearchContract.getDetailedEstimateNumbers().get(0).toLowerCase() + '%');
        } else if (detailedEstimateSearchContract.getDetailedEstimateNumbers() != null) {
            addAnd(params);
            params.append("de.estimateNumber in(:estimateNumbers)");
            paramValues.put("estimateNumbers", detailedEstimateSearchContract.getDetailedEstimateNumbers());
        }
        if (detailedEstimateSearchContract.getDepartments() != null) {
            addAnd(params);
            params.append("de.department  in (:departmentCodes)");
            paramValues.put("departmentCodes", detailedEstimateSearchContract.getDepartments());
        }
        if (detailedEstimateSearchContract.getTypeOfWork() != null) {
            addAnd(params);
            params.append("de.typeofwork in(:typeofwork)");
            paramValues.put("typeofwork", detailedEstimateSearchContract.getTypeOfWork());
        }
        if (detailedEstimateSearchContract.getSubTypeOfWork() != null) {
            addAnd(params);
            params.append("de.subtypeofwork in(:subtypeofwork)");
            paramValues.put("subtypeofwork", detailedEstimateSearchContract.getSubTypeOfWork());
        }
        if (detailedEstimateSearchContract.getStatuses() != null) {
            addAnd(params);
            params.append("de.status in(:status)");
            paramValues.put("status", detailedEstimateSearchContract.getStatuses());
        }
        if (detailedEstimateSearchContract.getFromDate() != null) {
            addAnd(params);
            params.append("de.estimatedate >=:estimatedate");
            paramValues.put("estimatedate", detailedEstimateSearchContract.getFromDate());
        }
        if (detailedEstimateSearchContract.getToDate() != null) {
            addAnd(params);
            params.append("de.estimatedate <=:estimatedate");
            paramValues.put("estimatedate", detailedEstimateSearchContract.getToDate());
        }
        if (detailedEstimateSearchContract.getFromAmount() != null) {
            addAnd(params);
            params.append("de.estimateValue >=:estimateValue");
            paramValues.put("de.estimateValue", detailedEstimateSearchContract.getFromAmount());
        }
        if (detailedEstimateSearchContract.getToAmount() != null) {
            addAnd(params);
            params.append("de.estimateValue <=:estimateValue");
            paramValues.put("estimateValue", detailedEstimateSearchContract.getToAmount());
        }
        if (detailedEstimateSearchContract.getSpillOverFlag() != null) {
            addAnd(params);
            params.append("de.spillOverFlag =:spillOverFlag");
            paramValues.put("spillOverFlag", detailedEstimateSearchContract.getSpillOverFlag());
        }
        if (detailedEstimateSearchContract.getCreatedBy() != null) {
            addAnd(params);
            params.append("lower(de.createdBy) =:createdBy");
            paramValues.put("createdBy", detailedEstimateSearchContract.getCreatedBy().toLowerCase());
        }
        if (detailedEstimateSearchContract.getWorkIdentificationNumbers() != null && detailedEstimateSearchContract.getWorkIdentificationNumbers().size() == 1) {
            addAnd(params);
            params.append(" lower(de.projectCode) like :workIdentificationNumbers");
            paramValues.put("workIdentificationNumbers", "%" + detailedEstimateSearchContract.getWorkIdentificationNumbers().get(0).toLowerCase() + "%");
        } else if (detailedEstimateSearchContract.getWorkIdentificationNumbers() != null && detailedEstimateSearchContract.getWorkIdentificationNumbers().size() > 1) {
            addAnd(params);
            params.append(" de.projectCode in :workIdentificationNumbers");
            paramValues.put("workIdentificationNumbers", detailedEstimateSearchContract.getWorkIdentificationNumbers());
        }

        if (detailedEstimateSearchContract.getNameOfWork() != null) {
            addAnd(params);
            params.append("upper(de.nameOfWork) like :nameOfWork");
            paramValues.put("nameOfWork", '%' + detailedEstimateSearchContract.getNameOfWork().toUpperCase() + '%');

        }

        if (detailedEstimateSearchContract.getWards() != null) {
            addAnd(params);
            params.append("de.ward =:ward");
            paramValues.put("ward", detailedEstimateSearchContract.getWards());
        }

        if (detailedEstimateSearchContract.getTechnicalSanctionNumbers() != null
                && detailedEstimateSearchContract.getTechnicalSanctionNumbers().size() == 1) {
            addAnd(params);
            params.append("ts.detailedestimate = de.id and ts.tenantId =:tenantId and ts.deleted=false and lower(ts.technicalsanctionnumber) like :technicalsanctionnumber");
            paramValues.put("technicalsanctionnumber", "%" + detailedEstimateSearchContract.getTechnicalSanctionNumbers().get(0).toLowerCase() + "%");
        } else if (detailedEstimateSearchContract.getTechnicalSanctionNumbers() != null
                && detailedEstimateSearchContract.getTechnicalSanctionNumbers().size() > 1) {
            addAnd(params);
            params.append("ts.detailedestimate = de.id and ts.tenantId =:tenantId and ts.deleted=false and ts.technicalsanctionnumber in :technicalsanctionnumber");
            paramValues.put("technicalsanctionnumber", detailedEstimateSearchContract.getTechnicalSanctionNumbers());
        }

        if (detailedEstimateSearchContract.getScheduleOfRate() != null
                && !detailedEstimateSearchContract.getScheduleOfRate().isEmpty()) {
            addAnd(params);
            params.append("activity.detailedestimate = de.id and activity.tenantId =:tenantId and activity.deleted=false and activity.scheduleofrate is not null and activity.scheduleofrate = :scheduleofrate");
            paramValues.put("scheduleofrate", detailedEstimateSearchContract.getScheduleOfRate());
        }

        params.append(" and de.deleted = false");
        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(DetailedEstimateHelper.class);

        return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
    }

}

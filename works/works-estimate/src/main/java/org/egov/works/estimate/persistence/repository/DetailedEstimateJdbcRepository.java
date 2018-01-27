package org.egov.works.estimate.persistence.repository;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.estimate.config.Constants;
import org.egov.works.estimate.persistence.helper.DetailedEstimateHelper;
import org.egov.works.estimate.web.contract.DetailedEstimate;
import org.egov.works.estimate.web.contract.DetailedEstimateSearchContract;
import org.egov.works.estimate.web.contract.OfflineStatus;
import org.egov.works.estimate.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DetailedEstimateJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egw_detailedestimate de";
    public static final String TABLE_NAME_TECHNICAL = ",egw_estimate_technicalsanction ts";
    public static final String TABLE_NAME_ACTIVITY = ", egw_estimate_activity activity";
    public static final String TABLE_NAME_AE = ", egw_abstractestimate ae";
    public static final String TABLE_NAME_AED = ", egw_abstractestimate_details aed";

    @Autowired
    private OfflineStatusRepository offlineStatusRepository;

    public List<DetailedEstimateHelper> search(DetailedEstimateSearchContract detailedEstimateSearchContract, final RequestInfo requestInfo) {
        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        Map<String, Object> paramValues = new HashMap<>();
        StringBuffer params = new StringBuffer();

        String tableName = TABLE_NAME;
        if ((detailedEstimateSearchContract.getTechnicalSanctionNumbers() != null
                && !detailedEstimateSearchContract.getTechnicalSanctionNumbers().isEmpty()) || StringUtils.isNotBlank(detailedEstimateSearchContract.getTechnicalSanctionNumberLike()))
            tableName += TABLE_NAME_TECHNICAL;

        if (detailedEstimateSearchContract.getScheduleOfRate() != null
                && !detailedEstimateSearchContract.getScheduleOfRate().isEmpty())
            tableName += TABLE_NAME_ACTIVITY;

        if ((detailedEstimateSearchContract.getAbstractEstimateNumbers() != null && !detailedEstimateSearchContract.getAbstractEstimateNumbers().isEmpty())
                || StringUtils.isNotBlank(detailedEstimateSearchContract.getAbstractEstimateNumberLike())) {
            tableName += TABLE_NAME_AE;
            tableName += TABLE_NAME_AED;
        }

        if (StringUtils.isNotBlank(detailedEstimateSearchContract.getAbstractEstimateNumberLike())) {
            addAnd(params);
            params.append(
                    "aed.id = (de.abstractestimatedetail) and aed.abstractestimate= ae.id and lower(ae.abstractestimatenumber) like (:abstractEstimateNumber) and aed.tenantId =:tenantId and ae.tenantId =:tenantId and aed.deleted=false and ae.deleted=false");
            paramValues.put("abstractEstimateNumber",
                    "%" + detailedEstimateSearchContract.getAbstractEstimateNumberLike().toLowerCase() + "%");
        }

        if (detailedEstimateSearchContract.getAbstractEstimateNumbers() != null
                && !detailedEstimateSearchContract.getAbstractEstimateNumbers().isEmpty()) {
            addAnd(params);
            params.append(
                    "aed.id = (de.abstractestimatedetail) and aed.abstractestimate= ae.id and ae.abstractestimatenumber in (:abstractEstimateNumbers) and aed.tenantId =:tenantId and ae.tenantId =:tenantId and aed.deleted=false and ae.deleted=false");
            paramValues.put("abstractEstimateNumbers", detailedEstimateSearchContract.getAbstractEstimateNumbers());
        }

        if (detailedEstimateSearchContract.getSortBy() != null
                && !detailedEstimateSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(detailedEstimateSearchContract.getSortBy());
            validateEntityFieldName(detailedEstimateSearchContract.getSortBy(), DetailedEstimate.class);
        }

        StringBuilder orderBy = new StringBuilder("order by de.createdtime");
        if (detailedEstimateSearchContract.getSortBy() != null
                && !detailedEstimateSearchContract.getSortBy().isEmpty()) {
            orderBy.delete(0,orderBy.length()).append("order by de.").append(detailedEstimateSearchContract.getSortBy());
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
        if (StringUtils.isNotBlank(detailedEstimateSearchContract.getDetailedEstimateNumberLike())) {
            addAnd(params);
            params.append("lower(de.estimateNumber) like :estimateNumbers ");
            paramValues.put("estimateNumbers",
                    '%' + detailedEstimateSearchContract.getDetailedEstimateNumberLike().toLowerCase() + '%');
        }
        if (detailedEstimateSearchContract.getDetailedEstimateNumbers() != null
                && !detailedEstimateSearchContract.getDetailedEstimateNumbers().isEmpty()) {
            addAnd(params);
            params.append("de.estimateNumber in(:estimateNumbers)");
            paramValues.put("estimateNumbers", detailedEstimateSearchContract.getDetailedEstimateNumbers());
        }
        if (detailedEstimateSearchContract.getDepartments() != null && !detailedEstimateSearchContract.getDepartments().isEmpty()) {
            addAnd(params);
            params.append("de.department  in (:departmentCodes)");
            paramValues.put("departmentCodes", detailedEstimateSearchContract.getDepartments());
        }
        if (detailedEstimateSearchContract.getTypeOfWork() != null) {
            addAnd(params);
            params.append("de.workstype in(:typeofwork)");
            paramValues.put("typeofwork", detailedEstimateSearchContract.getTypeOfWork());
        }
        if (detailedEstimateSearchContract.getSubTypeOfWork() != null) {
            addAnd(params);
            params.append("de.workssubtype in(:subtypeofwork)");
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
            paramValues.put("estimateValue", detailedEstimateSearchContract.getFromAmount());
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
        if (StringUtils.isNotBlank(detailedEstimateSearchContract.getWorkIdentificationNumberLike())) {
            addAnd(params);
            params.append(" lower(de.projectCode) like :workIdentificationNumbers");
            paramValues.put("workIdentificationNumbers",
                    "%" + detailedEstimateSearchContract.getWorkIdentificationNumberLike().toLowerCase() + "%");
        }
        if (detailedEstimateSearchContract.getWorkIdentificationNumbers() != null
                && !detailedEstimateSearchContract.getWorkIdentificationNumbers().isEmpty()) {
            addAnd(params);
            params.append(" de.projectCode in (:workIdentificationNumbers)");
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

        if (StringUtils.isNotBlank(detailedEstimateSearchContract.getTechnicalSanctionNumberLike())) {
            addAnd(params);
            params.append(
                    "ts.detailedestimate = de.id and ts.tenantId =:tenantId and ts.deleted=false and lower(ts.technicalsanctionnumber) like (:technicalsanctionnumber)");
            paramValues.put("technicalsanctionnumber",
                    "%" + detailedEstimateSearchContract.getTechnicalSanctionNumberLike().toLowerCase() + "%");
        }
        if (detailedEstimateSearchContract.getTechnicalSanctionNumbers() != null
                && !detailedEstimateSearchContract.getTechnicalSanctionNumbers().isEmpty()) {
            addAnd(params);
            params.append(
                    "ts.detailedestimate = de.id and ts.tenantId =:tenantId and ts.deleted=false and ts.technicalsanctionnumber in (:technicalsanctionnumber)");
            paramValues.put("technicalsanctionnumber", detailedEstimateSearchContract.getTechnicalSanctionNumbers());
        }

        if (detailedEstimateSearchContract.getScheduleOfRate() != null
                && !detailedEstimateSearchContract.getScheduleOfRate().isEmpty()) {
            addAnd(params);
            params.append(
                    "activity.detailedestimate = de.id and activity.tenantId =:tenantId and activity.deleted=false and activity.scheduleofrate is not null and activity.scheduleofrate = :scheduleofrate");
            paramValues.put("scheduleofrate", detailedEstimateSearchContract.getScheduleOfRate());
        }

        if(detailedEstimateSearchContract.getLoaCreated() != null) {
            addAnd(params);
            params.append("de.loaCreated =:loaCreated");
            paramValues.put("loaCreated", detailedEstimateSearchContract.getLoaCreated());
        }

        if(detailedEstimateSearchContract.getWithoutOfflineStatus() != null && detailedEstimateSearchContract.getWithoutOfflineStatus()) {
            List<OfflineStatus> offlineStatuses = offlineStatusRepository.searchOfflineStatus(detailedEstimateSearchContract.getTenantId(), Constants.DETAILEDESTIMATE_OFFLINE, null, requestInfo);
            String detailedEstimateNumbers = offlineStatuses.stream().map(OfflineStatus::getObjectNumber).collect(Collectors.joining(","));
            addAnd(params);
            params.append("de.estimatenumber not in (:detailedEstimateNumbers)");
            paramValues.put("detailedEstimateNumbers", detailedEstimateNumbers);
        }

        if(detailedEstimateSearchContract.getWithAllOfflineStatusAndLoaNotCreated() != null && detailedEstimateSearchContract.getWithAllOfflineStatusAndLoaNotCreated() != null) {
            List<OfflineStatus> offlineStatuses = offlineStatusRepository.searchOfflineStatus(detailedEstimateSearchContract.getTenantId(), Constants.DETAILEDESTIMATE_OFFLINE, Constants.DETAILEDESTIMATE_OFFLINESTATUS, requestInfo);
            String detailedEstimateNumbers = offlineStatuses.stream().map(OfflineStatus::getObjectNumber).collect(Collectors.joining(","));
            addAnd(params);
            params.append("de.status = 'TECHNICAL_SANCTIONED' and de.estimatenumber in (:detailedEstimateNumbers) and de.loaCreated=false");
            paramValues.put("detailedEstimateNumbers", detailedEstimateNumbers);
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

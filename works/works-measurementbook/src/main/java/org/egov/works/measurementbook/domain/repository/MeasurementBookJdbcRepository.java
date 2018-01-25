package org.egov.works.measurementbook.domain.repository;

import org.apache.commons.lang3.StringUtils;
import org.egov.works.measurementbook.common.persistence.repository.JdbcRepository;
import org.egov.works.measurementbook.persistence.helper.MeasurementBookHelper;
import org.egov.works.measurementbook.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MeasurementBookJdbcRepository extends JdbcRepository {

    @Autowired
    private MeasurementBookDetailRepository measurementBookDetailRepository;

    @Autowired
    private EstimateRepository estimateRepository;

    @Autowired
    private LetterOfAcceptanceRepository letterOfAcceptanceRepository;

    public static final String TABLE_NAME = "egw_measurementbook mb";
    public static final String MB_LOAESTIMATE_EXTENTION = ", egw_letterofacceptanceestimate loaestimate";

    public List<MeasurementBook> searchMeasurementBooks(final MeasurementBookSearchContract measurementBookSearchContract,
            final RequestInfo requestInfo) {

        String searchQuery = "select :selectfields from :tablename :condition :orderby ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (measurementBookSearchContract.getSortProperty() != null
                && !measurementBookSearchContract.getSortProperty().isEmpty()) {
            validateSortByOrder(measurementBookSearchContract.getSortProperty());
            validateEntityFieldName(measurementBookSearchContract.getSortProperty(), MeasurementBook.class);
        }

        if ((measurementBookSearchContract.getLoaNumbers() != null && !measurementBookSearchContract.getLoaNumbers().isEmpty())
                || StringUtils.isNotBlank(measurementBookSearchContract.getLoaNumberLike())
                || (measurementBookSearchContract.getDetailedEstimateNumbers() != null && !measurementBookSearchContract.getDetailedEstimateNumbers().isEmpty())
                || StringUtils.isNotBlank(measurementBookSearchContract.getDetailedEstimateNumberLike())
                || (measurementBookSearchContract.getWorkOrderNumbers() != null && !measurementBookSearchContract.getWorkOrderNumbers().isEmpty())
                || StringUtils.isNotBlank(measurementBookSearchContract.getWorkOrderNumberLike()))
            tableName += MB_LOAESTIMATE_EXTENTION;

        String orderBy = "order by mb.id";
        if (measurementBookSearchContract.getSortProperty() != null
                && !measurementBookSearchContract.getSortProperty().isEmpty()) {
            orderBy = "order by mb." + measurementBookSearchContract.getSortProperty();
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (measurementBookSearchContract.getTenantId() != null) {
            addAnd(params);
            params.append("mb.tenantId =:tenantId");
            paramValues.put("tenantId", measurementBookSearchContract.getTenantId());
        }
        if (measurementBookSearchContract.getIds() != null) {
            addAnd(params);
            params.append("mb.id in(:ids) ");
            paramValues.put("ids", measurementBookSearchContract.getIds());
        }
        if ((measurementBookSearchContract.getLoaNumbers() != null && !measurementBookSearchContract.getLoaNumbers().isEmpty())
                || StringUtils.isNotBlank(measurementBookSearchContract.getLoaNumberLike())
                || (measurementBookSearchContract.getDetailedEstimateNumbers() != null && !measurementBookSearchContract.getDetailedEstimateNumbers().isEmpty())
                || StringUtils.isNotBlank(measurementBookSearchContract.getDetailedEstimateNumberLike())
                || (measurementBookSearchContract.getWorkOrderNumbers() != null && !measurementBookSearchContract.getWorkOrderNumbers().isEmpty())
                || StringUtils.isNotBlank(measurementBookSearchContract.getWorkOrderNumberLike())) {
            addAnd(params);
            params.append("mb.letterOfAcceptanceEstimate = loaestimate.id");
        }

        if (measurementBookSearchContract.getLoaNumbers() != null && !measurementBookSearchContract.getLoaNumbers().isEmpty()) {
            addAnd(params);
            params.append("loaestimate.letterOfAcceptance in (:loaNumber)");
            paramValues.put("loaNumber", measurementBookSearchContract.getLoaNumbers());
        }

        if (StringUtils.isNotBlank(measurementBookSearchContract.getLoaNumberLike())) {
            addAnd(params);
            params.append("lower(loaestimate.letterOfAcceptance) like (:loaNumberlike)");
            paramValues.put("loaNumberlike", "%" + measurementBookSearchContract.getLoaNumberLike().toLowerCase() + "%");
        }

        if (measurementBookSearchContract.getDetailedEstimateNumbers() != null && !measurementBookSearchContract.getDetailedEstimateNumbers().isEmpty()) {
            addAnd(params);
            params.append("loaestimate.letterOfAcceptance in (:detailedEstimate)");
            paramValues.put("detailedEstimate", measurementBookSearchContract.getDetailedEstimateNumbers());
        }

        if (StringUtils.isNotBlank(measurementBookSearchContract.getDetailedEstimateNumberLike())) {
            addAnd(params);
            params.append("loaestimate.letterOfAcceptance like (:detailedEstimate)");
            paramValues.put("detailedEstimate", "%" + measurementBookSearchContract.getDetailedEstimateNumberLike() + "%");
        }

        if (measurementBookSearchContract.getWorkOrderNumbers() != null && !measurementBookSearchContract.getWorkOrderNumbers().isEmpty()) {
            addAnd(params);
            params.append("loaestimate.letterOfAcceptance in ((select letterofacceptance from egw_workorder as wo where wo.status = 'APPROVED' and wo.deleted = false and wo.tenantId =:tenantId and upper(wo.workordernumber) in (:workordernumbers)))");
            paramValues.put("workordernumbers", measurementBookSearchContract.getWorkOrderNumbers());
        }

        if (StringUtils.isNotBlank(measurementBookSearchContract.getWorkOrderNumberLike())) {
            addAnd(params);
            params.append("loaestimate.letterOfAcceptance in ((select letterofacceptance from egw_workorder as wo where wo.status = 'APPROVED' and wo.deleted = false and wo.tenantId =:tenantId and upper(wo.workordernumber) like (:workordernumberlike)))");
            paramValues.put("workordernumberlike", "%" + measurementBookSearchContract.getWorkOrderNumberLike().toUpperCase() + "%");
        }
        if (measurementBookSearchContract.getCreatedBy() != null) {
            addAnd(params);
            params.append("upper(mb.createdBy) =:createdBy");
            paramValues.put("createdBy", measurementBookSearchContract.getCreatedBy().toUpperCase());
        }

        if (measurementBookSearchContract.getFromDate() != null) {
            addAnd(params);
            params.append("mb.createdtime >=:createdtime");
            paramValues.put("createdtime", measurementBookSearchContract.getFromDate());
        }
        if (measurementBookSearchContract.getToDate() != null) {
            addAnd(params);
            params.append("mb.createdtime <=:createdtime");
            paramValues.put("createdtime", measurementBookSearchContract.getToDate());
        }

        if (StringUtils.isNotBlank(measurementBookSearchContract.getMbRefNumberLike())) {
            addAnd(params);
            params.append("upper(mb.mbRefNo) like (:mbRefNoLike)");
            paramValues.put("mbRefNoLike", "%" + measurementBookSearchContract.getMbRefNumberLike().toUpperCase() + "%");
        }
        if (measurementBookSearchContract.getMbRefNumbers() != null
                && !measurementBookSearchContract.getMbRefNumbers().isEmpty()) {
            addAnd(params);
            params.append("mb.mbRefNo in(:mbRefNo)");
            paramValues.put("mbRefNo", measurementBookSearchContract.getMbRefNumbers());
        }

        if (measurementBookSearchContract.getIsPartRate() != null) {
            addAnd(params);
            params.append("mb.isPartRate =:isPartRate");
            paramValues.put("isPartRate", measurementBookSearchContract.getIsPartRate());
        }

        if (measurementBookSearchContract.getIsBillCreated()!=null) {
            addAnd(params);
            params.append("mb.isBillCreated =:isBillCreated");
            paramValues.put("isBillCreated", measurementBookSearchContract.getIsBillCreated());
        }

        if (StringUtils.isNotBlank(measurementBookSearchContract.getLoaEstimateId())) {
            addAnd(params);
            params.append("mb.letterofacceptanceestimate =:loaEstimateId");
            paramValues.put("loaEstimateId", measurementBookSearchContract.getLoaEstimateId());
        }

        List<String> detailedEstimateNumbers = new ArrayList<>();
        if (measurementBookSearchContract.getDepartment() != null && !measurementBookSearchContract.getDepartment().isEmpty()) {
            List<DetailedEstimate> detailedEstimates = estimateRepository.searchDetailedEstimatesByDepartment(
                    measurementBookSearchContract.getDepartment(), measurementBookSearchContract.getTenantId(), requestInfo);
            for (DetailedEstimate detailedEstimate : detailedEstimates)
                detailedEstimateNumbers.add(detailedEstimate.getEstimateNumber());
            if (detailedEstimateNumbers != null && !detailedEstimateNumbers.isEmpty())
                searchByDetailedEstimateNumbers(detailedEstimateNumbers, params, paramValues);
        }

        if (measurementBookSearchContract.getStatuses() != null) {
            addAnd(params);
            params.append("mb.status in(:status)");
            paramValues.put("status", measurementBookSearchContract.getStatuses());
        }

        List<String> loaNumbers = null;
        if (measurementBookSearchContract.getContractorCodes() != null
                && !measurementBookSearchContract.getContractorCodes().isEmpty()) {
            List<LetterOfAcceptance> letterOfAcceptances = letterOfAcceptanceRepository.searchLetterOfAcceptance(
                    measurementBookSearchContract.getContractorCodes(), null, measurementBookSearchContract.getTenantId(),
                    requestInfo);
            loaNumbers = new ArrayList<String>();
            for (LetterOfAcceptance letterOfAcceptance : letterOfAcceptances)
                loaNumbers.add(letterOfAcceptance.getLoaNumber());
            searchByLOANumbers(loaNumbers, params, paramValues);
        }

        if (measurementBookSearchContract.getContractorNames() != null
                && !measurementBookSearchContract.getContractorNames().isEmpty()) {
            List<LetterOfAcceptance> letterOfAcceptances = letterOfAcceptanceRepository.searchLetterOfAcceptance(null,
                    measurementBookSearchContract.getContractorNames(), measurementBookSearchContract.getTenantId(), requestInfo);
            loaNumbers = new ArrayList<String>();
            for (LetterOfAcceptance letterOfAcceptance : letterOfAcceptances)
                loaNumbers.add(letterOfAcceptance.getLoaNumber());
            searchByLOANumbers(loaNumbers, params, paramValues);

        }

        if (StringUtils.isNotBlank(measurementBookSearchContract.getContractorCodeLike())) {
            List<LetterOfAcceptance> letterOfAcceptances = letterOfAcceptanceRepository.searchLetterOfAcceptance(
                    Arrays.asList(measurementBookSearchContract.getContractorCodeLike()), null,
                    measurementBookSearchContract.getTenantId(),
                    requestInfo);
            loaNumbers = new ArrayList<String>();
            for (LetterOfAcceptance letterOfAcceptance : letterOfAcceptances)
                loaNumbers.add(letterOfAcceptance.getLoaNumber());
            searchByLOANumberLike(loaNumbers, params, paramValues);
        }

        if (StringUtils.isNotBlank(measurementBookSearchContract.getContractorNameLike())) {
            List<LetterOfAcceptance> loas = letterOfAcceptanceRepository.searchLetterOfAcceptance(null,
                    Arrays.asList(measurementBookSearchContract.getContractorNameLike()),
                    measurementBookSearchContract.getTenantId(), requestInfo);
            loaNumbers = new ArrayList<String>();
            for (LetterOfAcceptance letterOfAcceptance : loas)
                loaNumbers.add(letterOfAcceptance.getLoaNumber());
            searchByLOANumberLike(loaNumbers, params, paramValues);

        }

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(MeasurementBookHelper.class);

        List<MeasurementBookHelper> measurementBookHelpers = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
        List<MeasurementBook> measurementBooks = new ArrayList<>();
        for (MeasurementBookHelper measurementBookHelper : measurementBookHelpers) {
            MeasurementBook measurementBook = measurementBookHelper.toDomain();

            MeasurementBookDetailSearchContract measurementBookDetailSearchCriteria = MeasurementBookDetailSearchContract
                    .builder()
                    .tenantId(measurementBook.getTenantId())
                    .measurementBookIds(Arrays.asList(measurementBook.getId())).build();

            measurementBook.setMeasurementBookDetails(
                    measurementBookDetailRepository.searchMeasurementBookDetail(measurementBookDetailSearchCriteria));

            measurementBooks.add(measurementBook);

        }

        return measurementBooks;
    }

    private void searchByLOANumbers(List<String> loaNumbers, StringBuilder params, Map<String, Object> paramValues) {
        addAnd(params);
        params.append("mb.letterOfAcceptanceEstimate = loaestimate.id and loaestimate.letterOfAcceptance in (:loaNumber)");
        paramValues.put("loaNumber", loaNumbers);
    }

    private void searchByLOANumberLike(List<String> loaNumbers, StringBuilder params, Map<String, Object> paramValues) {
        addAnd(params);
        params.append(
                "mb.letterOfAcceptanceEstimate = loaestimate.id and upper(loaestimate.letterOfAcceptance) like (:loaNumber)");
        paramValues.put("loaNumber", "%" + loaNumbers.get(0).toUpperCase() + "%");
    }

    private void searchByDetailedEstimateNumbers(List<String> detailedEstimateNumbers, StringBuilder params,
            Map<String, Object> paramValues) {

        if (detailedEstimateNumbers.size() == 1) {
            addAnd(params);
            params.append(
                    "mb.letterOfAcceptanceEstimate = loaestimate.id and upper(loaestimate.detailedEstimate) like (:detailedEstimate)");
            paramValues.put("detailedEstimate", "%" + detailedEstimateNumbers.get(0).toUpperCase() + "%");
        } else {
            addAnd(params);
            params.append(
                    "mb.letterOfAcceptanceEstimate = loaestimate.id and loaestimate.letterOfAcceptance in (:detailedEstimate)");
            paramValues.put("detailedEstimate", detailedEstimateNumbers);
        }

    }
}

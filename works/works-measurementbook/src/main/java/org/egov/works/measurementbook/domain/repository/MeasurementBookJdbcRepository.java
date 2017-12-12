package org.egov.works.measurementbook.domain.repository;

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


    public List<MeasurementBook> searchMeasurementBooks(final MeasurementBookSearchContract measurementBookSearchContract, final RequestInfo requestInfo) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (measurementBookSearchContract.getSortProperty() != null
                && !measurementBookSearchContract.getSortProperty().isEmpty()) {
            validateSortByOrder(measurementBookSearchContract.getSortProperty());
            validateEntityFieldName(measurementBookSearchContract.getSortProperty(), MeasurementBook.class);
        }

        if (measurementBookSearchContract.getDetailedEstimateNumbers() != null && !measurementBookSearchContract.getDetailedEstimateNumbers().isEmpty()
                || measurementBookSearchContract.getLoaNumbers() != null && !measurementBookSearchContract.getLoaNumbers().isEmpty())
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
        if (measurementBookSearchContract.getLoaNumbers() != null && !measurementBookSearchContract.getLoaNumbers().isEmpty()) {
            searchByLOANumbers(measurementBookSearchContract.getLoaNumbers(), params, paramValues);
        }

        if (measurementBookSearchContract.getDetailedEstimateNumbers() != null && !measurementBookSearchContract.getDetailedEstimateNumbers().isEmpty()) {
            searchByDetailedEstimateNumbers(measurementBookSearchContract.getDetailedEstimateNumbers(), params, paramValues);
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

        if (measurementBookSearchContract.getMbRefNumbers() != null && measurementBookSearchContract.getMbRefNumbers().size() == 1) {
            addAnd(params);
            params.append("upper(mb.mbRefNo) like (:mbRefNo)");
            paramValues.put("mbRefNo", "%" + measurementBookSearchContract.getMbRefNumbers().get(0).toUpperCase() + "%");
        } else if (measurementBookSearchContract.getMbRefNumbers() != null && measurementBookSearchContract.getMbRefNumbers().size() > 1) {
            addAnd(params);
            params.append("mb.mbRefNo in(:mbRefNo)");
            paramValues.put("mbRefNo", measurementBookSearchContract.getMbRefNumbers());
        }
        List<String> detailedEstimateNumbers = new ArrayList<>();
        if (measurementBookSearchContract.getDepartment() != null && !measurementBookSearchContract.getDepartment().isEmpty()) {
            List<DetailedEstimate> detailedEstimates = estimateRepository.searchDetailedEstimatesByDepartment(measurementBookSearchContract.getDepartment(),measurementBookSearchContract.getTenantId(),requestInfo);
            for(DetailedEstimate detailedEstimate : detailedEstimates)
              detailedEstimateNumbers.add(detailedEstimate.getEstimateNumber());
            if(detailedEstimateNumbers != null && !detailedEstimateNumbers.isEmpty())
            searchByDetailedEstimateNumbers(detailedEstimateNumbers,params,paramValues);
        }

        if (measurementBookSearchContract.getStatuses() != null) {
            addAnd(params);
            params.append("mb.status in(:status)");
            paramValues.put("status", measurementBookSearchContract.getStatuses());
        }

        List<String> loaNumbers = null;
        if (measurementBookSearchContract.getContractorCodes() != null && !measurementBookSearchContract.getContractorCodes().isEmpty()) {
           List<LetterOfAcceptance> loas = letterOfAcceptanceRepository.searchLetterOfAcceptance(measurementBookSearchContract.getContractorCodes(), null, measurementBookSearchContract.getTenantId(), requestInfo);
            loaNumbers = new ArrayList<String>();
            for(LetterOfAcceptance letterOfAcceptance : loas)
                loaNumbers.add(letterOfAcceptance.getLoaNumber());
            searchByLOANumbers(loaNumbers, params, paramValues);

        }

        if (measurementBookSearchContract.getContractorNames() != null && !measurementBookSearchContract.getContractorNames().isEmpty()) {
            List<LetterOfAcceptance> loas = letterOfAcceptanceRepository.searchLetterOfAcceptance(null,measurementBookSearchContract.getContractorNames(), measurementBookSearchContract.getTenantId(), requestInfo);
            loaNumbers = new ArrayList<String>();
            for(LetterOfAcceptance letterOfAcceptance : loas)
                loaNumbers.add(letterOfAcceptance.getLoaNumber());
            searchByLOANumbers(loaNumbers, params, paramValues);

        }

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(MeasurementBookHelper.class);

        List<MeasurementBookHelper> loaList = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
        List<MeasurementBook> measurementBooks = new ArrayList<>();
        for (MeasurementBookHelper measurementBookHelper : loaList) {
            MeasurementBook measurementBook = measurementBookHelper.toDomain();

            MeasurementBookDetailSearchContract measurementBookDetailSearchCriteria = MeasurementBookDetailSearchContract.builder()
                    .tenantId(measurementBook.getTenantId())
                    .measurementBookIds(Arrays.asList(measurementBook.getId())).build();

            measurementBook.setMeasurementBookDetails(measurementBookDetailRepository.searchMeasurementBookDetail(measurementBookDetailSearchCriteria));

            measurementBooks.add(measurementBook);

        }

        return measurementBooks;
    }

    private void searchByLOANumbers(List<String> loaNumbers, StringBuilder params, Map<String, Object> paramValues) {
        if (loaNumbers.size() == 1) {
            addAnd(params);
            params.append("mb.letterOfAcceptanceEstimate = loaestimate.id and upper(loaestimate.letterOfAcceptance) like (:loaNumber)");
            paramValues.put("loaNumber", "%" + loaNumbers.get(0).toUpperCase() + "%");
        } else {
            addAnd(params);
            params.append("mb.letterOfAcceptanceEstimate = loaestimate.id and loaestimate.letterOfAcceptance in (:loaNumber)");
            paramValues.put("loaNumber", loaNumbers);
        }
    }

    private void searchByDetailedEstimateNumbers(List<String> detailedEstimateNumbers, StringBuilder params, Map<String,Object> paramValues) {

        if (detailedEstimateNumbers.size() == 1) {
            addAnd(params);
            params.append("mb.letterOfAcceptanceEstimate = loaestimate.id and upper(loaestimate.detailedEstimate) like (:detailedEstimate)");
            paramValues.put("detailedEstimate", "%" + detailedEstimateNumbers.get(0).toUpperCase() + "%");
        } else {
            addAnd(params);
            params.append("mb.letterOfAcceptanceEstimate = loaestimate.id and loaestimate.letterOfAcceptance in (:detailedEstimate)");
            paramValues.put("detailedEstimate", detailedEstimateNumbers);
        }

    }
}

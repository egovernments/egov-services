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
    private MBContractorBillRepository mbContractorBillRepository;

    @Autowired
    private MeasurementBookDetailRepository measurementBookDetailRepository;

    public static final String TABLE_NAME = "egw_mb_measurementsheet mb";
    public static final String MB_LOAESTIMATE_EXTENTION = "egw_letterofacceptanceestimate loaestimate";


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

        String orderBy = "order by id";
        if (measurementBookSearchContract.getSortProperty() != null
                && !measurementBookSearchContract.getSortProperty().isEmpty()) {
            orderBy = "order by " + measurementBookSearchContract.getSortProperty();
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
        if (measurementBookSearchContract.getLoaNumbers() != null && measurementBookSearchContract.getLoaNumbers().size() == 1) {
            addAnd(params);
            params.append("mb.letterOfAcceptanceEstimate = loaestimate.id and loaestimate.letterOfAcceptance like (:loaNumber)");
            paramValues.put("loaNumber", "%" + measurementBookSearchContract.getLoaNumbers().get(0) + "%");
        } else if (measurementBookSearchContract.getLoaNumbers() != null && measurementBookSearchContract.getLoaNumbers().size() > 1) {
            addAnd(params);
            params.append("mb.letterOfAcceptanceEstimate = loaestimate.id and loaestimate.letterOfAcceptance in (:loaNumber)");
            paramValues.put("loaNumber", measurementBookSearchContract.getLoaNumbers());
        }

        if (measurementBookSearchContract.getDetailedEstimateNumbers() != null && measurementBookSearchContract.getDetailedEstimateNumbers().size() == 1) {
            addAnd(params);
            params.append("mb.letterOfAcceptanceEstimate = loaestimate.id and loaestimate.detailedEstimate like (:detailedEstimate)");
            paramValues.put("detailedEstimate", "%" + measurementBookSearchContract.getDetailedEstimateNumbers().get(0) + "%");
        } else if (measurementBookSearchContract.getDetailedEstimateNumbers() != null && measurementBookSearchContract.getDetailedEstimateNumbers().size() > 1) {
            addAnd(params);
            params.append("mb.letterOfAcceptanceEstimate = loaestimate.id and loaestimate.letterOfAcceptance in (:detailedEstimate)");
            paramValues.put("detailedEstimate", measurementBookSearchContract.getDetailedEstimateNumbers());
        }

        if (measurementBookSearchContract.getCreatedBy() != null) {
            addAnd(params);
            params.append("mb.createdBy =:createdBy");
            paramValues.put("createdBy", measurementBookSearchContract.getCreatedBy());
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
            params.append("mb.mbRefNo like (:mbRefNo)");
            paramValues.put("mb.mbRefNo", "%" + measurementBookSearchContract.getMbRefNumbers().get(0) + "%");
        } else if (measurementBookSearchContract.getMbRefNumbers() != null && measurementBookSearchContract.getMbRefNumbers().size() > 1) {
            addAnd(params);
            params.append("mb.mbRefNo in(:mbRefNo)");
            paramValues.put("mbRefNo", measurementBookSearchContract.getMbRefNumbers());
        }
        /*if (measurementBookSearchContract.getDepartment() != null) {
            addAnd(params);
            params.append("loa.department =:department");
            paramValues.put("department", measurementBookSearchContract.getDepartment());
        }*/

        if (measurementBookSearchContract.getStatuses() != null) {
            addAnd(params);
            params.append("mb.status in(:status)");
            paramValues.put("status", measurementBookSearchContract.getStatuses());
        }

        /*if (measurementBookSearchContract.getContractorCodes() != null && !measurementBookSearchContract.getContractorCodes().isEmpty()) {
            searchByContractorCodes(measurementBookSearchContract.getContractorCodes(), params, paramValues);
        }

        List<String> contractorCodes = new ArrayList<>();
        if (measurementBookSearchContract.getContractorNames() != null && !measurementBookSearchContract.getContractorNames().isEmpty()) {
            List<Contractor> contractors = worksMastersRepository.searchScheduleOfRates(measurementBookSearchContract.getTenantId(), measurementBookSearchContract.getContractorNames(), requestInfo);
            for (Contractor contractor : contractors)
                contractorCodes.add(contractor.getCode());
            if (!contractorCodes.isEmpty())
                searchByContractorCodes(contractorCodes, params, paramValues);
        }

        List<String> estimateNumbers = new ArrayList<>();
        if (measurementBookSearchContract.getDepartment() != null && !measurementBookSearchContract.getDepartment().isEmpty()) {
            List<DetailedEstimate> detailedEstimates = estimateRepository.searchDetailedEstimatesByDepartment(measurementBookSearchContract.getDepartment(), measurementBookSearchContract.getTenantId(), requestInfo);
            for (DetailedEstimate detailedEstimate : detailedEstimates)
                estimateNumbers.add(detailedEstimate.getEstimateNumber());

            addAnd(params);
            params.append("loaestimate.letterofacceptance = loa.loanumber and loaestimate.detailedestimate in :detailedestimatenumber");
            paramValues.put("detailedestimatenumber", measurementBookSearchContract.getDetailedEstimateNumbers());

        }*/
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

            MBContractorBillSearchCriteria mbContractorBillSearchCriteria = MBContractorBillSearchCriteria.builder()
                    .tenantId(measurementBook.getTenantId())
                    .measurementBookIds(Arrays.asList(measurementBook.getId())).build();

            MeasurementBookDetailSearchCriteria measurementBookDetailSearchCriteria = MeasurementBookDetailSearchCriteria.builder()
                    .tenantId(measurementBook.getTenantId())
                    .measurementBookIds(Arrays.asList(measurementBook.getId())).build();

            measurementBook.setMbContractorBills(mbContractorBillRepository.searchMBContractorBill(mbContractorBillSearchCriteria));
            measurementBook.setMeasurementBookDetails(measurementBookDetailRepository.searchMeasurementBookDetail(measurementBookDetailSearchCriteria));

            measurementBooks.add(measurementBook);

        }

        return measurementBooks;
    }
}

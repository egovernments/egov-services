package org.egov.works.qualitycontrol.persistence.repository;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.qualitycontrol.persistence.helper.QualityTestingHelper;
import org.egov.works.qualitycontrol.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class QualityTestingJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egw_qualitytesting qt";
    public static final String SEARCH_LOA_ESTIMATE_EXTENTION = ",egw_letterofacceptanceestimate lea";

    @Autowired
    private DetailedEstimateRepository detailedEstimateRepository;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private QualityTestingDetailJdbcRepository qualityTestingDetailJdbcRepository;

    @Autowired
    private LetterOfAcceptanceRepository letterOfAcceptanceRepository;

    public List<QualityTesting> search(final QualityTestingSearchContract qualityTestingSearchContract, final RequestInfo requestInfo) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        Map<String, Object> paramValues = new HashMap<>();
        StringBuffer params = new StringBuffer();
        String table = TABLE_NAME;


        if (qualityTestingSearchContract.getSortBy() != null
                && !qualityTestingSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(qualityTestingSearchContract.getSortBy());
            validateEntityFieldName(qualityTestingSearchContract.getSortBy(), QualityTestingHelper.class);
        }

        if(qualityTestingSearchContract.getLoaNumbers() != null && !qualityTestingSearchContract.getLoaNumbers().isEmpty() ||
                StringUtils.isNotBlank(qualityTestingSearchContract.getLoaNumberLike()) ||
                qualityTestingSearchContract.getDetailedEstimateNumbers() != null && !qualityTestingSearchContract.getDetailedEstimateNumbers().isEmpty() ||
          StringUtils.isNotBlank(qualityTestingSearchContract.getDetailedEstimateNumberLike()) ||
            qualityTestingSearchContract.getWorkIdentificationNumbers() != null && !qualityTestingSearchContract.getWorkIdentificationNumbers().isEmpty() ||
                StringUtils.isNotBlank(qualityTestingSearchContract.getWorkIdentificationNumberLike()) ||
                qualityTestingSearchContract.getWorkOrderNumbers() != null && !qualityTestingSearchContract.getWorkOrderNumbers().isEmpty() ||
                StringUtils.isNotBlank(qualityTestingSearchContract.getWorkOrderNumberLike()))
            table += SEARCH_LOA_ESTIMATE_EXTENTION;

        String orderBy = "order by qt.id";
        if (qualityTestingSearchContract.getSortBy() != null
                && !qualityTestingSearchContract.getSortBy().isEmpty()) {
            orderBy = "order by qt." + qualityTestingSearchContract.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", table);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (qualityTestingSearchContract.getTenantId() != null) {
            addAnd(params);
            params.append("qt.tenantId =:tenantId");
            paramValues.put("tenantId", qualityTestingSearchContract.getTenantId());
        }
        if (qualityTestingSearchContract.getIds() != null) {
            addAnd(params);
            params.append("qt.id in(:ids) ");
            paramValues.put("ids", qualityTestingSearchContract.getIds());
        }
        if (qualityTestingSearchContract.getFromDate() != null) {
            addAnd(params);
            params.append("qt.createdtime >=:fromDate");
            paramValues.put("fromDate", qualityTestingSearchContract.getFromDate());
        }
        if (qualityTestingSearchContract.getToDate() != null) {
            addAnd(params);
            params.append("qt.createdtime <=:toDate");
            paramValues.put("toDate", qualityTestingSearchContract.getToDate());
        }

        List<String> loaIds = new ArrayList<>();
        if(qualityTestingSearchContract.getLoaNumbers() != null && !qualityTestingSearchContract.getLoaNumbers().isEmpty()) {
           List<LetterOfAcceptance> letterOfAcceptances = letterOfAcceptanceRepository.searchLOAByLoaNumber(qualityTestingSearchContract.getTenantId(), qualityTestingSearchContract.getLoaNumbers(), requestInfo);
            if(letterOfAcceptances != null && !letterOfAcceptances.isEmpty()) {
                loaIds = letterOfAcceptances.stream().map(loa -> loa.getId()).collect(Collectors.toList());
                searchQualityTestingByLoaNumber(loaIds, params, paramValues);
            }
        }

        if(StringUtils.isNotBlank(qualityTestingSearchContract.getLoaNumberLike())) {
            List<LetterOfAcceptance> letterOfAcceptances = letterOfAcceptanceRepository.searchLOAByLoaNumber(qualityTestingSearchContract.getTenantId(), Arrays.asList(qualityTestingSearchContract.getLoaNumberLike()), requestInfo);
            if(letterOfAcceptances != null && !letterOfAcceptances.isEmpty()) {
                loaIds = letterOfAcceptances.stream().map(loa -> loa.getId()).collect(Collectors.toList());
                searchQualityTestingByLoaNumber(loaIds.get(0), params, paramValues);
            }
        }

        if(qualityTestingSearchContract.getDetailedEstimateNumbers() != null && !qualityTestingSearchContract.getDetailedEstimateNumbers().isEmpty()) {
            searchQualityTesting(qualityTestingSearchContract.getDetailedEstimateNumbers(),params, paramValues);
        }

        if(StringUtils.isNotBlank(qualityTestingSearchContract.getDetailedEstimateNumberLike())) {
            searchQualityTesting(qualityTestingSearchContract.getDetailedEstimateNumberLike(), params, paramValues);
        }

        List<String> estimateNumbers = new ArrayList<>();
        if(qualityTestingSearchContract.getWorkIdentificationNumbers() != null && !qualityTestingSearchContract.getWorkIdentificationNumbers().isEmpty()) {
            List<DetailedEstimate> estimates = detailedEstimateRepository.searchDetailedEsimates(qualityTestingSearchContract.getTenantId(),qualityTestingSearchContract.getWorkIdentificationNumbers(),requestInfo);
            if(estimates != null && !estimates.isEmpty()) {
                estimateNumbers = estimates.stream().map(e -> e.getEstimateNumber()).collect(Collectors.toList());
                searchQualityTesting(estimateNumbers, params, paramValues);
            }
        }

        if(StringUtils.isNotBlank(qualityTestingSearchContract.getWorkIdentificationNumberLike())) {
            List<DetailedEstimate> estimates = detailedEstimateRepository.searchDetailedEsimates(qualityTestingSearchContract.getTenantId(), Arrays.asList(qualityTestingSearchContract.getWorkIdentificationNumberLike()),requestInfo);
            if(estimates != null && !estimates.isEmpty()) {
                estimateNumbers = estimates.stream().map(e -> e.getEstimateNumber()).collect(Collectors.toList());
                searchQualityTesting(estimateNumbers.get(0), params, paramValues);
            }
        }

        List<String> loaNumbers = new ArrayList<>();
        if(qualityTestingSearchContract.getWorkOrderNumbers() != null && !qualityTestingSearchContract.getWorkOrderNumbers().isEmpty()) {
            List<WorkOrder> workOrders = workOrderRepository.searchWorkOrder(qualityTestingSearchContract.getTenantId(),qualityTestingSearchContract.getWorkOrderNumbers(),requestInfo);
            if(workOrders != null && !workOrders.isEmpty()) {
                loaNumbers = workOrders.stream().map(workorder -> workorder.getLetterOfAcceptance().getId()).collect(Collectors.toList());
                searchQualityTestingByLoaNumber(loaNumbers, params, paramValues);
            }
        }

        if(StringUtils.isNotBlank(qualityTestingSearchContract.getWorkOrderNumberLike())) {
            List<WorkOrder> workOrders = workOrderRepository.searchWorkOrder(qualityTestingSearchContract.getTenantId(), Arrays.asList(qualityTestingSearchContract.getWorkOrderNumberLike()),requestInfo);
            if(workOrders != null && !workOrders.isEmpty()) {
                loaNumbers = workOrders.stream().map(workorder -> workorder.getLetterOfAcceptance().getId()).collect(Collectors.toList());
                searchQualityTestingByLoaNumber(loaNumbers.get(0), params, paramValues);
            }
        }

        params.append(" and qt.deleted = false");
        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(QualityTestingHelper.class);

        List<QualityTestingHelper> qualityTestingHelpers = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
        List<QualityTesting> qualityTestings = new ArrayList<>();
        for(QualityTestingHelper qualityTestingHelper : qualityTestingHelpers) {
            QualityTesting qualityTesting = qualityTestingHelper.toDomain();
            QualityTestingDetailSearchContract qualityTestingDetailSearchContract = QualityTestingDetailSearchContract.builder()
                    .tenantId(qualityTesting.getTenantId()).qualityTesting(Arrays.asList(qualityTesting.getId())).build();
            qualityTesting.setQualityTestingDetails(qualityTestingDetailJdbcRepository.search(qualityTestingDetailSearchContract));
            qualityTestings.add(qualityTesting);

        }
        return qualityTestings;
    }

    private void searchQualityTestingByLoaNumber(List<String> loaNumbers, StringBuffer params, Map<String, Object> paramValues) {
        addAnd(params);
        params.append("qt.letterOfAcceptanceEstimate = lea.id and lea.letterofacceptance in(:letterOfAcceptanceEstimate) and lea.tenantId=:tenantId and lea.deleted=false");
        paramValues.put("letterOfAcceptanceEstimate", loaNumbers);
    }

    private void searchQualityTestingByLoaNumber(String loaNumber, StringBuffer params, Map<String, Object> paramValues) {
        addAnd(params);
        params.append("qt.letterOfAcceptanceEstimate = lea.id and lea.letterofacceptance in(:letterOfAcceptanceEstimate) and lea.tenantId=:tenantId and lea.deleted=false ");
        paramValues.put("letterOfAcceptanceEstimate", "%" + loaNumber + "%");
    }

    private void searchQualityTesting(List<String> estimateNumbers, StringBuffer params, Map<String, Object> paramValues) {
        addAnd(params);
        params.append("qt.letterOfAcceptanceEstimate = lea.id and lea.detailedestimate in (:detailedestimate)  and lea.tenantId=:tenantId and lea.deleted=false");
        paramValues.put("detailedestimate", estimateNumbers);
    }

    private void searchQualityTesting(String estimateNumber, StringBuffer params, Map<String, Object> paramValues) {
        addAnd(params);
        params.append("qt.letterOfAcceptanceEstimate = lea.id and lea.detailedestimate like (:detailedestimate) and lea.tenantId=:tenantId and lea.deleted=false ");
        paramValues.put("detailedestimate", "%" + estimateNumber.toUpperCase() + "%");
    }

}

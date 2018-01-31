package org.egov.works.workorder.domain.repository;

import org.egov.works.common.persistence.repository.JdbcRepository;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.workorder.persistence.helper.LetterOfAcceptanceEstimateHelper;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class LetterOfAcceptanceEstimateRepository extends JdbcRepository {

    @Autowired
    private AssetsLOARepository assetsForLOARepository;

    @Autowired
    private LoaActivityRepository loaActivityRepository;

    public static final String TABLE_NAME = "egw_letterofacceptanceestimate loaestimate";


    @Autowired
    private EstimateRepository estimateRepository;

    public List<LetterOfAcceptanceEstimate> searchLOAs(final LetterOfAcceptanceEstimateSearchContract letterOfAcceptanceEstimateSearchCriteria, final RequestInfo requestInfo) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (letterOfAcceptanceEstimateSearchCriteria.getSortBy() != null
                && !letterOfAcceptanceEstimateSearchCriteria.getSortBy().isEmpty()) {
            validateSortByOrder(letterOfAcceptanceEstimateSearchCriteria.getSortBy());
            validateEntityFieldName(letterOfAcceptanceEstimateSearchCriteria.getSortBy(), LetterOfAcceptanceEstimate.class);
        }

        StringBuilder orderBy = new StringBuilder("order by loaestimate.createdtime");
        if (letterOfAcceptanceEstimateSearchCriteria.getSortBy() != null
                && !letterOfAcceptanceEstimateSearchCriteria.getSortBy().isEmpty()) {
            orderBy.delete(0,orderBy.length()).append("order by loaestimate.").append(letterOfAcceptanceEstimateSearchCriteria.getSortBy());
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (letterOfAcceptanceEstimateSearchCriteria.getTenantId() != null) {
            addAnd(params);
            params.append("loaestimate.tenantId =:tenantId");
            paramValues.put("tenantId", letterOfAcceptanceEstimateSearchCriteria.getTenantId());
        }
        if (letterOfAcceptanceEstimateSearchCriteria.getIds() != null) {
            addAnd(params);
            params.append("loaestimate.id in(:ids) ");
            paramValues.put("ids", letterOfAcceptanceEstimateSearchCriteria.getIds());
        }


        if (letterOfAcceptanceEstimateSearchCriteria.getLetterOfAcceptanceIds() != null && !letterOfAcceptanceEstimateSearchCriteria.getLetterOfAcceptanceIds().isEmpty()) {
            addAnd(params);
            params.append("loaestimate.letterofacceptance in(:letterofacceptance)");
            paramValues.put("letterofacceptance", letterOfAcceptanceEstimateSearchCriteria.getLetterOfAcceptanceIds());
        }

        params.append(" and loaestimate.deleted = false");

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(LetterOfAcceptanceEstimateHelper.class);

        List<LetterOfAcceptanceEstimateHelper> loaEstimatesList = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
        List<LetterOfAcceptanceEstimate> loaEstimates = new ArrayList<>();
        for (LetterOfAcceptanceEstimateHelper letterOfAcceptanceEstimateHelper : loaEstimatesList) {
            LetterOfAcceptanceEstimate letterOfAcceptanceEstimate = letterOfAcceptanceEstimateHelper.toDomain();

            DetailedEstimateSearchContract detailedEstimateSearchContract = new DetailedEstimateSearchContract();
            detailedEstimateSearchContract.setDetailedEstimateNumbers(Arrays.asList(letterOfAcceptanceEstimate.getDetailedEstimate().getEstimateNumber()));
            detailedEstimateSearchContract.setStatuses(Arrays.asList(CommonConstants.STATUS_TECH_SANCTIONED));
            letterOfAcceptanceEstimate.setDetailedEstimate(estimateRepository.getDetailedEstimateByEstimateNumber(detailedEstimateSearchContract,letterOfAcceptanceEstimateSearchCriteria.getTenantId(),requestInfo).getDetailedEstimates().get(0));

            AssetsForLoaSearchContract assetsForLoaSearchCriteria = AssetsForLoaSearchContract.builder()
                    .tenantId(letterOfAcceptanceEstimate.getTenantId())
                    .letterOfAcceptanceEstimateIds(Arrays.asList(letterOfAcceptanceEstimate.getId())).build();

            LoaActivitySearchContract loaActivitySearchCriteria = LoaActivitySearchContract.builder()
                    .tenantId(letterOfAcceptanceEstimate.getTenantId())
                    .letterOfAcceptanceEstimateIds(Arrays.asList(letterOfAcceptanceEstimate.getId())).build();

            letterOfAcceptanceEstimate.setAssetForLOAs(assetsForLOARepository.searchLoaAssets(assetsForLoaSearchCriteria));

            letterOfAcceptanceEstimate.setLoaActivities(loaActivityRepository.searchLoaActivity(loaActivitySearchCriteria));


            loaEstimates.add(letterOfAcceptanceEstimate);


        }
        return loaEstimates;
    }
}

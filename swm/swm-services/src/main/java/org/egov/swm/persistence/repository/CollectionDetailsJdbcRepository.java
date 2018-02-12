package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.CollectionDetails;
import org.egov.swm.domain.model.CollectionDetailsSearch;
import org.egov.swm.domain.model.CollectionType;
import org.egov.swm.domain.service.CollectionTypeService;
import org.egov.swm.persistence.entity.CollectionDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CollectionDetailsJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_collectiondetails";

    @Autowired
    private CollectionTypeService collectionTypeService;

    @Transactional
    public void delete(final String tenantId, final String sourceSegregation) {
        delete(TABLE_NAME, tenantId, "sourceSegregation", sourceSegregation);
    }

    public List<CollectionDetails> search(final CollectionDetailsSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getSourceSegregationCode() != null) {
            addAnd(params);
            params.append("sourceSegregation =:sourceSegregation");
            paramValues.put("sourceSegregation", searchRequest.getSourceSegregationCode());
        }

        if (searchRequest.getSourceSegregationCodes() != null) {
            addAnd(params);
            params.append("sourceSegregation in (:sourceSegregations)");
            paramValues.put("sourceSegregations",
                    new ArrayList<>(Arrays.asList(searchRequest.getSourceSegregationCodes().split(","))));
        }

        if (searchRequest.getCollectionTypeCode() != null) {
            addAnd(params);
            params.append("collectionType =:collectionType");
            paramValues.put("collectionType", searchRequest.getCollectionTypeCode());
        }

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(CollectionDetailsEntity.class);

        final List<CollectionDetailsEntity> entityList = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues,
                row);

        final List<CollectionDetails> resultList = new ArrayList<>();

        if (entityList != null && !entityList.isEmpty()) {

            for (final CollectionDetailsEntity cde : entityList)
                resultList.add(cde.toDomain());

            populateCollectionTypes(resultList);
        }

        return resultList;
    }

    private void populateCollectionTypes(final List<CollectionDetails> collectionDetailsList) {

        final Map<String, CollectionType> collectionTypeMap = new HashMap<>();
        String tenantId = null;

        if (collectionDetailsList != null && !collectionDetailsList.isEmpty())
            tenantId = collectionDetailsList.get(0).getTenantId();

        final List<CollectionType> collectionTypes = collectionTypeService.getAll(tenantId, new RequestInfo());

        for (final CollectionType ct : collectionTypes)
            collectionTypeMap.put(ct.getCode(), ct);

        for (final CollectionDetails collectionDetails : collectionDetailsList)
            if (collectionDetails.getCollectionType() != null && collectionDetails.getCollectionType().getCode() != null
                    && !collectionDetails.getCollectionType().getCode().isEmpty())
                collectionDetails.setCollectionType(collectionTypeMap.get(collectionDetails.getCollectionType().getCode()));
    }

}
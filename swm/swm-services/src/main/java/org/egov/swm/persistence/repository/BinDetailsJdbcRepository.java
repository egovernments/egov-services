package org.egov.swm.persistence.repository;

import org.egov.swm.domain.model.BinDetails;
import org.egov.swm.domain.model.BinDetailsSearch;
import org.egov.swm.persistence.entity.BinDetailsEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BinDetailsJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_bindetails";

    private static final Logger LOG = LoggerFactory.getLogger(BinDetailsJdbcRepository.class);

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    @Transactional
    public void delete(final String tenantId, final String collectionPoint) {
        delete(TABLE_NAME, tenantId, "collectionPoint", collectionPoint);
    }

    public List<BinDetails> search(final BinDetailsSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getId() != null) {
            addAnd(params);
            params.append("id =:id");
            paramValues.put("id", searchRequest.getId());
        }

        if (searchRequest.getAssetCode() != null) {
            addAnd(params);
            params.append("asset =:asset");
            paramValues.put("asset", searchRequest.getAssetCode());
        }

        if (searchRequest.getCollectionPoint() != null) {
            addAnd(params);
            params.append("collectionPoint =:collectionPoint");
            paramValues.put("collectionPoint", searchRequest.getCollectionPoint());
        }

        if (searchRequest.getCollectionPoints() != null) {
            addAnd(params);
            params.append("collectionPoint in (:collectionPoints)");
            paramValues.put("collectionPoints", new ArrayList<>(Arrays.asList(searchRequest.getCollectionPoints().split(","))));
        }

        if (searchRequest.getCollectionPoint() != null) {
            addAnd(params);
            params.append("collectionPoint =:collectionPoint");
            paramValues.put("collectionPoint", searchRequest.getCollectionPoint());
        }

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(BinDetailsEntity.class);

        long start = System.currentTimeMillis();

            final List<BinDetailsEntity> entityList = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

        long end = System.currentTimeMillis();
        LOG.info("Time taken for searchQuery for Bind Details " + (end - start) + "ms");
        LOG.info("Search Query BinDetailsEntity " + searchQuery.toString() + "paramValues " + paramValues);

        final List<BinDetails> resultList = new ArrayList<>();

        if (entityList != null && !entityList.isEmpty()) {

            for (final BinDetailsEntity bde : entityList) {

                resultList.add(bde.toDomain());
            }

        }

        return resultList;
    }

}
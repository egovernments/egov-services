package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.BinDetailsSearch;
import org.egov.swm.domain.model.Boundary;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointDetailsSearch;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.persistence.entity.CollectionPointEntity;
import org.egov.swm.web.repository.BoundaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class CollectionPointJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_collectionpoint";

    @Autowired
    public BinDetailsJdbcRepository binIdDetailsJdbcRepository;

    @Autowired
    private BoundaryRepository boundaryRepository;

    @Autowired
    public CollectionPointDetailsJdbcRepository collectionPointDetailsJdbcRepository;

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    public Pagination<CollectionPoint> search(final CollectionPointSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), CollectionPointSearch.class);
        }

        String orderBy = "order by name";
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        if (searchRequest.getCodes() != null) {
            addAnd(params);
            params.append("code in (:codes)");
            paramValues.put("codes", new ArrayList<>(Arrays.asList(searchRequest.getCodes().split(","))));
        }
        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getCode() != null) {
            addAnd(params);
            params.append("code =:code");
            paramValues.put("code", searchRequest.getCode());
        }

        if (searchRequest.getName() != null) {
            addAnd(params);
            params.append("name =:name");
            paramValues.put("name", searchRequest.getName());
        }

        if (searchRequest.getLocationCode() != null) {
            addAnd(params);
            params.append("location =:location");
            paramValues.put("location", searchRequest.getLocationCode());
        }

        Pagination<CollectionPoint> page = new Pagination<>();
        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());
        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<CollectionPoint>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(CollectionPointEntity.class);

        final List<CollectionPoint> collectionPointList = new ArrayList<>();

        final List<CollectionPointEntity> collectionPointEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
                paramValues, row);
        CollectionPoint cp;
        BinDetailsSearch bds;
        CollectionPointDetailsSearch cpds;
        for (final CollectionPointEntity collectionPointEntity : collectionPointEntities) {

            cp = collectionPointEntity.toDomain();

            if (cp.getLocation() != null && cp.getLocation().getCode() != null) {

                final Boundary boundary = boundaryRepository.fetchBoundaryByCode(cp.getLocation().getCode(),
                        cp.getTenantId());

                if (boundary != null)
                    cp.setLocation(boundary);
            }

            bds = new BinDetailsSearch();
            bds.setCollectionPoint(cp.getCode());
            bds.setTenantId(cp.getTenantId());
            cp.setBinDetails(binIdDetailsJdbcRepository.search(bds));

            cpds = new CollectionPointDetailsSearch();
            cpds.setTenantId(cp.getTenantId());
            cpds.setCollectionPoint(cp.getCode());
            cp.setCollectionPointDetails(collectionPointDetailsJdbcRepository.search(cpds));

            collectionPointList.add(cp);
        }

        page.setTotalResults(collectionPointList.size());

        page.setPagedData(collectionPointList);

        return page;
    }

}
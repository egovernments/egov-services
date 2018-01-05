package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.BinDetails;
import org.egov.swm.domain.model.BinDetailsSearch;
import org.egov.swm.domain.model.Boundary;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointDetails;
import org.egov.swm.domain.model.CollectionPointDetailsSearch;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.CollectionType;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.service.BoundaryService;
import org.egov.swm.domain.service.CollectionTypeService;
import org.egov.swm.persistence.entity.CollectionPointEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class CollectionPointJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_collectionpoint";

    @Autowired
    public BinDetailsJdbcRepository binIdDetailsJdbcRepository;

    @Autowired
    public CollectionPointDetailsJdbcRepository collectionPointDetailsJdbcRepository;

    @Autowired
    private CollectionTypeService collectionTypeService;

    @Autowired
    private BoundaryService boundaryService;

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
        StringBuffer collectionPointCodes = new StringBuffer();

        for (final CollectionPointEntity collectionPointEntity : collectionPointEntities) {
            collectionPointList.add(collectionPointEntity.toDomain());

            if (collectionPointCodes.length() >= 1)
                collectionPointCodes.append(",");

            collectionPointCodes.append(collectionPointEntity.getCode());
        }

        if (collectionPointList != null && !collectionPointList.isEmpty()) {

            populateBoundarys(collectionPointList);

            populateBinDetails(collectionPointList, collectionPointCodes.toString());

            populateCollectionPointDetails(collectionPointList, collectionPointCodes.toString());
        }
        page.setTotalResults(collectionPointList.size());

        page.setPagedData(collectionPointList);

        return page;
    }

    private void populateBoundarys(List<CollectionPoint> collectionPointList) {

        String tenantId = null;
        Map<String, Boundary> boundaryMap = new HashMap<>();

        if (collectionPointList != null && !collectionPointList.isEmpty())
            tenantId = collectionPointList.get(0).getTenantId();

        List<Boundary> boundarys = boundaryService.getAll(tenantId, new RequestInfo());

        if (boundarys != null) {

            for (Boundary bd : boundarys) {

                boundaryMap.put(bd.getCode(), bd);

            }
        }

        for (CollectionPoint collectionPoint : collectionPointList) {

            if (collectionPoint.getLocation() != null && collectionPoint.getLocation().getCode() != null
                    && !collectionPoint.getLocation().getCode().isEmpty()) {

                collectionPoint.setLocation(boundaryMap.get(collectionPoint.getLocation().getCode()));
            }

        }

    }

    private void populateBinDetails(List<CollectionPoint> collectionPointList, String collectionPointCodes) {
        Map<String, List<BinDetails>> binDetailsMap = new HashMap<>();
        String tenantId = null;
        BinDetailsSearch bds;
        bds = new BinDetailsSearch();

        if (collectionPointList != null && !collectionPointList.isEmpty())
            tenantId = collectionPointList.get(0).getTenantId();

        bds.setCollectionPoints(collectionPointCodes);
        bds.setTenantId(tenantId);

        List<BinDetails> binDetails = binIdDetailsJdbcRepository.search(bds);

        for (BinDetails bd : binDetails) {

            if (binDetailsMap.get(bd.getCollectionPoint()) == null) {

                binDetailsMap.put(bd.getCollectionPoint(), Collections.singletonList(bd));

            } else {

                List<BinDetails> bdList = new ArrayList<>(binDetailsMap.get(bd.getCollectionPoint()));

                bdList.add(bd);

                binDetailsMap.put(bd.getCollectionPoint(), bdList);

            }
        }

        for (CollectionPoint collectionPoint : collectionPointList) {

            collectionPoint.setBinDetails(binDetailsMap.get(collectionPoint.getCode()));

        }

    }

    private void populateCollectionPointDetails(List<CollectionPoint> collectionPointList, String collectionPointCodes) {
        Map<String, List<CollectionPointDetails>> collectionPointDetailsMap = new HashMap<>();
        Map<String, CollectionType> collectionTypeMap = new HashMap<>();
        String tenantId = null;
        CollectionPointDetailsSearch cpds;
        cpds = new CollectionPointDetailsSearch();

        if (collectionPointList != null && !collectionPointList.isEmpty())
            tenantId = collectionPointList.get(0).getTenantId();

        cpds.setCollectionPoints(collectionPointCodes);
        cpds.setTenantId(tenantId);

        List<CollectionPointDetails> collectionPointDetails = collectionPointDetailsJdbcRepository.search(cpds);

        List<CollectionType> collectionTypes = collectionTypeService.getAll(tenantId, new RequestInfo());

        for (CollectionType ct : collectionTypes) {
            collectionTypeMap.put(ct.getCode(), ct);
        }

        for (CollectionPointDetails cpd : collectionPointDetails) {

            if (cpd.getCollectionType() != null && cpd.getCollectionType().getCode() != null
                    && !cpd.getCollectionType().getCode().isEmpty()) {

                cpd.setCollectionType(collectionTypeMap.get(cpd.getCollectionType().getCode()));
            }

            if (collectionPointDetailsMap.get(cpd.getCollectionPoint()) == null) {

                collectionPointDetailsMap.put(cpd.getCollectionPoint(), Collections.singletonList(cpd));

            } else {

                List<CollectionPointDetails> cpdList = new ArrayList<>(collectionPointDetailsMap.get(cpd.getCollectionPoint()));

                cpdList.add(cpd);

                collectionPointDetailsMap.put(cpd.getCollectionPoint(), cpdList);

            }
        }

        for (CollectionPoint collectionPoint : collectionPointList) {

            collectionPoint.setCollectionPointDetails(collectionPointDetailsMap.get(collectionPoint.getCode()));

        }

    }

}
package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.CollectionDetails;
import org.egov.swm.domain.model.CollectionDetailsSearch;
import org.egov.swm.domain.model.DumpingGround;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.SourceSegregation;
import org.egov.swm.domain.model.SourceSegregationSearch;
import org.egov.swm.domain.model.Tenant;
import org.egov.swm.domain.service.DumpingGroundService;
import org.egov.swm.domain.service.TenantService;
import org.egov.swm.persistence.entity.SourceSegregationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class SourceSegregationJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_sourcesegregation";

    @Autowired
    public CollectionDetailsJdbcRepository collectionDetailsJdbcRepository;

    @Autowired
    private DumpingGroundService dumpingGroundService;

    @Autowired
    private TenantService tenantService;

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    public Pagination<SourceSegregation> search(final SourceSegregationSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), SourceSegregationSearch.class);
        }

        String orderBy = "order by code";
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        if (searchRequest.getCode() != null) {
            addAnd(params);
            params.append("code in (:code)");
            paramValues.put("code", searchRequest.getCode());
        }

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

        if (searchRequest.getSourceSegregationDate() != null) {
            addAnd(params);
            params.append("sourceSegregationDate =:sourceSegregationDate");
            paramValues.put("sourceSegregationDate", searchRequest.getSourceSegregationDate());
        }

        if (searchRequest.getDumpingGroundCode() != null) {
            addAnd(params);
            params.append("dumpingGround =:dumpingGround");
            paramValues.put("dumpingGround", searchRequest.getDumpingGroundCode());
        }

        if (searchRequest.getUlbCode() != null) {
            addAnd(params);
            params.append("ulb =:ulb");
            paramValues.put("ulb", searchRequest.getUlbCode());
        }

        Pagination<SourceSegregation> page = new Pagination<>();
        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());
        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<SourceSegregation>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(SourceSegregationEntity.class);

        final List<SourceSegregation> sourceSegregationList = new ArrayList<>();

        final List<SourceSegregationEntity> sourceSegregationEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        SourceSegregation ss;

        StringBuffer sourceSegregationCodes = new StringBuffer();

        for (final SourceSegregationEntity sourceSegregationEntity : sourceSegregationEntities) {

            ss = sourceSegregationEntity.toDomain();

            if (sourceSegregationCodes.length() >= 1)
                sourceSegregationCodes.append(",");

            sourceSegregationCodes.append(ss.getCode());

            sourceSegregationList.add(ss);

        }
        if (sourceSegregationList != null && !sourceSegregationList.isEmpty()) {

            populateDumpingGrounds(sourceSegregationList);

            populateCollectionDetails(sourceSegregationList, sourceSegregationCodes.toString());

            populateULBs(sourceSegregationList);
        }
        page.setPagedData(sourceSegregationList);

        return page;
    }

    private void populateCollectionDetails(List<SourceSegregation> sourceSegregationList, String sourceSegregationCodes) {

        Map<String, List<CollectionDetails>> collectionDetailsMap = new HashMap<>();
        String tenantId = null;
        CollectionDetailsSearch cds;
        cds = new CollectionDetailsSearch();

        if (sourceSegregationList != null && !sourceSegregationList.isEmpty())
            tenantId = sourceSegregationList.get(0).getTenantId();

        cds.setSourceSegregationCodes(sourceSegregationCodes);
        cds.setTenantId(tenantId);

        List<CollectionDetails> collectionDetailss = collectionDetailsJdbcRepository.search(cds);

        for (CollectionDetails bd : collectionDetailss) {

            if (collectionDetailsMap.get(bd.getSourceSegregation()) == null) {

                collectionDetailsMap.put(bd.getSourceSegregation(), Collections.singletonList(bd));

            } else {

                List<CollectionDetails> bdList = new ArrayList<>(collectionDetailsMap.get(bd.getSourceSegregation()));

                bdList.add(bd);

                collectionDetailsMap.put(bd.getSourceSegregation(), bdList);

            }
        }

        for (SourceSegregation sourceSegregation : sourceSegregationList) {

            sourceSegregation.setCollectionDetails(collectionDetailsMap.get(sourceSegregation.getCode()));

        }

    }

    private void populateDumpingGrounds(List<SourceSegregation> sourceSegregationList) {

        Map<String, DumpingGround> dumpingGroundMap = new HashMap<>();
        String tenantId = null;

        if (sourceSegregationList != null && !sourceSegregationList.isEmpty())
            tenantId = sourceSegregationList.get(0).getTenantId();

        List<DumpingGround> dumpingGrounds = dumpingGroundService.getAll(tenantId, new RequestInfo());

        for (DumpingGround dg : dumpingGrounds) {
            dumpingGroundMap.put(dg.getCode(), dg);
        }

        for (SourceSegregation sourceSegregation : sourceSegregationList) {

            if (sourceSegregation.getDumpingGround() != null && sourceSegregation.getDumpingGround().getCode() != null
                    && !sourceSegregation.getDumpingGround().getCode().isEmpty()) {

                sourceSegregation.setDumpingGround(dumpingGroundMap.get(sourceSegregation.getDumpingGround().getCode()));
            }

        }
    }

    private void populateULBs(List<SourceSegregation> sourceSegregationList) {

        Map<String, Tenant> TenantMap = new HashMap<>();
        String tenantId = null;

        if (sourceSegregationList != null && !sourceSegregationList.isEmpty())
            tenantId = sourceSegregationList.get(0).getTenantId();

        List<Tenant> tenants = tenantService.getAll(tenantId, new RequestInfo());

        for (Tenant t : tenants) {
            TenantMap.put(t.getCode(), t);
        }

        for (SourceSegregation sourceSegregation : sourceSegregationList) {

            if (sourceSegregation.getUlb() != null && sourceSegregation.getUlb().getCode() != null
                    && !sourceSegregation.getUlb().getCode().isEmpty()) {

                sourceSegregation.setUlb(TenantMap.get(sourceSegregation.getUlb().getCode()));
            }

        }
    }

}
package org.egov.egf.voucher.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.voucher.domain.model.VoucherSubType;
import org.egov.egf.voucher.domain.model.VoucherSubTypeSearch;
import org.egov.egf.voucher.persistence.entity.VoucherSubTypeEntity;
import org.egov.egf.voucher.persistence.entity.VoucherSubTypeSearchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VoucherSubTypeJdbcRepository extends JdbcRepository {

    private static final Logger LOG = LoggerFactory
            .getLogger(VoucherSubTypeJdbcRepository.class);

    static {
        LOG.debug("init voucherSubType");
        init(VoucherSubTypeEntity.class);
        LOG.debug("end init voucherSubType");
    }

    public VoucherSubTypeJdbcRepository(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public VoucherSubTypeEntity create(VoucherSubTypeEntity entity) {
        super.create(entity);
        return entity;
    }

    public VoucherSubTypeEntity update(VoucherSubTypeEntity entity) {
        super.update(entity);
        return entity;

    }

    public Pagination<VoucherSubType> search(VoucherSubTypeSearch domain) {
        VoucherSubTypeSearchEntity voucherSubTypeSearchEntity = new VoucherSubTypeSearchEntity();
        voucherSubTypeSearchEntity.toEntity(domain);

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        Map<String, Object> paramValues = new HashMap<>();
        StringBuffer params = new StringBuffer();

        if (voucherSubTypeSearchEntity.getSortBy() != null
                && !voucherSubTypeSearchEntity.getSortBy().isEmpty()) {
            validateSortByOrder(voucherSubTypeSearchEntity.getSortBy());
            validateEntityFieldName(voucherSubTypeSearchEntity.getSortBy(),
                    VoucherSubTypeEntity.class);
        }

        String orderBy = "order by vouchername";
        if (voucherSubTypeSearchEntity.getSortBy() != null
                && !voucherSubTypeSearchEntity.getSortBy().isEmpty()) {
            orderBy = "order by " + voucherSubTypeSearchEntity.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename",
                VoucherSubTypeEntity.TABLE_NAME);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        // implement jdbc specfic search

        if (voucherSubTypeSearchEntity.getId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("id =:id");
            paramValues.put("id", voucherSubTypeSearchEntity.getId());
        }

        if (voucherSubTypeSearchEntity.getIds() != null
                && !voucherSubTypeSearchEntity.getIds().isEmpty()) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("id in (:ids)");
            paramValues.put("ids", voucherSubTypeSearchEntity.getIds());
        }

        if (voucherSubTypeSearchEntity.getVoucherName() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("voucherName = :voucherName");
            paramValues.put("voucherName",
                    voucherSubTypeSearchEntity.getVoucherName());
        }
        
        if (voucherSubTypeSearchEntity.getVoucherNamePrefix() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("voucherNamePrefix = :voucherNamePrefix");
            paramValues.put("voucherNamePrefix",
                    voucherSubTypeSearchEntity.getVoucherNamePrefix());
        }

        if (voucherSubTypeSearchEntity.getVoucherType() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("voucherType = :voucherType");
            paramValues.put("voucherType",
                    voucherSubTypeSearchEntity.getVoucherType());
        }

        if (voucherSubTypeSearchEntity.getCutOffDate() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("cutOffDate = :cutOffDate");
            paramValues.put("cutOffDate",
                    voucherSubTypeSearchEntity.getCutOffDate());
        }

        if (voucherSubTypeSearchEntity.getExclude() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("exclude = :exclude");
            paramValues.put("exclude", voucherSubTypeSearchEntity.getExclude());
        }

        Pagination<VoucherSubType> page = new Pagination<>();
        if (voucherSubTypeSearchEntity.getOffset() != null) {
            page.setOffset(voucherSubTypeSearchEntity.getOffset());
        }

        if (voucherSubTypeSearchEntity.getPageSize() != null) {
            page.setPageSize(voucherSubTypeSearchEntity.getPageSize());
        }

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition",
                    " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<VoucherSubType>) getPagination(searchQuery, page,
                paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset()
                        * page.getPageSize());

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(
                VoucherSubTypeEntity.class);

        List<VoucherSubTypeEntity> voucherSubTypeEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        page.setTotalResults(voucherSubTypeEntities.size());

        List<VoucherSubType> voucherSubTypes = new ArrayList<>();
        for (VoucherSubTypeEntity voucherSubTypeEntity : voucherSubTypeEntities) {

            voucherSubTypes.add(voucherSubTypeEntity.toDomain());
        }
        page.setPagedData(voucherSubTypes);

        return page;
    }
}

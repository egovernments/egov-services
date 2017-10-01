package org.egov.egf.voucher.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.voucher.persistence.entity.SubLedgerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SubLedgerJdbcRepository extends JdbcRepository {
    private static final Logger LOG = LoggerFactory.getLogger(SubLedgerJdbcRepository.class);

    static {
        LOG.debug("init subLedger");
        init(SubLedgerEntity.class);
        LOG.debug("end init subLedger");
    }

    public SubLedgerJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public SubLedgerEntity create(SubLedgerEntity entity) {
        super.create(entity);
        return entity;
    }

    public SubLedgerEntity update(SubLedgerEntity entity) {
        super.update(entity);
        return entity;

    }

    public SubLedgerEntity delete(final SubLedgerEntity entity) {
        super.delete(entity.TABLE_NAME, entity.getId());
        return entity;
    }

    /*
     * public Pagination<SubLedger> search(SubLedgerSearch domain) { SubLedgerSearchEntity subLedgerSearchEntity = new
     * SubLedgerSearchEntity(); subLedgerSearchEntity.toEntity(domain); String searchQuery =
     * "select :selectfields from :tablename :condition  :orderby   "; Map<String, Object> paramValues = new HashMap<>();
     * StringBuffer params = new StringBuffer(); if (subLedgerSearchEntity.getSortBy() != null &&
     * !subLedgerSearchEntity.getSortBy().isEmpty()) { validateSortByOrder(subLedgerSearchEntity.getSortBy());
     * validateEntityFieldName(subLedgerSearchEntity.getSortBy(), SubLedgerEntity.class); } String orderBy = "order by name"
     * ; if (subLedgerSearchEntity.getSortBy() != null && !subLedgerSearchEntity.getSortBy().isEmpty()) { orderBy =
     * "order by " + subLedgerSearchEntity.getSortBy(); } searchQuery = searchQuery.replace(":tablename",
     * SubLedgerEntity.TABLE_NAME); searchQuery = searchQuery.replace(":selectfields", " * "); // implement jdbc specfic search
     * if (subLedgerSearchEntity.getId() != null) { if (params.length() > 0) params.append(" and "); params.append( "id =:id");
     * paramValues.put("id", subLedgerSearchEntity.getId()); } if (subLedgerSearchEntity.getAccountDetailTypeId() != null) {
     * if (params.length() > 0) params.append(" and "); params.append( "accountDetailType =:accountDetailType");
     * paramValues.put("accountDetailType", subLedgerSearchEntity.getAccountDetailTypeId()); } if
     * (subLedgerSearchEntity.getAccountDetailKeyId() != null) { if (params.length() > 0) params.append(" and ");
     * params.append( "accountDetailKey =:accountDetailKey"); paramValues.put("accountDetailKey",
     * subLedgerSearchEntity.getAccountDetailKeyId()); } if (subLedgerSearchEntity.getAmount() != null) { if
     * (params.length() > 0) params.append(" and "); params.append("amount =:amount"); paramValues.put("amount",
     * subLedgerSearchEntity.getAmount()); } if (subLedgerSearchEntity.getIds() != null) { if (params.length() > 0)
     * params.append(" and "); params.append("ids =:ids"); paramValues.put("ids", subLedgerSearchEntity.getIds()); } if
     * (subLedgerSearchEntity.getId() != null) { if (params.length() > 0) { params.append(" and "); } params.append("id =:id");
     * paramValues.put("id", subLedgerSearchEntity.getId()); } if (subLedgerSearchEntity.getCode() != null) { if
     * (params.length() > 0) { params.append(" and "); } params.append("code =:code"); paramValues.put("code",
     * subLedgerSearchEntity.getCode()); } if (subLedgerSearchEntity.getName() != null) { if (params.length() > 0) {
     * params.append(" and "); } params.append("name =:name"); paramValues.put("name", subLedgerSearchEntity.getName()); } if
     * (subLedgerSearchEntity.getIdentifier() != null) { if (params.length() > 0) { params.append(" and "); } params.append(
     * "description =:description"); paramValues.put("description", subLedgerSearchEntity.getIdentifier()); } if
     * (subLedgerSearchEntity.getActive() != null) { if (params.length() > 0) { params.append(" and "); } params.append(
     * "active =:active"); paramValues.put("active", subLedgerSearchEntity.getActive()); } if
     * (subLedgerSearchEntity.getLevel() != null) { if (params.length() > 0) { params.append(" and "); } params.append(
     * "type =:type"); paramValues.put("type", subLedgerSearchEntity.getLevel()); } Pagination<SubLedger> page = new
     * Pagination<>(); if (subLedgerSearchEntity.getOffset() != null) { page.setOffset(subLedgerSearchEntity.getOffset()); }
     * if (subLedgerSearchEntity.getPageSize() != null) { page.setPageSize(subLedgerSearchEntity.getPageSize()); } if
     * (params.length() > 0) { searchQuery = searchQuery.replace(":condition", " where " + params.toString()); } else searchQuery
     * = searchQuery.replace(":condition", ""); searchQuery = searchQuery.replace(":orderby", orderBy); page =
     * (Pagination<SubLedger>) getPagination(searchQuery, page, paramValues); searchQuery = searchQuery + " :pagination";
     * searchQuery = searchQuery.replace(":pagination", "limit " + page.getPageSize() + " offset " + page.getOffset() *
     * page.getPageSize()); BeanPropertyRowMapper row = new BeanPropertyRowMapper(SubLedgerEntity.class);
     * List<SubLedgerEntity> subLedgerEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
     * page.setTotalResults(subLedgerEntities.size()); List<SubLedger> subLedgers = new ArrayList<>(); for
     * (SubLedgerEntity subLedgerEntity : subLedgerEntities) { subLedgers.add(subLedgerEntity.toDomain()); }
     * page.setPagedData(subLedgers); return page; }
     */

    public SubLedgerEntity findById(SubLedgerEntity entity) {
        List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

        Map<String, Object> paramValues = new HashMap<>();

        for (String s : list) {
            paramValues.put(s, getValue(getField(entity, s), entity));
        }

        List<SubLedgerEntity> subLedgers = namedParameterJdbcTemplate.query(
                getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
                new BeanPropertyRowMapper(SubLedgerEntity.class));
        if (subLedgers.isEmpty()) {
            return null;
        } else {
            return subLedgers.get(0);
        }

    }

}
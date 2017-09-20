package org.egov.egf.voucher.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.voucher.persistence.entity.LedgerDetailEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class LedgerDetailJdbcRepository extends JdbcRepository {
    private static final Logger LOG = LoggerFactory.getLogger(LedgerDetailJdbcRepository.class);

    static {
        LOG.debug("init ledgerDetail");
        init(LedgerDetailEntity.class);
        LOG.debug("end init ledgerDetail");
    }

    public LedgerDetailJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public LedgerDetailEntity create(LedgerDetailEntity entity) {
        entity.setId(UUID.randomUUID().toString().replace("-", ""));
        super.create(entity);
        return entity;
    }

    public LedgerDetailEntity update(LedgerDetailEntity entity) {
        super.update(entity);
        return entity;

    }

    public LedgerDetailEntity delete(final LedgerDetailEntity entity) {
        super.delete(entity.TABLE_NAME, entity.getId());
        return entity;
    }

    /*
     * public Pagination<LedgerDetail> search(LedgerDetailSearch domain) { LedgerDetailSearchEntity ledgerDetailSearchEntity = new
     * LedgerDetailSearchEntity(); ledgerDetailSearchEntity.toEntity(domain); String searchQuery =
     * "select :selectfields from :tablename :condition  :orderby   "; Map<String, Object> paramValues = new HashMap<>();
     * StringBuffer params = new StringBuffer(); if (ledgerDetailSearchEntity.getSortBy() != null &&
     * !ledgerDetailSearchEntity.getSortBy().isEmpty()) { validateSortByOrder(ledgerDetailSearchEntity.getSortBy());
     * validateEntityFieldName(ledgerDetailSearchEntity.getSortBy(), LedgerDetailEntity.class); } String orderBy = "order by name"
     * ; if (ledgerDetailSearchEntity.getSortBy() != null && !ledgerDetailSearchEntity.getSortBy().isEmpty()) { orderBy =
     * "order by " + ledgerDetailSearchEntity.getSortBy(); } searchQuery = searchQuery.replace(":tablename",
     * LedgerDetailEntity.TABLE_NAME); searchQuery = searchQuery.replace(":selectfields", " * "); // implement jdbc specfic search
     * if (ledgerDetailSearchEntity.getId() != null) { if (params.length() > 0) params.append(" and "); params.append( "id =:id");
     * paramValues.put("id", ledgerDetailSearchEntity.getId()); } if (ledgerDetailSearchEntity.getAccountDetailTypeId() != null) {
     * if (params.length() > 0) params.append(" and "); params.append( "accountDetailType =:accountDetailType");
     * paramValues.put("accountDetailType", ledgerDetailSearchEntity.getAccountDetailTypeId()); } if
     * (ledgerDetailSearchEntity.getAccountDetailKeyId() != null) { if (params.length() > 0) params.append(" and ");
     * params.append( "accountDetailKey =:accountDetailKey"); paramValues.put("accountDetailKey",
     * ledgerDetailSearchEntity.getAccountDetailKeyId()); } if (ledgerDetailSearchEntity.getAmount() != null) { if
     * (params.length() > 0) params.append(" and "); params.append("amount =:amount"); paramValues.put("amount",
     * ledgerDetailSearchEntity.getAmount()); } if (ledgerDetailSearchEntity.getIds() != null) { if (params.length() > 0)
     * params.append(" and "); params.append("ids =:ids"); paramValues.put("ids", ledgerDetailSearchEntity.getIds()); } if
     * (ledgerDetailSearchEntity.getId() != null) { if (params.length() > 0) { params.append(" and "); } params.append("id =:id");
     * paramValues.put("id", ledgerDetailSearchEntity.getId()); } if (ledgerDetailSearchEntity.getCode() != null) { if
     * (params.length() > 0) { params.append(" and "); } params.append("code =:code"); paramValues.put("code",
     * ledgerDetailSearchEntity.getCode()); } if (ledgerDetailSearchEntity.getName() != null) { if (params.length() > 0) {
     * params.append(" and "); } params.append("name =:name"); paramValues.put("name", ledgerDetailSearchEntity.getName()); } if
     * (ledgerDetailSearchEntity.getIdentifier() != null) { if (params.length() > 0) { params.append(" and "); } params.append(
     * "description =:description"); paramValues.put("description", ledgerDetailSearchEntity.getIdentifier()); } if
     * (ledgerDetailSearchEntity.getActive() != null) { if (params.length() > 0) { params.append(" and "); } params.append(
     * "active =:active"); paramValues.put("active", ledgerDetailSearchEntity.getActive()); } if
     * (ledgerDetailSearchEntity.getLevel() != null) { if (params.length() > 0) { params.append(" and "); } params.append(
     * "type =:type"); paramValues.put("type", ledgerDetailSearchEntity.getLevel()); } Pagination<LedgerDetail> page = new
     * Pagination<>(); if (ledgerDetailSearchEntity.getOffset() != null) { page.setOffset(ledgerDetailSearchEntity.getOffset()); }
     * if (ledgerDetailSearchEntity.getPageSize() != null) { page.setPageSize(ledgerDetailSearchEntity.getPageSize()); } if
     * (params.length() > 0) { searchQuery = searchQuery.replace(":condition", " where " + params.toString()); } else searchQuery
     * = searchQuery.replace(":condition", ""); searchQuery = searchQuery.replace(":orderby", orderBy); page =
     * (Pagination<LedgerDetail>) getPagination(searchQuery, page, paramValues); searchQuery = searchQuery + " :pagination";
     * searchQuery = searchQuery.replace(":pagination", "limit " + page.getPageSize() + " offset " + page.getOffset() *
     * page.getPageSize()); BeanPropertyRowMapper row = new BeanPropertyRowMapper(LedgerDetailEntity.class);
     * List<LedgerDetailEntity> ledgerDetailEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
     * page.setTotalResults(ledgerDetailEntities.size()); List<LedgerDetail> ledgerdetails = new ArrayList<>(); for
     * (LedgerDetailEntity ledgerDetailEntity : ledgerDetailEntities) { ledgerdetails.add(ledgerDetailEntity.toDomain()); }
     * page.setPagedData(ledgerdetails); return page; }
     */

    public LedgerDetailEntity findById(LedgerDetailEntity entity) {
        List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

        Map<String, Object> paramValues = new HashMap<>();

        for (String s : list) {
            paramValues.put(s, getValue(getField(entity, s), entity));
        }

        List<LedgerDetailEntity> ledgerdetails = namedParameterJdbcTemplate.query(
                getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
                new BeanPropertyRowMapper(LedgerDetailEntity.class));
        if (ledgerdetails.isEmpty()) {
            return null;
        } else {
            return ledgerdetails.get(0);
        }

    }

}
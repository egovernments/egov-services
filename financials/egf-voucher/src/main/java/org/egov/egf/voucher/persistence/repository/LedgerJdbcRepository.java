package org.egov.egf.voucher.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.egf.voucher.persistence.entity.LedgerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class LedgerJdbcRepository extends JdbcRepository {
    private static final Logger LOG = LoggerFactory.getLogger(LedgerJdbcRepository.class);

    static {
        LOG.debug("init ledger");
        init(LedgerEntity.class);
        LOG.debug("end init ledger");
    }

    public LedgerJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public LedgerEntity create(LedgerEntity entity) {
        super.create(entity);
        return entity;
    }

    public LedgerEntity update(LedgerEntity entity) {
        super.update(entity);
        return entity;

    }

    public LedgerEntity delete(final LedgerEntity entity) {
        super.delete(entity.TABLE_NAME, entity.getId());
        return entity;
    }

    /*
     * public Pagination<Ledger> search(LedgerSearch domain) { LedgerSearchEntity ledgerSearchEntity = new LedgerSearchEntity();
     * ledgerSearchEntity.toEntity(domain); String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";
     * Map<String, Object> paramValues = new HashMap<>(); StringBuffer params = new StringBuffer(); if
     * (ledgerSearchEntity.getSortBy() != null && !ledgerSearchEntity.getSortBy().isEmpty()) {
     * validateSortByOrder(ledgerSearchEntity.getSortBy()); validateEntityFieldName(ledgerSearchEntity.getSortBy(),
     * LedgerEntity.class); } String orderBy = "order by name"; if (ledgerSearchEntity.getSortBy() != null &&
     * !ledgerSearchEntity.getSortBy().isEmpty()) { orderBy = "order by " + ledgerSearchEntity.getSortBy(); } searchQuery =
     * searchQuery.replace(":tablename", LedgerEntity.TABLE_NAME); searchQuery = searchQuery.replace(":selectfields", " * "); //
     * implement jdbc specfic search if (ledgerSearchEntity.getId() != null) { if (params.length() > 0) params.append(" and ");
     * params.append( "id =:id"); paramValues.put("id", ledgerSearchEntity.getId()); } if (ledgerSearchEntity.getOrderId() !=
     * null) { if (params.length() > 0) params.append(" and "); params.append("orderId =:orderId"); paramValues.put("orderId",
     * ledgerSearchEntity.getOrderId()); } if (ledgerSearchEntity.getChartOfAccountId() != null) { if (params.length() > 0)
     * params.append(" and "); params.append( "chartOfAccount =:chartOfAccount"); paramValues.put("chartOfAccount",
     * ledgerSearchEntity.getChartOfAccountId()); } if (ledgerSearchEntity.getGlcode() != null) { if (params.length() > 0)
     * params.append(" and "); params.append("glcode =:glcode"); paramValues.put("glcode", ledgerSearchEntity.getGlcode()); } if
     * (ledgerSearchEntity.getDebitAmount() != null) { if (params.length() > 0) params.append(" and "); params.append(
     * "debitAmount =:debitAmount"); paramValues.put("debitAmount", ledgerSearchEntity.getDebitAmount()); } if
     * (ledgerSearchEntity.getCreditAmount() != null) { if (params.length() > 0) params.append(" and "); params.append(
     * "creditAmount =:creditAmount"); paramValues.put("creditAmount", ledgerSearchEntity.getCreditAmount()); } if
     * (ledgerSearchEntity.getFunctionId() != null) { if (params.length() > 0) params.append(" and "); params.append(
     * "function =:function"); paramValues.put("function", ledgerSearchEntity.getFunctionId()); } if
     * (ledgerSearchEntity.getSubLedger() != null) { if (params.length() > 0) params.append(" and "); params.append(
     * "subLedger =:subLedger" ); paramValues.put("subLedger", ledgerSearchEntity.getSubLedger()); } if
     * (ledgerSearchEntity.getIds() != null) { if (params.length() > 0) params.append(" and "); params.append("ids =:ids");
     * paramValues.put("ids", ledgerSearchEntity.getIds()); } if (ledgerSearchEntity.getId() != null) { if (params.length() > 0) {
     * params.append(" and "); } params.append("id =:id"); paramValues.put("id", ledgerSearchEntity.getId()); } if
     * (ledgerSearchEntity.getCode() != null) { if (params.length() > 0) { params.append(" and "); } params.append( "code =:code"
     * ); paramValues.put("code", ledgerSearchEntity.getCode()); } if (ledgerSearchEntity.getName() != null) { if (params.length()
     * > 0) { params.append(" and "); } params.append("name =:name"); paramValues.put("name", ledgerSearchEntity.getName()); } if
     * (ledgerSearchEntity.getIdentifier() != null) { if (params.length() > 0) { params.append(" and "); } params.append(
     * "description =:description"); paramValues.put("description", ledgerSearchEntity.getIdentifier()); } if
     * (ledgerSearchEntity.getActive() != null) { if (params.length() > 0) { params.append(" and "); } params.append(
     * "active =:active"); paramValues.put("active", ledgerSearchEntity.getActive()); } if (ledgerSearchEntity.getLevel() != null)
     * { if (params.length() > 0) { params.append(" and "); } params.append("type =:type"); paramValues.put("type",
     * ledgerSearchEntity.getLevel()); } Pagination<Ledger> page = new Pagination<>(); if (ledgerSearchEntity.getOffset() != null)
     * { page.setOffset(ledgerSearchEntity.getOffset()); } if (ledgerSearchEntity.getPageSize() != null) {
     * page.setPageSize(ledgerSearchEntity.getPageSize()); } if (params.length() > 0) { searchQuery =
     * searchQuery.replace(":condition", " where " + params.toString()); } else searchQuery = searchQuery.replace(":condition",
     * ""); searchQuery = searchQuery.replace(":orderby", orderBy); page = (Pagination<Ledger>) getPagination(searchQuery, page,
     * paramValues); searchQuery = searchQuery + " :pagination"; searchQuery = searchQuery.replace(":pagination", "limit " +
     * page.getPageSize() + " offset " + page.getOffset() * page.getPageSize()); BeanPropertyRowMapper row = new
     * BeanPropertyRowMapper(LedgerEntity.class); List<LedgerEntity> ledgerEntities =
     * namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row); page.setTotalResults(ledgerEntities.size());
     * List<Ledger> ledgers = new ArrayList<>(); for (LedgerEntity ledgerEntity : ledgerEntities) {
     * ledgers.add(ledgerEntity.toDomain()); } page.setPagedData(ledgers); return page; }
     */

    public LedgerEntity findById(LedgerEntity entity) {
        List<String> list = allIdentitiferFields.get(entity.getClass().getSimpleName());

        Map<String, Object> paramValues = new HashMap<>();

        for (String s : list) {
            paramValues.put(s, getValue(getField(entity, s), entity));
        }

        List<LedgerEntity> ledgers = namedParameterJdbcTemplate.query(
                getByIdQuery.get(entity.getClass().getSimpleName()).toString(), paramValues,
                new BeanPropertyRowMapper(LedgerEntity.class));
        if (ledgers.isEmpty()) {
            return null;
        } else {
            return ledgers.get(0);
        }

    }

}
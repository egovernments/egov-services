package org.egov.inv.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.JdbcRepository;
import org.egov.common.Pagination;
import org.egov.inv.model.MaterialIssue;
import org.egov.inv.model.MaterialIssueSearchContract;
import org.egov.inv.persistence.entity.MaterialIssueEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MaterialIssueJdbcRepository extends JdbcRepository {


	 private static final String updateQuery = "UPDATE materialissue SET materialissuestatus =:materialissuestatus WHERE issuenumber =:issuenumber AND tenantId =:tenantId";
   
    static {
        init(MaterialIssueEntity.class);
    }

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Autowired
    private StoreJdbcRepository storeJdbcRepository;
    
    public static synchronized void init(Class T) {
        String TABLE_NAME = "";

        List<String> insertFields = new ArrayList<>();
        List<String> updateFields = new ArrayList<>();
        List<String> uniqueFields = new ArrayList<>();

        String insertQuery = "";
        String updateQuery = "";
        String searchQuery = "";

        try {

            TABLE_NAME = (String) T.getDeclaredField("TABLE_NAME").get(null);
        } catch (Exception e) {

        }
        insertFields.addAll(fetchFields(T));
        uniqueFields.add("issueNumber");
        uniqueFields.add("tenantId");
        insertFields.removeAll(uniqueFields);
        allInsertQuery.put(T.getSimpleName(), insertQuery(insertFields, TABLE_NAME, uniqueFields));
        updateFields.addAll(insertFields);
        updateFields.remove("createdBy");
        updateQuery = updateQuery(updateFields, TABLE_NAME, uniqueFields);
        System.out.println(T.getSimpleName() + "--------" + insertFields);
        allInsertFields.put(T.getSimpleName(), insertFields);
        allUpdateFields.put(T.getSimpleName(), updateFields);
        allIdentitiferFields.put(T.getSimpleName(), uniqueFields);
        // allInsertQuery.put(T.getSimpleName(), insertQuery);
        allUpdateQuery.put(T.getSimpleName(), updateQuery);
        getByIdQuery.put(T.getSimpleName(), getByIdQuery(TABLE_NAME, uniqueFields));
        System.out.println(allInsertQuery);
    }


    public Pagination<MaterialIssue> search(final MaterialIssueSearchContract searchContract, String issueType) {
        String searchQuery = "select * from materialissue :condition :orderby";
        StringBuffer params = new StringBuffer();
        Map<String, Object> paramValues = new HashMap<>();
        if (searchContract.getSortBy() != null && !searchContract.getSortBy().isEmpty()) {
            validateSortByOrder(searchContract.getSortBy());
            validateEntityFieldName(searchContract.getSortBy(), MaterialIssueSearchContract.class);
        }
        String orderBy = "order by id";
        if (searchContract.getSortBy() != null && !searchContract.getSortBy().isEmpty()) {
            orderBy = "order by " + searchContract.getSortBy();
        }
        if (searchContract.getId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("id in (:ids)");
            paramValues.put("ids", searchContract.getId());
        }

        if (searchContract.getFromStore() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("fromstore = :fromstore");
            paramValues.put("fromstore", searchContract.getFromStore());
        }
        if (searchContract.getToStore() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("tostore = :tostore");
            paramValues.put("tostore", searchContract.getToStore());
        }
        if (searchContract.getIssueNoteNumber() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("issuenumber = :issuenumber");
            paramValues.put("issuenumber", searchContract.getIssueNoteNumber());
        }
        if (searchContract.getSupplier() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("supplier = :supplier");
            paramValues.put("supplier", searchContract.getSupplier());
        }
        if (searchContract.getIssueDate() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("issuedate = :issuedate");
            paramValues.put("issuedate", searchContract.getIssueDate());
        }
        if (searchContract.getMaterialIssueStatus() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("materialissuestatus = :materialissuestatus");
            paramValues.put("materialissuestatus", searchContract.getMaterialIssueStatus());
        }
        
        if (searchContract.getIssuePurpose() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("issuepurpose = :issuepurpose");
            paramValues.put("issuepurpose", searchContract.getIssuePurpose());
        }
        if (searchContract.getDescription() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("description = :description");
            paramValues.put("description", searchContract.getDescription());
        }
        if (searchContract.getTotalIssueValue() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("totalissuevalue = :totalissuevalue");
            paramValues.put("totalissuevalue", searchContract.getTotalIssueValue());
        }
        if (searchContract.getTenantId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("tenantid = :tenantid");
            paramValues.put("tenantid", searchContract.getTenantId());
        }
        if (issueType != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("issuetype = :issuetype");
            paramValues.put("issuetype", issueType);
        }
        if (searchContract.getScrapCreated() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("scrapCreated = :scrapCreated");
            paramValues.put("scrapCreated", searchContract.getScrapCreated());
        }
        Pagination<MaterialIssue> page = new Pagination<>();
        if (searchContract.getPageSize() != null)
            page.setPageSize(searchContract.getPageSize());
        if (searchContract.getPageNumber() != null)
            page.setOffset(searchContract.getPageNumber());
        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where deleted is not true and " + params.toString());
        else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);
        page = (Pagination<MaterialIssue>) getPagination(searchQuery, page, paramValues);

        searchQuery = searchQuery + " :pagination";
        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());
        BeanPropertyRowMapper row = new BeanPropertyRowMapper(MaterialIssueEntity.class);

        List<MaterialIssue> materialIssueList = new ArrayList<>();

        List<MaterialIssueEntity> materialIssueEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
                paramValues, row);

        for (MaterialIssueEntity materialIssueEntity : materialIssueEntities) {

            materialIssueList.add(materialIssueEntity.toDomain(issueType));
        }

        page.setTotalResults(materialIssueList.size());

        page.setPagedData(materialIssueList);

        return page;
    }

    public void updateStatus(String issuenumber, String materialissuestatus, String tenantId) {

		 Map<String,String> paramValues = new HashMap<>();
		 paramValues.put("issuenumber", issuenumber);
		 paramValues.put("materialissuestatus",materialissuestatus);
		 paramValues.put("tenantId", tenantId);
		namedParameterJdbcTemplate.update(updateQuery,paramValues);
		
	}

	


}
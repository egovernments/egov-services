package org.egov.inv.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.JdbcRepository;
import org.egov.common.Pagination;
import org.egov.inv.model.Disposal;
import org.egov.inv.model.DisposalSearchContract;
import org.egov.inv.model.MaterialIssue;
import org.egov.inv.model.MaterialIssueSearchContract;
import org.egov.inv.persistence.entity.DisposalEntity;
import org.egov.inv.persistence.entity.MaterialIssueEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
@Service
public class DisposalJdbcRepository extends JdbcRepository {
	   static {
	        init(DisposalEntity.class);
	    }

	public Pagination<Disposal> search(DisposalSearchContract searchContract) {
        String searchQuery = "select * from disposal :condition :orderby";
        StringBuffer params = new StringBuffer();
        Map<String, Object> paramValues = new HashMap<>();
        if (searchContract.getSortBy() != null && !searchContract.getSortBy().isEmpty()) {
            validateSortByOrder(searchContract.getSortBy());
            validateEntityFieldName(searchContract.getSortBy(), DisposalSearchContract.class);
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

        if (searchContract.getAuctionNumber() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("auctionnumber = :auctionnumber");
            paramValues.put("auctionnumber", searchContract.getAuctionNumber());
        }
        if (searchContract.getDisposalDate() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("disposaldate = :disposaldate");
            paramValues.put("disposaldate", searchContract.getDisposalDate());
        }
        if (searchContract.getDisposalNumber() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("disposalnumber = :disposalnumber");
            paramValues.put("disposalnumber", searchContract.getDisposalNumber());
        }
        if (searchContract.getDisposalStatus() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("disposalstatus = :disposalstatus");
            paramValues.put("disposalstatus", searchContract.getDisposalStatus());
        }
        if (searchContract.getHandOverTo() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("handoverto = :handoverto");
            paramValues.put("handoverto", searchContract.getHandOverTo());
        }
        if (searchContract.getStore() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("store = :store");
            paramValues.put("store", searchContract.getStore());
        }
        
        if (searchContract.getTotalDisposalValue() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("totaldisposalvalue = :totaldisposalvalue");
            paramValues.put("totaldisposalvalue", searchContract.getTotalDisposalValue());
        }
        if (searchContract.getStateId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("stateid = :stateid");
            paramValues.put("stateid", searchContract.getStateId());
        }
        if (searchContract.getTenantId() != null) {
            if (params.length() > 0)
                params.append(" and ");
            params.append("tenantid = :tenantid");
            paramValues.put("tenantid", searchContract.getTenantId());
        }
        Pagination<Disposal> page = new Pagination<>();
        if (searchContract.getPageSize() != null)
            page.setPageSize(searchContract.getPageSize());
        if (searchContract.getPageNumber() != null)
            page.setOffset(searchContract.getPageNumber());
        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where deleted is not true and " + params.toString());
        else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);
        page = (Pagination<Disposal>) getPagination(searchQuery, page, paramValues);

        searchQuery = searchQuery + " :pagination";
        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());
        BeanPropertyRowMapper row = new BeanPropertyRowMapper(DisposalEntity.class);

        List<Disposal> disposalList = new ArrayList<>();

        List<DisposalEntity> disposalEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
                paramValues, row);

        for (DisposalEntity disposalEntity : disposalEntities) {

        	disposalList.add(disposalEntity.toDomain());
        }

        page.setTotalResults(disposalList.size());

        page.setPagedData(disposalList);

        return page;
	}
}

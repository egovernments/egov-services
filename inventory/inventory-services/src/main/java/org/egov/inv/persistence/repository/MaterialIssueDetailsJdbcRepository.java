package org.egov.inv.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.JdbcRepository;
import org.egov.common.Pagination;
import org.egov.inv.model.MaterialIssue;
import org.egov.inv.model.MaterialIssueDetail;
import org.egov.inv.persistence.entity.MaterialIssueDetailEntity;
import org.egov.inv.persistence.entity.MaterialIssueEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class MaterialIssueDetailsJdbcRepository extends JdbcRepository {

	public Pagination<MaterialIssueDetail> search(String issueNumber, String tenantId, String issueType) {
		String searchQuery = "select * from materialissuedetail :condition :orderby";
		StringBuffer params = new StringBuffer();
		String orderBy = "order by id";
		Map<String, Object> paramValues = new HashMap<>();
		if(issueNumber != null)
		{
			if (params.length() > 0)
				params.append(" and ");
			params.append("materialissuenumber in (:issueNumbers)");
			paramValues.put("issueNumbers", issueNumber);
		}
		if(tenantId != null)
		{
			if (params.length() > 0)
				params.append(" and ");
			params.append("tenantId = :tenantId");
			paramValues.put("tenantId", tenantId);
		}
		if (params.length() > 0)
			searchQuery = searchQuery.replace(":condition", " where deleted is not true and " + params.toString());
		else
			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);
		Pagination<MaterialIssueDetail> page = new Pagination<>();
		BeanPropertyRowMapper row = new BeanPropertyRowMapper(MaterialIssueDetailEntity.class);

		List<MaterialIssueDetail> materialIssueDetailList = new ArrayList<>();

		List<MaterialIssueDetailEntity> materialIssueDetailEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
				paramValues, row);

		for (MaterialIssueDetailEntity materialIssueDetailEntity : materialIssueDetailEntities) {

			materialIssueDetailList.add(materialIssueDetailEntity.toDomain(issueType));
		}

		page.setTotalResults(materialIssueDetailList.size());

		page.setPagedData(materialIssueDetailList);

		return page;
	}

}

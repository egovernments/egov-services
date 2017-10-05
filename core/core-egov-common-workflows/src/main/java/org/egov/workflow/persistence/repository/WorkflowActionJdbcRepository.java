package org.egov.workflow.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.workflow.domain.model.Pagination;
import org.egov.workflow.persistence.entity.WorkflowAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Component
@Service
public class WorkflowActionJdbcRepository extends JdbcRepository{
	private static final Logger LOG = LoggerFactory.getLogger(WorkflowActionJdbcRepository.class);

	
	static {
		LOG.debug("init workflowAction");
		init(WorkflowAction.class);
		LOG.debug("end init workflowAction");
	}
	public WorkflowActionJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public WorkflowAction create(WorkflowAction entity) {
		super.create(entity);
		return entity;
	}

	public WorkflowAction update(WorkflowAction entity) {
		super.update(entity);
		return entity;

	}

	public WorkflowAction findById(WorkflowAction workflowAction) {
		List<String> list = allIdentitiferFields.get(workflowAction.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(workflowAction, s), workflowAction));
		}

		List<WorkflowAction> workflowActions = namedParameterJdbcTemplate.query(
				getByIdQuery.get(workflowAction.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(WorkflowAction.class));
		if (workflowActions.isEmpty()) {
			return null;
		} else {
			return workflowAction;
		}

	}
	
	public List<WorkflowAction> search(WorkflowAction workflowSearchEntity) {

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();


		String orderBy = "order by name";

		searchQuery = searchQuery.replace(":tablename", WorkflowAction.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search
		if (workflowSearchEntity.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", workflowSearchEntity.getTenantId());
		}
		if (workflowSearchEntity.getId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id =:id");
			paramValues.put("id", workflowSearchEntity.getId());
		}
		if (workflowSearchEntity.getType() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("type =:type");
			paramValues.put("type", workflowSearchEntity.getType());
		}
		if (workflowSearchEntity.getName() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("name =:name");
			paramValues.put("name", workflowSearchEntity.getName());
		}

		Pagination<WorkflowAction> page = new Pagination<>();

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(WorkflowAction.class);

		return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
	}

	
	public Boolean uniqueCheck(String fieldName, WorkflowAction workflowAction) {
		return super.uniqueCheck(fieldName,workflowAction);
	}

}

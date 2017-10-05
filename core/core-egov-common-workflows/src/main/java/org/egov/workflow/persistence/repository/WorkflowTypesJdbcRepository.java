package org.egov.workflow.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.workflow.persistence.entity.WorkflowTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Component
@Service
public class WorkflowTypesJdbcRepository extends JdbcRepository{
	private static final Logger LOG = LoggerFactory.getLogger(WorkflowTypesJdbcRepository.class);

	
	static {
		LOG.debug("init workflowTypes");
		init(WorkflowTypes.class);
		LOG.debug("end init workflowTypes");
	}
	public WorkflowTypesJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public WorkflowTypes create(WorkflowTypes entity) {
		super.create(entity);
		return entity;
	}

	public WorkflowTypes update(WorkflowTypes entity) {
		super.update(entity);
		return entity;

	}

	public WorkflowTypes findById(WorkflowTypes workflowTypes) {
		List<String> list = allIdentitiferFields.get(workflowTypes.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(workflowTypes, s), workflowTypes));
		}

		List<WorkflowTypes> workflowTypesModel = namedParameterJdbcTemplate.query(
				getByIdQuery.get(workflowTypes.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(WorkflowTypes.class));
		if (workflowTypesModel.isEmpty()) {
			return null;
		} else {
			return workflowTypes;
		}

	}
	
	public List<WorkflowTypes> search(WorkflowTypes workflowTypesSearchEntity) {
		String orderBy = "order by id";
		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		searchQuery = searchQuery.replace(":tablename", WorkflowTypes.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");
		// implement jdbc specfic search
		if (workflowTypesSearchEntity.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", workflowTypesSearchEntity.getTenantId());
		}
		if (workflowTypesSearchEntity.getId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id =:id");
			paramValues.put("id", workflowTypesSearchEntity.getId());
		}
		if (workflowTypesSearchEntity.getType() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("type =:type");
			paramValues.put("type", workflowTypesSearchEntity.getType());
		}
		if (workflowTypesSearchEntity.getLink() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("link =:link");
			paramValues.put("link", workflowTypesSearchEntity.getLink());
		}
		
		if (workflowTypesSearchEntity.getTypeFQN() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("typefqn =:typefqn");
			paramValues.put("typefqn", workflowTypesSearchEntity.getTypeFQN());
		}
		if (workflowTypesSearchEntity.getDisplayName() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("displayname =:displayname");
			paramValues.put("displayname", workflowTypesSearchEntity.getDisplayName());
		}
		
		if (workflowTypesSearchEntity.isGrouped()) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("grouped =:grouped");
			paramValues.put("grouped", workflowTypesSearchEntity.isGrouped());
		}
		if (workflowTypesSearchEntity.isEnabled() ) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("enabled =:enabled");
			paramValues.put("enabled", workflowTypesSearchEntity.isEnabled());
		}
		 
		if (workflowTypesSearchEntity.isEnabledInMs()) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("enabledinms =:enabledinms");
			paramValues.put("enabledinms", workflowTypesSearchEntity.isEnabledInMs());
		}
		 if (workflowTypesSearchEntity.getTenantId() != null) {
				if (params.length() > 0) {
					params.append(" and ");
				}
				params.append("tenantid =:tenantid");
				paramValues.put("tenantid", workflowTypesSearchEntity.getTenantId());
			}
		 searchQuery = searchQuery.replace(":condition", "");

			searchQuery = searchQuery.replace(":orderby", orderBy);

			BeanPropertyRowMapper row = new BeanPropertyRowMapper(WorkflowTypes.class);

			return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
	}

	
	public Boolean uniqueCheck(String fieldName, WorkflowTypes workflowTypes) {
		return super.uniqueCheck(fieldName,workflowTypes);
	}

}

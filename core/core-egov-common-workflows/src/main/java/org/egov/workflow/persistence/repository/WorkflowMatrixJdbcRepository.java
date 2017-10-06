package org.egov.workflow.persistence.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.workflow.persistence.entity.State;
import org.egov.workflow.persistence.entity.WorkFlowMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class WorkflowMatrixJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(WorkflowMatrixJdbcRepository.class);

	static {
		LOG.debug("init workflowMatrix");
		init(WorkFlowMatrix.class);
		LOG.debug("end init workflowMatrix");
	}

	public WorkflowMatrixJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public WorkFlowMatrix create(WorkFlowMatrix entity) {
		super.create(entity);
		return entity;
	}

	public WorkFlowMatrix update(WorkFlowMatrix entity) {
		super.update(entity);
		return entity;

	}

	public WorkFlowMatrix findById(WorkFlowMatrix workflowMatrix) {
		List<String> list = allIdentitiferFields.get(workflowMatrix.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(workflowMatrix, s), workflowMatrix));
		}

		List<WorkFlowMatrix> workflowMatrixModel = namedParameterJdbcTemplate.query(
				getByIdQuery.get(workflowMatrix.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(State.class));
		if (workflowMatrixModel.isEmpty()) {
			return null;
		} else {
			return workflowMatrix;
		}

	}

	public List<WorkFlowMatrix> search(WorkFlowMatrix workflowMatrix) {

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";
		String orderBy = "order by id";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		searchQuery = searchQuery.replace(":tablename", WorkFlowMatrix.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");
		// implement jdbc specfic search
		if (workflowMatrix.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", workflowMatrix.getTenantId());
		}

		if (workflowMatrix.getId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id =:id");
			paramValues.put("id", workflowMatrix.getId());
		}

		if (workflowMatrix.getDepartment() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			if(!workflowMatrix.getDepartment().isEmpty() && workflowMatrix.getDepartment() != null)
			{
			params.append("department =:department");
			paramValues.put("department", workflowMatrix.getDepartment());
			}
			else
			params.append("department =:department");
			paramValues.put("department","ANY");

		}

		if (workflowMatrix.getObjectType() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("objecttype =:type");
			paramValues.put("type", workflowMatrix.getObjectType());
		}

		if (workflowMatrix.getCurrentState() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			if(!workflowMatrix.getCurrentState().isEmpty()  && workflowMatrix.getCurrentState() != null )
			{
			params.append("currentstate ilike '%currentstate%'");
			paramValues.put("currentstate", workflowMatrix.getCurrentState());
			}
			else
			params.append("currentstate ilike '%currentstate%'");
			paramValues.put("currentstate", "NEW");

		}

		if (workflowMatrix.getPendingActions() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("pendingactions ilike '%pendingactions%'");
			paramValues.put("pendingactions", workflowMatrix.getPendingActions());
		}

		if (workflowMatrix.getCurrentDesignation() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("currentdesignation ilike '%currentdesignation%'");
			paramValues.put("currentdesignation", workflowMatrix.getCurrentDesignation());
		}

		if (workflowMatrix.getAdditionalRule() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("additionalrule =:additionalrule");
			paramValues.put("additionalrule", workflowMatrix.getAdditionalRule());
		}

		if (workflowMatrix.getNextState() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("nextstate '%nextstate%'");
			paramValues.put("nextstate", workflowMatrix.getNextState());
		}

		if (workflowMatrix.getNextAction() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("nextaction ilike '%nextaction%'");
			paramValues.put("nextaction", workflowMatrix.getNextAction());
		}

		if (workflowMatrix.getNextDesignation() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("nextdesignation =:nextdesignation");
			paramValues.put("nextdesignation", workflowMatrix.getNextDesignation());
		}

		if (workflowMatrix.getNextStatus() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("nextstatus =:nextstatus");
			paramValues.put("nextstatus", workflowMatrix.getNextStatus());
		}

		if (workflowMatrix.getValidActions() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("validactions =:validactions");
			paramValues.put("validactions", workflowMatrix.getValidActions());
		}

		if (workflowMatrix.getFromQty() != null && BigDecimal.ZERO.compareTo(workflowMatrix.getFromQty()) !=0) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("fromqty <=:fromqty");
			paramValues.put("fromqty", workflowMatrix.getFromQty());
		}

		if (workflowMatrix.getToQty() != null  && BigDecimal.ZERO.compareTo(workflowMatrix.getToQty()) !=0) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("toqty >=:toqty");
			paramValues.put("toqty", workflowMatrix.getToQty());
		}

		if (workflowMatrix.getFromDate() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("fromdate <=:fromdate");
			paramValues.put("fromdate", workflowMatrix.getFromDate());
		}
		if (workflowMatrix.getToDate() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("todate >=:todate");
			paramValues.put("todate", workflowMatrix.getToDate());
		}

		if (workflowMatrix.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantid =:tenantid");
			paramValues.put("tenantid", workflowMatrix.getTenantId());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(WorkFlowMatrix.class);

		return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
	}

	public Boolean uniqueCheck(String fieldName, WorkFlowMatrix workFlowMatrix) {
		return super.uniqueCheck(fieldName, workFlowMatrix);
	}

}

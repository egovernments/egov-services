package org.egov.workflow.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.workflow.persistence.entity.StateHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Component
@Service
public class StateHistoryJdbcRepository extends JdbcRepository{
	private static final Logger LOG = LoggerFactory.getLogger(StateHistoryJdbcRepository.class);

	
	static {
		LOG.debug("init stateHistory");
		init(StateHistory.class);
		LOG.debug("end init stateHistory");
	}
	public StateHistoryJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public StateHistory create(StateHistory entity) {
		super.create(entity);
		return entity;
	}

	public StateHistory update(StateHistory entity) {
		super.update(entity);
		return entity;

	}

	public StateHistory findById(StateHistory stateHistory) {
		List<String> list = allIdentitiferFields.get(stateHistory.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(stateHistory, s), stateHistory));
		}

		List<StateHistory> stateHistorys = namedParameterJdbcTemplate.query(
				getByIdQuery.get(stateHistory.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(StateHistory.class));
		if (stateHistorys.isEmpty()) {
			return null;
		} else {
			return stateHistorys.get(0);
		}

	}
	
	public List<StateHistory> search(StateHistory stateHistory) {
	
		String orderBy = "order by name";

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();


		searchQuery = searchQuery.replace(":tablename", StateHistory.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");
	
		// implement jdbc specfic search
		if (stateHistory.getId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id =:id");
			paramValues.put("id", stateHistory.getId());
		}
		if (stateHistory.getState().getId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("state_id =:state_id");
			paramValues.put("state_id", stateHistory.getState().getId());
		}
		if (stateHistory.getValue() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("value =:value");
			paramValues.put("value", stateHistory.getValue());
		}
		if (stateHistory.getOwnerPosition() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("owner_pos =:owner_pos");
			paramValues.put("owner_pos", stateHistory.getOwnerPosition());
		}
		if (stateHistory.getOwnerUser() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("owner_user =:owner_user");
			paramValues.put("owner_user", stateHistory.getOwnerUser());
		}
		if (stateHistory.getSenderName() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("sendername =:sendername");
			paramValues.put("sendername", stateHistory.getSenderName());
		}
		if (stateHistory.getNextAction() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("nextaction =:nextaction");
			paramValues.put("nextaction", stateHistory.getNextAction());
		}
		if (stateHistory.getInitiatorPosition() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("initiator_pos =:initiator_pos");
			paramValues.put("initiator_pos", stateHistory.getInitiatorPosition());
		}
		if (stateHistory.getNatureOfTask() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("natureoftask =:natureoftask");
			paramValues.put("natureoftask", stateHistory.getNatureOfTask());
		}
		if (stateHistory.getExtraInfo() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("extrainfo =:extrainfo");
			paramValues.put("extrainfo", stateHistory.getExtraInfo());
		}
		if (stateHistory.getExtraDateInfo() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("extradateinfo =:extradateinfo");
			paramValues.put("extradateinfo", stateHistory.getExtraDateInfo());
		}
		if (stateHistory.getDateInfo() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("dateinfo =:dateinfo");
			paramValues.put("dateinfo", stateHistory.getDateInfo());
		}
		if (stateHistory.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantid =:tenantid");
			paramValues.put("tenantid", stateHistory.getTenantId());
		}


		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(StateHistory.class);

		return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
	}
	

	
	public Boolean uniqueCheck(String fieldName, StateHistory states) {
		return super.uniqueCheck(fieldName,states);
	}

}

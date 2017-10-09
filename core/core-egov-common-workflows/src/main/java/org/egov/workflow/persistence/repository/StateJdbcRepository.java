package org.egov.workflow.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.workflow.persistence.entity.State;
import org.egov.workflow.persistence.entity.Enum.StateStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Component
@Service
public class StateJdbcRepository extends JdbcRepository {
	private static final Logger LOG = LoggerFactory.getLogger(StateJdbcRepository.class);

	static {
		LOG.debug("init state");
		init(State.class);
		LOG.debug("end init state");
	}
	/*rrrrrrrrrrrrrrrrrrrr*/
	public String getNextSequence() {
		return getSequence(State.SEQUENCE_NAME);
	}

	public String getSequence(String seqName) {
        String seqQuery = "select nextval('" + seqName + "')";
        return String.valueOf(jdbcTemplate.queryForObject(seqQuery, Long.class) + 1);
    }
/**/
	public StateJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public State create(State entity) {
		super.create(entity);
		return entity;
	}

	public State update(State entity) {
		super.update(entity);
		return entity;

	}

	public State findById(State state) {
		List<String> list = allIdentitiferFields.get(state.getClass().getSimpleName());

		Map<String, Object> paramValues = new HashMap<>();

		for (String s : list) {
			paramValues.put(s, getValue(getField(state, s), state));
		}

		List<State> states = namedParameterJdbcTemplate.query(
				getByIdQuery.get(state.getClass().getSimpleName()).toString(), paramValues,
				new BeanPropertyRowMapper(State.class));
		if (states.isEmpty() && states.size()<0) {
			return null;
		} else {
			return states.get(0);
		}

	}
	

	public List<State> search(State stateSearchEntity) {
		
		

		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();


		String orderBy = "order by createdDate DESC";

		searchQuery = searchQuery.replace(":tablename", State.TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		// implement jdbc specfic search

		if (stateSearchEntity.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", stateSearchEntity.getTenantId());
		}
		if (stateSearchEntity.getId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id =:id");
			paramValues.put("id", stateSearchEntity.getId());
		}
		if (stateSearchEntity.getType() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("type =:type");
			paramValues.put("type", stateSearchEntity.getType());
		}
		if (stateSearchEntity.getValue() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("value =:value");
			paramValues.put("value", stateSearchEntity.getValue());
		}
		if (stateSearchEntity.getSenderName() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("sendername =:sendername");
			paramValues.put("sendername", stateSearchEntity.getSenderName());
		}
		if (stateSearchEntity.getOwner_pos() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("owner_pos =:owner_pos");
			paramValues.put("owner_pos", stateSearchEntity.getOwner_pos());
		}
		if (stateSearchEntity.getOwner_user() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("owner_user =:owner_user");
			paramValues.put("owner_user", stateSearchEntity.getOwner_user());
		}
		
		if (stateSearchEntity.getInitiator_pos() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("initiator_pos =:initiator_pos");
			paramValues.put("initiator_pos", stateSearchEntity.getInitiator_pos());
		}
		if (stateSearchEntity.getStatus() != null && stateSearchEntity.getStatus() != StateStatus.ENDED.ordinal())
		
		{
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("status =:status");
			paramValues.put("status", stateSearchEntity.getStatus());
		}
		if (stateSearchEntity.getNextAction() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("nextaction =:nextaction");
			paramValues.put("nextaction", stateSearchEntity.getNextAction());
		}
		
		if (stateSearchEntity.getNatureOfTask() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("natureoftask =:natureoftask");
			paramValues.put("natureoftask", stateSearchEntity.getNatureOfTask());
		}
		if (stateSearchEntity.getDateInfo() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("dateinfo =:dateinfo");
			paramValues.put("dateinfo", stateSearchEntity.getDateInfo());
		}
		if (stateSearchEntity.getExtraDateInfo() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("extradateinfo =:extradateinfo");
			paramValues.put("extradateinfo", stateSearchEntity.getExtraDateInfo());
		}
		
		if (stateSearchEntity.getMyLinkId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("mylinkid =:mylinkid");
			paramValues.put("mylinkid", stateSearchEntity.getMyLinkId());
		}
		
		if (stateSearchEntity.getMyLinkId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("createdBy =:createdBy");
			paramValues.put("createdBy", stateSearchEntity.getOwner_user());
		}


		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(State.class);

		return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
	}

	public Boolean uniqueCheck(String fieldName, State states) {
		return super.uniqueCheck(fieldName, states);
	}

}

package org.egov.mr.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfigurationKeyValuesRowMapper implements ResultSetExtractor<Map<String, List<String>>> {
	@Override
	public Map<String, List<String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, List<String>> serviceConfigKeyValMap = new HashMap<String, List<String>>();

		while (rs.next()) {
			String serviceConfigKey = rs.getString("keyname");

			if (!serviceConfigKeyValMap.containsKey(serviceConfigKey)) {
				serviceConfigKeyValMap.put(serviceConfigKey, new ArrayList());
			}

			List<String> serviceConfigKeyVal = serviceConfigKeyValMap.get(serviceConfigKey);
			serviceConfigKeyVal.add(rs.getString("value"));
		}

		return serviceConfigKeyValMap;
	}
}

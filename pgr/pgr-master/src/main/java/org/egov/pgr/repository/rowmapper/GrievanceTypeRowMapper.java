/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.pgr.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.pgr.domain.model.Attribute;
import org.egov.pgr.domain.model.GrievanceType;
import org.egov.pgr.domain.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class GrievanceTypeRowMapper implements RowMapper<GrievanceType> {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(GrievanceTypeRowMapper.class);
	public Map<String, List<Value>> attribValue = new HashMap<>();
	public Map<String, Map<String, Attribute>> serviceAttrib = new HashMap<>();
	public Map<String, GrievanceType> serviceMap = new HashMap<>();
	public static final String separator = ">";

	@Override
	public GrievanceType mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		if (serviceMap.containsKey(rs.getString("code"))) {
			if (serviceAttrib.containsKey(rs.getString("code"))) {
				Map<String, Attribute> innerMap = serviceAttrib.get(rs.getString("code"));
				if (innerMap.containsKey(rs.getString("attributecode"))) {
					if (attribValue
							.containsKey(rs.getString("code").concat(separator + rs.getString("attributecode")))) {
						List<Value> innerValueList = attribValue
								.get(rs.getString("code").concat(separator + rs.getString("attributecode")));
						innerValueList.add(createValueObjectForMe(rs));
					} else {
						List<Value> innerValueList = new ArrayList<>();
						innerValueList.add(createValueObjectForMe(rs));
						attribValue.put(rs.getString("code").concat(separator + rs.getString("attributecode")),
								innerValueList);
					}
				} else {
					if (null != rs.getString("key")) {
						if(!rs.getString("key").isEmpty()) {
						List<Value> innerValueList = new ArrayList<>();
						innerValueList.add(createValueObjectForMe(rs));
						attribValue.put(rs.getString("code").concat(separator + rs.getString("attributecode")),
								innerValueList);
						innerMap.put(rs.getString("attributecode"), createAttributeObjectForMe(rs));
					} 
					}
				}

			} else {
				if (null != rs.getString("attributecode")) {
					if(!rs.getString("attributecode").isEmpty()) {
					List<Value> innerValueList = new ArrayList<>();
					innerValueList.add(createValueObjectForMe(rs));
					attribValue.put(rs.getString("code").concat(separator + rs.getString("attributecode")),
							innerValueList);
					Map<String, Attribute> innerMap = new HashMap<>();
					innerMap.put(rs.getString("attributecode"), createAttributeObjectForMe(rs));
					serviceAttrib.put(rs.getString("code"), innerMap);
				}
				}
			}
		} else {
			if (null != rs.getString("key")) {
				if (!rs.getString("key").isEmpty()) {
					List<Value> innerValueList = new ArrayList<>();
					innerValueList.add(createValueObjectForMe(rs));
					attribValue.put(rs.getString("code").concat(separator + rs.getString("attributecode")),
							innerValueList);
					Map<String, Attribute> innerMap = new HashMap<>();
					innerMap.put(rs.getString("attributecode"), createAttributeObjectForMe(rs));
					serviceAttrib.put(rs.getString("code"), innerMap);
				}
			}
					serviceMap.put(rs.getString("code"), createServiceTypeObjectForMe(rs));
		}
		return new GrievanceType();
	}
	
	private Value createValueObjectForMe(ResultSet rs) {
		Value value = new Value();
		try {
			value.setKey(rs.getString("key"));
			value.setName(rs.getString("keyname"));
		} catch (Exception e) {
			LOGGER.error("Encountered an Exception while creating Value Object using Result Set " + e);
		}
		return value;
	}
	
	private Attribute createAttributeObjectForMe(ResultSet rs) {
		Attribute attr = new Attribute();
		try {
			attr.setCode(null == rs.getString("attributecode") || rs.getString("attributecode").equals("")? "" : rs.getString("attributecode"));
			attr.setDatatype(null == rs.getString("datatype") || rs.getString("datatype").equals("")? "" : rs.getString("datatype"));
			attr.setDatatypeDescription(null == rs.getString("datatypedescription") || rs.getString("datatypedescription").equals("")? "" : rs.getString("datatypedescription"));
			attr.setDescription(null == rs.getString("attrdescription") || rs.getString("attrdescription").equals("")? "" :rs.getString("attrdescription"));
			attr.setRequired(null == rs.getString("required") || rs.getString("required").equals("") ? false : rs.getString("required").equals("Y")? true : false);
			attr.setVariable(null == rs.getString("required") || rs.getString("required").equals("") ? false : rs.getString("variable").equals("Y")? true : false);
			attr.setGroupCode(null == rs.getString("groupcode") || rs.getString("groupcode").equals("")? "" : rs.getString("groupcode"));
		} catch (Exception e) {
			LOGGER.error("Encountered an Exception while creating Attribute Object using Result Set " + e);
		}
		return attr;
	}
	
	private GrievanceType createServiceTypeObjectForMe(ResultSet rs) {
		GrievanceType grievanceType = new GrievanceType();
		try { 
			grievanceType.setId(rs.getLong("id"));
			grievanceType.setServiceName(rs.getString("name"));
			grievanceType.setServiceCode(rs.getString("code"));
			grievanceType.setTenantId(rs.getString("tenantid"));
			grievanceType.setDescription(rs.getString("description"));
			grievanceType.setCategory(rs.getInt("category"));
			grievanceType.setHasFinancialImpact(rs.getBoolean("hasfinancialimpact"));
			grievanceType.setSlaHours(rs.getInt("slahours"));
			grievanceType.setActive(rs.getBoolean("isactive"));
		} catch (Exception e) {
			LOGGER.error("Encountered an Exception while creating Service Type Object using Result Set " + e);
		}
		return grievanceType;
	}
}

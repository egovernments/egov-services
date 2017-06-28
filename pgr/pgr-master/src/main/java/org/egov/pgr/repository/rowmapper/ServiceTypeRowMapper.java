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

import org.egov.pgr.model.Attribute;
import org.egov.pgr.model.ServiceType;
import org.egov.pgr.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ServiceTypeRowMapper implements RowMapper<ServiceType> {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ServiceTypeRowMapper.class);
	public Map<String, List<Value>> attribValue = new HashMap<>();
	public Map<String, Map<String, Attribute>> serviceAttrib = new HashMap<>();
	public Map<String, ServiceType> serviceMap = new HashMap<>();
	public static final String separator = ">";

	@Override
	public ServiceType mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		if (serviceMap.containsKey(rs.getString("code"))) {
			if (serviceAttrib.containsKey(rs.getString("code"))) {
				Map<String, Attribute> innerMap = serviceAttrib.get(rs.getString("code"));
				if (innerMap.containsKey(rs.getString("attributecode"))) {
					if (attribValue.containsKey(rs.getString("code").concat(separator + rs.getString("attributecode")))) {
						List<Value> innerValueList = attribValue.get(rs.getString("code").concat(separator + rs.getString("attributecode")));
						innerValueList.add(createValueObjectForMe(rs));
					} else {
						List<Value> innerValueList = new ArrayList<>();
						innerValueList.add(createValueObjectForMe(rs));
						attribValue.put(rs.getString("code").concat(separator + rs.getString("attributecode")),innerValueList);
					}
				} else {
					List<Value> innerValueList = new ArrayList<>();
					innerValueList.add(createValueObjectForMe(rs));
					attribValue.put(rs.getString("code").concat(separator + rs.getString("attributecode")),innerValueList);
					innerMap.put(rs.getString("attributecode"), createAttributeObjectForMe(rs));
				}
			} else {
				List<Value> innerValueList = new ArrayList<>();
				innerValueList.add(createValueObjectForMe(rs));
				attribValue.put(rs.getString("code").concat(separator + rs.getString("attributecode")), innerValueList);
				Map<String, Attribute> innerMap = new HashMap<>();
				innerMap.put(rs.getString("attributecode"), createAttributeObjectForMe(rs));
				serviceAttrib.put(rs.getString("code"), innerMap);
			}
		} else {
			List<Value> innerValueList = new ArrayList<>();
			innerValueList.add(createValueObjectForMe(rs));
			attribValue.put(rs.getString("code").concat(separator + rs.getString("attributecode")), innerValueList);
			Map<String, Attribute> innerMap = new HashMap<>();
			innerMap.put(rs.getString("attributecode"), createAttributeObjectForMe(rs));
			serviceAttrib.put(rs.getString("code"), innerMap);
			serviceMap.put(rs.getString("code"), createServiceTypeObjectForMe(rs));
		}
		return new ServiceType();
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
			attr.setCode(rs.getString("attributecode"));
			attr.setDatatype(rs.getString("datatype"));
			attr.setDatatypeDescription(rs.getString("datatypedescription"));
			attr.setDescription(rs.getString("description"));
			attr.setRequired(rs.getString("required").equals("Y")? true : false);
			attr.setVariable(rs.getString("variable").equals("Y")? true : false);
			attr.setGroupCode(rs.getString("groupcode"));
		} catch (Exception e) {
			LOGGER.error("Encountered an Exception while creating Attribute Object using Result Set " + e);
		}
		return attr;
	}
	
	private ServiceType createServiceTypeObjectForMe(ResultSet rs) {
		ServiceType serviceType = new ServiceType();
		try { 
			serviceType.setId(rs.getLong("id"));
			serviceType.setServiceName(rs.getString("name"));
			serviceType.setServiceCode(rs.getString("code"));
			serviceType.setTenantId(rs.getString("tenantid"));
			serviceType.setDescription(rs.getString("description"));
			serviceType.setCategory(rs.getInt("category"));
			serviceType.setHasFinancialImpact(rs.getBoolean("hasfinancialimpact"));
		} catch (Exception e) {
			LOGGER.error("Encountered an Exception while creating Service Type Object using Result Set " + e);
		}
		return serviceType;
	}
}

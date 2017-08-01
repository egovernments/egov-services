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
package org.egov.pgr.repository;

import java.sql.Date;
import java.util.List;

import org.egov.pgr.domain.model.OTPConfig;
import org.egov.pgr.repository.rowmapper.OTPConfigRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OTPConfigRepository {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(OTPConfigRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private OTPConfigRowMapper otpConfigMapper;
	
	public OTPConfig persistOTPConfig(final OTPConfig otpConfig) {
		LOGGER.info("OTP Config Create Object :" + otpConfig);
		String query = "INSERT into egpgr_otp_config (tenantid, enabled, createddate) values (?,?,?)";
		final Object[] obj = new Object[] { otpConfig.getTenantId(), otpConfig.isOtpConfigEnabled()? "Y" : "N", 
		new Date(new java.util.Date().getTime()) };
		jdbcTemplate.update(query,obj);
		return otpConfig;
	}
	
	public boolean updateOTPConfig(final OTPConfig otpConfig) {
		LOGGER.info("OTP Config Update Object: " + otpConfig);
		String query = "UPDATE egpgr_otp_config SET enabled = ?, lastmodifieddate = ? "
				+ "WHERE tenantid = ? " ; 
		final Object[] obj = new Object[] { otpConfig.isOtpConfigEnabled()? "Y" : "N", 
			new Date(new java.util.Date().getTime()), otpConfig.getTenantId() };
		int updateStatus = jdbcTemplate.update(query,obj);
		LOGGER.info("Status of Updated : " + updateStatus);
		if(updateStatus > 0){
			return true;
		}
		return false; 
	}
	
	public List<OTPConfig> getAllOtpConfig(List<String> tenantList) {
		LOGGER.info("Tenant ID Received ::" + tenantList);
		final String queryStr = "SELECT tenantid, enabled FROM egpgr_otp_config " + constructQuery(tenantList);
		final List<OTPConfig> otpConfigs = jdbcTemplate.query(queryStr,
				otpConfigMapper);
		return otpConfigs;
	}
	
	private String constructQuery(List<String> tenantList){
		final StringBuilder query = new StringBuilder();
		if(tenantList.size() == 1) {
			query.append("WHERE tenantid = '" + tenantList.get(0) + "'");
		} else if(tenantList.size() > 1){
			query.append("WHERE tenantid IN (");
			for(int i=0; i<tenantList.size(); i++) {
				query.append("'" + tenantList.get(i) + "'");
				if(i != tenantList.size()-1){
					query.append(",");
				}
			}
			query.append(")");
		}
		
		return query.toString();
	}

}

/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.lams.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.DueNotice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class DueNoticeRowMapper implements ResultSetExtractor<List<DueNotice>> {
	public static final Logger logger = LoggerFactory.getLogger(NoticeRowMapper.class);

	@Override
	public List<DueNotice> extractData(ResultSet rs) throws SQLException, DataAccessException {

		List<DueNotice> dueNotices = new ArrayList<>();
		while (rs.next()) {
			DueNotice dueNotice = new DueNotice();
			try {
				dueNotice.setId((Long) (rs.getObject("id")));
				dueNotice.setNoticeNo(rs.getString("noticeno"));
				dueNotice.setAgreementNumber(rs.getString("agreementnumber"));
				dueNotice.setAllotteeName(rs.getString("allotteename"));
				dueNotice.setAllotteeMobileNumber(rs.getString("mobilenumber"));
				dueNotice.setAssetCategory((Long) rs.getObject("assetcategory"));
				dueNotice.setDueFromDate(rs.getTimestamp("duefromdate"));
				dueNotice.setDueToDate(rs.getTimestamp("duetodate"));
				dueNotice.setAction(rs.getString("action"));
				dueNotice.setStatus(rs.getString("status"));
				dueNotice.setNoticeType(rs.getString("noticetype"));
				dueNotice.setTenantId(rs.getString("tenantid"));
				dueNotice.setFileStore(rs.getString("filestore"));

				dueNotices.add(dueNotice);
			} catch (Exception e) {
				logger.info("exception in notice RowMapper : " + e);
				throw new RuntimeException("error while mapping notices from reult set : " + e.getCause());
			}
		}
		return dueNotices;
	}

}

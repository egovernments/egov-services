/*    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.lams.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.City;
import org.egov.lams.model.enums.Action;
import org.egov.lams.repository.TenantRepository;
import org.egov.lams.web.contract.FinancialYearContract;
import org.egov.lams.web.contract.FinancialYearContractResponse;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NoticeNumberUtil {
	public static final Logger logger = LoggerFactory.getLogger(NoticeNumberUtil.class);

	public static final String NOTICE_NUMBER_SEQUENCE = "SELECT nextval('seq_eglams_noticeno')";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private TenantRepository tenantRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public String getNoticeNumber(Action noticeType, String tenantId, RequestInfo requestInfo) {

		String squenceQuery = NOTICE_NUMBER_SEQUENCE;
		StringBuilder noticeNumber = new StringBuilder();
		String sequenceNumber = "";
		String currnetFinYear;
		City city = tenantRepository.fetchTenantByCode(tenantId);
		if (city != null) {
			noticeNumber.append(city.getCode());
		}
		if (Action.CREATE.equals(noticeType)) {
			noticeNumber.append("/AN/");
		} else if (Action.RENEWAL.equals(noticeType)) {
			noticeNumber.append("/RN/");
		} else if (Action.CANCELLATION.equals(noticeType)) {
			noticeNumber.append("/CN/");
		} else if (Action.EVICTION.equals(noticeType)) {
			noticeNumber.append("/EN/");
		} else if (Action.OBJECTION.equals(noticeType)) {
			noticeNumber.append("/ON/");
		} else if (Action.JUDGEMENT.equals(noticeType)) {
			noticeNumber.append("/JN/");
		} else {
			noticeNumber.append("");
		}
		try {
			sequenceNumber = jdbcTemplate.queryForObject(squenceQuery, String.class);
		} catch (Exception ex) {

			logger.error("Erorr while generating notice number squence" + ex);
		}
		currnetFinYear = getCurrentFinYear(tenantId, requestInfo);
		noticeNumber.append(String.format("%06d", Integer.valueOf(sequenceNumber)));
		noticeNumber.append("/").append(currnetFinYear);
		logger.info("Notice Number :" + noticeNumber);
		return noticeNumber.toString().toUpperCase();
	}

	private String getCurrentFinYear(String tenantId, RequestInfo requestInfo) {
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");

		final String finYearSearchurl = propertiesManager.getFinancialServiceHostName()
				+ propertiesManager.getFinancialYearSearchPath() + "?tenantId =" + tenantId + "&asOnDate="
				+ date.format(format);
		final List<FinancialYearContract> financialYears = restTemplate.postForObject(finYearSearchurl,
				new RequestInfoWrapper(requestInfo), FinancialYearContractResponse.class).getFinancialYears();
		if (financialYears.isEmpty()) {
			return "";
		} else {
			return financialYears.get(0).getFinYearRange();
		}

	}

}

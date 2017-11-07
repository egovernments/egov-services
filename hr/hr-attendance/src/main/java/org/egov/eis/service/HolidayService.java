/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.eis.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.model.Holiday;
import org.egov.eis.service.helper.HolidaySearchURLHelper;
import org.egov.eis.web.contract.HolidayResponse;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HolidayService {

    @Autowired
    private HolidaySearchURLHelper holidaySearchURLHelper;

    @Autowired
    private RestTemplate restTemplate;

    public List<Holiday> getHolidays(final String tenantId, final RequestInfo requestInfo) {
        final String url = holidaySearchURLHelper.searchURL(tenantId);

        final RequestInfoWrapper wrapper = new RequestInfoWrapper();
        wrapper.setRequestInfo(requestInfo);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);

        final HolidayResponse holidayResponse = restTemplate.postForObject(url, request, HolidayResponse.class);

        return holidayResponse.getHoliday();
    }

    public List<Date> getHolidaysBetweenDates(final String tenantId, final RequestInfo requestInfo, Date fromDate, Date toDate) {

        final String url = holidaySearchURLHelper.getHolidaysBetweenDatesUrl(tenantId, fromDate, toDate);

        final RequestInfoWrapper wrapper = new RequestInfoWrapper();
        wrapper.setRequestInfo(requestInfo);

        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);

        final HolidayResponse holidayResponse = restTemplate.postForObject(url, wrapper, HolidayResponse.class);

        return holidayResponse.getHoliday().stream().map(holiday -> holiday.getApplicableOn()).collect(Collectors.toList());
    }


}
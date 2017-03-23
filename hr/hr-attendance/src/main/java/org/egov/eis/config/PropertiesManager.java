/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.empernments.org
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
 *  In case of any queries, you can reach eGovernments Foundation at contact@empernments.org.
 */

package org.egov.eis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PropertiesManager {

    @Value("${egov.services.common_masters_service.hostname}")
    private String commonMastersServiceHostName;

    @Value("${egov.services.common_masters_service.holiday.basepath}")
    private String commonMastersServiceHolidayBasePath;

    @Value("${egov.services.common_masters_service.holidays.searchpath}")
    private String commonMastersServiceHolidaysSearchPath;

    @Value("${egov.services.hr_employee_service.hostname}")
    private String hrEmployeeServiceHostName;

    @Value("${egov.services.hr_employee_service.employees.basepath}")
    private String hrEmployeeServiceEmployeesBasePath;

    @Value("${egov.services.hr_employee_service.employees.searchpath}")
    private String hrEmployeeServiceEmployeesSearchPath;

    @Value("${egov.services.hr_masters_service.hostname}")
    private String hrMastersServiceHostName;

    @Value("${egov.services.hr_masters_service.hrconfiguration.basepath}")
    private String hrMastersServiceHRConfigurationBasePath;

    @Value("${egov.services.hr_masters_service.hrconfigurations.searchpath}")
    private String hrMastersServiceConfigurationsSearchPath;

    @Value("${egov.services.hr_masters_service.hrconfigurations.key}")
    private String hrMastersServiceConfigurationsKey;

    @Value("${egov.services.hr_masters_service.hrconfigurations.fivedayweek}")
    private String hrMastersServiceConfigurationsFiveDayWeek;

    @Value("${egov.services.hr_masters_service.hrconfigurations.sixdayweek}")
    private String hrMastersServiceConfigurationsSixDayWeek;

    @Value("${egov.services.hr_masters_service.hrconfigurations.fivedaywithsecondsaturday}")
    private String hrMastersServiceConfigurationsFiveDayWithSecondSaturday;

    @Value("${egov.services.hr_masters_service.hrconfigurations.fivedaywithsecondandfourthsaturday}")
    private String hrMastersServiceConfigurationsFiveDayWithSecondAndFourthSaturday;

}
package org.egov.eis.service.helper;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.web.contract.AttendanceGetRequest;
import org.egov.eis.web.contract.AttendanceReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component
public class EmployeeSearchURLHelper {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private PropertiesManager propertiesManager;

    public String searchURL(final Long id, final String tenantId) {
        final String BASE_URL = propertiesManager.getHrEmployeeServiceHostName()
                + propertiesManager.getHrEmployeeServiceEmployeesBasePath()
                + propertiesManager.getHrEmployeeServiceEmployeesSearchPath();
        final StringBuilder searchURL = new StringBuilder(BASE_URL + "?");

        if (tenantId == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + tenantId);

        if (id != null)
            searchURL.append("&id=" + id);

        searchURL.append("&pageSize=" + applicationProperties.attendanceSearchPageSizeMax());

        return searchURL.toString();
    }

    public String searchURLForReport(AttendanceReportRequest attendanceReportRequest) {
        final String BASE_URL = propertiesManager.getHrEmployeeServiceHostName()
                + propertiesManager.getHrEmployeeServiceEmployeesBasePath()
                + propertiesManager.getHrEmployeeServiceEmployeesSearchPath();
        final StringBuilder searchURL = new StringBuilder(BASE_URL + "?");

        if (attendanceReportRequest.getTenantId() == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + attendanceReportRequest.getTenantId() + "&isPrimary=true");

        if (!isEmpty(attendanceReportRequest.getCode()))
            searchURL.append("&code=" + attendanceReportRequest.getCode());

        if(attendanceReportRequest.getDesignationId()!=null && !attendanceReportRequest.getDesignationId().equals(""))
            searchURL.append("&designationId=" + attendanceReportRequest.getDesignationId());

        if(attendanceReportRequest.getDepartmentId()!=null && !attendanceReportRequest.getDepartmentId().equals(""))
            searchURL.append("&departmentId=" + attendanceReportRequest.getDepartmentId());

        if(attendanceReportRequest.getEmployeeType()!=null && !attendanceReportRequest.getEmployeeType().equals(""))
            searchURL.append("&employeeType=" + attendanceReportRequest.getEmployeeType());


        searchURL.append("&pageSize=" + applicationProperties.attendanceSearchPageSizeMax());

        return searchURL.toString();
    }
}
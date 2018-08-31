package org.egov.eis.service.helper;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class PositionSearchURLHelper {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private PropertiesManager propertiesManager;

    public String searchURL(final Date asOnDate, final Date toDate, final Long departmentId, final Long designationId, final String tenantId, final String destinationTenant) {
        final String baseUrl = propertiesManager.getHrEmployeeServiceHostName()
                + propertiesManager.getHrEmployeeServiceVacantPositionsBasePath()
                + propertiesManager.getHrEmployeeServiceEmployeesSearchPath();
        final StringBuilder searchURL = new StringBuilder(baseUrl + "?");
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        if (tenantId == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + tenantId);

        if (asOnDate != null)
            searchURL.append("&fromDate=" + sdf.format(asOnDate));

        if (toDate != null)
            searchURL.append("&toDate=" + sdf.format(toDate));

        if (designationId != null)
            searchURL.append("&designationId=" + designationId);

        if (departmentId != null)
            searchURL.append("&departmentId=" + departmentId);

        if (destinationTenant != null)
            searchURL.append("&destinationTenant=" + destinationTenant);

        searchURL.append("&pageSize=" + applicationProperties.employeeMovementSearchPageSizeMax());

        return searchURL.toString();
    }
}
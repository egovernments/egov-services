package org.egov.eis.service.helper;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeSearchURLHelper {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private PropertiesManager propertiesManager;

    public String searchURL(final Long id, final String tenantId) {
        final String baseUrl = propertiesManager.getHrEmployeeServiceHostName()
                + propertiesManager.getHrEmployeeServiceEmployeesBasePath()
                + "/" + id
                + propertiesManager.getHrEmployeeServiceEmployeesSearchPath();
        final StringBuilder searchURL = new StringBuilder(baseUrl + "?");

        if (tenantId == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + tenantId);

        if (id != null)
            searchURL.append("&id=" + id);

        searchURL.append("&pageSize=" + applicationProperties.employeeMovementSearchPageSizeMax());

        return searchURL.toString();
    }

    public String updateURL(final String tenantId) {
        final String baseUrl = propertiesManager.getHrEmployeeServiceHostName()
                + propertiesManager.getHrEmployeeServiceEmployeesBasePath()
                + propertiesManager.getHrEmployeeServiceEmployeesUpdatePath();
        final StringBuilder searchURL = new StringBuilder(baseUrl + "?");

        if (tenantId == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + tenantId);

        return searchURL.toString();
    }
}
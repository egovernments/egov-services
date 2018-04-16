package org.egov.eis.service.helper;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HRConfigurationSearchURLHelper {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private PropertiesManager propertiesManager;

    public String searchURL(final String tenantId) {
        final String BASE_URL = propertiesManager.getHrMastersServiceHostName()
                + propertiesManager.getHrMastersServiceHRConfigurationBasePath()
                + propertiesManager.getHrMastersServiceConfigurationsSearchPath();
        final StringBuilder searchURL = new StringBuilder(BASE_URL + "?");

        if (tenantId == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + tenantId);

        searchURL.append("&name=" + propertiesManager.getHrMastersServiceConfigurationsKey());

        searchURL.append("&pageSize=" + applicationProperties.attendanceSearchPageSizeMax());

        return searchURL.toString();
    }
}
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

    public StringBuilder searchURL(final String tenantId) {
        final String BASE_URL = propertiesManager.getHrMastersServiceHostName()
                + propertiesManager.getHrMastersServiceHRConfigurationBasePath()
                + propertiesManager.getHrMastersServiceConfigurationsSearchPath();
        final StringBuilder searchURL = new StringBuilder(BASE_URL + "?");

        if (tenantId == null)
            return searchURL;
        else
            searchURL.append("tenantId=" + tenantId);
        searchURL.append("&pageSize=" + applicationProperties.hrLeaveSearchPageSizeMax());

        return searchURL;
    }

    public String cuttOffDateSearchURL(final String tenantId) {
        StringBuilder url = searchURL(tenantId);
        url.append("&name=" + propertiesManager.getHrMastersServiceConfigurationsKey());
        return url.toString();
    }

    public String compensatoryDaysSearchURL(final String tenantId) {
        StringBuilder url = searchURL(tenantId);
        url.append("&name=" + propertiesManager.getHrMastersServiceCompensatoryConfigurationKey());
        return url.toString();
    }
    
    public String weeklyHolidaysSearchURL(final String tenantId) {
        StringBuilder url = searchURL(tenantId);
        url.append("&name=" + propertiesManager.getHrMastersServiceConfigurationsWeeklyHolidayKey());
        return url.toString();
    }
}
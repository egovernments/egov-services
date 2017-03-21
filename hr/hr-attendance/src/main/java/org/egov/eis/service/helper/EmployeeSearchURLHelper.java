package org.egov.eis.service.helper;

import org.egov.eis.config.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeSearchURLHelper {

    @Autowired
    private ApplicationProperties applicationProperties;

    public String searchURL(final Long id, final String tenantId) {
        final String BASE_URL = applicationProperties.attendanceServicesHREmployeeServiceSearchEmployeeHostURL();
        final StringBuilder searchURL = new StringBuilder(BASE_URL + "?");
        boolean isAppendAndSeperator = false;

        if (tenantId == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + tenantId);

        if (id != null) {
            isAppendAndSeperator = addAndIfRequired(isAppendAndSeperator, searchURL);
            searchURL.append("id=" + id);
        }

        searchURL.append("&pageSize=" + applicationProperties.attendanceSearchPageSizeMax());

        return searchURL.toString();
    }

    /**
     * This method is always called at the beginning of the method so that & is prepended before the next parameter is appended.
     *
     * @param appendAndFlag
     * @param searchURL
     * @return boolean indicates if the next parameter should append an "&"
     */
    private boolean addAndIfRequired(final boolean appendAndSeperatorFlag, final StringBuilder searchURL) {
        if (appendAndSeperatorFlag)
            searchURL.append("&");

        return true;
    }
}
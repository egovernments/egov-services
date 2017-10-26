package org.egov.eis.service.helper;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class AttendanceSearchURLHelper {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private PropertiesManager propertiesManager;

    private static String getCommaSeperatedIds(List<Long> idList) {
        if (idList.isEmpty())
            return "";

        StringBuilder query = new StringBuilder(idList.get(0).toString());
        for (int i = 1; i < idList.size(); i++) {
            query.append("," + idList.get(i));
        }

        return query.toString();
    }

    public String searchURL(final String tenantId, final Date validfromDate, List<Long> employeeIds, final String url) {
        final StringBuilder searchURL = new StringBuilder(url + "?");

        if (tenantId == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + tenantId);

        if (!employeeIds.isEmpty() && null != employeeIds)
            searchURL.append("&employeeIds=" + getCommaSeperatedIds(employeeIds));

        if (validfromDate != null && !validfromDate.equals("")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String fromDate = simpleDateFormat.format(validfromDate);
            searchURL.append("&fromDate=" + fromDate);
        }


        searchURL.append("&pageSize=" + applicationProperties.hrLeaveSearchPageSizeMax());

        return searchURL.toString();
    }

}
package org.egov.eis.service.helper;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class HolidaySearchURLHelper {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private PropertiesManager propertiesManager;

    public String searchURL(final String tenantId) {
        final String BASE_URL = propertiesManager.getCommonMastersServiceHostName()
                + propertiesManager.getCommonMastersServiceHolidayBasePath()
                + propertiesManager.getCommonMastersServiceHolidaysSearchPath();
        final StringBuilder searchURL = new StringBuilder(BASE_URL + "?");

        if (tenantId == null)
            return searchURL.toString();
        else
            searchURL.append("tenantId=" + tenantId);

        searchURL.append("&pageSize=" + applicationProperties.attendanceSearchPageSizeMax());

        return searchURL.toString();
    }

/*    public String searchByDateURL(final String tenantId, Date validFromdate) {
        String url = searchURL(tenantId);
        final StringBuilder searchURL = new StringBuilder(url);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fromDate = simpleDateFormat.format(validFromdate);
        searchURL.append("&fromDate=" + fromDate);
        String toDate = simpleDateFormat.format(new Date());
        searchURL.append("&toDate=" + toDate);

        return searchURL.toString();
    }*/

    public String getHolidaysBetweenDatesUrl(final String tenantId, Date fromdate, Date todate) {
        String url = searchURL(tenantId);
        final StringBuilder searchURL = new StringBuilder(url);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fromDate = simpleDateFormat.format(fromdate);
        searchURL.append("&fromDate=" + fromDate);
        String toDate = simpleDateFormat.format(todate);
        searchURL.append("&toDate=" + toDate);

        return searchURL.toString();
    }


}
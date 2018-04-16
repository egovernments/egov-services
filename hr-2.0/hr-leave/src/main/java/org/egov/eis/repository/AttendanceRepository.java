package org.egov.eis.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.service.helper.AttendanceSearchURLHelper;
import org.egov.eis.web.contract.AttendanceResponse;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Service
public class AttendanceRepository {

    private final RestTemplate restTemplate;
    private final String attendanceHost;
    private final String attendanceServiceUrl;

    @Autowired
    private AttendanceSearchURLHelper attendanceSearchURLHelper;

    public AttendanceRepository(final RestTemplate restTemplate,
                                @Value("${egov.services.hr_attendance.host}") final String attendanceServiceHost,
                                @Value("${egov.services.hr_attendance.searchpath}") final String attendanceServiceUrl) {
        this.restTemplate = restTemplate;
        this.attendanceHost = attendanceServiceHost;
        this.attendanceServiceUrl = attendanceServiceUrl;

    }

    public AttendanceResponse getAttendance(final String tenantId, final Date validFromDate, final List<Long> employeeIds, RequestInfo requestInfo) {
        String url = String.format("%s%s", attendanceHost, attendanceServiceUrl);
        final String searchUrl = attendanceSearchURLHelper.searchURL(tenantId, validFromDate, employeeIds, url);
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        return restTemplate.postForObject(searchUrl, requestInfoWrapper, AttendanceResponse.class);
    }


}
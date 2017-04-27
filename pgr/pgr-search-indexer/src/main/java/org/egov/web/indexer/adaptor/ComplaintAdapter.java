package org.egov.web.indexer.adaptor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.time.DateUtils;
import org.egov.web.indexer.contract.Assignment;
import org.egov.web.indexer.contract.Boundary;
import org.egov.web.indexer.contract.City;
import org.egov.web.indexer.contract.ComplaintType;
import org.egov.web.indexer.contract.Employee;
import org.egov.web.indexer.contract.ServiceRequest;
import org.egov.web.indexer.repository.BoundaryRepository;
import org.egov.web.indexer.repository.ComplaintTypeRepository;
import org.egov.web.indexer.repository.EmployeeRepository;
import org.egov.web.indexer.repository.TenantRepository;
import org.egov.web.indexer.repository.contract.ComplaintIndex;
import org.egov.web.indexer.repository.contract.GeoPoint;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class ComplaintAdapter {

    // private static final String NOASSIGNMENT = "NO ASSIGNMENT";
    private static final String COMPLETED = "COMPLETED";
    private static final String WITHDRAWN = "WITHDRAWN";
    private static final String REJECTED = "REJECTED";
    private static final String PROCESSING = "PROCESSING";
    private static final String FORWARDED = "FORWARDED";
    private static final String REGISTERED = "REGISTERED";
    private static final String REOPENED = "REOPENED";

    // private IndexerProperties propertiesManager;
    private BoundaryRepository boundaryRepository;
    private ComplaintTypeRepository complaintTypeRepository;
    private EmployeeRepository employeeRepository;
    private TenantRepository tenantRepository;

    public ComplaintAdapter(TenantRepository tenantRepository, BoundaryRepository boundaryRepository,
            ComplaintTypeRepository complaintTypeRepository, EmployeeRepository employeeRepository) {
        // this.propertiesManager = propertiesManager;
        this.boundaryRepository = boundaryRepository;
        this.complaintTypeRepository = complaintTypeRepository;
        this.employeeRepository = employeeRepository;
        this.tenantRepository = tenantRepository;
    }

    public ComplaintIndex indexOnCreate(ServiceRequest serviceRequest) {
        ComplaintIndex complaintIndex = new ComplaintIndex();
        setCommonDetails(complaintIndex, serviceRequest);
        // Default values set
        complaintIndex.setClosed(false);
        complaintIndex.setComplaintIsClosed("N");
        complaintIndex.setComplaintDuration(0);
        complaintIndex.setDurationRange("");
        complaintIndex.setComplaintPeriod(0);
        complaintIndex.setIsSLA("Y");
        complaintIndex.setRegistered(1);
        complaintIndex.setInProcess(1);
        complaintIndex.setAddressed(0);
        complaintIndex.setRejected(0);
        complaintIndex.setReOpened(0);
        complaintIndex.setComplaintAgeingdaysFromDue(0);
        return complaintIndex;
    }

    public ComplaintIndex indexOnUpdate(ServiceRequest serviceRequest) {
        ComplaintIndex complaintIndex = new ComplaintIndex();
        setCommonDetails(complaintIndex, serviceRequest);
        if (serviceRequest != null && !serviceRequest.getValues().isEmpty()) {
            String status = serviceRequest.getValues().get("status");
            // Update complaint level index variables
            if (status != null && !status.isEmpty() && (status.equalsIgnoreCase(COMPLETED)
                    || status.equalsIgnoreCase(WITHDRAWN) || status.equalsIgnoreCase(REJECTED))) {
                complaintIndex.setClosed(true);
                complaintIndex.setComplaintIsClosed("Y");
                if (complaintIndex.getCreatedDate() != null) {
                    final long duration = Math.abs(complaintIndex.getCreatedDate().getTime() - new Date().getTime())
                            / (24 * 60 * 60 * 1000);
                    complaintIndex.setComplaintDuration(duration);
                    if (duration < 3)
                        complaintIndex.setDurationRange("(<3 days)");
                    else if (duration < 7)
                        complaintIndex.setDurationRange("(3-7 days)");
                    else if (duration < 15)
                        complaintIndex.setDurationRange("(8-15 days)");
                    else if (duration < 30)
                        complaintIndex.setDurationRange("(16-30 days)");
                    else
                        complaintIndex.setDurationRange("(>30 days)");
                }
            } else {
                complaintIndex.setClosed(false);
                complaintIndex.setComplaintIsClosed("N");
                complaintIndex.setComplaintDuration(0);
                complaintIndex.setDurationRange("");
            }
            // update status related fields in index
            updateComplaintIndexStatusRelatedFields(status, complaintIndex);
        }
        if (complaintIndex.getCreatedDate() != null) {
            final long days = Math.abs(new Date().getTime() - complaintIndex.getCreatedDate().getTime())
                    / (24 * 60 * 60 * 1000);
            complaintIndex.setComplaintPeriod(days);
            Date lastDateToResolve = DateUtils.addHours(complaintIndex.getCreatedDate(),
                    (int) complaintIndex.getComplaintSLADays());
            final Date currentDate = new Date();
            // If difference is greater than 0 then complaint is within SLA
            if (lastDateToResolve.getTime() - currentDate.getTime() >= 0) {
                complaintIndex.setComplaintAgeingdaysFromDue(0);
                complaintIndex.setIsSLA("Y");
            } else {
                final long ageingDueDays = Math.abs(currentDate.getTime() - lastDateToResolve.getTime())
                        / (24 * 60 * 60 * 1000);
                complaintIndex.setComplaintAgeingdaysFromDue(ageingDueDays);
                complaintIndex.setIsSLA("N");
            }
        }
        return complaintIndex;
    }

    public ComplaintIndex updateComplaintIndexStatusRelatedFields(String status, final ComplaintIndex complaintIndex) {
        if (status != null && !status.isEmpty()) {
            if (status.equalsIgnoreCase(PROCESSING) || status.equalsIgnoreCase(FORWARDED)
                    || status.equalsIgnoreCase(REGISTERED)) {
                complaintIndex.setInProcess(1);
                complaintIndex.setAddressed(0);
                complaintIndex.setRejected(0);
            }
            if (status.equalsIgnoreCase(COMPLETED) || status.equalsIgnoreCase(WITHDRAWN)) {
                complaintIndex.setInProcess(0);
                complaintIndex.setAddressed(1);
                complaintIndex.setRejected(0);
            }
            if (status.equalsIgnoreCase(REJECTED)) {
                complaintIndex.setInProcess(0);
                complaintIndex.setAddressed(0);
                complaintIndex.setRejected(1);
            }
            if (status.equalsIgnoreCase(REOPENED)) {
                complaintIndex.setInProcess(1);
                complaintIndex.setAddressed(0);
                complaintIndex.setRejected(0);
                complaintIndex.setReOpened(1);
            }
        }
        return complaintIndex;
    }

    public void setCommonDetails(ComplaintIndex complaintIndex, ServiceRequest serviceRequest) {
        // Reading from serviceRequest
        complaintIndex.setCrn(serviceRequest.getCrn());
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            if (serviceRequest.getCreatedDate() != null)
                complaintIndex.setCreatedDate(formatter.parse(serviceRequest.getCreatedDate()));
            if (serviceRequest.getEscalationDate() != null)
                complaintIndex.setEscalationDate(formatter.parse(serviceRequest.getEscalationDate()));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        complaintIndex.setDetails(serviceRequest.getDetails());
        complaintIndex.setLandmarkDetails(serviceRequest.getLandmarkDetails());
        complaintIndex.setComplainantName(serviceRequest.getFirstName().concat(" "));
        complaintIndex.setComplainantMobile(serviceRequest.getPhone());
        complaintIndex.setComplainantEmail(serviceRequest.getEmail());
        complaintIndex.setComplaintTypeName(serviceRequest.getComplaintTypeName());
        complaintIndex.setComplaintTypeCode(serviceRequest.getComplaintTypeCode());

        InitializeComplaintTypeDetails(complaintIndex, serviceRequest.getComplaintTypeCode(), serviceRequest.getLat(),
                serviceRequest.getLng(), serviceRequest.getTenantId());

        // Reading from serviceRequest values map
        Map<String, String> values = serviceRequest.getValues();
        complaintIndex.setComplaintStatusName(values.get("status"));
        complaintIndex.setReceivingMode(values.get("receivingMode"));
        /*
         * complaintIndex.setSatisfactionIndex(Double.valueOf(values.get(
         * "citizenFeedback")));
         * complaintIndex.setSource(propertiesManager.getProperty(String.format(
         * "complaint.source.%s.%s", values.get("userType").toLowerCase(),
         * values.get("receivingMode").toLowerCase())));
         * complaintIndex.setSource(propertiesManager.getProperty(
         * "complaint.source.citizen.website"));
         * InitializeCityDetails(complaintIndex, values.get("tenantId"));
         */
        InitializeCityDetails(complaintIndex,serviceRequest.getTenantId());
        InitializeBoundaryDetails(complaintIndex, values.get("locationId"), values.get("child_location_id"),
                serviceRequest.getTenantId());
        InitializeEmployeeDetails(complaintIndex, values.get("assignment_id"), serviceRequest.getTenantId());

    }

    public void InitializeComplaintTypeDetails(ComplaintIndex complaintIndex, String complaintTypeCode, Double lat,
            Double lng, String tenantId) {
        if (complaintTypeCode != null && !complaintTypeCode.isEmpty()) {
            ComplaintType ctype = complaintTypeRepository.fetchComplaintTypeByCode(complaintTypeCode, tenantId);
            if (Objects.nonNull(lat) && Objects.nonNull(lng)) {
                complaintIndex.setComplaintSLADays(ctype.getSlaHours());
                complaintIndex.setComplaintGeo(new GeoPoint(lat, lng));
            }
        }
    }

    public void InitializeBoundaryDetails(ComplaintIndex complaintIndex, String locationId, String childLocationId,
            String tenantId) {
        Boundary boundary = null;
        if (locationId != null && !locationId.isEmpty()) {
            boundary = boundaryRepository.fetchBoundaryById(Long.valueOf(locationId), tenantId);
            if (Objects.nonNull(boundary)) {
                complaintIndex.setWardName(boundary.getName());
                complaintIndex.setWardNo(boundary.getBoundaryNum().toString());
                if (Objects.nonNull(boundary.getLongitude()) && Objects.nonNull(boundary.getLatitude()))
                    complaintIndex.setWardGeo(new GeoPoint(boundary.getLatitude(), boundary.getLongitude()));
            }
        }
        if (childLocationId != null && !childLocationId.isEmpty()) {
            boundary = boundaryRepository.fetchBoundaryById(Long.valueOf(childLocationId), tenantId);
            if (Objects.nonNull(boundary)) {
                complaintIndex.setLocalityName(boundary.getName());
                complaintIndex.setLocalityNo(boundary.getBoundaryNum().toString());
                if (Objects.nonNull(boundary.getLongitude()) && Objects.nonNull(boundary.getLatitude()))
                    complaintIndex.setLocalityGeo(new GeoPoint(boundary.getLatitude(), boundary.getLongitude()));
            }
        }
    }

    public void InitializeCityDetails(ComplaintIndex complaintIndex, String tenant) {
        City city = tenantRepository.fetchTenantByCode(tenant);
        if (city != null) {
            complaintIndex.setCityCode(city.getCode());
            complaintIndex.setCityDistrictCode(city.getDistrictCode());
            complaintIndex.setCityDistrictName(city.getDistrictName());
            //complaintIndex.setCityGrade(city.getGrade());
           // complaintIndex.setCityDomainUrl(city.getDomainURL());
            complaintIndex.setCityName(city.getName());
            complaintIndex.setCityRegionName(city.getRegionName());
        }
    }

    public void InitializeEmployeeDetails(ComplaintIndex complaintIndex, String assignmentId, String tenantId) {
        if (assignmentId != null && !assignmentId.isEmpty()) {
            complaintIndex.setAssigneeId(Long.valueOf(assignmentId));
            Employee employee = employeeRepository.fetchEmployeeByPositionId(Long.valueOf(assignmentId),
                    new LocalDate(), tenantId);
            if (employee != null) {
                complaintIndex.setAssigneeName(employee.getName());
                if (!employee.getAssignments().isEmpty()) {
                    Assignment assignment = employee.getAssignments().get(0);
                    complaintIndex.setDepartmentName(assignment.getDepartment().getName());
                    complaintIndex.setDepartmentCode(assignment.getDepartment().getCode());
                }
            }
        }
    }
}
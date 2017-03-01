package org.egov.web.indexer.adaptor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.egov.web.indexer.config.IndexerProperties;
import org.egov.web.indexer.contract.Assignment;
import org.egov.web.indexer.contract.Boundary;
import org.egov.web.indexer.contract.City;
import org.egov.web.indexer.contract.ComplaintType;
import org.egov.web.indexer.contract.Employee;
import org.egov.web.indexer.contract.ServiceRequest;
import org.egov.web.indexer.repository.BoundaryRepository;
import org.egov.web.indexer.repository.CityRepository;
import org.egov.web.indexer.repository.ComplaintTypeRepository;
import org.egov.web.indexer.repository.EmployeeRepository;
import org.egov.web.indexer.repository.contract.ComplaintIndex;
import org.egov.web.indexer.repository.contract.GeoPoint;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class ComplaintAdapter {

	private static final String NOASSIGNMENT = "NO ASSIGNMENT";

	private IndexerProperties propertiesManager;
	private BoundaryRepository boundaryRepository;
	private ComplaintTypeRepository complaintTypeRepository;
	private CityRepository cityRepository;
	private EmployeeRepository employeeRepository;

	public ComplaintAdapter(IndexerProperties propertiesManager, BoundaryRepository boundaryRepository,
			ComplaintTypeRepository complaintTypeRepository, CityRepository cityRepository,
			EmployeeRepository employeeRepository) {
		this.propertiesManager = propertiesManager;
		this.boundaryRepository = boundaryRepository;
		this.complaintTypeRepository = complaintTypeRepository;
		this.cityRepository = cityRepository;
		this.employeeRepository = employeeRepository;
	}

	public ComplaintIndex indexOnCreate(ServiceRequest serviceRequest) {
		ComplaintIndex complaintIndex = new ComplaintIndex();
		// Reading from serviceRequest
		complaintIndex.setCrn(serviceRequest.getCrn());
		try {
			// TODO : Need to handle UTC time zone (ex:2016-10-21T11:28:56.603Z)
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			complaintIndex.setCreatedDate(formatter.parse(serviceRequest.getCreatedDate()));
			complaintIndex.setEscalationDate(formatter.parse(serviceRequest.getEscalationDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		complaintIndex.setDetails(serviceRequest.getDetails());
		complaintIndex.setLandmarkDetails(serviceRequest.getLandmarkDetails());
		complaintIndex
				.setComplainantName(serviceRequest.getFirstName().concat(" ").concat(serviceRequest.getLastName()));
		complaintIndex.setComplainantMobile(serviceRequest.getPhone());
		complaintIndex.setComplainantEmail(serviceRequest.getEmail());
		complaintIndex.setComplaintTypeName(serviceRequest.getComplaintTypeName());
		complaintIndex.setComplaintTypeCode(serviceRequest.getComplaintTypeCode());

		InitializeComplaintTypeDetails(complaintIndex, serviceRequest.getComplaintTypeCode(), serviceRequest.getLat(),
				serviceRequest.getLng());

		// Reading from serviceRequest values map
		Map<String, String> values = serviceRequest.getValues();
		complaintIndex.setComplaintStatusName(values.get("complaintStatus"));
		complaintIndex.setSatisfactionIndex(Double.valueOf(values.get("citizenFeedback")));

		complaintIndex.setInitialFunctionarySLADays(Long.valueOf(values.get("escalationHours")));
		complaintIndex.setCurrentFunctionarySLADays(Long.valueOf(values.get("escalationHours")));

		complaintIndex.setSource(propertiesManager.getProperty(String.format("complaint.source.%s.%s",
				values.get("userType").toLowerCase(), values.get("receivingMode").toLowerCase())));
		InitializeBoundaryDetails(complaintIndex, values.get("locationId"), values.get("childLocationId"));
		InitializeCityDetails(complaintIndex, values.get("tenantId"));
		InitializeEmployeeDetails(complaintIndex, values.get("assignmentId"), values.get("tenantId"));

		// Default values set
		complaintIndex.setClosed(false);
		complaintIndex.setComplaintIsClosed("N");
		complaintIndex.setIfClosed(0);
		complaintIndex.setComplaintDuration(0);
		complaintIndex.setDurationRange("");
		complaintIndex.setComplaintPeriod(0);
		complaintIndex.setComplaintAgeingFromDue(0);
		complaintIndex.setIsSLA("Y");
		complaintIndex.setIfSLA(1);
		complaintIndex.setInitialFunctionaryAssigneddate(new Date());
		complaintIndex.setInitialFunctionaryAgeingFromDue(0);
		complaintIndex.setInitialFunctionaryIsSLA("Y");
		complaintIndex.setInitialFunctionaryIfSLA(1);
		complaintIndex.setCurrentFunctionaryAssigneddate(new Date());
		complaintIndex.setCurrentFunctionaryAgeingFromDue(0);
		complaintIndex.setCurrentFunctionaryIsSLA("Y");
		complaintIndex.setCurrentFunctionaryIfSLA(1);
		complaintIndex.setEscalationLevel(0);
		complaintIndex.setReasonForRejection("");
		complaintIndex.setRegistered(1);
		complaintIndex.setInProcess(1);
		complaintIndex.setAddressed(0);
		complaintIndex.setRejected(0);
		complaintIndex.setReOpened(0);
		complaintIndex.setComplaintAgeingdaysFromDue(0);
		return complaintIndex;
	}

	public void InitializeComplaintTypeDetails(ComplaintIndex complaintIndex, String complaintTypeCode, Double lat,
			Double lng) {
		ComplaintType ctype = complaintTypeRepository.fetchComplaintTypeByCode(complaintTypeCode);
		if (Objects.nonNull(lat) && Objects.nonNull(lng)) {
			complaintIndex.setComplaintSLADays(ctype.getSlaHours());
			complaintIndex.setComplaintGeo(new GeoPoint(lat, lng));
		}
	}

	public void InitializeBoundaryDetails(ComplaintIndex complaintIndex, String locationId, String childLocationId) {
		Boundary boundary = boundaryRepository.fetchBoundaryById(Long.valueOf(locationId));
		if (Objects.nonNull(boundary)) {
			complaintIndex.setWardName(boundary.getName());
			complaintIndex.setWardNo(boundary.getBoundaryNum().toString());
			if (Objects.nonNull(boundary.getLongitude()) && Objects.nonNull(boundary.getLatitude()))
				complaintIndex.setWardGeo(new GeoPoint(boundary.getLatitude(), boundary.getLongitude()));
		}
		boundary = boundaryRepository.fetchBoundaryById(Long.valueOf(childLocationId));
		if (Objects.nonNull(boundary)) {
			complaintIndex.setLocalityName(boundary.getName());
			complaintIndex.setLocalityNo(boundary.getBoundaryNum().toString());
			if (Objects.nonNull(boundary.getLongitude()) && Objects.nonNull(boundary.getLatitude()))
				complaintIndex.setLocalityGeo(new GeoPoint(boundary.getLatitude(), boundary.getLongitude()));
		}
	}

	public void InitializeCityDetails(ComplaintIndex complaintIndex, String tenantId) {
		City city = cityRepository.fetchCityById(Long.valueOf(tenantId));
		if (city != null) {
			complaintIndex.setCityCode(city.getCode());
			complaintIndex.setCityDistrictCode(city.getDistrictCode());
			complaintIndex.setCityDistrictName(city.getDistrictName());
			complaintIndex.setCityGrade(city.getGrade());
			complaintIndex.setCityDomainUrl(city.getDomainURL());
			complaintIndex.setCityName(city.getName());
			complaintIndex.setCityRegionName(city.getRegionName());
		}
	}

	public void InitializeEmployeeDetails(ComplaintIndex complaintIndex, String assignmentId, String tenantId) {
		complaintIndex.setAssigneeId(Long.valueOf(assignmentId));
		Employee employee = employeeRepository.fetchEmployeeByPositionId(Long.valueOf(assignmentId), new LocalDate(),
				tenantId);
		if (employee != null) {
			complaintIndex.setAssigneeName(employee.getName());
			complaintIndex.setCurrentFunctionaryMobileNumber(
					employee.getMobileNumber() != null ? employee.getMobileNumber() : "");
			complaintIndex.setInitialFunctionaryMobileNumber(
					employee.getMobileNumber() != null ? employee.getMobileNumber() : "");
			if (!employee.getAssignments().isEmpty()) {
				Assignment assignment = employee.getAssignments().get(0);
				complaintIndex.setDepartmentName(assignment.getDepartment().getName());
				complaintIndex.setDepartmentCode(assignment.getDepartment().getCode());
				complaintIndex
						.setInitialFunctionaryName(employee.getName() + " : " + assignment.getDesignation().getName()
								+ " : " + NOASSIGNMENT + " : " + assignment.getDesignation().getName());
				complaintIndex
						.setCurrentFunctionaryName(employee.getName() + " : " + assignment.getDesignation().getName()
								+ " : " + NOASSIGNMENT + " : " + assignment.getDesignation().getName());
			}

		}
	}
}
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
import org.egov.web.indexer.contract.ServiceRequest;
import org.egov.web.indexer.repository.AssignmentRepository;
import org.egov.web.indexer.repository.BoundaryRepository;
import org.egov.web.indexer.repository.CityRepository;
import org.egov.web.indexer.repository.ComplaintTypeRepository;
import org.egov.web.indexer.repository.contract.ComplaintIndex;
import org.egov.web.indexer.repository.contract.GeoPoint;
import org.springframework.stereotype.Service;

@Service
public class ComplaintAdapter {

	private static final String NOASSIGNMENT = "NO ASSIGNMENT";

	private IndexerProperties propertiesManager;
	private BoundaryRepository boundaryRepository;
	private ComplaintTypeRepository complaintTypeRepository;
	private CityRepository cityRepository;
	private AssignmentRepository assignmentRepository;

	public ComplaintAdapter(IndexerProperties propertiesManager, BoundaryRepository boundaryRepository,
			ComplaintTypeRepository complaintTypeRepository, CityRepository cityRepository,
			AssignmentRepository assignmentRepository) {
		this.propertiesManager = propertiesManager;
		this.boundaryRepository = boundaryRepository;
		this.complaintTypeRepository = complaintTypeRepository;
		this.cityRepository = cityRepository;
		this.assignmentRepository = assignmentRepository;
	}

	public ComplaintIndex adapt(ServiceRequest serviceRequest) {
		if (serviceRequest != null && !serviceRequest.getValues().isEmpty()
				&& serviceRequest.getValues().get("complaintStatus").equalsIgnoreCase("REGISTERED")) {
			return indexOnCreate(serviceRequest);

		}
		return null;
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
		// Need to read from service request - citizenfeedback
		complaintIndex.setSatisfactionIndex(0);

		// Reading from serviceRequest values map
		Map<String, String> values = serviceRequest.getValues();
		complaintIndex.setComplaintStatusName(values.get("complaintStatus"));

		// Need to get escalation no of hrs based on complainttype and
		// designation from values map
		complaintIndex.setInitialFunctionarySLADays(0);
		complaintIndex.setCurrentFunctionarySLADays(0);

		complaintIndex.setSource(propertiesManager.getProperty(String.format("complaint.source.%s.%s",
				values.get("userType").toLowerCase(), values.get("receivingMode").toLowerCase())));
		InitializeBoundaryDetails(complaintIndex, values.get("locationId"), values.get("childLocationId"));
		InitializeCityDetails(complaintIndex, values.get("tenantId"));
		InitializeAssignmentDetails(complaintIndex, values.get("assignmentId"));

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
		// calls complaintTypeRepository : Get slahours from complaintype based
		// on
		// code
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
		// read tenantid from value map
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

	public void InitializeAssignmentDetails(ComplaintIndex complaintIndex, String assignmentId) {
		complaintIndex.setAssigneeId(Long.valueOf(assignmentId));
		// read assignmentid from value map
		Assignment assignment = assignmentRepository.fetchAssignmentById(Long.valueOf(assignmentId));
		if (assignment != null) {
			complaintIndex.setAssigneeName(assignment.getName());
			complaintIndex.setDepartmentName(assignment.getDepartmentName());
			complaintIndex.setDepartmentCode(assignment.getDepartmentCode());
			complaintIndex.setInitialFunctionaryName(assignment.getName() + " : " + assignment.getDesignationName()
					+ " : " + NOASSIGNMENT + " : " + assignment.getDesignationName());
			complaintIndex.setCurrentFunctionaryName(assignment.getName() + " : " + assignment.getDesignationName()
					+ " : " + NOASSIGNMENT + " : " + assignment.getDesignationName());
			complaintIndex.setCurrentFunctionaryMobileNumber(
					assignment.getMobileNumber() != null ? assignment.getMobileNumber() : "");
			complaintIndex.setInitialFunctionaryMobileNumber(
					assignment.getMobileNumber() != null ? assignment.getMobileNumber() : "");
		}
	}
}
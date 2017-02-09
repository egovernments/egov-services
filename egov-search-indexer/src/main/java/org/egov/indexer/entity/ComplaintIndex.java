package org.egov.indexer.entity;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ComplaintIndex {

	public static final String ES_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	public static final String DEFAULT_TIMEZONE = "IST";

	@JsonProperty("id")
	private String id;

	@JsonProperty("crn")
	private String crn;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
	@JsonProperty("createdDate")
	private Date createdDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
	@JsonProperty("escalationDate")
	private Date escalationDate;

	@JsonProperty("complaintStatusName")
	private String complaintStatusName;

	@JsonProperty("complainantName")
	private String complainantName;

	@JsonProperty("complainantMobile")
	private String complainantMobile;

	@JsonProperty("complainantEmail")
	private String complainantEmail;

	@JsonProperty("complaintTypeName")
	private String complaintTypeName;

	@JsonProperty("complaintTypeCode")
	private String complaintTypeCode;

	@JsonProperty("assigneeId")
	private Long assigneeId;

	@JsonProperty("assigneeName")
	private String assigneeName;

	@JsonProperty("details")
	private String details;

	@JsonProperty("landmarkDetails")
	private String landmarkDetails;

	@JsonProperty("receivingMode")
	private String receivingMode;

	@JsonProperty("departmentName")
	private String departmentName;

	@JsonProperty("departmentCode")
	private String departmentCode;

	@JsonProperty("wardName")
	private String wardName;

	@JsonProperty("wardNo")
	private String wardNo;

	// private GeoPoint wardGeo;

	@JsonProperty("localityName")
	private String localityName;

	@JsonProperty("localityNo")
	private String localityNo;

	// private GeoPoint localityGeo;

	// private GeoPoint complaintGeo;

	@JsonProperty("satisfactionIndex")
	private double satisfactionIndex;

	@JsonProperty("complaintAgeingdaysFromDue")
	private long complaintAgeingdaysFromDue;

	@JsonProperty("cityCode")
	private String cityCode;

	@JsonProperty("cityDistrictCode")
	private String cityDistrictCode;

	@JsonProperty("cityDistrictName")
	private String cityDistrictName;

	@JsonProperty("cityGrade")
	private String cityGrade;

	@JsonProperty("cityRegionName")
	private String cityRegionName;

	@JsonProperty("cityName")
	private String cityName;

	@JsonProperty("cityDomainUrl")
	private String cityDomainUrl;

	@JsonProperty("complaintDuration")
	private double complaintDuration;

	@JsonProperty("closed")
	private boolean closed;

	@JsonProperty("complaintIsClosed")
	private String complaintIsClosed;

	@JsonProperty("ifClosed")
	private int ifClosed;

	@JsonProperty("durationRange")
	private String durationRange;

	@JsonProperty("source")
	private String source;

	@JsonProperty("complaintPeriod")
	private double complaintPeriod;

	@JsonProperty("complaintSLADays")
	private long complaintSLADays;

	@JsonProperty("complaintAgeingFromDue")
	private double complaintAgeingFromDue;

	@JsonProperty("isSLA")
	private String isSLA;

	@JsonProperty("ifSLA")
	private int ifSLA;

	@JsonProperty("currentFunctionaryName")
	private String currentFunctionaryName;


	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
	@JsonProperty("currentFunctionaryAssigneddate")
	private Date currentFunctionaryAssigneddate;

	@JsonProperty("currentFunctionarySLADays")
	private long currentFunctionarySLADays;

	@JsonProperty("currentFunctionaryAgeingFromDue")
	private double currentFunctionaryAgeingFromDue;

	@JsonProperty("currentFunctionaryIsSLA")
	private String currentFunctionaryIsSLA;

	@JsonProperty("currentFunctionaryIfSLA")
	private int currentFunctionaryIfSLA;

	@JsonProperty("currentFunctionaryMobileNumber")
	private String currentFunctionaryMobileNumber;

	@JsonProperty("closedByFunctionaryName")
	private String closedByFunctionaryName;

	@JsonProperty("initialFunctionaryName")
	private String initialFunctionaryName;


	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
	@JsonProperty("initialFunctionaryAssigneddate")
	private Date initialFunctionaryAssigneddate;

	@JsonProperty("initialFunctionarySLADays")
	private long initialFunctionarySLADays;

	@JsonProperty("initialFunctionaryAgeingFromDue")
	private double initialFunctionaryAgeingFromDue;

	@JsonProperty("initialFunctionaryIsSLA")
	private String initialFunctionaryIsSLA;

	@JsonProperty("initialFunctionaryIfSLA")
	private int initialFunctionaryIfSLA;

	@JsonProperty("escalation1FunctionaryName")
	private String escalation1FunctionaryName;


	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
	@JsonProperty("escalation1FunctionaryAssigneddate")
	private Date escalation1FunctionaryAssigneddate;

	@JsonProperty("escalation1FunctionarySLADays")
	private long escalation1FunctionarySLADays;

	@JsonProperty("escalation1FunctionaryAgeingFromDue")
	private double escalation1FunctionaryAgeingFromDue;

	@JsonProperty("escalation1FunctionaryIsSLA")
	private String escalation1FunctionaryIsSLA;

	@JsonProperty("escalation1FunctionaryIfSLA")
	private int escalation1FunctionaryIfSLA;

	@JsonProperty("escalation2FunctionaryName")
	private String escalation2FunctionaryName;


	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
	@JsonProperty("escalation2FunctionaryAssigneddate")
	private Date escalation2FunctionaryAssigneddate;

	@JsonProperty("escalation2FunctionarySLADays")
	private long escalation2FunctionarySLADays;

	@JsonProperty("escalation2FunctionaryAgeingFromDue")
	private double escalation2FunctionaryAgeingFromDue;

	@JsonProperty("escalation2FunctionaryIsSLA")
	private String escalation2FunctionaryIsSLA;

	@JsonProperty("escalation2FunctionaryIfSLA")
	private int escalation2FunctionaryIfSLA;

	@JsonProperty("escalation3FunctionaryName")
	private String escalation3FunctionaryName;


	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
	@JsonProperty("escalation3FunctionaryAssigneddate")
	private Date escalation3FunctionaryAssigneddate;

	@JsonProperty("escalation3FunctionarySLADays")
	private long escalation3FunctionarySLADays;

	@JsonProperty("escalation3FunctionaryAgeingFromDue")
	private double escalation3FunctionaryAgeingFromDue;

	@JsonProperty("escalation3FunctionaryIsSLA")
	private String escalation3FunctionaryIsSLA;

	@JsonProperty("escalation3FunctionaryIfSLA")
	private int escalation3FunctionaryIfSLA;

	@JsonProperty("escalationLevel")
	private int escalationLevel;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
	@JsonProperty("complaintReOpenedDate")
	private Date complaintReOpenedDate;

	@JsonProperty("reasonForRejection")
	private String reasonForRejection;

	@JsonProperty("registered")
	private int registered;

	@JsonProperty("inProcess")
	private int inProcess;

	@JsonProperty("addressed")
	private int addressed;

	@JsonProperty("rejected")
	private int rejected;
	
	@JsonProperty("reOpened")
	private int reOpened;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCrn() {
		return crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getEscalationDate() {
		return escalationDate;
	}

	public void setEscalationDate(Date escalationDate) {
		this.escalationDate = escalationDate;
	}

	public String getComplaintStatusName() {
		return complaintStatusName;
	}

	public void setComplaintStatusName(String complaintStatusName) {
		this.complaintStatusName = complaintStatusName;
	}

	public String getComplainantName() {
		return complainantName;
	}

	public void setComplainantName(String complainantName) {
		this.complainantName = complainantName;
	}

	public String getComplainantMobile() {
		return complainantMobile;
	}

	public void setComplainantMobile(String complainantMobile) {
		this.complainantMobile = complainantMobile;
	}

	public String getComplainantEmail() {
		return complainantEmail;
	}

	public void setComplainantEmail(String complainantEmail) {
		this.complainantEmail = complainantEmail;
	}

	public String getComplaintTypeName() {
		return complaintTypeName;
	}

	public void setComplaintTypeName(String complaintTypeName) {
		this.complaintTypeName = complaintTypeName;
	}

	public String getComplaintTypeCode() {
		return complaintTypeCode;
	}

	public void setComplaintTypeCode(String complaintTypeCode) {
		this.complaintTypeCode = complaintTypeCode;
	}

	public Long getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getLandmarkDetails() {
		return landmarkDetails;
	}

	public void setLandmarkDetails(String landmarkDetails) {
		this.landmarkDetails = landmarkDetails;
	}

	public String getReceivingMode() {
		return receivingMode;
	}

	public void setReceivingMode(String receivingMode) {
		this.receivingMode = receivingMode;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getWardNo() {
		return wardNo;
	}

	public void setWardNo(String wardNo) {
		this.wardNo = wardNo;
	}

	public String getLocalityName() {
		return localityName;
	}

	public void setLocalityName(String localityName) {
		this.localityName = localityName;
	}

	public String getLocalityNo() {
		return localityNo;
	}

	public void setLocalityNo(String localityNo) {
		this.localityNo = localityNo;
	}

	public double getSatisfactionIndex() {
		return satisfactionIndex;
	}

	public void setSatisfactionIndex(double satisfactionIndex) {
		this.satisfactionIndex = satisfactionIndex;
	}

	public long getComplaintAgeingdaysFromDue() {
		return complaintAgeingdaysFromDue;
	}

	public void setComplaintAgeingdaysFromDue(long complaintAgeingdaysFromDue) {
		this.complaintAgeingdaysFromDue = complaintAgeingdaysFromDue;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityDistrictCode() {
		return cityDistrictCode;
	}

	public void setCityDistrictCode(String cityDistrictCode) {
		this.cityDistrictCode = cityDistrictCode;
	}

	public String getCityDistrictName() {
		return cityDistrictName;
	}

	public void setCityDistrictName(String cityDistrictName) {
		this.cityDistrictName = cityDistrictName;
	}

	public String getCityGrade() {
		return cityGrade;
	}

	public void setCityGrade(String cityGrade) {
		this.cityGrade = cityGrade;
	}

	public String getCityRegionName() {
		return cityRegionName;
	}

	public void setCityRegionName(String cityRegionName) {
		this.cityRegionName = cityRegionName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityDomainUrl() {
		return cityDomainUrl;
	}

	public void setCityDomainUrl(String cityDomainUrl) {
		this.cityDomainUrl = cityDomainUrl;
	}

	public double getComplaintDuration() {
		return complaintDuration;
	}

	public void setComplaintDuration(double complaintDuration) {
		this.complaintDuration = complaintDuration;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public String getComplaintIsClosed() {
		return complaintIsClosed;
	}

	public void setComplaintIsClosed(String complaintIsClosed) {
		this.complaintIsClosed = complaintIsClosed;
	}

	public int getIfClosed() {
		return ifClosed;
	}

	public void setIfClosed(int ifClosed) {
		this.ifClosed = ifClosed;
	}

	public String getDurationRange() {
		return durationRange;
	}

	public void setDurationRange(String durationRange) {
		this.durationRange = durationRange;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public double getComplaintPeriod() {
		return complaintPeriod;
	}

	public void setComplaintPeriod(double complaintPeriod) {
		this.complaintPeriod = complaintPeriod;
	}

	public long getComplaintSLADays() {
		return complaintSLADays;
	}

	public void setComplaintSLADays(long complaintSLADays) {
		this.complaintSLADays = complaintSLADays;
	}

	public double getComplaintAgeingFromDue() {
		return complaintAgeingFromDue;
	}

	public void setComplaintAgeingFromDue(double complaintAgeingFromDue) {
		this.complaintAgeingFromDue = complaintAgeingFromDue;
	}

	public String getIsSLA() {
		return isSLA;
	}

	public void setIsSLA(String isSLA) {
		this.isSLA = isSLA;
	}

	public int getIfSLA() {
		return ifSLA;
	}

	public void setIfSLA(int ifSLA) {
		this.ifSLA = ifSLA;
	}

	public String getCurrentFunctionaryName() {
		return currentFunctionaryName;
	}

	public void setCurrentFunctionaryName(String currentFunctionaryName) {
		this.currentFunctionaryName = currentFunctionaryName;
	}

	public Date getCurrentFunctionaryAssigneddate() {
		return currentFunctionaryAssigneddate;
	}

	public void setCurrentFunctionaryAssigneddate(Date currentFunctionaryAssigneddate) {
		this.currentFunctionaryAssigneddate = currentFunctionaryAssigneddate;
	}

	public long getCurrentFunctionarySLADays() {
		return currentFunctionarySLADays;
	}

	public void setCurrentFunctionarySLADays(long currentFunctionarySLADays) {
		this.currentFunctionarySLADays = currentFunctionarySLADays;
	}

	public double getCurrentFunctionaryAgeingFromDue() {
		return currentFunctionaryAgeingFromDue;
	}

	public void setCurrentFunctionaryAgeingFromDue(double currentFunctionaryAgeingFromDue) {
		this.currentFunctionaryAgeingFromDue = currentFunctionaryAgeingFromDue;
	}

	public String getCurrentFunctionaryIsSLA() {
		return currentFunctionaryIsSLA;
	}

	public void setCurrentFunctionaryIsSLA(String currentFunctionaryIsSLA) {
		this.currentFunctionaryIsSLA = currentFunctionaryIsSLA;
	}

	public int getCurrentFunctionaryIfSLA() {
		return currentFunctionaryIfSLA;
	}

	public void setCurrentFunctionaryIfSLA(int currentFunctionaryIfSLA) {
		this.currentFunctionaryIfSLA = currentFunctionaryIfSLA;
	}

	public String getCurrentFunctionaryMobileNumber() {
		return currentFunctionaryMobileNumber;
	}

	public void setCurrentFunctionaryMobileNumber(String currentFunctionaryMobileNumber) {
		this.currentFunctionaryMobileNumber = currentFunctionaryMobileNumber;
	}

	public String getClosedByFunctionaryName() {
		return closedByFunctionaryName;
	}

	public void setClosedByFunctionaryName(String closedByFunctionaryName) {
		this.closedByFunctionaryName = closedByFunctionaryName;
	}

	public String getInitialFunctionaryName() {
		return initialFunctionaryName;
	}

	public void setInitialFunctionaryName(String initialFunctionaryName) {
		this.initialFunctionaryName = initialFunctionaryName;
	}

	public Date getInitialFunctionaryAssigneddate() {
		return initialFunctionaryAssigneddate;
	}

	public void setInitialFunctionaryAssigneddate(Date initialFunctionaryAssigneddate) {
		this.initialFunctionaryAssigneddate = initialFunctionaryAssigneddate;
	}

	public long getInitialFunctionarySLADays() {
		return initialFunctionarySLADays;
	}

	public void setInitialFunctionarySLADays(long initialFunctionarySLADays) {
		this.initialFunctionarySLADays = initialFunctionarySLADays;
	}

	public double getInitialFunctionaryAgeingFromDue() {
		return initialFunctionaryAgeingFromDue;
	}

	public void setInitialFunctionaryAgeingFromDue(double initialFunctionaryAgeingFromDue) {
		this.initialFunctionaryAgeingFromDue = initialFunctionaryAgeingFromDue;
	}

	public String getInitialFunctionaryIsSLA() {
		return initialFunctionaryIsSLA;
	}

	public void setInitialFunctionaryIsSLA(String initialFunctionaryIsSLA) {
		this.initialFunctionaryIsSLA = initialFunctionaryIsSLA;
	}

	public int getInitialFunctionaryIfSLA() {
		return initialFunctionaryIfSLA;
	}

	public void setInitialFunctionaryIfSLA(int initialFunctionaryIfSLA) {
		this.initialFunctionaryIfSLA = initialFunctionaryIfSLA;
	}

	public String getEscalation1FunctionaryName() {
		return escalation1FunctionaryName;
	}

	public void setEscalation1FunctionaryName(String escalation1FunctionaryName) {
		this.escalation1FunctionaryName = escalation1FunctionaryName;
	}

	public Date getEscalation1FunctionaryAssigneddate() {
		return escalation1FunctionaryAssigneddate;
	}

	public void setEscalation1FunctionaryAssigneddate(Date escalation1FunctionaryAssigneddate) {
		this.escalation1FunctionaryAssigneddate = escalation1FunctionaryAssigneddate;
	}

	public long getEscalation1FunctionarySLADays() {
		return escalation1FunctionarySLADays;
	}

	public void setEscalation1FunctionarySLADays(long escalation1FunctionarySLADays) {
		this.escalation1FunctionarySLADays = escalation1FunctionarySLADays;
	}

	public double getEscalation1FunctionaryAgeingFromDue() {
		return escalation1FunctionaryAgeingFromDue;
	}

	public void setEscalation1FunctionaryAgeingFromDue(double escalation1FunctionaryAgeingFromDue) {
		this.escalation1FunctionaryAgeingFromDue = escalation1FunctionaryAgeingFromDue;
	}

	public String getEscalation1FunctionaryIsSLA() {
		return escalation1FunctionaryIsSLA;
	}

	public void setEscalation1FunctionaryIsSLA(String escalation1FunctionaryIsSLA) {
		this.escalation1FunctionaryIsSLA = escalation1FunctionaryIsSLA;
	}

	public int getEscalation1FunctionaryIfSLA() {
		return escalation1FunctionaryIfSLA;
	}

	public void setEscalation1FunctionaryIfSLA(int escalation1FunctionaryIfSLA) {
		this.escalation1FunctionaryIfSLA = escalation1FunctionaryIfSLA;
	}

	public String getEscalation2FunctionaryName() {
		return escalation2FunctionaryName;
	}

	public void setEscalation2FunctionaryName(String escalation2FunctionaryName) {
		this.escalation2FunctionaryName = escalation2FunctionaryName;
	}

	public Date getEscalation2FunctionaryAssigneddate() {
		return escalation2FunctionaryAssigneddate;
	}

	public void setEscalation2FunctionaryAssigneddate(Date escalation2FunctionaryAssigneddate) {
		this.escalation2FunctionaryAssigneddate = escalation2FunctionaryAssigneddate;
	}

	public long getEscalation2FunctionarySLADays() {
		return escalation2FunctionarySLADays;
	}

	public void setEscalation2FunctionarySLADays(long escalation2FunctionarySLADays) {
		this.escalation2FunctionarySLADays = escalation2FunctionarySLADays;
	}

	public double getEscalation2FunctionaryAgeingFromDue() {
		return escalation2FunctionaryAgeingFromDue;
	}

	public void setEscalation2FunctionaryAgeingFromDue(double escalation2FunctionaryAgeingFromDue) {
		this.escalation2FunctionaryAgeingFromDue = escalation2FunctionaryAgeingFromDue;
	}

	public String getEscalation2FunctionaryIsSLA() {
		return escalation2FunctionaryIsSLA;
	}

	public void setEscalation2FunctionaryIsSLA(String escalation2FunctionaryIsSLA) {
		this.escalation2FunctionaryIsSLA = escalation2FunctionaryIsSLA;
	}

	public int getEscalation2FunctionaryIfSLA() {
		return escalation2FunctionaryIfSLA;
	}

	public void setEscalation2FunctionaryIfSLA(int escalation2FunctionaryIfSLA) {
		this.escalation2FunctionaryIfSLA = escalation2FunctionaryIfSLA;
	}

	public String getEscalation3FunctionaryName() {
		return escalation3FunctionaryName;
	}

	public void setEscalation3FunctionaryName(String escalation3FunctionaryName) {
		this.escalation3FunctionaryName = escalation3FunctionaryName;
	}

	public Date getEscalation3FunctionaryAssigneddate() {
		return escalation3FunctionaryAssigneddate;
	}

	public void setEscalation3FunctionaryAssigneddate(Date escalation3FunctionaryAssigneddate) {
		this.escalation3FunctionaryAssigneddate = escalation3FunctionaryAssigneddate;
	}

	public long getEscalation3FunctionarySLADays() {
		return escalation3FunctionarySLADays;
	}

	public void setEscalation3FunctionarySLADays(long escalation3FunctionarySLADays) {
		this.escalation3FunctionarySLADays = escalation3FunctionarySLADays;
	}

	public double getEscalation3FunctionaryAgeingFromDue() {
		return escalation3FunctionaryAgeingFromDue;
	}

	public void setEscalation3FunctionaryAgeingFromDue(double escalation3FunctionaryAgeingFromDue) {
		this.escalation3FunctionaryAgeingFromDue = escalation3FunctionaryAgeingFromDue;
	}

	public String getEscalation3FunctionaryIsSLA() {
		return escalation3FunctionaryIsSLA;
	}

	public void setEscalation3FunctionaryIsSLA(String escalation3FunctionaryIsSLA) {
		this.escalation3FunctionaryIsSLA = escalation3FunctionaryIsSLA;
	}

	public int getEscalation3FunctionaryIfSLA() {
		return escalation3FunctionaryIfSLA;
	}

	public void setEscalation3FunctionaryIfSLA(int escalation3FunctionaryIfSLA) {
		this.escalation3FunctionaryIfSLA = escalation3FunctionaryIfSLA;
	}

	public int getEscalationLevel() {
		return escalationLevel;
	}

	public void setEscalationLevel(int escalationLevel) {
		this.escalationLevel = escalationLevel;
	}

	public Date getComplaintReOpenedDate() {
		return complaintReOpenedDate;
	}

	public void setComplaintReOpenedDate(Date complaintReOpenedDate) {
		this.complaintReOpenedDate = complaintReOpenedDate;
	}

	public String getReasonForRejection() {
		return reasonForRejection;
	}

	public void setReasonForRejection(String reasonForRejection) {
		this.reasonForRejection = reasonForRejection;
	}

	public int getRegistered() {
		return registered;
	}

	public void setRegistered(int registered) {
		this.registered = registered;
	}

	public int getInProcess() {
		return inProcess;
	}

	public void setInProcess(int inProcess) {
		this.inProcess = inProcess;
	}

	public int getAddressed() {
		return addressed;
	}

	public void setAddressed(int addressed) {
		this.addressed = addressed;
	}

	public int getRejected() {
		return rejected;
	}

	public void setRejected(int rejected) {
		this.rejected = rejected;
	}

	public int getReOpened() {
		return reOpened;
	}

	public void setReOpened(int reOpened) {
		this.reOpened = reOpened;
	}
}

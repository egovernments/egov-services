package org.egov.web.indexer.repository.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
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

    @JsonProperty("wardGeo")
    private GeoPoint wardGeo;

    @JsonProperty("localityName")
    private String localityName;

    @JsonProperty("localityNo")
    private String localityNo;

    @JsonProperty("localityGeo")
    private GeoPoint localityGeo;

    @JsonProperty("complaintGeo")
    private GeoPoint complaintGeo;

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

    @JsonProperty("initialFunctionaryMobileNumber")
    private String initialFunctionaryMobileNumber;

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
}

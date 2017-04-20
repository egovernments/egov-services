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

    @JsonProperty("durationRange")
    private String durationRange;

    @JsonProperty("source")
    private String source;

    @JsonProperty("complaintPeriod")
    private double complaintPeriod;

    @JsonProperty("complaintSLADays")
    private long complaintSLADays;

    @JsonProperty("isSLA")
    private String isSLA;

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

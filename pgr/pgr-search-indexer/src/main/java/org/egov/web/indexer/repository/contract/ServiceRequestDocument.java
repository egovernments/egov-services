package org.egov.web.indexer.repository.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServiceRequestDocument {

    private static final String ES_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String DISPLAY_NO = "NO";
    private static final String DISPLAY_YES = "YES";
    public static final int YES = 1;
    public static final int NO = 0;

    @JsonProperty("id")
    private String id;

    @JsonProperty("crn")
    private String crn;

    @JsonProperty("createdDate")
    private String createdDate;

    @JsonProperty("lastModifiedDate")
    private String lastModifiedDate;

    @JsonProperty("escalationDate")
    private String escalationDate;

    @JsonProperty("serviceStatusName")
    private String serviceStatusName;

    @JsonProperty("serviceStatusCode")
    private String serviceStatusCode;

    @JsonProperty("requesterName")
    private String requesterName;

    @JsonProperty("requesterMobile")
    private String requesterMobile;

    @JsonProperty("requesterEmail")
    private String requesterEmail;

    @JsonProperty("requesterId")
    private Long requesterId;

    @JsonProperty("serviceTypeName")
    private String serviceTypeName;

    @JsonProperty("serviceTypeCode")
    private String serviceTypeCode;

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
    private String wardGeo;

    @JsonProperty("localityName")
    private String localityName;

    @JsonProperty("localityNo")
    private String localityNo;

    @JsonProperty("localityGeo")
    private String localityGeo;

    @JsonProperty("serviceGeo")
    private String serviceGeo;

    @JsonProperty("satisfactionIndex")
    private double satisfactionIndex;

    @JsonProperty("serviceStatusAgeingDaysFromDue")
    private long serviceStatusAgeingDaysFromDue;

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

    @JsonProperty("serviceDuration")
    private double serviceDuration;

    @JsonProperty("durationRange")
    private String durationRange;

    @JsonProperty("source")
    private String source;

    @JsonProperty("serviceStatusPeriod")
    private double serviceStatusPeriod;

    @JsonProperty("serviceSLADays")
    private int complaintSLADays;

    @JsonProperty("isWithinSLA")
    private int isWithinSLA;

    @JsonProperty("isWithinSLAText")
    public String isWithinSLAText() {
        return convertToDisplayableBoolean(isWithinSLA);
    }

    @JsonProperty("isRegistered")
    private int isRegistered;

    @JsonProperty("isRegisteredText")
    public String isRegisteredText() {
        return convertToDisplayableBoolean(isRegistered);
    }

    @JsonProperty("isInProcess")
    private int isInProcess;

    @JsonProperty("isInProcessText")
    public String isInProcessText() {
        return convertToDisplayableBoolean(isInProcess);
    }

    @JsonProperty("isClosed")
    private int isClosed;

    @JsonProperty("isClosedText")
    public String isClosedText() {
        return convertToDisplayableBoolean(isClosed);
    }

    @JsonProperty("isAddressed")
    private int isAddressed;

    @JsonProperty("isAddressedText")
    public String isAddressedText() {
        return convertToDisplayableBoolean(isAddressed);
    }

    @JsonProperty("rejected")
    private int rejected;

    @JsonProperty("reOpened")
    private int reOpened;

    @JsonProperty("processingFee")
    private double processingFee;

    @JsonProperty("keyword")
    private List<String> keywords;

    @JsonProperty("categoryName")
    private String serviceCategoryName;

    @JsonProperty("categoryId")
    private Long serviceCategoryId;

    @JsonProperty("tenantId")
    private String tenantId;

    private String convertToDisplayableBoolean(int booleanAsInteger) {
        return booleanAsInteger == 0 ? DISPLAY_NO : DISPLAY_YES;
    }
}
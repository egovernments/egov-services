package org.pgr.batch.repository.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Getter
@Builder
public class WorkflowRequest {

    public enum Action {
        CREATE("create"), END("close") ,UPDATE("update");

        private String strValue;

        Action(String strValue) {
            this.strValue = strValue;
        }

        public static Boolean isCreate(String value) {
            return Action.CREATE.strValue.equalsIgnoreCase(value);
        }

        public static Boolean isEnd(String value) {
            return Action.END.strValue.equalsIgnoreCase(value);
        }

        public static String forComplaintStatus(String complaintStatus) {
            if (complaintStatus.equalsIgnoreCase("REGISTERED")) {
                return CREATE.strValue;
            } else if (complaintStatus.equalsIgnoreCase("COMPLETED") || complaintStatus.equalsIgnoreCase("REJECTED") || complaintStatus.equalsIgnoreCase("WITHDRAWN")) {
                return END.strValue;
            }
            return UPDATE.strValue;
        }
    }

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("type")
    private String type;

    @JsonProperty("description")
    private String description;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("created_Date")
    private Date createdDate;

    @JsonProperty("last_updated")
    private Date lastupdatedSince;

    @JsonProperty("status")
    private String status;

    @JsonProperty("action")
    private String action;

    @JsonProperty("business_key")
    private String businessKey;

    @JsonProperty("assignee")
    private Long assignee;

    @JsonProperty("group")
    private String group;

    @JsonProperty("sender_name")
    private String senderName;

    @JsonProperty("values")
    private Map<String, Attribute> values;
    
    @JsonProperty("service_request_id")
    private String crn;

    private String tenantId;

    public String getValueForKey(String key) {
        if (Objects.nonNull(values.get(key)))
            return values.get(key).getValues().get(0).getName();
        return "";
    }
    
    @JsonIgnore
	public boolean isCreate() {
		return Action.isCreate(this.getAction());
	}
    
    @JsonIgnore
	public boolean isClosed() {
		return Action.isEnd(this.getAction());
	}
}

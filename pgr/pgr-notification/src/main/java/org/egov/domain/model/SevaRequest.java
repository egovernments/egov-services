package org.egov.domain.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static org.apache.commons.lang.StringUtils.isEmpty;

public class SevaRequest {

    private static final String PHONE = "phone";
    private static final String EMAIL = "email";
    private static final String CRN = "service_request_id";
    private static final String SERVICE_NAME = "service_name";
    private static final String VALUES_STATUS = "status";
    private static final String VALUES = "values";
    private static final String DESCRIPTION = "description";
    private static final String FIRST_NAME = "first_name";
    private static final String LOCATION_NAME = "location_name";
    private static final String REQUESTED_DATE = "requested_datetime";
    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";

    private HashMap<String, Object> sevaRequestMap;

	public SevaRequest(HashMap<String, Object> sevaRequestMap) {
		this.sevaRequestMap = sevaRequestMap;
	}

	public String getMobileNumber() {
        return (String) this.sevaRequestMap.get(PHONE);
	}

	public String getComplainantEmail() {
        return (String) this.sevaRequestMap.get(EMAIL);
	}

	public boolean isComplainantEmailAbsent() {
		return isEmpty(getComplainantEmail());
	}

	public String getComplaintTypeName() {
        return (String) this.sevaRequestMap.get(SERVICE_NAME);
	}

	public String getCrn() {
        return (String) this.sevaRequestMap.get(CRN);
	}

    public String getStatusName() {
        return getValues().get(VALUES_STATUS);
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, String> getValues() {
        return (HashMap<String, String>) this.sevaRequestMap.get(VALUES);
    }

    public String getFormattedCreatedDate() {
        return new SimpleDateFormat(DATE_FORMAT).format(getCreatedDate());

    }

    private Date getCreatedDate() {
        return (Date) this.sevaRequestMap.get(REQUESTED_DATE);
    }

    public String getDetails() {
        return (String) this.sevaRequestMap.get(DESCRIPTION);
    }

    public String getLocationName() {
        return getValues().get(LOCATION_NAME);
    }

    public String getComplainantName() {
	    //TODO: Remove default once retrieval of FirstName from userInfo is implemented.
        return (String) this.sevaRequestMap.getOrDefault(FIRST_NAME, "placeholder");
    }
}

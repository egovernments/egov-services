package org.egov.web.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RequestInfo {
    private String apiId;

    private String ver;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    private Date ts;

    private String action;

    private String did;

    private String key;

    private String msgId;

    private String requesterId;

    private String authToken;
}

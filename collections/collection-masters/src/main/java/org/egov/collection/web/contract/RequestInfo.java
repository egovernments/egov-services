package org.egov.collection.web.contract;



import lombok.Getter;
import lombok.Setter;



import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@Setter
public class RequestInfo {

    private String apiId;

    private String ver;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    private String ts;

    private String action;

    private String did;

    private String key;

    private String msgId;

    private String requesterId;

    private String authToken;

    private User userInfo;

}

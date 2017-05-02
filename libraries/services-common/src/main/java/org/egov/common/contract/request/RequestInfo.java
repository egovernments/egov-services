package org.egov.common.contract.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestInfo {

    private String apiId;

    private String ver;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    private Date ts;

    private String action;

    private String did;

    private String key;

    private String msgId;

    private String authToken;

    private String correlationId;

    private User userInfo;
}
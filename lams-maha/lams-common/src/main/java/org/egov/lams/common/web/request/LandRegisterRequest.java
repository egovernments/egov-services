package org.egov.lams.common.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.egov.common.contract.request.RequestInfo;
import org.egov.lams.common.web.contract.LandRegister;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramki on 23/10/17.
 */
public class LandRegisterRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("landRegisters")
    @Valid
    private List<LandRegister> landRegisters = new ArrayList<LandRegister>();
}

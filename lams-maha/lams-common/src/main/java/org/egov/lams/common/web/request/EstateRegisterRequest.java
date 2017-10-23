package org.egov.lams.common.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.egov.lams.common.web.contract.EstateRegister;
import org.egov.lams.common.web.contract.RequestInfo;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramki on 23/10/17.
 */
public class EstateRegisterRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("landRegisters")
    @Valid
    private List<EstateRegister> landRegisters = new ArrayList<EstateRegister>();
}

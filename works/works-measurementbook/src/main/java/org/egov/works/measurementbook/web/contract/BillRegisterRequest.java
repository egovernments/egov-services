package org.egov.works.measurementbook.web.contract;

import java.util.ArrayList;
import java.util.List;


import lombok.Data;


@Data
public class BillRegisterRequest {

    private RequestInfo requestInfo = new RequestInfo();
    
    private List<BillRegister> billRegisters = new ArrayList<>();
}
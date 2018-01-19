package org.egov.inv.model;

import lombok.*;
import org.egov.common.contract.request.RequestInfo;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillRegisterRequest {

    private RequestInfo requestInfo;

    private List<BillRegister> billRegisters;
}
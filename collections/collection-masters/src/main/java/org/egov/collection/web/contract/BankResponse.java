package org.egov.collection.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankResponse {

    private List<Bank> bankList;

    private ResponseInfo responseInfo;
}

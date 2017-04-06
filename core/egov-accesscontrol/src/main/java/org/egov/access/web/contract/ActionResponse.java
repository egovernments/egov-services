package org.egov.access.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class ActionResponse {

    private ResponseInfo responseInfo;

    private List<ActionContract> actions;

}

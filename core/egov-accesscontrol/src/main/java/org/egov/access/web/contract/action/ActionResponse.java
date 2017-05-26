package org.egov.access.web.contract.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;


@Getter
@AllArgsConstructor
public class ActionResponse {
    private ResponseInfo responseInfo;
    private List<ActionContract> actions;
}

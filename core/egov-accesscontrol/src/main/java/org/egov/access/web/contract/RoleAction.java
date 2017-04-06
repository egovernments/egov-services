package org.egov.access.web.contract;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoleAction {

    private Long action;

    private String roleCode;
}

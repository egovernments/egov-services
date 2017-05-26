package org.egov.access.web.controller;

import org.egov.access.domain.model.Role;
import org.egov.access.domain.model.RoleSearchCriteria;
import org.egov.access.domain.service.RoleService;
import org.egov.access.web.contract.RoleContract;
import org.egov.access.web.contract.RoleRequest;
import org.egov.access.web.contract.RoleResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/roles")
public class RoleController {

    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping(value = "_search")
    public RoleResponse getRoles(@RequestParam(value = "code", required = false) String code,
                                 @RequestBody final RoleRequest roleRequest) {
        RoleSearchCriteria roleSearchCriteria = RoleSearchCriteria.builder().codes(code).build();
        List<Role> roles = roleService.getRoles(roleSearchCriteria);
        return getSuccessResponse(roles);
    }

    private RoleResponse getSuccessResponse(final List<Role> roles) {
        final ResponseInfo responseInfo = ResponseInfo.builder()
                .status(HttpStatus.OK.toString())
                .build();
        List<RoleContract> roleContracts = new RoleContract().getRoles(roles);
        return new RoleResponse(responseInfo, roleContracts);
    }
}

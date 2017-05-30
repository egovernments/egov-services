package org.egov.access.web.controller;

import org.egov.access.domain.model.Role;
import org.egov.access.domain.criteria.RoleSearchCriteria;
import org.egov.access.domain.service.RoleService;
import org.egov.access.web.contract.role.RoleContract;
import org.egov.access.web.contract.role.RoleRequest;
import org.egov.access.web.contract.role.RoleResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


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
        RoleSearchCriteria roleSearchCriteria = RoleSearchCriteria.builder().
                codes(Arrays.stream(code.split(",")).map(String::trim).collect(Collectors.toList())).build();
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

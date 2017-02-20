package org.egov.workflow.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesManager {

    @Value("${egov.services.user.hostname}")
    private String userServiceHostname;

    @Value("${egov.services.user.get_user_details}")
    private String getUserDetailsContext;

    @Value("${egov.services.user.get_users}")
    private String getUsersContext;

    @Value("${egov.services.eis.hostname}")
    private String eisServiceHostname;

    @Value("${egov.services.eis.position_by_id}")
    private String getPositionsByIdContext;

    @Value("${egov.services.eis.position_by_employee_code}")
    private String getPositionByEmployeeCodeContext;

    @Value("${egov.services.eis.department_by_position}")
    private String getDepartmentByPositionContext;

    @Value("${egov.services.eis.positionhierarchys}")
    private String getPositionHierarchysContext;

    @Value("${egov.services.eis.employee_by_role}")
    private String getEmployeesByRoleContext;

    @Value("${egov.services.eis.department_by_user}")
    private String getDepartmentByUserContext;
    
    @Value("${egov.services.eis.user_by_position}")
    private String getUserByPositionContext;

    public String getUserDetailsUrl() {
        return this.userServiceHostname + this.getUserDetailsContext;
    }

    public String getUsersUrl() {
        return this.getUsersContext;
    }

    public String getPositionsByIdUrl() {
        return this.eisServiceHostname + this.getPositionsByIdContext;
    }

    public String getPositionByEmployeeCodeUrl() {
        return this.eisServiceHostname + this.getPositionByEmployeeCodeContext;
    }

    public String getDepartmentByPositionUrl() {
        return this.getDepartmentByPositionContext;
    }

    public String getPositionHierarchyUrl() {
        return this.eisServiceHostname + this.getPositionHierarchysContext;
    }

    public String getEmployeeByRoleNameUrl() {
        return this.eisServiceHostname + this.getEmployeesByRoleContext;
    }

    public String getDepartmentByUserUrl() {
        return this.getDepartmentByUserContext;
    }
    
    public String getUserByPositionUrl() {
        return this.getUserByPositionContext;
    }
}

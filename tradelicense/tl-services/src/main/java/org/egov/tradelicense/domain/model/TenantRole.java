package org.egov.tradelicense.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TenantRole   {
  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("roles")
  private List<Role> roles = new ArrayList<Role>();

  public TenantRole tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }


  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public TenantRole roles(List<Role> roles) {
    this.roles = roles;
    return this;
  }

  public TenantRole addRolesItem(Role rolesItem) {
    this.roles.add(rolesItem);
    return this;
  }


  @Valid
  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TenantRole tenantRole = (TenantRole) o;
    return Objects.equals(this.tenantId, tenantRole.tenantId) &&
        Objects.equals(this.roles, tenantRole.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tenantId, roles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TenantRole {\n");
    
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


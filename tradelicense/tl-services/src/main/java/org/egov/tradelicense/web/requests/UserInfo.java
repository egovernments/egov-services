package org.egov.tradelicense.web.requests;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.tradelicense.domain.model.Role;
import org.egov.tradelicense.domain.model.TenantRole;

import com.fasterxml.jackson.annotation.JsonProperty;


public class UserInfo   {
  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("username")
  private String username = null;

  @JsonProperty("mobile")
  private String mobile = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("primaryrole")
  private List<Role> primaryrole = new ArrayList<Role>();

  @JsonProperty("additionalroles")
  private List<TenantRole> additionalroles = null;

  public UserInfo tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public UserInfo id(Integer id) {
    this.id = id;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public UserInfo username(String username) {
    this.username = username;
    return this;
  }
  
  @NotNull
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserInfo mobile(String mobile) {
    this.mobile = mobile;
    return this;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public UserInfo email(String email) {
    this.email = email;
    return this;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserInfo primaryrole(List<Role> primaryrole) {
    this.primaryrole = primaryrole;
    return this;
  }

  public UserInfo addPrimaryroleItem(Role primaryroleItem) {
    this.primaryrole.add(primaryroleItem);
    return this;
  }

  @NotNull
  @Valid
  public List<Role> getPrimaryrole() {
    return primaryrole;
  }

  public void setPrimaryrole(List<Role> primaryrole) {
    this.primaryrole = primaryrole;
  }

  public UserInfo additionalroles(List<TenantRole> additionalroles) {
    this.additionalroles = additionalroles;
    return this;
  }

  public UserInfo addAdditionalrolesItem(TenantRole additionalrolesItem) {
    if (this.additionalroles == null) {
      this.additionalroles = new ArrayList<TenantRole>();
    }
    this.additionalroles.add(additionalrolesItem);
    return this;
  }


  @Valid
  public List<TenantRole> getAdditionalroles() {
    return additionalroles;
  }

  public void setAdditionalroles(List<TenantRole> additionalroles) {
    this.additionalroles = additionalroles;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInfo userInfo = (UserInfo) o;
    return Objects.equals(this.tenantId, userInfo.tenantId) &&
        Objects.equals(this.id, userInfo.id) &&
        Objects.equals(this.username, userInfo.username) &&
        Objects.equals(this.mobile, userInfo.mobile) &&
        Objects.equals(this.email, userInfo.email) &&
        Objects.equals(this.primaryrole, userInfo.primaryrole) &&
        Objects.equals(this.additionalroles, userInfo.additionalroles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tenantId, id, username, mobile, email, primaryrole, additionalroles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserInfo {\n");
    
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    mobile: ").append(toIndentedString(mobile)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    primaryrole: ").append(toIndentedString(primaryrole)).append("\n");
    sb.append("    additionalroles: ").append(toIndentedString(additionalroles)).append("\n");
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


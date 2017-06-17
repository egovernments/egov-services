package org.egov.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * This is acting ID token of the authenticated user on the server. Any value provided by the clients will be ignored and actual user based on authtoken will be used on the server.
 * Author : Narendra
 */

public class UserInfo   {
  @JsonProperty("tenantId")
  @NotNull
  private String tenantId = null;

  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("username")
  @NotNull
  private String username = null;

  @JsonProperty("mobile")
  private String mobile = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("primaryrole")
  @NotNull
  private List<Role> primaryrole = new ArrayList<Role>();

  @JsonProperty("additionalroles")
  private List<TenantRole> additionalroles = new ArrayList<TenantRole>();

   /**
   * Unique Identifier of the tenant to which user primarily belongs
   * @return tenantId
  **/
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

   /**
   * User id of the authenticated user. Will be deprecated in future
   * @return id
  **/
   public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


   /**
   * Unique user name of the authenticated user
   * @return username
  **/
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


   /**
   * mobile number of the autheticated user
   * @return mobile
  **/
  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }


   /**
   * email address of the authenticated user
   * @return email
  **/
    public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }



   /**
   * List of all the roles for the primary tenant
   * @return primaryrole
  **/
  public List<Role> getPrimaryrole() {
    return primaryrole;
  }

  public void setPrimaryrole(List<Role> primaryrole) {
    this.primaryrole = primaryrole;
  }


   /**
   * array of additional tenantids authorized for the authenticated user
   * @return additionalroles
  **/
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


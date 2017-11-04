package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object holds the Enum information.   
 */
@ApiModel(description = "This object holds the Enum information.   ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-01T09:47:46.371Z")

public class CommonEnum   {
  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("code")
  private String code = null;

  public CommonEnum tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Enum
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Enum")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public CommonEnum name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of the Enum  
   * @return name
  **/
  @ApiModelProperty(value = "name of the Enum  ")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CommonEnum code(String code) {
    this.code = code;
    return this;
  }

   /**
   * code of Enum   
   * @return code
  **/
  @ApiModelProperty(value = "code of Enum   ")


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommonEnum commonEnum = (CommonEnum) o;
    return Objects.equals(this.tenantId, commonEnum.tenantId) &&
        Objects.equals(this.name, commonEnum.name) &&
        Objects.equals(this.code, commonEnum.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tenantId, name, code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommonEnum {\n");
    
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
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


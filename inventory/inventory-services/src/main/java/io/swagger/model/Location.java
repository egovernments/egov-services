package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object holds the boundary information.   
 */
@ApiModel(description = "This object holds the boundary information.   ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-06T06:06:21.932Z")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("name")
  private String name = null;

  public Location id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Location 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Location ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Location tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenantid of the Location
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenantid of the Location")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Location code(String code) {
    this.code = code;
    return this;
  }

   /**
   * code of the Location 
   * @return code
  **/
  @ApiModelProperty(required = true, value = "code of the Location ")
  @NotNull


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Location name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of the Location 
   * @return name
  **/
  @ApiModelProperty(value = "name of the Location ")

 @Pattern(regexp="^[a-zA-Z ]*$")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Location location = (Location) o;
    return Objects.equals(this.id, location.id) &&
        Objects.equals(this.tenantId, location.tenantId) &&
        Objects.equals(this.code, location.code) &&
        Objects.equals(this.name, location.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, code, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Location {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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


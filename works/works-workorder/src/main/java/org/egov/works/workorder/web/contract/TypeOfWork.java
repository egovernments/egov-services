package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * An Object that holds Type of Work and Sub Type of work master data. The sub type of work will have parent type of work reference.
 */
@ApiModel(description = "An Object that holds Type of Work and Sub Type of work master data. The sub type of work will have parent type of work reference.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class TypeOfWork {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("active")
    private Boolean active = null;

    @JsonProperty("parent")
    private String parent = null;

    public TypeOfWork id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Type Of Work
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Type Of Work")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TypeOfWork tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Type Of Work
     *
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Type Of Work")
    @NotNull

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public TypeOfWork name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Name of the Type Of Work
     *
     * @return name
     **/
    @ApiModelProperty(required = true, value = "Name of the Type Of Work")
    @NotNull

    @Pattern(regexp = "[a-zA-Z0-9\\s\\.,]+")
    @Size(min = 1, max = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeOfWork code(String code) {
        this.code = code;
        return this;
    }

    /**
     * Unique code of the Type Of Work
     *
     * @return code
     **/
    @ApiModelProperty(required = true, value = "Unique code of the Type Of Work")
    @NotNull

    @Pattern(regexp = "[a-zA-Z0-9-\\\\]+")
    @Size(min = 1, max = 100)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TypeOfWork description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Description of the Type Of Work
     *
     * @return description
     **/
    @ApiModelProperty(value = "Description of the Type Of Work")

    @Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
    @Size(max = 1024)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeOfWork active(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * If status of Type Of Work is TRUE then the type of work is active else it is in-active. The default is active.
     *
     * @return active
     **/
    @ApiModelProperty(required = true, value = "If status of Type Of Work is TRUE then the type of work is active else it is in-active. The default is active.")
    @NotNull


    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public TypeOfWork parent(String parent) {
        this.parent = parent;
        return this;
    }

    /**
     * Parent Type Of Work of the Sub type of work.
     *
     * @return parent
     **/
    @ApiModelProperty(value = "Parent Type Of Work of the Sub type of work.")


    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TypeOfWork typeOfWork = (TypeOfWork) o;
        return Objects.equals(this.id, typeOfWork.id) &&
                Objects.equals(this.tenantId, typeOfWork.tenantId) &&
                Objects.equals(this.name, typeOfWork.name) &&
                Objects.equals(this.code, typeOfWork.code) &&
                Objects.equals(this.description, typeOfWork.description) &&
                Objects.equals(this.active, typeOfWork.active) &&
                Objects.equals(this.parent, typeOfWork.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, name, code, description, active, parent);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TypeOfWork {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    active: ").append(toIndentedString(active)).append("\n");
        sb.append("    parent: ").append(toIndentedString(parent)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}


package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * the department to which the asset belongs,This master data is defined under MDMS and only the code field needs to be sent during creation of asset.
 */
@ApiModel(description = "the department to which the asset belongs,This master data is defined under MDMS and only the code field needs to be sent during creation of asset.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class Department {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("code")
    private String code = null;

    public Department id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Unique identifier of the department
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique identifier of the department")


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Department name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Name of the department.
     *
     * @return name
     **/
    @ApiModelProperty(value = "Name of the department.")


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department code(String code) {
        this.code = code;
        return this;
    }

    /**
     * Department code.
     *
     * @return code
     **/
    @ApiModelProperty(value = "Department code.")


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Department department = (Department) o;
        return Objects.equals(this.id, department.id) &&
                Objects.equals(this.name, department.name) &&
                Objects.equals(this.code, department.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Department {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
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


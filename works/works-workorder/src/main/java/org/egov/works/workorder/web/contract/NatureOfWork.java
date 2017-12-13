package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * An Object that holds Nature of work master details
 */
@ApiModel(description = "An Object that holds Nature of work master details")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class NatureOfWork {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("expenditureType")
    private ExpenditureType expenditureType = null;

    public NatureOfWork id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Nature Of Work
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Nature Of Work")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NatureOfWork tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Nature Of Work
     *
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Nature Of Work")
    @NotNull

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public NatureOfWork name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Name of the Nature Of Work
     *
     * @return name
     **/
    @ApiModelProperty(required = true, value = "Name of the Nature Of Work")
    @NotNull

    @Pattern(regexp = "[a-zA-Z0-9\\s\\.,]+")
    @Size(min = 1, max = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NatureOfWork code(String code) {
        this.code = code;
        return this;
    }

    /**
     * Code of the Nature Of Work
     *
     * @return code
     **/
    @ApiModelProperty(required = true, value = "Code of the Nature Of Work")
    @NotNull

    @Pattern(regexp = "[a-zA-Z0-9-\\\\]+")
    @Size(min = 1, max = 100)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public NatureOfWork expenditureType(ExpenditureType expenditureType) {
        this.expenditureType = expenditureType;
        return this;
    }

    /**
     * Expenditure type Enum for the nature of work
     *
     * @return expenditureType
     **/
    @ApiModelProperty(required = true, value = "Expenditure type Enum for the nature of work")
    @NotNull

    @Valid

    public ExpenditureType getExpenditureType() {
        return expenditureType;
    }

    public void setExpenditureType(ExpenditureType expenditureType) {
        this.expenditureType = expenditureType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NatureOfWork natureOfWork = (NatureOfWork) o;
        return Objects.equals(this.id, natureOfWork.id) &&
                Objects.equals(this.tenantId, natureOfWork.tenantId) &&
                Objects.equals(this.name, natureOfWork.name) &&
                Objects.equals(this.code, natureOfWork.code) &&
                Objects.equals(this.expenditureType, natureOfWork.expenditureType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, name, code, expenditureType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NatureOfWork {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    expenditureType: ").append(toIndentedString(expenditureType)).append("\n");
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


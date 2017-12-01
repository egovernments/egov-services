package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Fund is a defining concept in municipal accounting – where it is required to segregate all accounting transactions into designated funds. Each fund needs to be treated as an independent accounting entity – in other words, all vouchers within a fund must be self-balancing and balance sheets and IncomeExpenditure reports must be generated for each fund. A hierarchy of funds may be defined – i.e. each fund can have multiple sub-funds and so on.
 */
@ApiModel(description = "Fund is a defining concept in municipal accounting – where it is required to segregate all accounting transactions into designated funds. Each fund needs to be treated as an independent accounting entity – in other words, all vouchers within a fund must be self-balancing and balance sheets and IncomeExpenditure reports must be generated for each fund. A hierarchy of funds may be defined – i.e. each fund can have multiple sub-funds and so on. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class Fund {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("identifier")
    private String identifier = null;

    @JsonProperty("parent")
    private Long parent = null;

    @JsonProperty("active")
    private Boolean active = null;

    @JsonProperty("isParent")
    private Boolean isParent = null;

    @JsonProperty("level")
    private Long level = null;

    @JsonProperty("auditDetails")
    private Auditable auditDetails = null;

    public Fund id(String id) {
        this.id = id;
        return this;
    }

    /**
     * identifier appears as prefix in all the vouchers accounted in the books of the Fund. Each fund must have an identifier – each voucher belonging to a fund must have the identifier embedded in the voucher number for easy identification. Fund is taken at a voucher head level for each voucher transaction.
     *
     * @return id
     **/
    @ApiModelProperty(value = "identifier appears as prefix in all the vouchers accounted in the books of the Fund. Each fund must have an identifier – each voucher belonging to a fund must have the identifier embedded in the voucher number for easy identification. Fund is taken at a voucher head level for each voucher transaction. ")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Fund name(String name) {
        this.name = name;
        return this;
    }

    /**
     * name is the name of the fund . Example :Municipal Fund,Capital Fund. Also name is unique.
     *
     * @return name
     **/
    @ApiModelProperty(required = true, value = "name is the name of the fund . Example :Municipal Fund,Capital Fund. Also name is unique. ")
    @NotNull

    @Size(min = 2, max = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Fund code(String code) {
        this.code = code;
        return this;
    }

    /**
     * code is a unique number given to each fund . ULB may refer this for the short name
     *
     * @return code
     **/
    @ApiModelProperty(required = true, value = "code is a unique number given to each fund . ULB may refer this for the short name ")
    @NotNull

    @Size(min = 2, max = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Fund identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    /**
     * identifier of the Fund
     *
     * @return identifier
     **/
    @ApiModelProperty(required = true, value = "identifier of the Fund ")
    @NotNull


    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Fund parent(Long parent) {
        this.parent = parent;
        return this;
    }

    /**
     * parent adding a parent will create the fund as a sub-fund (child) of a fund already created (parent fund).
     *
     * @return parent
     **/
    @ApiModelProperty(value = "parent adding a parent will create the fund as a sub-fund (child) of a fund already created (parent fund). ")


    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Fund active(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * active is a boolean value which says whether fund is in use or not . If Fund is active, then accounting of transactions under the fund is enabled. If Fund becomes inactive, and no transactions can be accounted under the Fund.
     *
     * @return active
     **/
    @ApiModelProperty(required = true, value = "active is a boolean value which says whether fund is in use or not . If Fund is active, then accounting of transactions under the fund is enabled. If Fund becomes inactive, and no transactions can be accounted under the Fund. ")
    @NotNull


    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Fund isParent(Boolean isParent) {
        this.isParent = isParent;
        return this;
    }

    /**
     * isParent is updated internally so that system can identify whether the fund is parent or child. Only child which is not parent for any other fund can only participate in transaction .
     *
     * @return isParent
     **/
    @ApiModelProperty(value = "isParent is updated internally so that system can identify whether the fund is parent or child. Only child which is not parent for any other fund can only participate in transaction . ")


    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }

    public Fund level(Long level) {
        this.level = level;
        return this;
    }

    /**
     * level identifies what is the level of the fund in the tree structure. Top most parent will have level 0 and its child will have level as 1
     *
     * @return level
     **/
    @ApiModelProperty(required = true, value = "level identifies what is the level of the fund in the tree structure. Top most parent will have level 0 and its child will have level as 1 ")
    @NotNull


    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public Fund auditDetails(Auditable auditDetails) {
        this.auditDetails = auditDetails;
        return this;
    }

    /**
     * Get auditDetails
     *
     * @return auditDetails
     **/
    @ApiModelProperty(value = "")

    @Valid

    public Auditable getAuditDetails() {
        return auditDetails;
    }

    public void setAuditDetails(Auditable auditDetails) {
        this.auditDetails = auditDetails;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fund fund = (Fund) o;
        return Objects.equals(this.id, fund.id) &&
                Objects.equals(this.name, fund.name) &&
                Objects.equals(this.code, fund.code) &&
                Objects.equals(this.identifier, fund.identifier) &&
                Objects.equals(this.parent, fund.parent) &&
                Objects.equals(this.active, fund.active) &&
                Objects.equals(this.isParent, fund.isParent) &&
                Objects.equals(this.level, fund.level) &&
                Objects.equals(this.auditDetails, fund.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, identifier, parent, active, isParent, level, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Fund {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
        sb.append("    parent: ").append(toIndentedString(parent)).append("\n");
        sb.append("    active: ").append(toIndentedString(active)).append("\n");
        sb.append("    isParent: ").append(toIndentedString(isParent)).append("\n");
        sb.append("    level: ").append(toIndentedString(level)).append("\n");
        sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
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


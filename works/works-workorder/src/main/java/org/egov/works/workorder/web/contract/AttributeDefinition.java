package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Hold the Asset dynamic fields details as list of json object.
 */
@ApiModel(description = "Hold the Asset dynamic fields details as list of json object.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class AttributeDefinition {
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("isActive")
    private Boolean isActive = null;

    @JsonProperty("isMandatory")
    private Boolean isMandatory = null;

    @JsonProperty("values")
    private String values = null;

    @JsonProperty("localText")
    private String localText = null;

    @JsonProperty("regExFormate")
    private String regExFormate = null;

    @JsonProperty("url")
    private String url = null;

    @JsonProperty("order")
    private String order = null;

    @JsonProperty("columns")
    private List<AttributeDefinition> columns = null;

    public AttributeDefinition name(String name) {
        this.name = name;
        return this;
    }

    /**
     * name of the custom dynamic field (label name).
     *
     * @return name
     **/
    @ApiModelProperty(required = true, value = "name of the custom dynamic field (label name).")
    @NotNull


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttributeDefinition type(String type) {
        this.type = type;
        return this;
    }

    /**
     * type of the custom field. Valid types are ('string'[text box],'text'[text area],'number'[text box allow numbers only],'datetime'[calender],'singlevaluelist'[drop down for single value selection],'multivaluelist'[drop down for multi value selection],'checkbox','radio','table')
     *
     * @return type
     **/
    @ApiModelProperty(required = true, value = "type of the custom field. Valid types are ('string'[text box],'text'[text area],'number'[text box allow numbers only],'datetime'[calender],'singlevaluelist'[drop down for single value selection],'multivaluelist'[drop down for multi value selection],'checkbox','radio','table')")
    @NotNull


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AttributeDefinition isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    /**
     * if the value is true then the field will be shown in custom fields of asset
     *
     * @return isActive
     **/
    @ApiModelProperty(required = true, value = "if the value is true then the field will be shown in custom fields of asset")
    @NotNull


    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public AttributeDefinition isMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
        return this;
    }

    /**
     * value will be true if field is mandatory otherwise false.
     *
     * @return isMandatory
     **/
    @ApiModelProperty(value = "value will be true if field is mandatory otherwise false.")


    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public AttributeDefinition values(String values) {
        this.values = values;
        return this;
    }

    /**
     * comma seprated values we can pass if field type is selected as('singlevaluelist','multivaluelist','checkbox','radio').
     *
     * @return values
     **/
    @ApiModelProperty(value = "comma seprated values we can pass if field type is selected as('singlevaluelist','multivaluelist','checkbox','radio').")


    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public AttributeDefinition localText(String localText) {
        this.localText = localText;
        return this;
    }

    /**
     * .
     *
     * @return localText
     **/
    @ApiModelProperty(value = ".")


    public String getLocalText() {
        return localText;
    }

    public void setLocalText(String localText) {
        this.localText = localText;
    }

    public AttributeDefinition regExFormate(String regExFormate) {
        this.regExFormate = regExFormate;
        return this;
    }

    /**
     * .
     *
     * @return regExFormate
     **/
    @ApiModelProperty(value = ".")


    public String getRegExFormate() {
        return regExFormate;
    }

    public void setRegExFormate(String regExFormate) {
        this.regExFormate = regExFormate;
    }

    public AttributeDefinition url(String url) {
        this.url = url;
        return this;
    }

    /**
     * configure if field type are ('singlevaluelist','multivaluelist') and @values is null.
     *
     * @return url
     **/
    @ApiModelProperty(value = "configure if field type are ('singlevaluelist','multivaluelist') and @values is null.")


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AttributeDefinition order(String order) {
        this.order = order;
        return this;
    }

    /**
     * order of the table column.
     *
     * @return order
     **/
    @ApiModelProperty(value = "order of the table column.")


    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public AttributeDefinition columns(List<AttributeDefinition> columns) {
        this.columns = columns;
        return this;
    }

    public AttributeDefinition addColumnsItem(AttributeDefinition columnsItem) {
        if (this.columns == null) {
            this.columns = new ArrayList<AttributeDefinition>();
        }
        this.columns.add(columnsItem);
        return this;
    }

    /**
     * Get columns
     *
     * @return columns
     **/
    @ApiModelProperty(value = "")

    @Valid

    public List<AttributeDefinition> getColumns() {
        return columns;
    }

    public void setColumns(List<AttributeDefinition> columns) {
        this.columns = columns;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttributeDefinition attributeDefinition = (AttributeDefinition) o;
        return Objects.equals(this.name, attributeDefinition.name) &&
                Objects.equals(this.type, attributeDefinition.type) &&
                Objects.equals(this.isActive, attributeDefinition.isActive) &&
                Objects.equals(this.isMandatory, attributeDefinition.isMandatory) &&
                Objects.equals(this.values, attributeDefinition.values) &&
                Objects.equals(this.localText, attributeDefinition.localText) &&
                Objects.equals(this.regExFormate, attributeDefinition.regExFormate) &&
                Objects.equals(this.url, attributeDefinition.url) &&
                Objects.equals(this.order, attributeDefinition.order) &&
                Objects.equals(this.columns, attributeDefinition.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, isActive, isMandatory, values, localText, regExFormate, url, order, columns);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AttributeDefinition {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
        sb.append("    isMandatory: ").append(toIndentedString(isMandatory)).append("\n");
        sb.append("    values: ").append(toIndentedString(values)).append("\n");
        sb.append("    localText: ").append(toIndentedString(localText)).append("\n");
        sb.append("    regExFormate: ").append(toIndentedString(regExFormate)).append("\n");
        sb.append("    url: ").append(toIndentedString(url)).append("\n");
        sb.append("    order: ").append(toIndentedString(order)).append("\n");
        sb.append("    columns: ").append(toIndentedString(columns)).append("\n");
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


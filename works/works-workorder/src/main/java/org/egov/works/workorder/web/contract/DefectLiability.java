package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * Hold the asset defect liability related information. Defect liability can be measured in years, months and days.
 */
@ApiModel(description = "Hold the asset defect liability related information. Defect liability can be measured in years, months and days.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class DefectLiability {
    @JsonProperty("year")
    private Long year = null;

    @JsonProperty("month")
    private Long month = null;

    @JsonProperty("day")
    private Long day = null;

    public DefectLiability year(Long year) {
        this.year = year;
        return this;
    }

    /**
     * Number of years.
     *
     * @return year
     **/
    @ApiModelProperty(value = "Number of years.")


    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public DefectLiability month(Long month) {
        this.month = month;
        return this;
    }

    /**
     * Number of months.
     *
     * @return month
     **/
    @ApiModelProperty(value = "Number of months.")


    public Long getMonth() {
        return month;
    }

    public void setMonth(Long month) {
        this.month = month;
    }

    public DefectLiability day(Long day) {
        this.day = day;
        return this;
    }

    /**
     * Number of days.
     *
     * @return day
     **/
    @ApiModelProperty(value = "Number of days.")


    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefectLiability defectLiability = (DefectLiability) o;
        return Objects.equals(this.year, defectLiability.year) &&
                Objects.equals(this.month, defectLiability.month) &&
                Objects.equals(this.day, defectLiability.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DefectLiability {\n");

        sb.append("    year: ").append(toIndentedString(year)).append("\n");
        sb.append("    month: ").append(toIndentedString(month)).append("\n");
        sb.append("    day: ").append(toIndentedString(day)).append("\n");
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


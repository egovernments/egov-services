package org.egov.inv.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class for web request. Array of Scrap items  are used in case of create or update
 */
@ApiModel(description = "Contract class for web request. Array of Scrap items  are used in case of create or update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-27T06:37:03.617Z")

public class ScrapRequest {
    @JsonProperty("requestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("scraps")
    private List<Scrap> scraps = null;

    public ScrapRequest requestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    /**
     * Get requestInfo
     *
     * @return requestInfo
     **/
    @ApiModelProperty(value = "")

    @Valid

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public ScrapRequest scraps(List<Scrap> scraps) {
        this.scraps = scraps;
        return this;
    }

    public ScrapRequest addScrapsItem(Scrap scrapsItem) {
        if (this.scraps == null) {
            this.scraps = new ArrayList<Scrap>();
        }
        this.scraps.add(scrapsItem);
        return this;
    }

    /**
     * Used for search result and create only
     *
     * @return scraps
     **/
    @ApiModelProperty(value = "Used for search result and create only")

    @Valid

    public List<Scrap> getScraps() {
        return scraps;
    }

    public void setScraps(List<Scrap> scraps) {
        this.scraps = scraps;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScrapRequest scrapRequest = (ScrapRequest) o;
        return Objects.equals(this.requestInfo, scrapRequest.requestInfo) &&
                Objects.equals(this.scraps, scrapRequest.scraps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestInfo, scraps);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ScrapRequest {\n");

        sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
        sb.append("    scraps: ").append(toIndentedString(scraps)).append("\n");
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


package org.egov.inv.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class for web response. Array of Scrap items  are used in case of search ,create or update request.
 */
@ApiModel(description = "Contract class for web response. Array of Scrap items  are used in case of search ,create or update request.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-27T06:37:03.617Z")

public class ScrapResponse {
    @JsonProperty("responseInfo")
    private ResponseInfo responseInfo = null;

    @JsonProperty("scraps")
    private List<Scrap> scraps = null;

    @JsonProperty("page")
    private Page page = null;

    public ScrapResponse responseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
        return this;
    }

    /**
     * Get responseInfo
     *
     * @return responseInfo
     **/
    @ApiModelProperty(value = "")

    @Valid

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public ScrapResponse scraps(List<Scrap> scraps) {
        this.scraps = scraps;
        return this;
    }

    public ScrapResponse addScrapsItem(Scrap scrapsItem) {
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

    public ScrapResponse page(Page page) {
        this.page = page;
        return this;
    }

    /**
     * Get page
     *
     * @return page
     **/
    @ApiModelProperty(value = "")

    @Valid

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScrapResponse scrapResponse = (ScrapResponse) o;
        return Objects.equals(this.responseInfo, scrapResponse.responseInfo) &&
                Objects.equals(this.scraps, scrapResponse.scraps) &&
                Objects.equals(this.page, scrapResponse.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseInfo, scraps, page);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ScrapResponse {\n");

        sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
        sb.append("    scraps: ").append(toIndentedString(scraps)).append("\n");
        sb.append("    page: ").append(toIndentedString(page)).append("\n");
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


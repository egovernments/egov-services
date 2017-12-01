package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class to send response. Array of Notice items are used in case of search results, also multiple  Notice item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of Notice items are used in case of search results, also multiple  Notice item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class NoticeResponse {
    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo = null;

    @JsonProperty("notices")
    private List<Notice> notices = null;

    public NoticeResponse responseInfo(ResponseInfo responseInfo) {
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

    public NoticeResponse notices(List<Notice> notices) {
        this.notices = notices;
        return this;
    }

    public NoticeResponse addNoticesItem(Notice noticesItem) {
        if (this.notices == null) {
            this.notices = new ArrayList<Notice>();
        }
        this.notices.add(noticesItem);
        return this;
    }

    /**
     * Used for search result and create only
     *
     * @return notices
     **/
    @ApiModelProperty(value = "Used for search result and create only")

    @Valid

    public List<Notice> getNotices() {
        return notices;
    }

    public void setNotices(List<Notice> notices) {
        this.notices = notices;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NoticeResponse noticeResponse = (NoticeResponse) o;
        return Objects.equals(this.responseInfo, noticeResponse.responseInfo) &&
                Objects.equals(this.notices, noticeResponse.notices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseInfo, notices);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NoticeResponse {\n");

        sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
        sb.append("    notices: ").append(toIndentedString(notices)).append("\n");
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


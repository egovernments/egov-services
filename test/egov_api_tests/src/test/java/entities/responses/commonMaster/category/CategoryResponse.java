package entities.responses.commonMaster.category;

import entities.responses.commonMaster.ResponseInfo;
import org.codehaus.jackson.annotate.JsonProperty;

public class CategoryResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    @JsonProperty("Category")
    private Category[] category;

    public ResponseInfo getResponseInfo() {
        return this.responseInfo;
    }

    public void setResponseInfo(ResponseInfo ResponseInfo) {
        this.responseInfo = ResponseInfo;
    }

    public Category[] getCategory() {
        return this.category;
    }

    public void setCategory(Category[] Category) {
        this.category = Category;
    }
}

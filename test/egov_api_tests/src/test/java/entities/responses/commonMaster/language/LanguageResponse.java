package entities.responses.commonMaster.language;

import entities.responses.commonMaster.ResponseInfo;
import org.codehaus.jackson.annotate.JsonProperty;

public class LanguageResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo ResponseInfo;
    @JsonProperty("Language")
    private Language[] Language;

    public ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public Language[] getLanguage() {
        return this.Language;
    }

    public void setLanguage(Language[] Language) {
        this.Language = Language;
    }
}

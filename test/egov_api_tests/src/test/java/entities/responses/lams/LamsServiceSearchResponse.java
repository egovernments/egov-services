package entities.responses.lams;

import org.codehaus.jackson.annotate.JsonProperty;

public class LamsServiceSearchResponse {

    private Agreements[] Agreements;

    @JsonProperty("ResposneInfo")
    private ResposneInfo ResposneInfo;

    public Agreements[] getAgreements() {
        return this.Agreements;
    }

    public void setAgreements(Agreements[] Agreements) {
        this.Agreements = Agreements;
    }

    public ResposneInfo getResposneInfo() {
        return this.ResposneInfo;
    }

    public void setResposneInfo(ResposneInfo ResposneInfo) {
        this.ResposneInfo = ResposneInfo;
    }
}

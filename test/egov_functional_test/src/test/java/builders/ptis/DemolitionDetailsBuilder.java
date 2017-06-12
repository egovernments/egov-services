package builders.ptis;

import entities.ptis.DemolitionDetail;

/**
 * Created by bimal on 3/3/17.
 */
public class DemolitionDetailsBuilder {

    DemolitionDetail demolitionDetail = new DemolitionDetail();

    public DemolitionDetailsBuilder() {

    }

    public DemolitionDetailsBuilder withReasonForDemolition(String ReasonForDemolition) {
        demolitionDetail.setReasonForDemolition(ReasonForDemolition);
        return this;
    }

    public DemolitionDetailsBuilder withSurveyNumber(String SurveryNumber) {
        demolitionDetail.setSurveyNumber(SurveryNumber);
        return this;
    }

    public DemolitionDetailsBuilder withPattaNumber(String PattaNumber) {
        demolitionDetail.setPattaNumber(PattaNumber);
        return this;
    }

    public DemolitionDetailsBuilder withMarketValue(String MarketValue) {
        demolitionDetail.setMarketValue(MarketValue);
        return this;
    }

    public DemolitionDetailsBuilder withCaptialValue(String CaptialValue) {
        demolitionDetail.setCapitalValue(CaptialValue);
        return this;
    }

    public DemolitionDetailsBuilder withNorth(String North) {
        demolitionDetail.setNorth(North);
        return this;
    }

    public DemolitionDetailsBuilder withEast(String East) {
        demolitionDetail.setEast(East);
        return this;
    }

    public DemolitionDetailsBuilder withWest(String West) {
        demolitionDetail.setWest(West);
        return this;
    }

    public DemolitionDetailsBuilder withSouth(String South) {
        demolitionDetail.setSouth(South);
        return this;
    }

    public DemolitionDetail build() {
        return demolitionDetail;
    }

}

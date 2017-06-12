package builders.commonMaster.createHoliday;

import entities.requests.commonMasters.createHoliday.CreateHolidayRequest;
import entities.requests.commonMasters.createHoliday.Holiday;
import entities.requests.commonMasters.createHoliday.RequestInfo;

public final class CreateHolidayRequestBuilder {

    CreateHolidayRequest createHolidayRequest = new CreateHolidayRequest();
    RequestInfo requestInfo = new RequestInfo();
    Holiday holiday = new Holiday();

    public CreateHolidayRequestBuilder() {
        createHolidayRequest.setRequestInfo(requestInfo);
        createHolidayRequest.setHoliday(holiday);
    }

    public CreateHolidayRequestBuilder withHoliday(Holiday Holiday) {
        createHolidayRequest.setHoliday(Holiday);
        return this;
    }

    public CreateHolidayRequestBuilder withRequestInfo(RequestInfo RequestInfo) {
        createHolidayRequest.setRequestInfo(RequestInfo);
        return this;
    }

    public CreateHolidayRequest build() {
        return createHolidayRequest;
    }
}

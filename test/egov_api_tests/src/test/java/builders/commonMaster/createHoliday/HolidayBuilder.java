package builders.commonMaster.createHoliday;

import entities.requests.commonMasters.createHoliday.CalendarYear;
import entities.requests.commonMasters.createHoliday.Holiday;

public final class HolidayBuilder {

    Holiday holiday = new Holiday();
    CalendarYear calendarYear = new CalendarYear();

    public HolidayBuilder() {
        holiday.setCalendarYear(calendarYear);
        holiday.setTenantId("1");
    }

    public HolidayBuilder withCalendarYear(CalendarYear calendarYear) {
        holiday.setCalendarYear(calendarYear);
        return this;
    }

    public HolidayBuilder withName(String name) {
        holiday.setName(name);
        return this;
    }

    public HolidayBuilder withTenantId(String tenantId) {
        holiday.setTenantId(tenantId);
        return this;
    }

    public HolidayBuilder withId(int id) {
        holiday.setId(id);
        return this;
    }

    public HolidayBuilder withApplicableOn(String applicableOn) {
        holiday.setApplicableOn(applicableOn);
        return this;
    }

    public Holiday build() {
        return holiday;
    }
}

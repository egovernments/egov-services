package builders.commonMaster.createHoliday;

import entities.requests.commonMasters.createHoliday.CalendarYear;

public final class CalendarYearBuilder {

    CalendarYear calendarYear = new CalendarYear();

    public CalendarYearBuilder() {
        calendarYear.setId(2);
        calendarYear.setName(2017);
        calendarYear.setStartDate("01/01/2017");
        calendarYear.setEndDate("31/12/2017");
        calendarYear.setActive(true);
        calendarYear.setTenantId("1");
    }

    public CalendarYearBuilder withEndDate(String endDate) {
        calendarYear.setEndDate(endDate);
        return this;
    }

    public CalendarYearBuilder withName(int name) {
        calendarYear.setName(name);
        return this;
    }

    public CalendarYearBuilder withTenantId(String tenantId) {
        calendarYear.setTenantId(tenantId);
        return this;
    }

    public CalendarYearBuilder withActive(boolean active) {
        calendarYear.setActive(active);
        return this;
    }

    public CalendarYearBuilder withId(int id) {
        calendarYear.setId(id);
        return this;
    }

    public CalendarYearBuilder withStartDate(String startDate) {
        calendarYear.setStartDate(startDate);
        return this;
    }

    public CalendarYear build() {
        return calendarYear;
    }
}

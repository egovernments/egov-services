package entities.responses.commonMaster.holiday;

public class Holiday {
    private CalendarYear calendarYear;
    private String name;
    private String tenantId;
    private int id;
    private String applicableOn;

    public CalendarYear getCalendarYear() {
        return this.calendarYear;
    }

    public void setCalendarYear(CalendarYear calendarYear) {
        this.calendarYear = calendarYear;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApplicableOn() {
        return this.applicableOn;
    }

    public void setApplicableOn(String applicableOn) {
        this.applicableOn = applicableOn;
    }
}

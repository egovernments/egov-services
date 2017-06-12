package entities.responses.eGovEIS.searchEISMasters.hrConfigurations;

import org.codehaus.jackson.annotate.JsonProperty;

public class HRConfiguration {
    private String[] Include_enclosed_holidays;
    @JsonProperty("Weekly_holidays")
    private String[] Weekly_holidays;
    private String[] Autogenerate_employeecode;

    public String[] getInclude_enclosed_holidays() {
        return this.Include_enclosed_holidays;
    }

    public void setInclude_enclosed_holidays(String[] Include_enclosed_holidays) {
        this.Include_enclosed_holidays = Include_enclosed_holidays;
    }

    public String[] getWeekly_holidays() {
        return this.Weekly_holidays;
    }

    public void setWeekly_holidays(String[] Weekly_holidays) {
        this.Weekly_holidays = Weekly_holidays;
    }

    public String[] getAutogenerate_employeecode() {
        return this.Autogenerate_employeecode;
    }

    public void setAutogenerate_employeecode(String[] Autogenerate_employeecode) {
        this.Autogenerate_employeecode = Autogenerate_employeecode;
    }
}

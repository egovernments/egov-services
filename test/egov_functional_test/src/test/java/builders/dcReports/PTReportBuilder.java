package builders.dcReports;

import entities.dcReports.PTReport;

public class PTReportBuilder {

    PTReport ptReport = new PTReport();

    public PTReportBuilder withFromDate(String date) {
        ptReport.setFromDate(date);
        return this;
    }

    public PTReportBuilder withToDate(String date) {
        ptReport.setToDate(date);
        return this;
    }

    public PTReport build() {
        return ptReport;
    }
}

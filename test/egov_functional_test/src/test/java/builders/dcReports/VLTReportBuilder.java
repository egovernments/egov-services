package builders.dcReports;

import entities.dcReports.VLTReport;

public class VLTReportBuilder {

    VLTReport vltReport = new VLTReport();

    public VLTReportBuilder withFromDate(String date) {
        vltReport.setFromDate(date);
        return this;
    }

    public VLTReportBuilder withToDate(String date) {
        vltReport.setToDate(date);
        return this;
    }

    public VLTReport build() {
        return vltReport;
    }
}

package steps.dcReports;

import cucumber.api.java8.En;
import entities.dcReports.PTReport;
import entities.dcReports.VLTReport;
import excelDataFiles.PTISDataReader;
import pages.dcReports.DailyCollectionReportPage;
import pages.ptis.PropertyAcknowledgementPage;
import steps.BaseSteps;

public class DailyCollectionReportSteps extends BaseSteps implements En {

    public DailyCollectionReportSteps() {

        And("^user need to enter the date to get the vlt report details$", () -> {
            String vltReportInfo = "report1";

            VLTReport vltReport = new PTISDataReader(ptisTestDataFileName).getVLTReportInfo(vltReportInfo);
            pageStore.get(DailyCollectionReportPage.class).enterVLTReportDetails(vltReport);
            pageStore.get(PropertyAcknowledgementPage.class).toCloseAdditionalConnectionPage();
        });

        And("^user need to enter the date to get the pt report details$", () -> {
            String ptReportInfo = "report2";

            PTReport ptReport = new PTISDataReader(ptisTestDataFileName).getPTReportInfo(ptReportInfo);
            pageStore.get(DailyCollectionReportPage.class).enterPTReportDetails(ptReport);
            pageStore.get(PropertyAcknowledgementPage.class).toCloseAdditionalConnectionPage();

        });
    }
}

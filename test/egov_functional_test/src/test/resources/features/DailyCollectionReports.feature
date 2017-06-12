Feature: To find the daily collection reports

  As a registered user of the system
  I want to be able to search the daily collection reports

  @Sanity @DailyCollectionReports
  Scenario: To find the daily collection vlt reports

    Given juniorAssistant logs in
    And user will select the required screen as "Daily Collection Report(VLT)"
    And user need to enter the date to get the vlt report details
    And current user logs out


  @Sanity @DailyCollectionReports
  Scenario: To find the daily collection pt reports

    Given juniorAssistant logs in
    And user will select the required screen as "Daily collection report(PT)"
    And user need to enter the date to get the pt report details
    And current user logs out



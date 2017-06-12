Feature: Create regular estimate/Create LOA for estimate/Create-Track milestone/Generate Contractor Bill
  As a registered of the system
  I want to able Create regular estimate, LOA, Milestone/Track Milestone/Generate Contractor Bill


  @Works
  Scenario Outline: Create Regular Estimate

    Given assis_Engineer logs in
    And user will select the required screen as "create estimate"
    And he enters estimate header details as <estimateHeaderDetails>
    And he enters financial details as <financialDetails>
    And he enters work details as for <workDetails>
    And he enters approver details as <approverDetails1>
    And he forwards to DEE and closes the acknowledgement
    Then user will be notified by "successfully"
    And current user logs out

    And deputyExecutiveEngineer logs in
    And he chooses to act upon above application number
    And he enters approver details as <approverDetails2>
    And he submit the application to superIntendent
    Then user will be notified by "forwarded"
    And current user logs out

    And superIntendent logs in
    And he chooses to act upon above application number
    And he enters approver details as <approverDetails3>
    And he submit the application to commissioner
    Then user will be notified by "forwarded"
    And current user logs out

    And commissioner logs in
    And he chooses to act upon above application number
    And he enters the AdminSanctionNumber
    And he enters approver details as <approverDetails4>
    And he submit the application to assis_Engineer
    Then user will be notified by "forwarded"
    And current user logs out

    And assis_Engineer logs in
    And he chooses to act upon above application number
    And he enters the details for approve
    And he approves the application
    Then user will be notified by "done"
    And current user logs out

    Examples:
      | estimateHeaderDetails | financialDetails    | workDetails    | approverDetails1        | approverDetails2 | approverDetails3 | approverDetails4 |
      | Estimate_1            | EstimateFinancial_1 | EstimateWork_1 | deputyExecutiveEngineer | SuperIntendent   | commissioner1    | assis_Engineer   |
      | Estimate_2            | EstimateFinancial_2 | EstimateWork_2 | deputyExecutiveEngineer | SuperIntendent   | commissioner1    | assis_Engineer   |

  @Works
  Scenario Outline: Create Letter of Acceptance/ Create,Track Milestone/Generate Contractor Bill

    When assis_Engineer logs in
    And user will select the required screen as "Create Letter of Acceptance"
    And he select the required application
    And he enters the mandatory details
    Then he save the file and view the LOA pdf
    Then user will be notified by "successfully"

    And user will select the required screen as "Search/View LOA"
    And he search for LOA

    And user will select the required screen as "Modify LOA"
    And he search for LOA for modify
    Then user will be notified by "successfully"

    And user will select the required screen as "create milestone"
    And he search and select the required file
    And he stores the loa number and enters details
    And he save the file and close
    Then user will be notified by "successfully"

    And user will select the required screen as "Track Milestone"
    And he search application using loa number
    And he select the application
    And he enters the milestone details
    And he save the file and close
    Then user will be notified by "successfully"

    And user will select the required screen as "create contractor bill"
    And he search application using loa number
    And he select the required file
    And he enters contractor details for part bill <approverDetails1>
    Then user will be notified by "successfully"
    And current user logs out

    And deputyExecutiveEngineer logs in
    And he chooses to act upon above application number
    And he approves the bill
    Then user will be notified by "approved"
    And current user logs out

    And assis_Engineer logs in
    And user will select the required screen as "create contractor bill"
    And he search application using loa number
    And he select the required file
    And he enters contractor details for full bill <approverDetails1>
    Then user will be notified by "successfully"
    And current user logs out

    And deputyExecutiveEngineer logs in
    And he chooses to act upon above application number
    And he approves the bill
    Then user will be notified by "approved"
    And current user logs out

    Examples:
      | approverDetails1        |
      | deputyExecutiveEngineer |

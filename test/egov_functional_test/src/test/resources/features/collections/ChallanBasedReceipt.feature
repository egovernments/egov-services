Feature: Create/Collect Challan Based Receipt

  As a registered user of the system
  I am able to Create/Collect Challan Based Receipt
#  @Collections  @Sanity @Smoke
  @WIP
  Scenario Outline: System should be able to Create Challan

    Given juniorAssistant logs in
    And user will select the required screen as "Create Challan"
    And he enters challan details
    And he create challan and closes acknowledgement
    Then user will be notified by "successfully"
    And current user logs out

    And seniorAssistant logs in
    And he chooses to act upon above application number
    And he validate the challan
    Then user will be notified by "Validated"

    And user will select the required screen as "challan receipt"
    And he search for challan number
    And he pay using <paymentMethod>
    And user closes the acknowledgement
    And current user logs out

    Examples:
      | paymentMethod |
      | cash          |
      | cheque        |
      | dd            |
















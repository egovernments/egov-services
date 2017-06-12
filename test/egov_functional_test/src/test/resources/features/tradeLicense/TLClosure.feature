Feature: Trade license closure


  # Trade License Closure #
  @Sanity @TradeLicense
  Scenario Outline: Registered user choose for trade license closure
    Given creator logs in
    And user will select the required screen as "Search Trade License"
    And he choose a trade license for closure as <closureDetails>
    And he forwards for approver sanitaryInspector
    And he confirms to proceed
    And he closes the acknowledgement page
    And current user logs out

    When sanitaryInspector logs in
    And he chooses to act upon above application number
    And he forwards for approver commissioner
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    When commissioner logs in
    And he chooses to act upon above application number
    And he approves the closure
    And he confirms to proceed
    And he closes acknowledgement page
    And current user logs out

    Examples:
      | closureDetails    |
      | licenceForClosure |
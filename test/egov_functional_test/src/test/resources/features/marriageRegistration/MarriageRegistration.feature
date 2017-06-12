Feature: Marriage Registration
  An valid system user can create marriage registration

  @Marriage @Sanity
  Scenario Outline: Create Marriage Registration
    When juniorAssistant logs in
    And user will select the required screen as "Create Marriage Registration"
    And he enters the applicants details as <generalInformation>
    And he enters the bridegroom information as <bridegroomInformation> <brideInformation>
    And he enters the Witnesses Information
    And he enters the checklist
    And he forward to commissioner and closes the acknowledgement
    And current user logs out

    When creator logs in
    And user will select the required screen as "Collect Fee"
    And he search for above application number to collect marriage Registration fee
    And he collect the registration charges and closes the acknowledgement
    And current user logs out

    When commissioner logs in
    And he chooses to act upon above application number
    And he approve the new marriage application  and close the acknowledgement
    Then user will be notified by "successfully"
    And current user logs out

    When juniorAssistant logs in
    And he chooses to act upon above application number
    And he enters the serial and page number
    And print the marraige cerificate
    And current user logs out

    Examples:
      | generalInformation | bridegroomInformation | brideInformation |
      | generalInfo        | bridegroomInfo        | brideInfo        |
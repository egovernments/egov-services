Feature: Title transfer of a Property

  As a registered user of the system
  I should able to transfer title of a property

  # TRANSFER OF OWNERSHIP SCREEN #

  @Sanity @PropertyTax
  Scenario Outline: Register Choose to do title Transfer

    Given commissioner logs in
    And user will select the required screen as "data entry screen" with condition as "ptis"
    And he creates a new assessment for a private residential property
    Then dataEntry Details saved successfully
    And he choose to add edit DCB
    And he choose to close the dataentry acknowledgement screen
    And current user logs out

    Given juniorAssistant logs in
    And user will select the required screen as "collect tax"
    And he searches for assessment with number
    And he chooses to pay tax
    And he pay tax using Cash

    And user will select the required screen as "Transfer Ownership"
    And he searches for assessment with number
    And he chooses Registration already done button
    And he enters registration details for the property <registrationDetails>
    And he enters enclosure details

    And he forwards for approval to billCollector
    And he will copy the acknowledgement message with assessment number title
    And user will be notified by "successfully"
    And current user logs out

    When billCollector logs in
    And he chooses to act upon above assessment number
    And he forwards for approval to revenueInspector
    And current user closes acknowledgement
    And current user logs out

    When revenueInspector logs in
    And he chooses to act upon above assessment number
    And he forwards for approval to revenueOfficer
    And current user closes acknowledgement
    And current user logs out

    Given juniorAssistant logs in
    And user will select the required screen as "Property Mutation Fee"
    And he searches for the assessment with mutation assessment number
    And he pay tax using Cash
    And current user logs out

    When revenueOfficer logs in
    And he chooses to act upon above assessment number
    And he forwards for approval to commissioner
    And current user closes acknowledgement
    And current user logs out

    When commissioner logs in
    And he chooses to act upon above assessment number
    And he approved the property with remarks "property approved" for transfer of ownership
    And current user closes acknowledgement

    And he chooses to act upon above assessment number
    And he does a digital signature

    When commissioner closes acknowledgement
    And current user logs out

    And juniorAssistant logs in
    And he chooses to act upon above assessment number
    And he generate title transfer notice
    And current user logs out

    Examples:
      | registrationDetails |
      | register            |

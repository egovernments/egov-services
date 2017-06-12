Feature: Collection

  As a registered user of the system
  I want to be able to collect taxes
  So that the property records are up to date.

  Background:

    Given commissioner logs in
    And user will select the required screen as "Data entry screen" with condition as "ptis"
    And he creates a new assessment for a private residential property
    Then dataEntry Details saved successfully
    And he choose to add edit DCB
    And he choose to close the dataentry acknowledgement screen
    And current user logs out

  @WIP
  Scenario: online payment for property tax

    Given User will Visit Property Tax onlinepayent link
    And User will enter Assessment Number and click on search button
    And user will fill amount and select the AXIS Bank Payment Gateway and click on PayOnline
    And user will select the card, enter all the details and click on pay now button
    Then user will be notified by "received."







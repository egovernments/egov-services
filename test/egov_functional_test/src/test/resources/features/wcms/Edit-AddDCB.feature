Feature: To create a data entry screen for water charges and apply for add/edit DCB

  As a registered user of the system
  I want to create a data entry screen
  So that the connection records are up to date.

  Background:It will run the data entry screen of property tax

    Given commissioner logs in
    And user will select the required screen as "Data entry screen" with condition as "ptis"
    And he creates a new assessment for a private residential property
    Then dataEntry Details saved successfully
    And he choose to add edit DCB
    And he choose to close the dataentry acknowledgement screen
    And current user logs out

  @Sanity @WaterCharges
  Scenario Outline: To create a data entry screen and edit/add DCB for the water charges

    Given admin logs in
    And user will select the required screen as "Data Entry Screen" with condition as "wtms"
    And user will enter the details of data entry screen for water charges <connectionDetails>
    And user will click on the add/edit dcb
    And user will enter the details of DCB
    And user will close the dcb form
    Then user will be notified by "successfully"
    And current user logs out

    Examples:
      | connectionDetails   |
      | dataEntryInfo       |
      | connectionInfoMeter |

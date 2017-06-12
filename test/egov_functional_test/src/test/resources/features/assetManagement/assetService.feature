Feature: Create/Search for Asset Service

  As a registered user of the system
  I should able to create/search a asset service

  Scenario Outline: Create a Asset Service

    Given assetCreator logs in
    And user will select the required screen as "Create Asset"
    And user will enter the details as <headerDetails> and <locationDetails>
    And user will enter the category details as <categoryDetails> and with asset summary status as <assetStatus>
    And user will be notified the success page with an asset application number

    And user will select the required screen as "Modify Asset"
    And user will search the asset application based on category details
    And user will update the details in asset modify screen based on <categoryDetails>
    Then user will be notified by "Updated"
    And current user logs out

    Examples:
      | headerDetails | locationDetails | assetStatus | categoryDetails |
      | header1       | location1       | CREATED     | Land            |
      | header2       | location1       | CREATED     | market          |
      | header3       | location1       | CREATED     | kalyanaMandapam |
      | header4       | location1       | CREATED     | lakesAndPonds   |
      | header5       | location1       | CREATED     | roads           |
      | header6       | location1       | CREATED     | community       |
      | header7       | location1       | CREATED     | usufruct        |
      | header8       | location1       | CREATED     | shopping        |
      | header9       | location1       | CREATED     | parkingSpace    |
      | header10      | location1       | CREATED     | slaughterHouse  |
      | header11      | location1       | CREATED     | parks           |
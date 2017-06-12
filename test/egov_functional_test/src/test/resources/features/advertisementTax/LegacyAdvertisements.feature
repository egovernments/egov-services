Feature: Create/Update LegacyAdvertisements
  As a registered user of system
  I am able to create/update legacyAdvertisements

  @AdvertisementTax  @Sanity
  Scenario Outline: Create/Update LegacyAdvertisements

    When admin logs in
    And user will select the required screen as "create legacy advertisement"
    And he enters advertisement details as <advertisementDetails>
    And he enters permission details as <permissionDetails>
    And he enters locality details as <localityDetails>
    And he enters structure details as <structureDetails>
    And he enters arrear details
    And he submit the application and closes the acknowledgement
    Then user will be notified by "successfully"

    And user will select the required screen as "update legacy advertisements"
    And he search for required file by application number
    And he update the legacy advertisement and close the acknowledgement
    Then user will be notified by "updated"
    And current user logs out

    Examples:
      | advertisementDetails | permissionDetails | localityDetails | structureDetails |
      | advertisement1       | permission1       | locality1       | structure1       |
      | advertisement2       | permission2       | locality2       | structure2       |
      | advertisement3       | permission3       | locality3       | structure3       |

  @AdvertisementTax  @Sanity
  Scenario Outline: Create/Renewal LegacyAdvertisements

    When admin logs in
    And user will select the required screen as "create legacy advertisement"
    And he enters advertisement details as <advertisementDetails>
    And he enters permission details as <permissionDetails>
    And he enters locality details as <localityDetails>
    And he enters structure details as <structureDetails>
    And he enters arrear details
    And he submit the application and closes the acknowledgement
    Then user will be notified by "successfully"

    And user will select the required screen as "Advertisement Renewal"
    And he search for required file by application number for renewal
    And he request for renewal and forward to commissioner
    Then user will be notified by "forwarded"
    And current user logs out

    And commissioner logs in
    And he chooses to act upon above application number
    And he approves the advertisement application
    Then user will be notified by "Successful"
    And current user logs out

    Examples:
      | advertisementDetails | permissionDetails | localityDetails | structureDetails |
      | advertisement1       | permission1       | locality1       | structure1       |
      | advertisement2       | permission2       | locality2       | structure2       |
      | advertisement3       | permission3       | locality3       | structure3       |


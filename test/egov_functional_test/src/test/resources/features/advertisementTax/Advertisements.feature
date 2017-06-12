Feature: Create/search Advertisement
  As a registered user of the system
  I am able to create/search Advertisements

  @AdvertisementTax @Sanity
  Scenario Outline: Create/Search/Collect Tax Agency wise

    # Create Agency

    Given admin logs in
    And user will select the required screen as "create agency"
    And he enter details for agency creation
    And he submit the details and closes acknowledgement
    Then user will be notified by "created"

    And user will select the required screen as "search agency"
    And he enter details for search agency
    And he view and closes the acknowledgement
#    And current user logs out

   # Create Advertisement

#    And creator logs in
    And user will select the required screen as "create advertisement"
    And he enters advertisement details as <advertisementDetails>
    And he enter agency name
    And he enters permission details as <permissionDetails>
    And he enters locality details as <localityDetails>
    And he enters structure details as <structureDetails>
    And he enter approver details as <approverDetails>
    And he forwards and closes the acknowledgement
    Then user will be notified by "successfully"
    And current user logs out

    And commissioner logs in
    And he chooses to act upon above application number
    And he approves the advertisement application
    Then user will be notified by "approved"
    And current user logs out

  #  Collect Advertisement Tax by Agency wise

    And creator logs in
    And user will select the required screen as "Collect Advertisement Tax"
    And he choose to collect advertisement tax by agency wise
    And he selects the agency for Tax/Fees collection
    And he choose to collect advertisement tax
    And current user logs out

    Examples:
      | advertisementDetails | permissionDetails | localityDetails | structureDetails | approverDetails |
      | advertisement1       | permission1       | locality1       | structure1       | commissioner1   |
      | advertisement2       | permission2       | locality2       | structure2       | commissioner1   |
      | advertisement3       | permission3       | locality3       | structure3       | commissioner1   |

  @AdvertisementTax @Sanity
  Scenario Outline: Create/Search/CollectTax/Deactivate AdvertisementWise

#  Create Advertisements

#    Given creator logs in
    Given admin logs in
    And user will select the required screen as "create advertisement"
    And he enters advertisement details as <advertisementDetails>
    And he enters permission details as <permissionDetails>
    And he enters locality details as <localityDetails>
    And he enters structure details as <structureDetails>
    And he enter approver details as <approverDetails>
    And he forwards and closes the acknowledgement
    Then user will be notified by "successfully"
    And current user logs out

    And commissioner logs in
    And he chooses to act upon above application number
    And he approves the advertisement application
    Then user will be notified by "approved"
    And current user logs out

#  Search Advertisements

#    And creator logs in
    Given admin logs in
    And user will select the required screen as "search advertisement"
    And he search and select the required advertisement
    And he view and close the acknowledgement
    And current user logs out

# Collect Advertisement Tax

    And creator logs in
    And user will select the required screen as "Collect Advertisement Tax"
    And he search advertisement by advertisement number
    And he choose advertisement for collecting advertisement tax
    And current user logs out

# Deactivate Advertisement

    And admin logs in
    And user will select the required screen as "Deactivate Advertisement"
    And he search for advertisement for deactivate
    And he deactivates the advertisement with remarks and date
    Then user will be notified by "Deactivated"
    And user closes the acknowledgement pages
    And current user logs out

    Examples:
      | advertisementDetails | permissionDetails | localityDetails | structureDetails | approverDetails |
      | advertisement1       | permission1       | locality1       | structure1       | commissioner1   |
      | advertisement2       | permission2       | locality2       | structure2       | commissioner1   |
      | advertisement3       | permission3       | locality3       | structure3       | commissioner1   |

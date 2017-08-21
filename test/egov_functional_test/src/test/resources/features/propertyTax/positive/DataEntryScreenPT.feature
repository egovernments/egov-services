Feature: Creating a Property Tax with Data Entry Screen

  Scenario: Create A DataEntryScreen
    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value narendra
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen verifies myTasks has visible value My Tasks
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Data Entry
    And user on Home screen clicks on firstMenuItem

    ### On Create Data Entry Screen ###
    And user on PropertyTax screen verifies dataEntryText has visible value Create DataEntry
    And user on PropertyTax screen types on oldPropertyId value --10 digit random numbers
    And user on PropertyTax screen types on ownerName value --"PTOwner ",4 random characters
    And user on PropertyTax screen types on phoneNumber value --"1",9 digit random numbers
    And user on PropertyTax screen selects gender with value as Male
    And user on PropertyTax screen clicks radio button or checkbox on primaryOwner

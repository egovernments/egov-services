Feature: Registering a Complaint

  @PGR
  Scenario: Creating a complaint on behalf of Citizen

    ### On Login Screen ###
    And user on Login screen waits for kurnoolText to be visible
    Given user on Login screen types on username value ramana
    And user on Login screen types on password value demo
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen clicks on menu
    And user on Home screen types on search value Officials Register Grievance
    And user on Home screen clicks on firstMenuItem

    ### On Create Complaint Grievance Screen ###
    And user on Grievance screen clicks on receivingMode
    And user on Grievance screen clicks on receivingModeDropdown value Call
    And user on Grievance screen types on name value user
